package com.tony.aliyun.sms.config

import com.tony.aliyun.sms.AliyunSmsManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunSmsProperties::class)
public class AliyunSmsConfig(
    private val aliyunSMSProperties: AliyunSmsProperties,
) {

    @Bean
    internal fun smsService() = AliyunSmsManager(
        aliyunSMSProperties.accessKeyId,
        aliyunSMSProperties.accessKeySecret,
        aliyunSMSProperties.signName,
        aliyunSMSProperties.timeout
    )
}

@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsProperties
    @ConstructorBinding
    constructor(
        public val accessKeyId: String,
        public val accessKeySecret: String,
        public val signName: String,
        public val timeout: String,
    )
