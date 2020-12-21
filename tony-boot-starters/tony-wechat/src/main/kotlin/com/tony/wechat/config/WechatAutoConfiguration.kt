package com.tony.wechat.config

import com.tony.wechat.service.WechatService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(WechatProperties::class)
internal class WechatAutoConfiguration(
    private val wechatProperties: WechatProperties
) {

    @Bean
    fun wechatService() =
        let {
            val (
                subscriptionAppId,
                miniProgramAppId,
                appSecret,
                miniProgramAppSecret
            ) = wechatProperties

            WechatService(
                miniProgramAppId,
                subscriptionAppId,
                appSecret,
                miniProgramAppSecret
            )
        }
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
data class WechatProperties(
    val subscriptionAppId: String,
    val miniProgramAppId: String,
    val appSecret: String,
    val miniProgramAppSecret: String
)
