package com.tony.webcore.config

import com.tony.webcore.converter.EnumIntValueConverterFactory
import com.tony.webcore.converter.EnumStringValueConverterFactory
import com.tony.webcore.log.DefaultRequestTraceLogger
import com.tony.webcore.log.HttpServletRequestReplacedFilter
import com.tony.webcore.log.RequestTraceLogger
import com.tony.webcore.log.TraceIdFilter
import com.tony.webcore.log.TraceLoggingFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication
internal class WebConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(EnumIntValueConverterFactory())
        registry.addConverterFactory(EnumStringValueConverterFactory())
    }

    @ConditionalOnMissingBean(RequestTraceLogger::class)
    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    fun defaultRequestTraceLogger(): RequestTraceLogger = DefaultRequestTraceLogger()

    @Bean
    fun traceIdFilter() = TraceIdFilter()

    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    fun httpServletRequestReplacedFilter() = HttpServletRequestReplacedFilter()

    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    fun traceLoggingFilter(requestTraceLogger: RequestTraceLogger) = TraceLoggingFilter(requestTraceLogger)
}
