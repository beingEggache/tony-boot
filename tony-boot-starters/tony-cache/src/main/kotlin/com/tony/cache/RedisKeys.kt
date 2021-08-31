@file:Suppress("unused")

package com.tony.cache

import com.tony.core.utils.defaultIfBlank
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
object RedisKeys {

    private lateinit var environment: Environment

    private val keyPrefix: String by lazy {
        environment.getProperty("cache.key-prefix")
            .defaultIfBlank(environment.getProperty("spring.application.name", ""))
    }

    @Resource
    private fun environment(environment: Environment) {
        RedisKeys.environment = environment
    }

    fun genKey(template: String, vararg args: Any?) =
        "$keyPrefix:${String.format(template, *args)}"
}
