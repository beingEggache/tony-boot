package com.tony.aliyun.sms.config

import com.tony.aliyun.sms.service.SmsService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunSMSProperties::class)
class AliyunSmsAutoConfiguration(
    private val aliyunSMSProperties: AliyunSMSProperties
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
class AliyunSMSProperties {
    var accessKeyId: String? = null
    var accessKeySecret: String? = null
    var signName: String? = null
    var timeout: String? = null
}
