@file:Suppress("unused")

package com.tony.cache

import com.tony.Env
import com.tony.Env.envPropByLazy

object RedisKeys {

    private val keyPrefix: String by
    envPropByLazy("cache.key-prefix", Env.prop("spring.application.name", ""))

    fun genKey(template: String, vararg args: Any?) =
        "$keyPrefix:${String.format(template, *args)}"
}
