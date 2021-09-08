package com.tony.cache.config

import com.tony.cache.aspect.DefaultRedisCacheAspect
import com.tony.cache.aspect.RedisCacheAspect
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class RedisCacheConfig {

    @Bean
    @ConditionalOnMissingBean(RedisCacheAspect::class)
    fun redisCacheAspect(): RedisCacheAspect = DefaultRedisCacheAspect()
}
