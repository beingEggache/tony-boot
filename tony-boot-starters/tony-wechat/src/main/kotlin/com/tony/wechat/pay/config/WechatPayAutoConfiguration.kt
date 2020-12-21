package com.tony.wechat.pay.config

import com.tony.wechat.pay.service.WechatPayService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(WechatPayProperties::class)
internal class WechatPayAutoConfiguration(
    private val wechatProperties: WechatPayProperties
) {

    @Bean
    fun wechatPayService() =
        let {
            val (
                appId,
                miniProgramAppId,
                subscriptionAppId,
                mchId,
                mchSecretKey
            ) = wechatProperties

            WechatPayService(
                appId,
                miniProgramAppId,
                subscriptionAppId,
                mchId,
                mchSecretKey
            )
        }
}

@Suppress("ConfigurationProperties")
@ConstructorBinding
@ConfigurationProperties(prefix = "wechat")
internal data class WechatPayProperties(
    val appId: String,
    val miniProgramAppId: String,
    val subscriptionAppId: String,
    val mchId: String,
    val mchSecretKey: String
)
