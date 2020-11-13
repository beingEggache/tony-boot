package com.tony.cache.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RedisCacheProperties::class)
internal class RedisCacheConfig

@ConstructorBinding
@ConfigurationProperties(prefix = "cache")
internal data class RedisCacheProperties(
    val keyPrefix: String = ""
)
