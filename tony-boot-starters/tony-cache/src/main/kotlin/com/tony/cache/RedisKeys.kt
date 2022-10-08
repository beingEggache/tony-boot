package com.tony.cache

import com.tony.Env
import com.tony.Env.getPropertyByLazy

public object RedisKeys {

    private val keyPrefix: String by
    getPropertyByLazy("cache.key-prefix", Env.getProperty("spring.application.name", ""))

    @JvmStatic
    public fun genKey(template: String, vararg args: Any?): String =
        "$keyPrefix:${String.format(template, *args)}"
}
