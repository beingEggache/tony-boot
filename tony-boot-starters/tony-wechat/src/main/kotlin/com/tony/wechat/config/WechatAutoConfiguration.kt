package com.tony.wechat.config

import com.tony.wechat.client.WechatClient
import com.tony.wechat.service.WechatService
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
internal class WechatAutoConfiguration {

    @Resource
    lateinit var mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter

    @PostConstruct
    fun init() {
        val supportedMediaTypes = mappingJackson2HttpMessageConverter.supportedMediaTypes
        mappingJackson2HttpMessageConverter.supportedMediaTypes =
            listOf(*supportedMediaTypes.toTypedArray(), MediaType.TEXT_PLAIN)
    }

    @Bean
    fun wechatService(
        wechatProperties: WechatProperties,
        wechatClient: WechatClient
    ) = WechatService(wechatProperties, wechatClient)
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
data class WechatProperties(
    val subscriptionAppId: String?,
    val miniProgramAppId: String?,
    val appSecret: String?,
    val miniProgramAppSecret: String?
)
