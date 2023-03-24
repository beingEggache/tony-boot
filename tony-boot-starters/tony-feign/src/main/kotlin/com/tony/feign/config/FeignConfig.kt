package com.tony.feign.config

import com.tony.feign.decoder.DefaultErrorDecoder
import com.tony.feign.interceptor.AppInterceptor
import com.tony.feign.interceptor.NetworkInterceptor
import com.tony.feign.log.DefaultFeignRequestTraceLogger
import com.tony.feign.log.FeignLogInterceptor
import com.tony.feign.log.FeignRequestTraceLogger
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import okhttp3.OkHttpClient
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.util.concurrent.TimeUnit

@EnableConfigurationProperties(FeignConfigProperties::class)
@Configuration
@PropertySource("classpath:feign.properties")
public class FeignConfig {

    @Bean
    internal fun encoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringFormEncoder(SpringEncoder(messageConverters))

    @Bean
    internal fun decoder(
        messageConverters: ObjectFactory<HttpMessageConverters>,
        customizers: ObjectProvider<HttpMessageConverterCustomizer>,
    ): Decoder = SpringDecoder(messageConverters, customizers)

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

@ConstructorBinding
@ConfigurationProperties(prefix = "feign")
internal data class FeignConfigProperties(
    @DefaultValue("0")
    val callTimeout: Long = 0,
    @DefaultValue("10000")
    val connectTimeout: Long = 10000,
    @DefaultValue("10000")
    val readTimeout: Long = 10000,
    @DefaultValue("10000")
    val writeTimeout: Long = 10000,
    @DefaultValue("10000")
    val pingInterval: Long = 10000,
    @DefaultValue("true")
    val retryOnConnectionFailure: Boolean = true,
    @DefaultValue("true")
    val followRedirects: Boolean = true,
)
