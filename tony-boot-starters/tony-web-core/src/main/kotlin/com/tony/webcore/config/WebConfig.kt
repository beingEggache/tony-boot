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
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebProperties::class)
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

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebCorsProperties::class)
@ConditionalOnExpression("\${web.cors.enabled:false}")
internal class WebCorsConfig(
    private val webCorsProperties: WebCorsProperties
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*(webCorsProperties.allowedOrigins ?: arrayOf()))
            .allowedMethods("*")
            .allowedHeaders(*(webCorsProperties.allowedHeaders ?: arrayOf()))
            .allowCredentials(webCorsProperties.allowCredentials ?: false)
    }
}

@Suppress("ArrayInDataClass")
@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web")
internal data class WebProperties(
    val responseWrapEnabled: Boolean = true,
    val responseWrapExcludePatterns: Array<String> = arrayOf(),
    val traceLoggerEnabled: Boolean = true,
    val jsonNullValueOptimizedEnabled: Boolean = true
)

@Suppress("ArrayInDataClass")
@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web.cors")
@ConditionalOnExpression("\${web.cors-enabled:false}")
internal data class WebCorsProperties(
    val allowedOrigins: Array<String>?,
    val allowedHeaders: Array<String>?,
    val allowCredentials: Boolean? = false
)
