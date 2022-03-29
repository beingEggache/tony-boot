@file:Suppress("unused")

package com.tony.cache

import java.util.Date
import java.util.concurrent.TimeUnit

/**
 *
 * @author tangli
 * @since 2021-03-24 10:12
 */
object RedisLists {

    fun rightPush(
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

    fun rightPush(
        key: String,
        value: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value)
        RedisManager.redisTemplate.expire(key, timeout, timeUnit)
        return rightPush
    }

    fun rightPop(key: String): String? {
        val string = RedisManager.redisTemplate.opsForList().rightPop(key)?.toString()
        if (string.isNullOrBlank()) return string
        return string.substring(0, string.length)
    }
}
