package com.tony.feign.interceptor.response

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
 * Unwrap response
 *
 * @author tangli
 * @since 2023/08/02 21:00
 */
internal class UnwrapResponseInterceptor : ResponseInterceptor {
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

/**
 * Global response interceptor provider.
 *
 * 用这个避免自动注册.
 *
 * @param T
 * @property obj
 * @author tangli
 * @since 2023/08/02 21:00
 */
public class GlobalResponseInterceptorProvider<T : ResponseInterceptor>(
    private val obj: T,
) : ObjectProvider<T> {
    override fun getObject(vararg args: Any?): T = obj

    override fun getObject(): T = obj

    override fun getIfAvailable(): T = obj

    override fun getIfUnique(): T = obj
}
