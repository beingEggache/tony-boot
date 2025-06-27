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

package tony.feign.config

import feign.RequestInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.lang.Nullable
import org.springframework.util.unit.DataSize
import tony.crypto.CryptoProvider
import tony.feign.FeignTargeter
import tony.feign.codec.DefaultErrorDecoder
import tony.feign.interceptor.request.GlobalRequestInterceptorProvider
import tony.feign.interceptor.request.UseRequestProcessorsRequestInterceptor
import tony.feign.interceptor.response.DefaultUnwrapResponseInterceptor
import tony.feign.interceptor.response.GlobalResponseInterceptorProvider
import tony.feign.interceptor.response.UnwrapResponseInterceptorProvider
import tony.feign.log.DefaultFeignRequestLogger
import tony.feign.log.FeignLogInterceptor
import tony.feign.log.FeignRequestLogger
import tony.feign.okhttp.interceptor.AppInterceptor
import tony.feign.okhttp.interceptor.NetworkInterceptor
import tony.misc.YamlPropertySourceFactory

/**
 * FeignConfig
 * @author Tony
 * @date 2023/05/25 19:43
 */
@PropertySource("classpath:feign.config.yml", factory = YamlPropertySourceFactory::class)
@EnableConfigurationProperties(value = [FeignConfigProperties::class, RequestLogProperties::class])
@Configuration(proxyBeanMethods = false)
private class FeignConfig(
    private val requestLogProperties: RequestLogProperties,
) {
    @Bean
    private fun encoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringFormEncoder(SpringEncoder(messageConverters))

    @Bean
    private fun decoder(
        messageConverters: ObjectFactory<HttpMessageConverters>,
        customizers: ObjectProvider<HttpMessageConverterCustomizer>,
    ): Decoder =
        SpringDecoder(messageConverters, customizers)

    @ConditionalOnMissingBean(ErrorDecoder::class)
    @Bean
    private fun errorDecoder() =
        DefaultErrorDecoder()

    @ConditionalOnMissingBean(FeignRequestLogger::class)
    @ConditionalOnExpression(
        "\${spring.cloud.openfeign.okhttp.enabled:true} and \${web.log.request.enabled:true}"
    )
    @Bean
    private fun feignRequestLogger(): FeignRequestLogger =
        DefaultFeignRequestLogger()

    @ConditionalOnExpression(
        "\${spring.cloud.openfeign.okhttp.enabled:true} and \${web.log.request.enabled:true}"
    )
    @Bean
    private fun feignLogInterceptor(feignRequestLogger: FeignRequestLogger) =
        FeignLogInterceptor(
            feignRequestLogger,
            requestLogProperties.requestBodyMaxSize.toBytes(),
            requestLogProperties.responseBodyMaxSize.toBytes()
        )

    @ConditionalOnMissingBean(name = ["useRequestProcessorsRequestInterceptor"])
    @Bean("useRequestProcessorsRequestInterceptor")
    private fun useRequestProcessorsRequestInterceptor(): GlobalRequestInterceptorProvider<RequestInterceptor> =
        GlobalRequestInterceptorProvider(UseRequestProcessorsRequestInterceptor())

    @Bean
    private fun unwrapResponseInterceptorProvider(
        @Nullable
        cryptoProvider: CryptoProvider?,
    ) =
        UnwrapResponseInterceptorProvider(DefaultUnwrapResponseInterceptor(cryptoProvider))

    @Bean
    private fun feignTargeter(
        globalRequestInterceptors: List<GlobalRequestInterceptorProvider<*>>,
        globalResponseInterceptors: List<GlobalResponseInterceptorProvider<*>>,
        unwrapResponseInterceptors: List<UnwrapResponseInterceptorProvider<*>>,
    ) =
        FeignTargeter(
            globalRequestInterceptors.map { it.getObject() },
            globalResponseInterceptors.map { it.getObject() },
            unwrapResponseInterceptors.map { it.getObject() }
        )

    @ConditionalOnExpression("\${spring.cloud.openfeign.okhttp.enabled:true}")
    @ConditionalOnMissingBean(OkHttpClient::class)
    @Bean
    private fun okHttpClient(
        appInterceptors: List<AppInterceptor>,
        networkInterceptors: List<NetworkInterceptor>,
        feignConfigProperties: FeignConfigProperties,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
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
            }.build()
}

@ConfigurationProperties(prefix = "spring.cloud.openfeign.okhttp")
private data class FeignConfigProperties(
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

@ConditionalOnExpression("\${spring.cloud.openfeign.okhttp.enabled:true}")
@ConditionalOnBean(OkHttpClient::class)
@ConfigurationProperties(prefix = "web.log.request")
private data class RequestLogProperties(
    /**
     * 是否记录request日志。
     */
    @DefaultValue("true")
    val enabled: Boolean,
    /**
     * request日志请求体长度, 超过只显示ContentType
     */
    @DefaultValue("50KB")
    val requestBodyMaxSize: DataSize = DataSize.ofKilobytes(50),
    /**
     * request日志响应体长度, 超过只显示ContentType
     */
    @DefaultValue("50KB")
    val responseBodyMaxSize: DataSize = DataSize.ofKilobytes(50),
)
