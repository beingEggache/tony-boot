@file:Suppress("unused")

package com.tony.cache

import com.tony.core.utils.asTo
import com.tony.core.utils.jsonToObj
import java.util.concurrent.TimeUnit

object RedisValues {

    @JvmStatic
    @JvmOverloads
    fun set(
        key: String,
        value: String,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.stringRedisTemplate.opsForValue().set(key, value)
    else RedisUtils.stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun set(
        key: String,
        value: Number,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.redisTemplate.opsForValue().set(key, value)
    else RedisUtils.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.redisTemplate.opsForValue().set(key, value)
    else RedisUtils.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    fun <T> get(key: String, hashKey: String): T? =
        RedisUtils.stringRedisTemplate.boundHashOps<String, T>(key).get(hashKey)

    @JvmStatic
    fun getMap(key: String): Map<String, Any> =
        RedisUtils.redisTemplate.opsForHash<String, Any>().entries(key)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun getInt(key: String): Int? =
        RedisUtils.redisTemplate.opsForValue().get(key).asTo()

    @JvmStatic
    inline fun <reified T> getObject(key: String): T? = getString(key)?.jsonToObj()

    @JvmStatic
    fun getString(key: String) = RedisUtils.stringRedisTemplate.opsForValue().get(key)

    @JvmStatic
    fun hasKey(key: String) = RedisUtils.redisTemplate.hasKey(key)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Long = 1L) = RedisUtils.redisTemplate.boundValueOps(key).increment(delta)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Double = 1.0) = RedisUtils.redisTemplate.boundValueOps(key).increment(delta)
}
