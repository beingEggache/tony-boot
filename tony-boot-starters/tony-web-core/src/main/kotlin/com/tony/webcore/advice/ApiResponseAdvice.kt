package com.tony.webcore.advice

import com.tony.core.ApiResult
import com.tony.core.EMPTY_RESULT
import com.tony.core.ListResult
import com.tony.core.utils.asTo
import com.tony.webcore.WebApp
import com.tony.webcore.utils.antPathMatcher
import com.tony.webcore.utils.matchAny
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
@ConditionalOnWebApplication
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
        body == null -> ApiResult(EMPTY_RESULT, WebApp.successCode)
        Collection::class.java.isAssignableFrom(body.javaClass) -> ApiResult(
            ListResult(body.asTo<Collection<Any>>()), WebApp.successCode
        )
        else -> ApiResult(body, WebApp.successCode)
    }

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ) = MappingJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType) &&
        !ApiResult::class.java.isAssignableFrom(returnType.parameterType)
}
