package com.tony.openfeign.config

import com.tony.openfeign.decoder.DefaultErrorDecoder
import com.tony.openfeign.log.DefaultFeignRequestTraceLogger
import com.tony.openfeign.log.FeignRequestTraceLogger
import com.tony.openfeign.log.OpenFeignLogInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@EnableConfigurationProperties(OpenFeignConfigProperties::class)
@Configuration
class OpenFeignConfig {

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
        openFeignConfigProperties: OpenFeignConfigProperties
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(openFeignConfigProperties.callTimeout, TimeUnit.SECONDS)
            .connectTimeout(openFeignConfigProperties.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(openFeignConfigProperties.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(openFeignConfigProperties.writeTimeout, TimeUnit.SECONDS)
            .pingInterval(openFeignConfigProperties.pingInterval, TimeUnit.SECONDS)
            .retryOnConnectionFailure(openFeignConfigProperties.retryOnConnectionFailure)
            .followRedirects(openFeignConfigProperties.followRedirects)
            .apply {
                interceptors.forEach(::addInterceptor)
            }
            .build()
        return okHttpClient
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "open-feign")
class OpenFeignConfigProperties(
    val callTimeout: Long = 0,
    val connectTimeout: Long = 10000,
    val readTimeout: Long = 10000,
    val writeTimeout: Long = 10000,
    val pingInterval: Long = 10000,
    val retryOnConnectionFailure: Boolean = true,
    val followRedirects: Boolean = true,
)
