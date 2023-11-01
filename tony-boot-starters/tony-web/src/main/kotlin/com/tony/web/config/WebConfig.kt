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

package com.tony.web.config

/**
 * WebConfig
 *
 * @author Tang Li
 * @date 2023/5/25 10:35
 */
import com.fasterxml.jackson.databind.ObjectMapper
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.jackson.InjectableValueSupplier
import com.tony.jackson.InjectableValuesBySupplier
import com.tony.jackson.NullValueBeanSerializerModifier
import com.tony.utils.createObjectMapper
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import com.tony.web.advice.ExceptionHandler
import com.tony.web.advice.InjectRequestBodyAdvice
import com.tony.web.advice.WrapResponseBodyAdvice
import com.tony.web.converter.EnumIntValueConverterFactory
import com.tony.web.converter.EnumStringValueConverterFactory
import com.tony.web.filter.RequestReplaceToRepeatReadFilter
import com.tony.web.filter.TraceIdFilter
import com.tony.web.filter.TraceLoggingFilter
import com.tony.web.listeners.ContextClosedListener
import com.tony.web.listeners.DefaultContextClosedListener
import com.tony.web.log.DefaultRequestTraceLogger
import com.tony.web.log.RequestTraceLogger
import com.tony.web.support.RequestBodyFieldInjector
import com.tony.web.support.RequestBodyFieldInjectorComposite
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.HttpHeaders
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.DefaultCorsProcessor
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * WebConfig
 *
 * @author Tang Li
 * @date 2023/5/25 10:35
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(value = [WebProperties::class, WebCorsProperties::class])
internal class WebConfig(
    private val webProperties: WebProperties,
    private val webCorsProperties: WebCorsProperties,
) : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(EnumIntValueConverterFactory())
        registry.addConverterFactory(EnumStringValueConverterFactory())
    }

    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    internal fun requestReplaceToRepeatReadFilter() =
        RequestReplaceToRepeatReadFilter(webProperties.traceLogExcludePatterns)

    @Bean
    internal fun traceIdFilter() =
        TraceIdFilter()

    @ConditionalOnMissingBean(RequestTraceLogger::class)
    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    internal fun defaultRequestTraceLogger(): RequestTraceLogger =
        DefaultRequestTraceLogger()

    @ConditionalOnExpression("\${web.trace-logger-enabled:true}")
    @Bean
    internal fun traceLoggingFilter(requestTraceLogger: RequestTraceLogger): TraceLoggingFilter {
        val logger = getLogger(TraceLoggingFilter::class.java.name)
        logger.info("Request trace log is enabled")
        if (webProperties.traceLoggerEnabled && webProperties.traceLogExcludePatterns.isNotEmpty()) {
            logger.info("Request trace log exclude patterns: ${webProperties.traceLogExcludePatterns}")
        }
        return TraceLoggingFilter(requestTraceLogger, webProperties.traceLogExcludePatterns)
    }

    @ConditionalOnExpression("\${web.wrap-response-body-enabled:true}")
    @Bean
    internal fun wrapResponseBodyAdvice(): WrapResponseBodyAdvice =
        WrapResponseBodyAdvice()
            .apply {
                getLogger(WrapResponseBodyAdvice::class.java.name)
                    .info("Response wrap is enabled")
            }

    @Bean
    internal fun exceptionHandler() =
        ExceptionHandler()

    @ConditionalOnMissingBean(ContextClosedListener::class)
    @Bean
    internal fun contextClosedListener() =
        DefaultContextClosedListener()

    @ConditionalOnExpression("\${web.cors.enabled:true}")
    @Bean
    internal fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration =
            CorsConfiguration().apply {
                allowCredentials = webCorsProperties.allowCredentials
                allowedOriginPatterns = webCorsProperties.allowedOrigins.ifEmpty { setOf("*") }.toList()
                allowedHeaders = webCorsProperties.allowedHeaders.ifEmpty { setOf("*") }.toList()
                allowedMethods = webCorsProperties.allowedMethods.ifEmpty { setOf("*") }.toList()
                exposedHeaders = webCorsProperties.exposedHeaders.plus(HttpHeaders.CONTENT_DISPOSITION).toList()
                maxAge = webCorsProperties.maxAge

                getLogger(CorsFilter::class.java.name)
                    .info(
                        listOf(
                            "Cors is enabled",
                            "allowCredentials is $allowCredentials",
                            "allowedOrigins is $allowedOriginPatterns",
                            "allowedHeaders is $allowedHeaders",
                            "allowedMethods is $allowedMethods",
                            "exposedHeaders is $exposedHeaders",
                            "maxAge is $maxAge"
                        ).joinToString()
                    )
            }
        source.registerCorsConfiguration("/**", corsConfiguration)
        return CorsFilter(source)
            .apply {
                setCorsProcessor(ApiCorsProcessor())
            }
    }

    @Bean
    internal fun jackson2ObjectMapperBuilder(
        jackson2ObjectMapperBuilderCustomizer: Jackson2ObjectMapperBuilderCustomizer,
    ): Jackson2ObjectMapperBuilder =
        Jackson2ObjectMapperBuilder()
            .apply {
                jackson2ObjectMapperBuilderCustomizer.customize(this)
            }

    @Bean
    internal fun objectMapper(
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

    @Bean
    internal fun initMappingJackson2HttpMessageConverter(
        mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter,
        objectMapper: ObjectMapper,
    ): String {
        if (webProperties.fillResponseNullValueEnabled) {
            getLogger(NullValueBeanSerializerModifier::class.java.name).info("Fill response null value enabled")
            mappingJackson2HttpMessageConverter.objectMapper =
                objectMapper.apply {
                    serializerFactory =
                        serializerFactory
                            .withSerializerModifier(NullValueBeanSerializerModifier())
                }
        }
        return ""
    }

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
    ): InjectRequestBodyAdvice =
        InjectRequestBodyAdvice(requestBodyFieldInjectorComposite)
            .apply {
                getLogger(RequestBodyFieldInjectorComposite::class.java.name)
                    .info("Request body inject is enabled")
            }
}

