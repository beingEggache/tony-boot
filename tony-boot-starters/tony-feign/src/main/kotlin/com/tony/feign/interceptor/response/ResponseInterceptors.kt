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

package com.tony.feign.interceptor.response

/**
 * Feign 响应拦截器 provider.
 *
 * 用这个避免自动注册.
 *
 * @author tangli
 * @date 2023/08/02 19:00
 */
import com.tony.ApiProperty
import com.tony.ApiResultLike
import com.tony.ENCRYPTED_HEADER_NAME
import com.tony.ERROR_CODE_HEADER_NAME
import com.tony.ListResult
import com.tony.exception.ApiException
import com.tony.misc.notSupportResponseWrapClasses
import com.tony.utils.convertTo
import com.tony.utils.getLogger
import com.tony.utils.isArrayLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.jsonNode
import com.tony.utils.rawClass
import com.tony.utils.toJavaType
import feign.InvocationContext
import feign.ResponseInterceptor
import java.util.Locale
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType

/**
 * Global response interceptor provider.
 *
 * 用这个避免自动注册.
 *
 * @param T
 * @property obj
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
 * 响应拦截器. 将全局统一响应的具体返回数据抽取出来
 * @author tangli
 * @date 2023/09/13 19:34
 * @since 1.0.0
 */
internal class DefaultUnwrapResponseInterceptor : UnwrapResponseInterceptor {
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
        if (!hasErrorCode && (returnRawClass.isTypesOrSubTypesOf(*notSupportResponseWrapClasses) || !isJson)) {
            return chain.next(invocationContext)
        }
        val hasEncrypted = !responseHeaders[ENCRYPTED_HEADER_NAME].isNullOrEmpty()
        if (hasEncrypted) {
            logger.warn("Not support encrypted response")
            throw ApiException("Not support encrypted response", ApiProperty.badRequestCode)
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
        return if (returnRawClass.isArrayLikeType()) {
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
