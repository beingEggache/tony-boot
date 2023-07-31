package com.tony.feign.config

import com.tony.feign.decoder.DefaultErrorDecoder
import com.tony.feign.decoder.UnwrapResponseDecoder
import com.tony.feign.interceptor.AppInterceptor
import com.tony.feign.interceptor.DefaultGlobalHeaderInterceptor
import com.tony.feign.interceptor.GlobalHeaderInterceptor
import com.tony.feign.interceptor.NetworkInterceptor
import com.tony.feign.log.DefaultFeignRequestTraceLogger
import com.tony.feign.log.FeignLogInterceptor
import com.tony.feign.log.FeignRequestTraceLogger
import com.tony.misc.YamlPropertySourceFactory
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * FeignConfig
 *
 * @author tangli
 * @since 2023/5/25 15:43
 */
@EnableConfigurationProperties(FeignConfigProperties::class)
@Configuration
@PropertySource("classpath:feign.config.yml", factory = YamlPropertySourceFactory::class)
public class FeignConfig {

    @Bean
    internal fun encoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringFormEncoder(SpringEncoder(messageConverters))

    @Bean
    internal fun decoder(
        messageConverters: ObjectFactory<HttpMessageConverters>,
        customizers: ObjectProvider<HttpMessageConverterCustomizer>,
    ): Decoder = UnwrapResponseDecoder(messageConverters, customizers)

    @ConditionalOnMissingBean(ErrorDecoder::class)
    @Bean
    internal fun errorDecoder() = DefaultErrorDecoder()

    @ConditionalOnMissingBean(FeignRequestTraceLogger::class)
    @ConditionalOnExpression("\${feign.okhttp.enabled:true}")
    @Bean
    internal fun feignRequestTraceLogger(): FeignRequestTraceLogger = DefaultFeignRequestTraceLogger()

    @ConditionalOnExpression("\${feign.okhttp.enabled:true}")
    @Bean
    internal fun feignLogInterceptor(
        feignRequestTraceLogger: FeignRequestTraceLogger,
    ) = FeignLogInterceptor(feignRequestTraceLogger)

    @ConditionalOnMissingBean(GlobalHeaderInterceptor::class)
    @Bean
    internal fun globalHeaderInterceptor() = DefaultGlobalHeaderInterceptor()

    @ConditionalOnExpression("\${feign.okhttp.enabled:true}")
    @ConditionalOnMissingBean(OkHttpClient::class)
    @Bean
    internal fun okHttpClient(
        appInterceptors: List<AppInterceptor>,
        networkInterceptors: List<NetworkInterceptor>,
        feignConfigProperties: FeignConfigProperties,
    ): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(feignConfigProperties.callTimeout, TimeUnit.SECONDS)
        .connectTimeout(feignConfigProperties.connectTimeout, TimeUnit.SECONDS)
        .readTimeout(feignConfigProperties.readTimeout, TimeUnit.SECONDS)
        .writeTimeout(feignConfigProperties.writeTimeout, TimeUnit.SECONDS)
        .pingInterval(feignConfigProperties.pingInterval, TimeUnit.SECONDS)
        .retryOnConnectionFailure(feignConfigProperties.retryOnConnectionFailure)
        .followRedirects(feignConfigProperties.followRedirects)
        .apply {
            appInterceptors.forEach(::addInterceptor)
            networkInterceptors.forEach(::addNetworkInterceptor)
        }
        .build()
}

@ConfigurationProperties(prefix = "feign")
internal data class FeignConfigProperties
    @ConstructorBinding
    constructor(
        @DefaultValue("0")
        val callTimeout: Long,
        @DefaultValue("10000")
        val connectTimeout: Long,
        @DefaultValue("10000")
        val readTimeout: Long,
        @DefaultValue("10000")
        val writeTimeout: Long,
        @DefaultValue("10000")
        val pingInterval: Long,
        @DefaultValue("true")
        val retryOnConnectionFailure: Boolean,
        @DefaultValue("true")
        val followRedirects: Boolean,
    )
