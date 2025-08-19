/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.web.autoconfigure

/**
 * WebConfig
 *
 * @author tangli
 * @date 2023/05/25 19:35
 */
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDateTime
import java.util.Date
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.autoconfigure.jackson.JacksonProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.format.FormatterRegistry
import org.springframework.http.HttpHeaders
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpResponse
import org.springframework.util.unit.DataSize
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.DefaultCorsProcessor
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import tony.core.jackson.InjectableValueSupplier
import tony.core.jackson.InjectableValuesBySupplier
import tony.core.jackson.NullValueBeanSerializerModifier
import tony.core.misc.YamlPropertySourceFactory
import tony.core.model.ofApiResult
import tony.core.utils.applyIf
import tony.core.utils.asTo
import tony.core.utils.createObjectMapper
import tony.core.utils.getLogger
import tony.core.utils.toJsonString
import tony.web.advice.ExceptionHandler
import tony.web.advice.WrapResponseBodyAdvice
import tony.web.converter.EnumIntValueConverterFactory
import tony.web.converter.EnumStringValueConverterFactory
import tony.web.filter.RequestReplaceToRepeatReadFilter
import tony.web.filter.TraceIdFilter
import tony.web.filter.TraceLogFilter
import tony.web.log.DefaultTraceLogger
import tony.web.log.TraceLogger

/**
 * WebConfig
 *
 * @author tangli
 * @date 2023/05/25 19:35
 */
