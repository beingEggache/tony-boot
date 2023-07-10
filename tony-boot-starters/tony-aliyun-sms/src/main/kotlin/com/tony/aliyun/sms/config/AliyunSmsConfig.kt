package com.tony.aliyun.sms.config

import com.tony.aliyun.sms.AliyunSmsManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunSmsProperties::class)
public class AliyunSmsConfig(
    private val aliyunSMSProperties: AliyunSmsProperties,
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
public class AliyunSmsProperties {
    public val accessKeyId: String? = null
    public val accessKeySecret: String? = null
    public val signName: String? = null
    public val timeout: String? = null
}
