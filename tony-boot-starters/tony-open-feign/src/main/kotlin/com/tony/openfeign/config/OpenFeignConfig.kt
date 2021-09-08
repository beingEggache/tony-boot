package com.tony.openfeign.config

import com.tony.core.utils.createObjectMapper
import com.tony.openfeign.log.DefaultFeignRequestTraceLogger
import com.tony.openfeign.log.FeignRequestTraceLogger
import feign.Client
import feign.codec.Decoder
import feign.codec.Encoder
import feign.form.spring.SpringFormEncoder
import feign.okhttp.OkHttpClient
import okhttp3.Interceptor
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
class OpenFeignConfig {

    private val objectMapper = createObjectMapper()

    @Resource
    lateinit var mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter

    @PostConstruct
    fun init() {
        mappingJackson2HttpMessageConverter.objectMapper = objectMapper
    }

    @Bean
    fun encoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringFormEncoder(SpringEncoder(messageConverters))

    @Bean
    fun decoder(messageConverters: ObjectFactory<HttpMessageConverters>): Decoder =
        SpringDecoder(messageConverters)

    @ConditionalOnMissingBean(FeignRequestTraceLogger::class)
    @Bean
    fun feignRequestTraceLogger(): FeignRequestTraceLogger = DefaultFeignRequestTraceLogger()

    @ConditionalOnMissingBean(Client::class)
    @Bean
    fun okHttpClient(interceptors: List<Interceptor>): Client {
        val okHttpClient = okhttp3.OkHttpClient.Builder()
            .callTimeout(OpenFeignConfigProperties.callTimeout, TimeUnit.SECONDS)
            .connectTimeout(OpenFeignConfigProperties.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(OpenFeignConfigProperties.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(OpenFeignConfigProperties.writeTimeout, TimeUnit.SECONDS)
            .pingInterval(OpenFeignConfigProperties.pingInterval, TimeUnit.SECONDS)
            .retryOnConnectionFailure(OpenFeignConfigProperties.retryOnConnectionFailure)
            .followRedirects(OpenFeignConfigProperties.followRedirects)
            .apply {
                interceptors.forEach(::addInterceptor)
            }
            .build()
        return OkHttpClient(okHttpClient)
    }
}

@Component
internal object OpenFeignConfigProperties {

    private lateinit var environment: Environment

    @Resource
    @JvmSynthetic
    private fun environment(environment: Environment) {
        OpenFeignConfigProperties.environment = environment
    }

    val callTimeout: Long by lazy {
        environment.getProperty("open-feign.call-timeout", Long::class.java, 0)
    }
    val connectTimeout: Long by lazy {
        environment.getProperty<Long>("open-feign.connect-timeout", Long::class.java, 10000)
    }
    val readTimeout: Long by lazy {
        environment.getProperty("open-feign.read-timeout", Long::class.java, 10000)
    }
    val writeTimeout: Long by lazy {
        environment.getProperty("open-feign.write-timeout", Long::class.java, 10000)
    }
    val pingInterval: Long by lazy {
        environment.getProperty("open-feign.ping-interval", Long::class.java, 10000)
    }
    val retryOnConnectionFailure: Boolean by lazy {
        environment.getProperty("open-feign.retry-on-connection-failure", Boolean::class.java, true)
    }
    val followRedirects: Boolean by lazy {
        environment.getProperty("open-feign.follow-redirects", Boolean::class.java, true)
    }
}
