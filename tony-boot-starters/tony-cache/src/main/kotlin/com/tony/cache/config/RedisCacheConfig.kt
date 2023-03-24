package com.tony.cache.config

import com.tony.cache.aspect.DefaultRedisCacheAspect
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RedisCacheProperties::class)
internal class RedisCacheConfig {

    @Bean
    internal fun redisCacheAspect(): DefaultRedisCacheAspect = DefaultRedisCacheAspect()
}

@ConstructorBinding
@ConfigurationProperties(prefix = "cache")
private data class RedisCacheProperties(
    val keyPrefix: String?,
)
