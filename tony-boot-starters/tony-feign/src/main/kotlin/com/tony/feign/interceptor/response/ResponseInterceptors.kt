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
 * @author Tang Li
 * @date 2023/08/02 21:00
 */
import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResultLike
import com.tony.ListResult
import com.tony.exception.ApiException
import com.tony.misc.notSupportResponseWrapClasses
import com.tony.utils.isArrayLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.jsonNode
import com.tony.utils.jsonToObj
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
 * @author Tang Li
 * @date 2023/08/02 21:00
 */
public class GlobalResponseInterceptorProvider<T : ResponseInterceptor>(
    private val obj: T,
) : ObjectProvider<T> {
    override fun getObject(vararg args: Any?): T = obj

    override fun getObject(): T = obj

    override fun getIfAvailable(): T = obj

    override fun getIfUnique(): T = obj
}

internal class UnwrapResponseInterceptorProvider(
    private val obj: UnwrapResponseInterceptor,
) : ObjectProvider<UnwrapResponseInterceptor> {
    override fun getObject(vararg args: Any?): UnwrapResponseInterceptor = obj

    override fun getObject(): UnwrapResponseInterceptor = obj

    override fun getIfAvailable(): UnwrapResponseInterceptor = obj

    override fun getIfUnique(): UnwrapResponseInterceptor = obj
}

/**
 * 响应拦截器. 将全局统一响应的具体返回数据抽取出来
 * @author Tang Li
 * @date 2023/09/13 10:34
 * @since 1.0.0
 */
public class UnwrapResponseInterceptor : ResponseInterceptor {
    override fun aroundDecode(invocationContext: InvocationContext): Any {
        val returnType = invocationContext.returnType()
        val returnJavaType = returnType.toJavaType()
        val returnRawClass = returnType.rawClass()

        val response = invocationContext.response()
        val isJson = response.headers()[CONTENT_TYPE]?.firstOrNull() == MediaType.APPLICATION_JSON_VALUE
        if (returnRawClass.isTypesOrSubTypesOf(*notSupportResponseWrapClasses) && !isJson) {
            return invocationContext.proceed()
        }

        val jsonNode = response.body().asInputStream().jsonNode()
        val message = jsonNode.get(ApiResultLike<*>::getMessage.name.lTrimAndDecapitalize()).asText()
        val code = jsonNode.get(ApiResultLike<*>::getCode.name.lTrimAndDecapitalize()).asInt()

        if (code != ApiProperty.okCode) {
            throw ApiException(message, code)
        }

        val dataJsonNode = jsonNode.get(ApiResult<*>::getData.name.lTrimAndDecapitalize())
        if (returnRawClass.isArrayLikeType()) {
            val itemFieldName = ListResult<*>::getItems.name.lTrimAndDecapitalize()
            return dataJsonNode.get(itemFieldName).toString().jsonToObj(returnJavaType)
        }

        return dataJsonNode.toString().jsonToObj(returnJavaType)
    }

    private fun String.lTrimAndDecapitalize(): String = this.substring(3)
        .replaceFirstChar { it.lowercase(Locale.getDefault()) }
}
