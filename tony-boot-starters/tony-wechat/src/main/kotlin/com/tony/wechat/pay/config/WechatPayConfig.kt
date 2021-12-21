package com.tony.wechat.pay.config

import com.tony.wechat.WechatPropProvider
import com.tony.wechat.client.WechatPayClient
import com.tony.wechat.config.WechatProperties
import com.tony.wechat.pay.WechatPayManager
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(WechatProperties::class)
internal class WechatPayConfig {

    @Bean
    fun wechatPayService(
        wechatProperties: WechatProperties,
        wechatPayClient: WechatPayClient,
        wechatPropProvider: WechatPropProvider
    ) = WechatPayManager(wechatProperties, wechatPayClient, wechatPropProvider)
}
