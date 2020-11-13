package com.tony.aliyun.oss.config

import com.tony.aliyun.oss.service.OssService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AliyunOSSProperties::class)
internal class AliyunOSSAutoConfiguration(
    private val aliyunOSSProperties: AliyunOSSProperties) {

    @Bean
    fun ossService() = let {

        val (accessKeyId,
            accessKeySecret,
            bucketName,
            endpoint) = aliyunOSSProperties

        OssService(accessKeyId,
            accessKeySecret,
            bucketName,
            endpoint)
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "aliyun.oss")
internal data class AliyunOSSProperties(
    val accessKeyId: String,
    val accessKeySecret: String,
    val bucketName: String,
    val endpoint: String)