/**
 * WebProperties
 *
 * @author Tang Li
 * @date 2023/5/25 10:35
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = "web")
internal data class WebProperties
    @ConstructorBinding
    constructor(
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
         * 是否记录请求日志。
         */
        @DefaultValue("true")
        val traceLoggerEnabled: Boolean,
        /**
         * 请求日志排除url
         */
        val traceLogExcludePatterns: List<String> = listOf(),
        /**
         * 是否处理响应json null值。
         */
        @DefaultValue("true")
        val fillResponseNullValueEnabled: Boolean,
    )

/**
 * WebCorsProperties
 *
 * @author Tang Li
 * @date 2023/5/25 10:35
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = "web.cors")
public data class WebCorsProperties
    @ConstructorBinding
    constructor(
        @DefaultValue("false")
        val enabled: Boolean,
        val allowedOrigins: Set<String> = setOf(),
        val allowedHeaders: Set<String> = setOf(),
        val allowedMethods: Set<String> = setOf(),
        val exposedHeaders: Set<String> = setOf(),
        val maxAge: Long = Long.MAX_VALUE,
        @DefaultValue("true")
        val allowCredentials: Boolean,
    )

/**
 * ApiCorsProcessor
 *
 * @author Tang Li
 * @date 2023/5/25 10:35
 */
internal class ApiCorsProcessor : DefaultCorsProcessor() {
    private companion object {
        @JvmStatic
        val invalidCorsRequestResponseByteArray by lazy(LazyThreadSafetyMode.NONE) {
            ApiResult(
                EMPTY_RESULT,
                HttpServletResponse.SC_FORBIDDEN,
                "Invalid CORS request"
            ).toJsonString().toByteArray()
        }
    }

    override fun rejectRequest(response: ServerHttpResponse) {
        response.body.write(invalidCorsRequestResponseByteArray)
        response.flush()
    }
}
