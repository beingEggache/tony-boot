package com.tony.web.advice

import com.tony.utils.rawClass
import com.tony.web.support.RequestBodyFieldInjectorComposite
import java.lang.reflect.Type
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter

/**
 * RequestBodyFieldInjectAdvice is
 * @author tangli
 * @since 2023/06/07 18:07
 */
@RestControllerAdvice
internal class InjectRequestBodyAdvice(
    private val requestBodyFieldInjectorComposite: RequestBodyFieldInjectorComposite,
) : RequestBodyAdviceAdapter() {

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        return requestBodyFieldInjectorComposite.supports(targetType.rawClass())
    }

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Any {
        return requestBodyFieldInjectorComposite.injectValues(body)
    }
}
