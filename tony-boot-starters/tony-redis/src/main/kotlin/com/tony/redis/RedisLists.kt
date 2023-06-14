package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.SpringContexts
import com.tony.redis.RedisManager.trimQuotes
import com.tony.redis.service.RedisService
import com.tony.utils.defaultIfBlank
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * redis 列表操作单例.
 *
 * @author tangli
 * @since 2021-03-24 10:12
 */
public object RedisLists {

    private val redisService: RedisService by SpringContexts.getBeanByLazy()

    /**
     * 同 [RedisTemplate.opsForList().rightPush()].
     *
     * @param key   健
     * @param value 值
     * @param date 过期时间
     */
    @JvmStatic
    @JvmOverloads
    public fun rightPushString(
        key: String,
        value: String,
        date: Date? = null,
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value)
        if (date != null) {
            RedisManager.redisTemplate.expireAt(key, date)
        }
        return rightPush
    }

    /**
     * Right push string
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @return
     */
    @JvmStatic
    @JvmOverloads
    public fun rightPushString(
        key: String,
        value: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Long? {
        val rightPush = RedisManager.redisTemplate.opsForList().rightPush(key, value)
        RedisManager.redisTemplate.expire(key, timeout, timeUnit)
        return rightPush
    }

    @JvmStatic
    @JvmOverloads
    public fun <T> rightPushObj(
        key: String,
        value: T,
        date: Date? = null,
    ): Long? = rightPushString(key, value.toJsonString(), date)

    @JvmStatic
    @JvmOverloads
    public fun <T> rightPushObj(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Long? = rightPushString(key, value.toJsonString(), timeout, timeUnit)

    @JvmStatic
    public fun rightPopString(key: String): String {
        val string = RedisManager.redisTemplate.boundListOps(key).rightPop()?.toString().defaultIfBlank()
        if (string.isBlank()) return string
        return string.trimQuotes()
    }

    public inline fun <reified T> rightPopObj(key: String): T? {
        val rightPopString = rightPopString(key)
        if (rightPopString.isBlank()) return null
        return rightPopString.jsonToObj()
    }

    @JvmStatic
    public fun <T> rightPopObj(key: String, clazz: Class<T>): T? {
        val rightPopString = rightPopString(key)
        if (rightPopString.isBlank()) return null
        return rightPopString.jsonToObj(clazz)
    }

    @JvmStatic
    public fun <T> rightPopObj(key: String, typeReference: TypeReference<T>): T? {
        val rightPopString = rightPopString(key)
        if (rightPopString.isBlank()) return null
        return rightPopString.jsonToObj(typeReference)
    }

    @JvmOverloads
    @JvmStatic
    public fun size(key: String, defaults: Long? = null): Long? =
        RedisManager.redisTemplate.boundListOps(key).size() ?: defaults

    @JvmStatic
    public fun <T : Any> leftPush(key: String, value: T): Long? =
        redisService.leftPush(key, value)

    @JvmStatic
    public fun <T : Any> leftPop(key: String, type: Class<T>): T? =
        redisService.leftPop(key, type)

    @JvmStatic
    public inline fun <reified T : Any> leftPop(key: String): T? =
        leftPop(key, T::class.java)

    @JvmStatic
    public fun <T : Any> index(key: String, index: Long, type: Class<T>): T? =
        redisService.index(key, index, type)

    @JvmStatic
    public fun <T : Any> index(key: String, index: Long, type: JavaType): T? =
        redisService.index(key, index, type)

    @JvmStatic
    public fun <T : Any> index(key: String, index: Long, type: TypeReference<T>): T? =
        redisService.index(key, index, type)
}
