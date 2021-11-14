package com.tony.feign.config

import com.tony.feign.decoder.DefaultErrorDecoder
import com.tony.feign.log.DefaultFeignRequestTraceLogger
import com.tony.feign.log.FeignRequestTraceLogger
import com.tony.feign.log.OpenFeignLogInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import okhttp3.Interceptor
import okhttp3.OkHttpClient

import org.springframework.beans.factory.ObjectFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties

import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder

import java.util.concurrent.TimeUnit

@EnableConfigurationProperties(FeignConfigProperties::class)
@Configuration
class FeignConfig {

    @Bean
    fun encoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringFormEncoder(SpringEncoder(messageConverters))

    @Bean
    fun decoder(messageConverters: ObjectFactory<HttpMessageConverters>): Decoder =
        SpringDecoder(messageConverters)

    @ConditionalOnMissingBean(ErrorDecoder::class)
    @Bean
    fun errorDecoder() = DefaultErrorDecoder()

    @ConditionalOnMissingBean(FeignRequestTraceLogger::class)
    @Bean
    fun feignRequestTraceLogger(): FeignRequestTraceLogger = DefaultFeignRequestTraceLogger()

    @Bean
    internal fun openFeignLogInterceptor(
        feignRequestTraceLogger: FeignRequestTraceLogger
    ) = OpenFeignLogInterceptor(feignRequestTraceLogger)

    @ConditionalOnMissingBean(OkHttpClient::class)
    @Bean
    fun okHttpClient(
        interceptors: List<Interceptor>,
        feignConfigProperties: FeignConfigProperties
    ): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(feignConfigProperties.callTimeout, TimeUnit.SECONDS)
        .connectTimeout(feignConfigProperties.connectTimeout, TimeUnit.SECONDS)
        .readTimeout(feignConfigProperties.readTimeout, TimeUnit.SECONDS)
        .writeTimeout(feignConfigProperties.writeTimeout, TimeUnit.SECONDS)
        .pingInterval(feignConfigProperties.pingInterval, TimeUnit.SECONDS)
        .retryOnConnectionFailure(feignConfigProperties.retryOnConnectionFailure)
        .followRedirects(feignConfigProperties.followRedirects)
        .apply {
            interceptors.forEach(::addInterceptor)
        }
        .build()
}

@ConstructorBinding
@ConfigurationProperties(prefix = "feign")
class FeignConfigProperties(
    val callTimeout: Long = 0,
    val connectTimeout: Long = 10000,
    val readTimeout: Long = 10000,
    val writeTimeout: Long = 10000,
    val pingInterval: Long = 10000,
    val retryOnConnectionFailure: Boolean = true,
    val followRedirects: Boolean = true,
)
