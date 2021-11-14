package com.tony.aliyun.sms.config

import com.tony.aliyun.sms.service.SmsService

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@Configuration
@EnableConfigurationProperties(AliyunSmsProperties::class)
class AliyunSmsAutoConfiguration(
    private val aliyunSMSProperties: AliyunSmsProperties
) {

    @Bean
    fun smsService() = SmsService(
        aliyunSMSProperties.accessKeyId ?: "",
        aliyunSMSProperties.accessKeySecret ?: "",
        aliyunSMSProperties.signName ?: "",
        aliyunSMSProperties.timeout ?: ""
    )
}

@ConfigurationProperties(prefix = "aliyun.sms")
class AliyunSmsProperties {
    var accessKeyId: String? = null
    var accessKeySecret: String? = null
    var signName: String? = null
    var timeout: String? = null
}
