package com.tony.web.config

import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.utils.createObjectMapper
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import com.tony.web.converter.EnumIntValueConverterFactory
import com.tony.web.converter.EnumStringValueConverterFactory
import com.tony.web.filter.RequestReplaceToRepeatReadFilter
import com.tony.web.filter.TraceIdFilter
import com.tony.web.filter.TraceLoggingFilter
import com.tony.web.jackson.NullValueBeanSerializerModifier
import com.tony.web.log.DefaultRequestTraceLogger
import com.tony.web.log.RequestTraceLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.DefaultCorsProcessor
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = [WebProperties::class, WebCorsProperties::class])
internal class WebConfig(
    private val webProperties: WebProperties,
    private val webCorsProperties: WebCorsProperties
) : WebMvcConfigurer {

    private val logger = getLogger(WebConfig::class.java.name)

    @Resource
    lateinit var mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(EnumIntValueConverterFactory())
        registry.addConverterFactory(EnumStringValueConverterFactory())
    }

    @Bean
    fun requestReplaceToRepeatReadFilter() = RequestReplaceToRepeatReadFilter()

    @Bean
    fun traceIdFilter() = TraceIdFilter()

    @ConditionalOnMissingBean(RequestTraceLogger::class)
    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    fun defaultRequestTraceLogger(): RequestTraceLogger = DefaultRequestTraceLogger()

    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    fun traceLoggingFilter(requestTraceLogger: RequestTraceLogger): TraceLoggingFilter {
        logger.info("Request trace log is enabled.")
        return TraceLoggingFilter(requestTraceLogger)
    }

    @ConditionalOnProperty("web.cors.enabled")
    @Bean
    fun corsFilter(): CorsFilter {
        logger.info("Cors is enabled.")
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration().apply {
            if (webCorsProperties.allowCredentials == true) {
                allowCredentials = webCorsProperties.allowCredentials
            }
            allowedOriginPatterns = webCorsProperties.allowedOrigins
            allowedHeaders = webCorsProperties.allowedHeaders.ifEmpty { listOf("*") }
            allowedMethods = webCorsProperties.allowedHeaders.ifEmpty { listOf("*") }
            maxAge = webCorsProperties.maxAge
        }
        source.registerCorsConfiguration("/**", corsConfiguration)
        return CorsFilter(source).apply {
            setCorsProcessor(ApiCorsProcessor())
        }
    }

    @PostConstruct
    fun init() {
        if (webProperties.jsonNullValueOptimizedEnabled) {
            logger.info("Response json null value optimizing is enabled.")
            mappingJackson2HttpMessageConverter.objectMapper = createObjectMapper().apply {
                serializerFactory =
                    serializerFactory
                        .withSerializerModifier(NullValueBeanSerializerModifier())
            }
        }
    }
}

@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web")
internal data class WebProperties(
    /**
     * 是否包装返回值。
     */
    val responseWrapEnabled: Boolean = true,
    /**
     * 包装返回值白名单url（ant pattern）。
     */
    val responseWrapExcludePatterns: List<String> = listOf(),
    /**
     * 是否记录请求日志。
     */
    val traceLoggerEnabled: Boolean = true,
    /**
     * 是否处理响应json null值。
     */
    val jsonNullValueOptimizedEnabled: Boolean = true
)

@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web.cors")
@ConditionalOnExpression("\${web.cors-enabled:false}")
internal data class WebCorsProperties(
    val enabled: Boolean = false,
    val allowedOrigins: List<String> = listOf(),
    val allowedHeaders: List<String> = listOf(),
    val allowedMethods: List<String> = listOf(),
    val maxAge: Long = Long.MAX_VALUE,
    val allowCredentials: Boolean? = true
)

internal class ApiCorsProcessor : DefaultCorsProcessor() {

    private companion object {
        @JvmStatic
        val invalidCorsRequestResponseByteArray = ApiResult(
            EMPTY_RESULT,
            HttpServletResponse.SC_FORBIDDEN,
            "Invalid CORS request"
        ).toJsonString().toByteArray()
    }

    override fun rejectRequest(response: ServerHttpResponse) {
        response.body.write(invalidCorsRequestResponseByteArray)
        response.flush()
    }
}
