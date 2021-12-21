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
        val rightPush = RedisManager.stringRedisTemplate.opsForList().rightPush(key, value)
        if (date != null) {
            RedisManager.stringRedisTemplate.expireAt(key, date)
        }
        return rightPush
    }

    fun rightPush(
        key: String,
        value: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long? {
        val rightPush = RedisManager.stringRedisTemplate.opsForList().rightPush(key, value)
        RedisManager.stringRedisTemplate.expire(key, timeout, timeUnit)
        return rightPush
    }

    fun rightPop(key: String): String? = RedisManager.stringRedisTemplate.opsForList().rightPop(key)
}
