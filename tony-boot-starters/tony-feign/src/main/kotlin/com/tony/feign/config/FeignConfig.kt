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

package com.tony.feign.config

import com.tony.feign.FeignTargeter
import com.tony.feign.codec.DefaultErrorDecoder
import com.tony.feign.interceptor.request.GlobalRequestInterceptorProvider
import com.tony.feign.interceptor.response.GlobalResponseInterceptorProvider
import com.tony.feign.interceptor.response.UnwrapResponseInterceptor
import com.tony.feign.interceptor.response.UnwrapResponseInterceptorProvider
import com.tony.feign.log.DefaultFeignRequestTraceLogger
import com.tony.feign.log.FeignLogInterceptor
import com.tony.feign.log.FeignRequestTraceLogger
import com.tony.feign.okhttp.interceptor.AppInterceptor
import com.tony.feign.okhttp.interceptor.NetworkInterceptor
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
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * FeignConfig
 * @author Tony
 * @date 2023/5/25 15:43
 * @since 1.0.0
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
    ): Decoder = SpringDecoder(messageConverters, customizers)

    @ConditionalOnMissingBean(ErrorDecoder::class)
    @Bean
    internal fun errorDecoder() = DefaultErrorDecoder()

    @ConditionalOnMissingBean(FeignRequestTraceLogger::class)
    @ConditionalOnExpression("\${spring.cloud.openfeign.okhttp.enabled:true}")
    @Bean
    internal fun feignRequestTraceLogger(): FeignRequestTraceLogger = DefaultFeignRequestTraceLogger()

    @ConditionalOnExpression("\${spring.cloud.openfeign.okhttp.enabled:true}")
    @Bean
    internal fun feignLogInterceptor(
        feignRequestTraceLogger: FeignRequestTraceLogger,
    ) = FeignLogInterceptor(feignRequestTraceLogger)

    @Bean
    internal fun unwrapResponseInterceptorProvider() =
        UnwrapResponseInterceptorProvider(UnwrapResponseInterceptor())

    @Bean
    internal fun feignTargeter(
        globalRequestInterceptors: List<GlobalRequestInterceptorProvider<*>>,
        globalResponseInterceptors: List<GlobalResponseInterceptorProvider<*>>,
        unwrapResponseInterceptorProvider: UnwrapResponseInterceptorProvider,
    ) = FeignTargeter(
        globalRequestInterceptors.map { it.getObject() },
        globalResponseInterceptors.map { it.getObject() },
        unwrapResponseInterceptorProvider.`object`
    )

    @ConditionalOnExpression("\${spring.cloud.openfeign.okhttp.enabled:true}")
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

@ConfigurationProperties(prefix = "spring.cloud.openfeign.okhttp")
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
