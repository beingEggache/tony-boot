package com.tony.cache.config

import com.tony.cache.aspect.DefaultRedisCacheAspect

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class RedisCacheConfig {

    @Bean
    fun redisCacheAspect(): DefaultRedisCacheAspect = DefaultRedisCacheAspect()
}
