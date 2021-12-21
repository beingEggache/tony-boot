@file:Suppress("unused")

package com.tony.cache

import com.tony.Env
import com.tony.Env.getPropertyByLazy

object RedisKeys {

    private val keyPrefix: String by
    getPropertyByLazy("cache.key-prefix", Env.getProperty("spring.application.name", ""))

    @JvmStatic
    fun genKey(template: String, vararg args: Any?) =
        "$keyPrefix:${String.format(template, *args)}"
}
