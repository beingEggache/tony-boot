package com.tony.aliyun.oss.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunOssProperties::class)
internal class AliyunOssConfig

@ConstructorBinding
@ConfigurationProperties(prefix = "aliyun.oss")
internal data class AliyunOssProperties(
    val accessKeyId: String,
    val accessKeySecret: String,
    val bucketName: String,
    val endpoint: String
)
