package com.tony.aliyun.oss.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunOssProperties::class)
internal class AliyunOssConfig

@ConfigurationProperties(prefix = "aliyun.oss")
internal data class AliyunOssProperties
    @ConstructorBinding
    constructor(
        val accessKeyId: String,
        val accessKeySecret: String,
        val bucketName: String,
        val endpoint: String,
    )
