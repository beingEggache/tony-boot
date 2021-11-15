package com.tony.wechat.pay.config

import com.tony.wechat.client.WechatPayClient
import com.tony.wechat.pay.service.WechatPayService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(WechatPayProperties::class)
internal class WechatPayAutoConfiguration {

    @Bean
    fun wechatPayService(
        wechatProperties: WechatPayProperties,
        wechatPayClient: WechatPayClient
    ) = WechatPayService(wechatProperties, wechatPayClient)
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
data class WechatPayProperties(
    val appId: String?,
    val miniProgramAppId: String?,
    val subscriptionAppId: String?,
    val mchId: String?,
    val mchSecretKey: String?
)
