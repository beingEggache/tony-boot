/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.feign.interceptor.response

/**
 * Feign 响应拦截器 provider.
 *
 * 用这个避免自动注册.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
import feign.InvocationContext
import feign.ResponseInterceptor
import java.util.Locale
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import tony.core.ApiProperty
import tony.core.ENCRYPTED_HEADER_NAME
import tony.core.ERROR_CODE_HEADER_NAME
import tony.core.crypto.CryptoProvider
import tony.core.crypto.symmetric.decryptToString
import tony.core.exception.ApiException
import tony.core.misc.notSupportResponseWrapClassCollection
import tony.core.model.ApiResultLike
import tony.core.model.ListResult
import tony.core.utils.convertTo
import tony.core.utils.getLogger
import tony.core.utils.isArrayLikeType
import tony.core.utils.isTypesOrSubTypesOf
import tony.core.utils.jsonNode
import tony.core.utils.jsonToObj
import tony.core.utils.rawClass
import tony.core.utils.toJavaType
import tony.core.utils.trimQuotes

/**
 * 全局响应拦截器 Provider。
 *
 * 用于手动注册全局 [ResponseInterceptor]，避免自动注册带来的副作用。
 * 适用场景：需要灵活控制拦截器注册时机和范围。
 * @param [T] 拦截器[ResponseInterceptor]类型
 * @param obj 具体的 [ResponseInterceptor] 实例
 * @author tangli
 * @date 2023/08/02 19:00
 */
public class GlobalResponseInterceptorProvider<T : ResponseInterceptor>(
    private val obj: T,
) : ObjectProvider<T> {
    override fun getObject(vararg args: Any?): T =
        obj

    override fun getObject(): T =
        obj

    override fun getIfAvailable(): T =
        obj

    override fun getIfUnique(): T =
        obj
}

/**
 * 解包响应拦截器 Provider。
 *
 * 用于手动注册解包拦截器，便于扩展和替换默认实现。
 * 适用场景：需要自定义响应解包逻辑或支持多种解包策略。
 * @param obj 具体的 UnwrapResponseInterceptor 实例
 * @author tangli
 * @date 2023/08/02 19:00
 */
internal class UnwrapResponseInterceptorProvider<T : UnwrapResponseInterceptor>(
    private val obj: T,
) : ObjectProvider<T> {
    override fun getObject(vararg args: Any?): T =
        obj

    override fun getObject(): T =
        obj

    override fun getIfAvailable(): T =
        obj

    override fun getIfUnique(): T =
        obj
}

/**
 * 默认解包响应拦截器。
 *
 * 主要功能：
 * 1. 自动识别统一响应结构（如 ApiResultLike、ListResult），并提取 data 字段。
 * 2. 支持响应体加密场景，自动解密并反序列化。
 * 3. 检查 code 字段，非 okCode 时抛出 ApiException。
 *
 * 适用场景：
 * - 需要对后端统一响应结构自动解包、异常抛出、加密解密的场景。
 *
 * 类型支持：
 * - 支持普通对象、集合、数组等多种返回类型。
 * - 支持自定义响应结构扩展（建议通过 SPI 或配置方式）。
 *
 * 异常行为：
 * - 字段缺失、类型不符、解密失败、JSON 解析失败等均会抛出 ApiException。
 * - 详细异常信息便于定位问题。
 *
 * 性能影响：
 * - 每次响应均需读取流并反序列化，建议响应体不要过大。
 *
 * 扩展方式：
 * - 可自定义实现 UnwrapResponseInterceptor 并通过 Provider 注册。
 *
 * @param cryptoProvider 可选的加密解密实现
 * @author tangli
 * @date 2023/08/02 19:00
 */
internal class DefaultUnwrapResponseInterceptor(
    private val cryptoProvider: CryptoProvider?,
) : UnwrapResponseInterceptor {
    private val logger = getLogger()

    override fun intercept(
        invocationContext: InvocationContext,
        chain: ResponseInterceptor.Chain,
    ): Any {
        val returnType = invocationContext.returnType()
        val returnJavaType = returnType.toJavaType()
        val returnRawClass = returnType.rawClass()

        val response = invocationContext.response()
        val responseHeaders = response.headers()
        val isJson =
            responseHeaders[CONTENT_TYPE]
                ?.firstOrNull() == MediaType.APPLICATION_JSON_VALUE

        val hasErrorCode = !responseHeaders[ERROR_CODE_HEADER_NAME].isNullOrEmpty()
        if (!hasErrorCode && (returnRawClass.isTypesOrSubTypesOf(notSupportResponseWrapClassCollection) || !isJson)) {
            return chain.next(invocationContext)
        }
        val hasEncrypted = !responseHeaders[ENCRYPTED_HEADER_NAME].isNullOrEmpty()
        if (hasEncrypted && this.cryptoProvider == null) {
            logger.warn("Not support unwrap encrypted response")
            throw ApiException("Not support unwrap encrypted response", ApiProperty.badRequestCode)
        }

        val jsonNode =
            response
                .body()
                .asInputStream()
                .jsonNode()

        val message =
            jsonNode
                .get(messageFieldName)
                .asText()

        val code =
            jsonNode
                .get(codeFieldName)
                .asInt()

        if (code != ApiProperty.okCode) {
            throw ApiException(message, code)
        }

        val dataJsonNode = jsonNode.get(dataFieldName)
        return if (hasEncrypted && this.cryptoProvider != null) {
            dataJsonNode
                .toString()
                .trimQuotes()
                .decryptToString(
                    cryptoProvider.algorithm,
                    cryptoProvider.secret,
                    cryptoProvider.encoding
                ).jsonToObj(returnJavaType)
        } else if (returnRawClass.isArrayLikeType()) {
            dataJsonNode
                .get(rowsFieldName)
                .convertTo(returnJavaType)
        } else {
            dataJsonNode.convertTo(returnJavaType)
        }
    }

    private companion object {
        @JvmStatic
        private val messageFieldName = ApiResultLike<*>::getMessage.name.lTrimAndDecapitalize()

        @JvmStatic
        private val codeFieldName = ApiResultLike<*>::getCode.name.lTrimAndDecapitalize()

        @JvmStatic
        private val dataFieldName = ApiResultLike<*>::getData.name.lTrimAndDecapitalize()

        @JvmStatic
        private val rowsFieldName = ListResult<*>::getRows.name.lTrimAndDecapitalize()

        private fun String.lTrimAndDecapitalize(): String =
            this
                .substring(3)
                .replaceFirstChar { it.lowercase(Locale.getDefault()) }
    }
}
