package com.tony.webcore.advice

import com.tony.core.ApiProperty
import com.tony.core.ApiResult
import com.tony.core.ApiResult.Companion.EMPTY_RESULT
import com.tony.core.ListResult
import com.tony.core.utils.asTo
import com.tony.webcore.WebApp
import com.tony.webcore.utils.antPathMatcher
import com.tony.webcore.utils.matchAny
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
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

@ConditionalOnWebApplication
@ConditionalOnExpression("\${web.response-wrap-enabled:true}")
@RestControllerAdvice
internal class ApiResponseAdvice : ResponseBodyAdvice<Any?> {

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ) = when {
        antPathMatcher.matchAny(WebApp.excludeJsonResultUrlPatterns, request.uri.path) -> body
        body == null -> ApiResult(EMPTY_RESULT, ApiProperty.successCode)
        body.isNotCollectionLike() -> ApiResult(body, ApiProperty.successCode)
        else -> {
            if (body.javaClass.isArray) {
                ApiResult(toListResult(body), ApiProperty.successCode)
            } else {
                ApiResult(
                    ListResult(body.asTo<Collection<*>>()), ApiProperty.successCode
                )
            }
        }
    }

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ) = MappingJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType) &&
        returnType.parameterType.isNotAssignableFrom(
            ApiResult::class.java,
            Number::class.java,
            Enum::class.java,
            Date::class.java,
            TemporalAccessor::class.java,
            java.lang.Character::class.java,
            Char::class.java,
            java.lang.Boolean::class.java,
            Boolean::class.java
        )

    private companion object Utils {
        private fun Any.isNotCollectionLike() =
            !Collection::class.java.isAssignableFrom(javaClass) && !javaClass.isArray

        private fun Class<*>.isNotAssignableFrom(vararg clazzes: Class<*>) = clazzes.all {
            !it.isAssignableFrom(this)
        }

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
