package com.tony.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.cache.RedisManager.trimQuotes
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
        RedisManager.redisTemplate.setEnableTransactionSupport(false)
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
    public fun indexString(key: String, index: Long): String =
        RedisManager.redisTemplate.boundListOps(key).index(index)?.toString().defaultIfBlank()

    public inline fun <reified T> indexObj(key: String, index: Long): T? {
        val indexString = indexString(key, index)
        if (indexString.isBlank()) return null
        return indexString.jsonToObj()
    }

    @JvmStatic
    public fun <T> indexObj(key: String, index: Long, clazz: Class<T>): T? {
        val indexString = indexString(key, index)
        if (indexString.isBlank()) return null
        return indexString.jsonToObj(clazz)
    }

    @JvmStatic
    public fun <T> indexObj(key: String, index: Long, typeReference: TypeReference<T>): T? {
        val indexString = indexString(key, index)
        if (indexString.isBlank()) return null
        return indexString.jsonToObj(typeReference)
    }
}
