@file:Suppress("unused")

package com.tony.cache

import com.tony.core.utils.doIf
import com.tony.core.utils.jsonToObj
import java.util.Date
import java.util.concurrent.TimeUnit

@Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
object RedisMaps {

    @JvmStatic
    fun delete(key: String, vararg hashKey: String) =
        RedisUtils.redisTemplate.opsForHash<String, Any>()
            .hasKey(key, hashKey)
            .doIf {
                RedisUtils.redisTemplate.opsForHash<String, Any>().delete(key, hashKey)
            }

    @JvmStatic
    fun <T> get(key: String, hashKey: String): T? = RedisUtils.stringRedisTemplate.boundHashOps<String, T>(key).get(hashKey)

    @JvmStatic
    fun getMap(key: String): Map<String, Any> =
        RedisUtils.redisTemplate.opsForHash<String, Any>().entries(key)

    @JvmStatic
    inline fun <reified T> getObject(key: String, hashKey: String): T? =
        get<String>(key, hashKey)?.jsonToObj()

    @JvmStatic
    fun hasKey(key: String, hashKey: String): Boolean =
        RedisUtils.stringRedisTemplate.boundHashOps<String, Any>(key).hasKey(hashKey) ?: false

    @JvmStatic
    @JvmOverloads
    fun <T> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        if (timeout == 0L) {
            RedisUtils.redisTemplate.boundHashOps<String, T>(key).put(hashKey, value)
        } else {
            RedisUtils.redisTemplate.boundHashOps<String, T>(key).apply {
                put(hashKey, value)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    fun <T> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date
    ) {
        RedisUtils.redisTemplate.boundHashOps<String, T>(key).apply {
            put(hashKey, value)
            expireAt(date)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> putIfAbsent(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        if (timeout == 0L) {
            RedisUtils.redisTemplate.boundHashOps<String, T>(key).putIfAbsent(hashKey, value)
        } else {
            RedisUtils.redisTemplate.boundHashOps<String, T>(key).apply {
                putIfAbsent(hashKey, value)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    fun <T> putIfAbsent(
        key: String,
        hashKey: String,
        value: T,
        date: Date
    ) {
        RedisUtils.redisTemplate.boundHashOps<String, T>(key).apply {
            putIfAbsent(hashKey, value)
            expireAt(date)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> putAll(
        key: String,
        map: Map<String, T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        if (timeout == 0L) {
            RedisUtils.redisTemplate.boundHashOps<String, T>(key).putAll(map)
        } else {
            RedisUtils.redisTemplate.boundHashOps<String, T>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    fun <T> putAll(
        key: String,
        map: Map<String, T>,
        date: Date
    ) {
        RedisUtils.redisTemplate.boundHashOps<String, T>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }
}
