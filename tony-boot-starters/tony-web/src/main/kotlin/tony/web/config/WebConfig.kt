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

package tony.web.config

/**
 * WebConfig
 *
 * @author tangli
 * @date 2023/05/25 19:35
 */
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.BeanUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
import tony.ApiResult
import tony.jackson.InjectableValueSupplier
import tony.jackson.InjectableValuesBySupplier
import tony.jackson.NullValueBeanSerializerModifier
import tony.misc.YamlPropertySourceFactory
import tony.utils.asTo
import tony.utils.createObjectMapper
import tony.utils.getLogger
import tony.utils.toJsonString
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
@EnableConfigurationProperties(value = [WebProperties::class, TraceLogProperties::class, WebCorsProperties::class])
@Configuration(proxyBeanMethods = false)
private class WebConfig(
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

    @ConditionalOnExpression("\${web.log.trace.enabled:true}")
    @Bean
    private fun requestReplaceToRepeatReadFilter() =
        RequestReplaceToRepeatReadFilter(traceLogProperties.excludePatterns)

    @Bean
    private fun traceIdFilter() =
        TraceIdFilter()

    @ConditionalOnMissingBean(TraceLogger::class)
    @ConditionalOnExpression("\${web.log.trace.enabled:true}")
    @Bean
    private fun defaultTraceLogger(): TraceLogger =
        DefaultTraceLogger()

    @ConditionalOnExpression("\${web.log.trace.enabled:true}")
    @Bean
    private fun traceLogFilter(traceLogger: TraceLogger): TraceLogFilter =
        TraceLogFilter(
            traceLogger,
            traceLogProperties.excludePatterns,
            traceLogProperties.requestBodyMaxSize.toBytes(),
            traceLogProperties.responseBodyMaxSize.toBytes()
        )

    @ConditionalOnExpression("\${web.wrap-response-body-enabled:true}")
    @Bean
    private fun wrapResponseBodyAdvice(): WrapResponseBodyAdvice =
        WrapResponseBodyAdvice()

    @Bean
    private fun exceptionHandler() =
        ExceptionHandler()

    @ConditionalOnExpression("\${web.cors.enabled:true}")
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
    ): ObjectMapper =
        createObjectMapper()
            .apply {
                jackson2ObjectMapperBuilder.configure(this)
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
@ConfigurationProperties(prefix = "web.log.trace")
internal data class TraceLogProperties(
    /**
     * 是否记录trace日志。
     */
    @DefaultValue("true")
    val enabled: Boolean,
    /**
     * trace日志排除url
     */
    val excludePatterns: List<String> = listOf(),
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
@ConfigurationProperties(prefix = "web.cors")
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
            ApiResult(
                Unit,
                HttpServletResponse.SC_FORBIDDEN,
                "Invalid CORS request"
            ).toJsonString().toByteArray(Charsets.UTF_8)
        }
    }

    override fun rejectRequest(response: ServerHttpResponse) {
        response
            .body
            .write(invalidCorsRequestResponseByteArray)
        response.flush()
    }
}
