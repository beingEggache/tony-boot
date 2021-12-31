package com.tony.aliyun.sms.config

import com.tony.aliyun.sms.AliyunSmsManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunSmsProperties::class)
class AliyunSmsConfig(
    private val aliyunSMSProperties: AliyunSmsProperties
) {

    @Bean
    internal fun smsService() = AliyunSmsManager(
        aliyunSMSProperties.accessKeyId ?: "",
        aliyunSMSProperties.accessKeySecret ?: "",
        aliyunSMSProperties.signName ?: "",
        aliyunSMSProperties.timeout ?: ""
    )
}

@ConstructorBinding
@ConfigurationProperties(prefix = "aliyun.sms")
class AliyunSmsProperties {
    var accessKeyId: String? = null
    var accessKeySecret: String? = null
    var signName: String? = null
    var timeout: String? = null
}
