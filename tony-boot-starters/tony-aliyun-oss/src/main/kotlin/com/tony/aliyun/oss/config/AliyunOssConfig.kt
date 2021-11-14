package com.tony.aliyun.oss.config

import com.tony.aliyun.oss.service.OssService

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties

@Configuration
@EnableConfigurationProperties(AliyunOssProperties::class)
internal class AliyunOssAutoConfiguration(
    private val aliyunOssProperties: AliyunOssProperties
) {

    @Bean
    fun ossService() = let {

        val (
            accessKeyId,
            accessKeySecret,
            bucketName,
            endpoint
        ) = aliyunOssProperties

        OssService(
            accessKeyId,
            accessKeySecret,
            bucketName,
            endpoint
        )
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "aliyun.oss")
internal data class AliyunOssProperties(
    val accessKeyId: String,
    val accessKeySecret: String,
    val bucketName: String,
    val endpoint: String
)
