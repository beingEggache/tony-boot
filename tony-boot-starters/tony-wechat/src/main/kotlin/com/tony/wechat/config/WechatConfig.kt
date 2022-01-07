@file:Suppress("SpringComponentScan")

package com.tony.wechat.config

import com.tony.PROJECT_GROUP
import com.tony.wechat.DefaultWechatApiAccessTokenProvider
import com.tony.wechat.DefaultWechatPropProvider
import com.tony.wechat.WechatApiAccessTokenProvider
import com.tony.wechat.WechatPropProvider
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

@EnableFeignClients("$PROJECT_GROUP.wechat.client")
@Configuration
@EnableConfigurationProperties(WechatProperties::class)
internal class WechatConfig {

    @Resource
    lateinit var mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter

    @PostConstruct
    private fun init() {
        val supportedMediaTypes = mappingJackson2HttpMessageConverter.supportedMediaTypes
        mappingJackson2HttpMessageConverter.supportedMediaTypes =
            listOf(*supportedMediaTypes.toTypedArray(), MediaType.TEXT_PLAIN)
    }

    @ConditionalOnMissingBean(WechatApiAccessTokenProvider::class)
    @Bean
    internal fun apiAccessTokenProviderWrapper(): WechatApiAccessTokenProvider =
        DefaultWechatApiAccessTokenProvider()

    @ConditionalOnMissingBean(WechatPropProvider::class)
    @Bean
    internal fun wechatApiPropProvider(wechatProperties: WechatProperties) =
        DefaultWechatPropProvider(wechatProperties)
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
data class WechatProperties(
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?,
    val app: LinkedHashMap<String, WechatAppProperties>?
) {
    fun getAppByAppId(appId: String) = app?.entries?.firstOrNull { it.value.appId == appId }?.key
}

data class WechatAppProperties(
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?
)