@ConditionalOnWebApplication(
    type =
        ConditionalOnWebApplication
            .Type
            .SERVLET
)
@PropertySource("classpath:web.config.yml", factory = YamlPropertySourceFactory::class)
@EnableAutoConfiguration(
    excludeName = [
        "org.springframework.boot.autoconfigure.http.client.reactive.ClientHttpConnectorAutoConfiguration",
        "org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration",
        "org.springframework.boot.autoconfigure.http.client.HttpClientAutoConfiguration",
        "org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration",
        "org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration",
        "org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration",
        "org.springframework.boot.autoconfigure.session.SessionAutoConfiguration"
    ]
)
@EnableConfigurationProperties(value = [WebProperties::class, TraceLogProperties::class, WebCorsProperties::class])
@AutoConfiguration
private class WebAutoConfiguration(
    private val webProperties: WebProperties,
    private val traceLogProperties: TraceLogProperties,
    private val webCorsProperties: WebCorsProperties,
) : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(EnumIntValueConverterFactory())
        registry.addConverterFactory(EnumStringValueConverterFactory())
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        if (webProperties.fillResponseNullValueEnabled) {
            converters
                .firstOrNull { it is MappingJackson2HttpMessageConverter }
                .asTo<MappingJackson2HttpMessageConverter>()
                ?.let { mappingJackson2HttpMessageConverter ->
                    getLogger(
                        NullValueBeanSerializerModifier::class.java
                            .name
                    ).info("Fill response null value enabled")
                    mappingJackson2HttpMessageConverter
                        .objectMapper
                        .apply {
                            serializerFactory =
                                serializerFactory
                                    .withSerializerModifier(NullValueBeanSerializerModifier())
                        }
                }
        }
    }

    @ConditionalOnBooleanProperty(prefix = "web.log.trace", value = ["enabled"], matchIfMissing = true)
    @Bean
    private fun requestReplaceToRepeatReadFilter() =
        RequestReplaceToRepeatReadFilter(traceLogProperties.excludePatterns)

    @Bean
    private fun traceIdFilter() =
        TraceIdFilter()

    @ConditionalOnMissingBean(TraceLogger::class)
    @ConditionalOnBooleanProperty(prefix = "web.log.trace", value = ["enabled"], matchIfMissing = true)
    @Bean
    private fun defaultTraceLogger(): TraceLogger =
        DefaultTraceLogger(
            traceLogProperties.enableDesensitized,
            traceLogProperties.desensitizedFields,
            traceLogProperties.desensitizedRequestHeaders,
            traceLogProperties.desensitizedResponseHeaders
        )

    @ConditionalOnBooleanProperty(prefix = "web.log.trace", value = ["enabled"], matchIfMissing = true)
    @Bean
    private fun traceLogFilter(traceLogger: TraceLogger): TraceLogFilter {
        val logger = LoggerFactory.getLogger(TraceLogFilter::class.java)

        logger.info(
            "Trace log is enabled. " +
                "Request body size limit is {}, " +
                "Response body size limit is {} ",
            traceLogProperties.requestBodyMaxSize,
            traceLogProperties.responseBodyMaxSize
        )
        if (traceLogProperties.excludePatterns.isNotEmpty()) {
            logger.info("Request trace log exclude patterns: {}", traceLogProperties.excludePatterns)
        }

        if (traceLogProperties.enableDesensitized) {
            traceLogProperties.desensitizedFields.applyIf(traceLogProperties.desensitizedFields.isNotEmpty()) {
                logger.info("Desensitized Fields is {}.", traceLogProperties.desensitizedFields)
            }
            traceLogProperties.desensitizedRequestHeaders.applyIf(
                traceLogProperties.desensitizedRequestHeaders.isNotEmpty()
            ) {
                logger.info("Desensitized Request Headers is {}.", traceLogProperties.desensitizedRequestHeaders)
            }
            traceLogProperties.desensitizedResponseHeaders.applyIf(
                traceLogProperties.desensitizedResponseHeaders.isNotEmpty()
            ) {
                logger.info("Desensitized Response Headers is {}.", traceLogProperties.desensitizedResponseHeaders)
            }
        }

        return TraceLogFilter(
            traceLogger,
            traceLogProperties.excludePatterns,
            traceLogProperties.requestBodyMaxSize.toBytes(),
            traceLogProperties.responseBodyMaxSize.toBytes()
        )
    }

    @ConditionalOnBooleanProperty(
        prefix = "web",
        value = ["wrap-response-body-enabled"],
        matchIfMissing = true
    )
    @Bean
    private fun wrapResponseBodyAdvice(): WrapResponseBodyAdvice =
        WrapResponseBodyAdvice()

    @Bean
    private fun exceptionHandler() =
        ExceptionHandler()

    @ConditionalOnBooleanProperty(prefix = "web.cors", value = ["enabled"])
    @Bean
    private fun corsFilter(): CorsFilter {
        val corsConfiguration =
            CorsConfiguration().apply {
                BeanUtils.copyProperties(webCorsProperties, this)
                getLogger(CorsFilter::class.java.name)
                    .info(
                        "Cors is enabled. " +
                            "allowCredentials is $allowCredentials,  " +
                            "allowedOriginPatterns is $allowedOriginPatterns, " +
                            "allowedHeaders is $allowedHeaders, " +
                            "allowedMethods is $allowedMethods, " +
                            "exposedHeaders is $exposedHeaders, " +
                            "maxAge is $maxAge"
                    )
            }
        return CorsFilter(
            UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", corsConfiguration) }
        ).apply {
            setCorsProcessor(ApiCorsProcessor())
        }
    }

    @Bean
    private fun jackson2ObjectMapperBuilder(
        jackson2ObjectMapperBuilderCustomizer: Jackson2ObjectMapperBuilderCustomizer,
    ): Jackson2ObjectMapperBuilder =
        Jackson2ObjectMapperBuilder()
            .apply {
                jackson2ObjectMapperBuilderCustomizer.customize(this)
            }

    @Bean
    private fun objectMapper(
        jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder,
        injectableValueSuppliers: List<InjectableValueSupplier>,
        jacksonProperties: JacksonProperties,
    ): ObjectMapper =
        createObjectMapper()
            .apply {
                jackson2ObjectMapperBuilder.configure(this)
                if (jacksonProperties.dateFormat?.isNotBlank() == true) {
                    configOverride(Date::class.java).format = JsonFormat.Value.forPattern(jacksonProperties.dateFormat)
                    configOverride(LocalDateTime::class.java).format =
                        JsonFormat.Value.forPattern(jacksonProperties.dateFormat)
                }
                injectableValues =
                    InjectableValuesBySupplier(
                        injectableValueSuppliers
                            .associateBy { it.name }
                    )
            }
}

