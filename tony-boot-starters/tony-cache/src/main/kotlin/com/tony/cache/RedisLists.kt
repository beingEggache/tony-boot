@file:Suppress("unused")

package com.tony.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 *
 * @author tangli
 * @since 2021-03-24 10:12
 */
object RedisLists {

    @JvmStatic
    @JvmOverloads
    fun rightPushString(
        key: String,
        value: String,
        date: Date? = null
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value)
        if (date != null) {
            RedisManager.redisTemplate.expireAt(key, date)
        }
        return rightPush
    }

    @JvmStatic
    @JvmOverloads
    fun rightPushString(
        key: String,
        value: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value)
        RedisManager.redisTemplate.expire(key, timeout, timeUnit)
        return rightPush
    }

    @JvmStatic
    @JvmOverloads
    fun <T> rightPushObj(
        key: String,
        value: T,
        date: Date? = null
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value.toJsonString())
        if (date != null) {
            RedisManager.redisTemplate.expireAt(key, date)
        }
        return rightPush
    }

    @JvmStatic
    @JvmOverloads
    fun <T> rightPushObj(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value.toJsonString())
        RedisManager.redisTemplate.expire(key, timeout, timeUnit)
        return rightPush
    }

    @JvmStatic
    fun rightPopString(key: String): String? {
        val string = RedisManager.redisTemplate.opsForList().rightPop(key)?.toString()
        if (string.isNullOrBlank()) return string
        return string.substring(0, string.length)
    }

    @JvmStatic
    fun <T> rightPopObj(key: String, clazz: Class<T>): T? {
        val string = RedisManager.redisTemplate.opsForList().rightPop(key)?.toString()
        if (string.isNullOrBlank()) return null
        return string.substring(0, string.length).jsonToObj(clazz)
    }

    @JvmStatic
    fun <T> rightPopObj(key: String, typeReference: TypeReference<T>): T? {
        val string = RedisManager.redisTemplate.opsForList().rightPop(key)?.toString()
        if (string.isNullOrBlank()) return null
        return string.substring(0, string.length).jsonToObj(typeReference)
    }
}
