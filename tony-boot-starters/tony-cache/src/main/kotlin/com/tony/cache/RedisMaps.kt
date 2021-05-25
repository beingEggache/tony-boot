@file:Suppress("unused")

package com.tony.cache

import com.tony.core.enums.EnumValue
import com.tony.core.enums.getCreator
import com.tony.core.utils.doIf
import com.tony.core.utils.getLogger
import com.tony.core.utils.jsonToObj
import org.springframework.data.redis.core.RedisTemplate
import java.io.Serializable
import java.util.Date
import java.util.concurrent.TimeUnit

object RedisMaps {

    private val logger = getLogger()

    @JvmStatic
    fun hasKey(key: String, hashKey: String): Boolean =
        true == RedisUtils.redisTemplate.boundHashOps<String, Any>(key).hasKey(hashKey)

    @JvmStatic
    fun delete(key: String, vararg hashKey: String) =
        RedisUtils.redisTemplate.opsForHash<String, Any>()
            .hasKey(key, hashKey)
            .doIf {
                RedisUtils.redisTemplate.opsForHash<String, Any>().delete(key, hashKey)
            }

    @JvmStatic
    @JvmOverloads
    fun putString(
        key: String,
        hashKey: String,
        value: String,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    @JvmStatic
    fun putString(
        key: String,
        hashKey: String,
        value: String,
        date: Date
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, date)

    @JvmStatic
    @JvmOverloads
    fun putNumber(
        key: String,
        hashKey: String,
        value: Number,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    @JvmStatic
    fun putNumber(
        key: String,
        hashKey: String,
        value: Number,
        date: Date
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, date)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> putObj(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    @JvmStatic
    fun <T : Any> putObj(
        key: String,
        hashKey: String,
        value: T,
        date: Date
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, date)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    @JvmStatic
    fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date
    ) = RedisUtils.redisTemplate.put(key, hashKey, value, date)

    @JvmStatic
    @JvmOverloads
    fun putAll(
        key: String,
        map: Map<String, Any?>?,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        if (map == null) {
            logger.warn("map was null")
            return
        }
        if (timeout == 0L) {
            RedisUtils.redisTemplate.boundHashOps<String, Any?>(key).putAll(map)
        } else {
            RedisUtils.redisTemplate.boundHashOps<String, Any?>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date
    ) {
        if (map == null) {
            logger.warn("map was null")
            return
        }
        RedisUtils.redisTemplate.boundHashOps<String, Any>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

    @JvmStatic
    fun getString(key: String, hashKey: String): String? =
        RedisUtils.redisTemplate.boundHashOps<String, String>(key).get(hashKey)

    @JvmStatic
    fun getNumber(key: String, hashKey: String): Number? =
        RedisUtils.redisTemplate.boundHashOps<String, Number>(key).get(hashKey)

    @JvmStatic
    fun getInt(key: String, hashKey: String): Int? = getNumber(key, hashKey)?.toInt()

    @JvmStatic
    fun getLong(key: String, hashKey: String): Long? = getNumber(key, hashKey)?.toLong()

    @JvmStatic
    fun getDouble(key: String, hashKey: String): Double? = getNumber(key, hashKey)?.toDouble()

    @JvmStatic
    inline fun <reified T> getObj(key: String, hashKey: String): T? =
        RedisUtils.stringRedisTemplate.boundHashOps<String, String>(key).get(hashKey)?.jsonToObj()

    @JvmStatic
    fun <T : Any> get(key: String, hashKey: String): T? =
        RedisUtils.redisTemplate.boundHashOps<String, T>(key).get(hashKey)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified E, KEY> getEnum(key: String, hashKey: String): E?
        where E : EnumValue<KEY>, E : Enum<E>, KEY : Serializable {
        val value = RedisUtils.redisTemplate.boundHashOps<String, KEY>(key).get(hashKey)
            ?: return null
        return getCreator(E::class.java).create(value)
    }

    @JvmStatic
    fun getMap(key: String): Map<String, Any>? =
        RedisUtils.redisTemplate.opsForHash<String, Any>().entries(key)

    private fun <T> RedisTemplate<String, T>.put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
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

    private fun <T> RedisTemplate<String, T>.put(
        key: String,
        hashKey: String,
        value: T,
        date: Date
    ) {
        boundHashOps<String, T>(key).apply {
            put(hashKey, value)
            expireAt(date)
        }
    }
}
