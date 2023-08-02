package com.tony.web.advice

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.ListResult
import com.tony.misc.notSupportResponseWrapClasses
import com.tony.utils.antPathMatchAny
import com.tony.utils.asTo
import com.tony.utils.isArrayLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.web.WebApp
import com.tony.web.WebContext
import java.util.Collections
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * 响应包装, 将直接返回的类型包装为全局结构 [ApiResult].
 *
 * @author tangli
 * @since 2023/5/25 10:51
 */
@RestControllerAdvice
internal class WrapResponseBodyAdvice : ResponseBodyAdvice<Any?> {

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): ApiResult<*> =
        when {
            body == null -> ApiResult(EMPTY_RESULT, ApiProperty.okCode)
            !body::class.java.isArrayLikeType() -> ApiResult(body, ApiProperty.okCode)
            else -> if (body::class.java.isArray) {
                ApiResult(toListResult(body), ApiProperty.okCode)
            } else {
                ApiResult(ListResult(body.asTo<Collection<*>>()), ApiProperty.okCode)
            }
        }

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ) = !WebContext.url.path.antPathMatchAny(WebApp.responseWrapExcludePatterns) &&
        converterType.isTypesOrSubTypesOf(MappingJackson2HttpMessageConverter::class.java) &&
        !returnType.parameterType.isTypesOrSubTypesOf(*notSupportResponseWrapClasses)

    private companion object Utils {

        @JvmStatic
        private fun toListResult(body: Any?) = when (body) {
            is ByteArray -> ListResult<Byte>(body)
            is ShortArray -> ListResult<Short>(body)
            is IntArray -> ListResult<Int>(body)
            is LongArray -> ListResult<Long>(body)
            is FloatArray -> ListResult<Float>(body)
            is DoubleArray -> ListResult<Double>(body)
            is BooleanArray -> ListResult<Boolean>(body)
            is CharArray -> ListResult<Char>(body)
            is Array<*> -> ListResult<Any>(body)
            else -> ListResult(Collections.EMPTY_LIST)
        }
    }
}
