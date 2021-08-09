package com.tony.cache.config

import com.tony.cache.aspect.DefaultRedisCacheAspect
import com.tony.cache.aspect.RedisCacheAspect
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RedisCacheProperties::class)
internal class RedisCacheConfig {

    @Bean
    @ConditionalOnMissingBean(RedisCacheAspect::class)
    fun redisCacheAspect() = DefaultRedisCacheAspect()
}

@ConstructorBinding
@ConfigurationProperties(prefix = "cache")
internal data class RedisCacheProperties(
    val keyPrefix: String = ""
)
