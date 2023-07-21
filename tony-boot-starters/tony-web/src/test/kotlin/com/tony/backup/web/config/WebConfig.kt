@file:Suppress("unused")

package com.tony.backup.web.config

import com.tony.backup.web.advice.InjectRequestBodyAdvice
import com.tony.backup.web.support.RequestBodyFieldInjector
import com.tony.backup.web.support.RequestBodyFieldInjectorComposite
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean

/**
 * WebConfig
 *
 * @author tangli
 * @since 2023/5/25 10:35
 */
internal class WebConfig {

    @ConditionalOnExpression("\${web.inject-request-body-enabled:true}")
    @Bean
    internal fun requestBodyFieldInjectorComposite(
        requestBodyFieldInjectors: List<RequestBodyFieldInjector>,
    ): RequestBodyFieldInjectorComposite =
        RequestBodyFieldInjectorComposite(requestBodyFieldInjectors)

    @ConditionalOnExpression("\${web.inject-request-body-enabled:true}")
    @Bean
    internal fun injectRequestBodyAdvice(
        requestBodyFieldInjectorComposite: RequestBodyFieldInjectorComposite,
    ): InjectRequestBodyAdvice = InjectRequestBodyAdvice(requestBodyFieldInjectorComposite)
}

