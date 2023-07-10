package com.tony.wechat.config

import com.tony.wechat.DefaultWechatApiAccessTokenProvider
import com.tony.wechat.DefaultWechatPropProvider
import com.tony.wechat.WechatApiAccessTokenProvider
import com.tony.wechat.WechatPropProvider
import com.tony.wechat.client.WechatClient
import javax.annotation.Resource
import kotlin.reflect.full.findAnnotation
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.FeignClientBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
@EnableConfigurationProperties(WechatProperties::class)
internal class WechatConfig {

    @Resource
    private fun initMappingJackson2HttpMessageConverter(
        mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter,
    ) {
        val supportedMediaTypes = mappingJackson2HttpMessageConverter.supportedMediaTypes
        mappingJackson2HttpMessageConverter.supportedMediaTypes =
            listOf(*supportedMediaTypes.toTypedArray(), MediaType.TEXT_PLAIN)
    }

    @Bean
    internal fun wechatClient(applicationContext: ApplicationContext): WechatClient =
        WechatClient::class.findAnnotation<FeignClient>().let {
            FeignClientBuilder(applicationContext)
                .forType(WechatClient::class.java, it?.value)
                .url(it?.url)
                .build()
        }

    @ConditionalOnMissingBean(WechatApiAccessTokenProvider::class)
    @Bean
    internal fun apiAccessTokenProviderWrapper(): WechatApiAccessTokenProvider = DefaultWechatApiAccessTokenProvider()

    @ConditionalOnMissingBean(WechatPropProvider::class)
    @Bean
    internal fun wechatApiPropProvider(wechatProperties: WechatProperties) = DefaultWechatPropProvider(wechatProperties)
}

/**
 * 微信配置
 *
 * @author tangli
 * @since 2023/5/25 15:22
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
public data class WechatProperties(
    val token: String?,
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?,
    val app: LinkedHashMap<String, WechatAppProperties>?,
) {
    public fun getAppByAppId(appId: String): String? = app?.entries?.firstOrNull { it.value.appId == appId }?.key
}

/**
 * 微信配置
 *
 * @author tangli
 * @since 2023/5/25 15:22
 */
public data class WechatAppProperties(
    val token: String?,
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?,
)