/**
 * WebProperties
 *
 * @author tangli
 * @date 2023/05/25 19:35
 */
@ConditionalOnWebApplication(
    type =
        ConditionalOnWebApplication
            .Type
            .SERVLET
)
@ConfigurationProperties(prefix = "web")
internal data class WebProperties(
    /**
     * 是否包装返回值。
     */
    @DefaultValue("true")
    val wrapResponseBodyEnabled: Boolean,
    /**
     * 是否注入请求。
     */
    @DefaultValue("true")
    val injectRequestBodyEnabled: Boolean,
    /**
     * 包装返回值白名单url（ant pattern）。
     */
    val wrapResponseExcludePatterns: List<String> = listOf(),
    /**
     * 是否处理响应json null值。
     */
    @DefaultValue("true")
    val fillResponseNullValueEnabled: Boolean,
)

@ConditionalOnWebApplication(
    type =
        ConditionalOnWebApplication
            .Type
            .SERVLET
)
@ConfigurationProperties(prefix = "web.log.trace", ignoreInvalidFields = false, ignoreUnknownFields = false)
internal data class TraceLogProperties(
    /**
     * 是否记录trace日志。
     */
    @DefaultValue("true")
    val enabled: Boolean,
    /**
     * trace日志排除url
     */
    val excludePatterns: Set<String> = setOf(),
    /**
     * trace日志请求体长度, 超过只显示ContentType
     */
    @DefaultValue("50KB")
    val requestBodyMaxSize: DataSize = DataSize.ofKilobytes(50),
    /**
     * trace日志响应体长度, 超过只显示ContentType
     */
    @DefaultValue("50KB")
    val responseBodyMaxSize: DataSize = DataSize.ofKilobytes(50),
    /**
     * 是否启用日志脱敏
     */
    @DefaultValue("false")
    val enableDesensitized: Boolean,
    /**
     * trace日志需要被脱敏的字段.
     */
    val desensitizedFields: Set<String> = setOf(),
    /**
     * trace日志需要被脱敏的请求头.
     */
    val desensitizedRequestHeaders: Set<String> = setOf(),
    /**
     * trace日志需要被脱敏的响应头.
     */
    val desensitizedResponseHeaders: Set<String> = setOf(),
)

/**
 * WebCorsProperties
 *
 * @author tangli
 * @date 2023/05/25 19:35
 */
@ConditionalOnWebApplication(
    type =
        ConditionalOnWebApplication
            .Type
            .SERVLET
)
@ConfigurationProperties(prefix = "web.cors", ignoreInvalidFields = false, ignoreUnknownFields = false)
internal data class WebCorsProperties(
    @DefaultValue("false")
    val enabled: Boolean,
    val allowedOriginPatterns: List<String> = listOf("*"),
    val allowedHeaders: List<String> = listOf("*"),
    val allowedMethods: List<String> = listOf("*"),
    val exposedHeaders: List<String> = listOf(HttpHeaders.CONTENT_DISPOSITION),
    val maxAge: Long = Long.MAX_VALUE,
    @DefaultValue("true")
    val allowCredentials: Boolean,
)

/**
 * ApiCorsProcessor
 *
 * @author tangli
 * @date 2023/05/25 19:35
 */
internal class ApiCorsProcessor : DefaultCorsProcessor() {
    private companion object {
        @JvmStatic
        val invalidCorsRequestResponseByteArray by lazy(LazyThreadSafetyMode.NONE) {
            ofApiResult(
                Unit,
                HttpServletResponse.SC_FORBIDDEN,
                "Invalid CORS request"
            ).toJsonString()
                .toByteArray(Charsets.UTF_8)
        }
    }

    override fun rejectRequest(response: ServerHttpResponse) {
        response
            .body
            .write(invalidCorsRequestResponseByteArray)
        response.flush()
    }
}
