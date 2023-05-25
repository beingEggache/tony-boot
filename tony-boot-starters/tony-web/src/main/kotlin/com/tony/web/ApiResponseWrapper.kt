package com.tony.web

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.ApiResultLike
import com.tony.ListResult
import com.tony.utils.antPathMatchAny
import com.tony.utils.asTo
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.time.temporal.TemporalAccessor
import java.util.Collections
import java.util.Date

/**
 * 响应包装, 将直接返回的类型包装为全局结构 [ApiResult].
 *
 * @author tangli
 * @since 2023/5/25 10:51
 */
@ConditionalOnExpression("\${web.response-wrap-enabled:true}")
@RestControllerAdvice
internal class ApiResponseWrapper : ResponseBodyAdvice<Any?> {

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ) = when {
        body == null -> ApiResult(EMPTY_RESULT, ApiProperty.okCode)
        body.isNotCollectionLike() -> ApiResult(body, ApiProperty.okCode)
        else -> if (body.javaClass.isArray) {
            ApiResult(toListResult(body), ApiProperty.okCode)
        } else {
            ApiResult(ListResult(body.asTo<Collection<*>>()), ApiProperty.okCode)
        }
    }

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ) = !WebContext.url.path.antPathMatchAny(WebApp.responseWrapExcludePatterns) &&
        MappingJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType) &&
        notSupportClasses.all {
            !it.isAssignableFrom(returnType.parameterType)
        }

    private companion object Utils {
        private fun Any.isNotCollectionLike() =
            !Collection::class.java.isAssignableFrom(javaClass) && !javaClass.isArray

        @JvmStatic
        private val notSupportClasses: Array<Class<*>> = arrayOf(
            ApiResultLike::class.java,
            Number::class.java,
            Enum::class.java,
            Date::class.java,
            TemporalAccessor::class.java,
            // because converterType is StringHttpMessageConverter
            /*java.lang.CharSequence::class.java,
            CharSequence::class.java,*/
            java.lang.Character::class.java,
            Char::class.java,
            java.lang.Boolean::class.java,
            Boolean::class.java,
        )

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
