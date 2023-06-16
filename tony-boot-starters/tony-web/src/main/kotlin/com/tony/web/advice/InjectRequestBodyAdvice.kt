package com.tony.web.advice

import com.tony.utils.rawClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import java.lang.reflect.Type

/**
 * RequestBodyFieldInjectAdvice is
 * @author tangli
 * @since 2023/06/07 18:07
 */
@ConditionalOnExpression("\${web.inject-request-body-enabled:true}")
@RestControllerAdvice
internal class InjectRequestBodyAdvice(
    private val requestBodyFieldInjectorComposite: RequestBodyFieldInjectorComposite,
) : RequestBodyAdviceAdapter() {

    private val logger: Logger = LoggerFactory.getLogger(InjectRequestBodyAdvice::class.java)

    init {
        logger.info("Request body inject is enabled.")
    }

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        return requestBodyFieldInjectorComposite.supports(targetType.rawClass()!!)
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
