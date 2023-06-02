package com.tony.redis

import com.tony.SpringContexts.Env

/**
 * redis key 操作静态单例.
 *
 * @author tangli
 * @since 2023/5/24 18:12
 */
public object RedisKeys {

    private val keyPrefix: String by Env.getPropertyByLazy("cache.key-prefix", "")

    /**
     * 生成redis 缓存键名.
     */
    @JvmStatic
    public fun genKey(template: String, vararg args: Any?): String =
        "${if (keyPrefix.isBlank()) "" else "$keyPrefix:"}${String.format(template, *args)}"
}
