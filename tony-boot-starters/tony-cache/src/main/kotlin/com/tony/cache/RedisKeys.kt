@file:Suppress("unused")

package com.tony.cache

import com.tony.cache.config.RedisCacheProperties
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
object RedisKeys {

    private val keyPrefix: String by lazy {
        redisCacheProperties.keyPrefix
    }
    private lateinit var redisCacheProperties: RedisCacheProperties

    @Suppress("unused")
    @Resource
    private fun prop(redisCacheProperties: RedisCacheProperties) {
        RedisKeys.redisCacheProperties = redisCacheProperties
    }

    fun genKey(template: String, vararg args: Any?) =
        "$keyPrefix:${String.format(template, *args)}"
}
