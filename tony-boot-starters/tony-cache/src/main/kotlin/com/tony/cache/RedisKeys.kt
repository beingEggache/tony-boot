@file:Suppress("unused")

package com.tony.cache

import com.tony.Env

object RedisKeys {

    private val keyPrefix: String by lazy {
        Env.prop("cache.key-prefix", Env.prop("spring.application.name", ""))
    }

    fun genKey(template: String, vararg args: Any?) =
        "$keyPrefix:${String.format(template, *args)}"
}
