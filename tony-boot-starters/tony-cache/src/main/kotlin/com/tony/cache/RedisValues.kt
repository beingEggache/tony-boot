@file:Suppress("unused")

package com.tony.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.cache.RedisManager.trimQuotes
import com.tony.enums.EnumCreator.Companion.getCreator
import com.tony.enums.EnumValue
import com.tony.utils.asTo
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import java.io.Serializable
import java.util.concurrent.TimeUnit

object RedisValues {

    @JvmStatic
    fun hasKey(key: String): Boolean = RedisManager.redisTemplate.hasKey(key)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Long = 1L, initial: Long? = null): Long? {
        if (initial != null) {
            RedisManager.redisTemplate.boundValueOps(key).setIfAbsent(initial)
        }
        return RedisManager.redisTemplate.boundValueOps(key).increment(delta)
    }

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Double = 1.0, initial: Double? = null): Double? {
        if (initial != null) {
            RedisManager.redisTemplate.boundValueOps(key).setIfAbsent(initial)
        }
        return RedisManager.redisTemplate.boundValueOps(key).increment(delta)
    }

    @JvmStatic
    @JvmOverloads
    fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) {
        RedisManager.redisTemplate.opsForValue().set(key, value)
    } else {
        RedisManager.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)
    }

    @JvmStatic
    @JvmOverloads
    fun <T : Any> setObj(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) {
        RedisManager.redisTemplate.opsForValue().set(key, value.toJsonString())
    } else {
        RedisManager.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)
    }

    @JvmStatic
    fun getString(key: String): String? {
        val string = RedisManager.redisTemplate.opsForValue().get(key)?.toString()
        if (string.isNullOrBlank()) return string
        return string.trimQuotes()
    }

    @JvmStatic
    internal fun getNumber(key: String): Number? =
        RedisManager.redisTemplate.opsForValue().get(key).asTo()

    @JvmStatic
    fun getInt(key: String): Int? = getNumber(key)?.toInt()

    @JvmStatic
    fun getLong(key: String): Long? = getNumber(key)?.toLong()

    @JvmStatic
    fun getDouble(key: String): Double? = getNumber(key)?.toDouble()

    @JvmStatic
    inline fun <reified T> getObj(key: String): T? =
        RedisManager.redisTemplate.opsForValue().get(key).toJsonString().jsonToObj()

    @JvmStatic
    fun <T> getObj(key: String, typeReference: TypeReference<T>): T? =
        getString(key)?.jsonToObj(typeReference)

    @JvmStatic
    fun <T> getObj(key: String, javaType: JavaType): T? =
        getString(key)?.jsonToObj(javaType)

    @JvmStatic
    fun <T : Any> get(key: String): T? =
        RedisManager.redisTemplate.opsForValue().get(key).asTo()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified E, KEY> getEnum(key: String): E?
        where E : EnumValue<KEY>, E : Enum<E>, KEY : Serializable {
        val value = RedisManager.redisTemplate.opsForValue().get(key)
            ?: return null
        return getCreator(E::class.java).create(value as KEY)
    }
}
