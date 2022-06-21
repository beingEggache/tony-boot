@file:Suppress("unused")

package com.tony.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.cache.RedisManager.trimQuotes
import com.tony.utils.defaultIfBlank
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
    ): Long? = rightPushString(key, value.toJsonString(), date)

    @JvmStatic
    @JvmOverloads
    fun <T> rightPushObj(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long? = rightPushString(key, value.toJsonString(), timeout, timeUnit)

    @JvmStatic
    fun rightPopString(key: String): String {
        RedisManager.redisTemplate.setEnableTransactionSupport(false)
        val string = RedisManager.redisTemplate.boundListOps(key).rightPop()?.toString().defaultIfBlank()
        if (string.isBlank()) return string
        return string.trimQuotes()
    }

    inline fun <reified T> rightPopObj(key: String): T? {
        val rightPopString = rightPopString(key)
        if (rightPopString.isBlank()) return null
        return rightPopString.jsonToObj()
    }

    @JvmStatic
    fun <T> rightPopObj(key: String, clazz: Class<T>): T? {
        val rightPopString = rightPopString(key)
        if (rightPopString.isBlank()) return null
        return rightPopString.jsonToObj(clazz)
    }

    @JvmStatic
    fun <T> rightPopObj(key: String, typeReference: TypeReference<T>): T? {
        val rightPopString = rightPopString(key)
        if (rightPopString.isBlank()) return null
        return rightPopString.jsonToObj(typeReference)
    }

    @JvmOverloads
    @JvmStatic
    fun size(key: String, defaults: Long? = null): Long? =
        RedisManager.redisTemplate.boundListOps(key).size() ?: defaults

    @JvmStatic
    fun indexString(key: String, index: Long): String =
        RedisManager.redisTemplate.boundListOps(key).index(index)?.toString().defaultIfBlank()

    inline fun <reified T> indexObj(key: String, index: Long): T? {
        val indexString = indexString(key, index)
        if (indexString.isBlank()) return null
        return indexString.jsonToObj()
    }

    @JvmStatic
    fun <T> indexObj(key: String, index: Long, clazz: Class<T>): T? {
        val indexString = indexString(key, index)
        if (indexString.isBlank()) return null
        return indexString.jsonToObj(clazz)
    }

    @JvmStatic
    fun <T> indexObj(key: String, index: Long, typeReference: TypeReference<T>): T? {
        val indexString = indexString(key, index)
        if (indexString.isBlank()) return null
        return indexString.jsonToObj(typeReference)
    }
}
