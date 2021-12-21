package com.tony.wechat.config

import com.tony.wechat.DefaultWechatApiAccessTokenProviderWrapper
import com.tony.wechat.DefaultWechatPropProvider
import com.tony.wechat.WechatApiAccessTokenProvider
import com.tony.wechat.WechatApiAccessTokenProviderWrapper
import com.tony.wechat.WechatManager
import com.tony.wechat.WechatPropProvider
import com.tony.wechat.client.WechatClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import javax.annotation.PostConstruct
import javax.annotation.Resource

@EnableFeignClients("com.tony.wechat.client")
@Configuration
@EnableConfigurationProperties(WechatProperties::class)
internal class WechatConfig {

    @Resource
    lateinit var mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter

    @PostConstruct
    fun init() {
        val supportedMediaTypes = mappingJackson2HttpMessageConverter.supportedMediaTypes
        mappingJackson2HttpMessageConverter.supportedMediaTypes =
            listOf(*supportedMediaTypes.toTypedArray(), MediaType.TEXT_PLAIN)
    }

    @Bean
    fun apiAccessTokenProvider() = WechatApiAccessTokenProvider()

    @ConditionalOnMissingBean(WechatApiAccessTokenProviderWrapper::class)
    @Bean
    fun apiAccessTokenProviderWrapper(
        apiAccessTokenProvider: WechatApiAccessTokenProvider
    ): WechatApiAccessTokenProviderWrapper =
        DefaultWechatApiAccessTokenProviderWrapper(apiAccessTokenProvider)

    @ConditionalOnMissingBean(WechatPropProvider::class)
    @Bean
    fun wechatApiPropProvider(wechatProperties: WechatProperties) =
        DefaultWechatPropProvider(wechatProperties)

    @Bean
    fun wechatService(
        wechatClient: WechatClient,
        wechatPropProvider: WechatPropProvider,
        apiAccessTokenProviderWrapper: WechatApiAccessTokenProviderWrapper
    ) = WechatManager(wechatClient, wechatPropProvider, apiAccessTokenProviderWrapper)
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
data class WechatProperties(
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?,
    val app: LinkedHashMap<String, WechatAppProperties>
) {
    fun getAppByAppId(appId: String) = app.entries.firstOrNull { it.value.appId == appId }?.key
}

data class WechatAppProperties(
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?
)
