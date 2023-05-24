package com.tony.cache

import com.tony.cache.RedisManager.trimQuotes
import com.tony.enums.EnumCreator
import com.tony.enums.EnumValue
import com.tony.utils.doIf
import com.tony.utils.getLogger
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import org.springframework.data.redis.core.RedisTemplate
import java.io.Serializable
import java.util.Date
import java.util.concurrent.TimeUnit

public object RedisMaps {

    private val logger = getLogger()

    @JvmStatic
    public fun hasKey(key: String, hashKey: String): Boolean =
        true == RedisManager.redisTemplate.boundHashOps<String, Any>(key).hasKey(hashKey)

    @JvmStatic
    public fun delete(key: String, vararg hashKey: String): Unit =
        RedisManager.redisTemplate.opsForHash<String, Any>()
            .hasKey(key, hashKey)
            .doIf {
                RedisManager.redisTemplate.opsForHash<String, Any>().delete(key, hashKey)
            }

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> putObj(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    @JvmStatic
    public fun <T : Any> putObj(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, date)

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    @JvmStatic
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, date)

    @JvmStatic
    @JvmOverloads
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ) {
        if (map == null) {
            logger.warn("Arg map is null.")
            return
        }
        if (timeout == 0L) {
            RedisManager.redisTemplate.boundHashOps<String, Any?>(key).putAll(map)
        } else {
            RedisManager.redisTemplate.boundHashOps<String, Any?>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ) {
        if (map == null) {
            logger.warn("Arg map is null.")
            return
        }
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

    @JvmStatic
    public fun getString(key: String, hashKey: String): String? {
        val string = RedisManager.redisTemplate.boundHashOps<String, String>(key).get(hashKey)
        if (string.isNullOrBlank()) return string
        return string.trimQuotes()
    }

    @JvmStatic
    public fun getNumber(key: String, hashKey: String): Number? =
        RedisManager.redisTemplate.boundHashOps<String, Number>(key).get(hashKey)

    @JvmStatic
    public fun getInt(key: String, hashKey: String): Int? = getNumber(key, hashKey)?.toInt()

    @JvmStatic
    public fun getLong(key: String, hashKey: String): Long? = getNumber(key, hashKey)?.toLong()

    @JvmStatic
    public fun getDouble(key: String, hashKey: String): Double? = getNumber(key, hashKey)?.toDouble()

    @JvmStatic
    public inline fun <reified T> getObj(key: String, hashKey: String): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey)?.toJsonString()?.jsonToObj()

    @JvmStatic
    public fun <T : Any> get(key: String, hashKey: String): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey)

    public inline fun <reified E, KEY> getEnum(key: String, hashKey: String): E?
        where E : EnumValue<KEY>, E : Enum<E>, KEY : Serializable {
        val value = RedisManager.redisTemplate.boundHashOps<String, KEY>(key).get(hashKey)
            ?: return null
        return EnumCreator.getCreator(E::class.java).create(value)
    }

    @JvmStatic
    public fun getMap(key: String): Map<String, Any> =
        RedisManager.redisTemplate.opsForHash<String, Any>().entries(key)

    private fun <T : Any> RedisTemplate<String, T>.put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ) {
        if (timeout == 0L) {
            boundHashOps<String, T>(key).put(hashKey, value)
        } else {
            boundHashOps<String, T>(key).apply {
                put(hashKey, value)
                expire(timeout, timeUnit)
            }
        }
    }

    private fun <T : Any> RedisTemplate<String, T>.put(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ) {
        boundHashOps<String, T>(key).apply {
            put(hashKey, value)
            expireAt(date)
        }
    }
}
