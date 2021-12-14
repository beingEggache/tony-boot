package com.tony.wechat.config

import com.tony.wechat.client.WechatClient
import com.tony.wechat.service.DefaultWechatApiAccessTokenProviderWrapper
import com.tony.wechat.service.DefaultWechatPropProvider
import com.tony.wechat.service.WechatApiAccessTokenProvider
import com.tony.wechat.service.WechatApiAccessTokenProviderWrapper
import com.tony.wechat.service.WechatPropProvider
import com.tony.wechat.service.WechatService
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
    fun apiAccessTokenProvider(wechatClient: WechatClient) = WechatApiAccessTokenProvider(wechatClient)

    @ConditionalOnMissingBean(WechatApiAccessTokenProviderWrapper::class)
    @Bean
    fun apiAccessTokenProviderWrapper(
        apiAccessTokenProvider: WechatApiAccessTokenProvider
    ): WechatApiAccessTokenProviderWrapper =
        DefaultWechatApiAccessTokenProviderWrapper(apiAccessTokenProvider)

    @ConditionalOnMissingBean(WechatPropProvider::class)
    @Bean
    fun wechatApiPropProvider(wechatProperties: WechatProperties) = DefaultWechatPropProvider(wechatProperties)

    @Bean
    fun wechatService(
        wechatClient: WechatClient,
        wechatPropProvider: WechatPropProvider,
        apiAccessTokenProviderWrapper: WechatApiAccessTokenProviderWrapper
    ) = WechatService(wechatClient, wechatPropProvider, apiAccessTokenProviderWrapper)
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
data class WechatProperties(
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?,
    val app: Map<String, WechatAppProperties>
) {
    fun sourceByAppId(appId: String) = app.entries.filter { it.value.appId == appId }.firstOrNull()?.key
}

data class WechatAppProperties(
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?
)
