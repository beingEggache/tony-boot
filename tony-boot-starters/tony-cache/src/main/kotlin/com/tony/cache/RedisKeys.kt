package com.tony.cache

import com.tony.SpringContexts.Env

public object RedisKeys {

    private val keyPrefix: String by Env.getPropertyByLazy("cache.key-prefix", "")

    @JvmStatic
    public fun genKey(template: String, vararg args: Any?): String =
        "${if (keyPrefix.isBlank()) "" else "$keyPrefix:"}${String.format(template, *args)}"
}
