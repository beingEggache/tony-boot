@file:Suppress("unused")

package com.tony.cache

import com.tony.core.enums.EnumCreator.Companion.getCreator
import com.tony.core.enums.EnumValue
import com.tony.core.utils.asTo
import com.tony.core.utils.jsonToObj
import java.io.Serializable
import java.util.concurrent.TimeUnit

object RedisValues {

    @JvmStatic
    fun hasKey(key: String): Boolean = RedisUtils.redisTemplate.hasKey(key)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Long = 1L): Long? =
        RedisUtils.redisTemplate.boundValueOps(key).increment(delta)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Double = 1.0): Double? =
        RedisUtils.redisTemplate.boundValueOps(key).increment(delta)

    @JvmStatic
    @JvmOverloads
    fun setString(
        key: String,
        value: String,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.stringRedisTemplate.opsForValue().set(key, value)
    else RedisUtils.stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun setNumber(
        key: String,
        value: Number,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.redisTemplate.opsForValue().set(key, value)
    else RedisUtils.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> setObj(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.redisTemplate.opsForValue().set(key, value)
    else RedisUtils.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) = if (timeout == 0L) RedisUtils.redisTemplate.opsForValue().set(key, value)
    else RedisUtils.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    fun getString(key: String): String? =
        RedisUtils.stringRedisTemplate.opsForValue().get(key)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    internal fun getNumber(key: String): Number? =
        RedisUtils.redisTemplate.opsForValue().get(key).asTo()

    @JvmStatic
    fun getInt(key: String): Int? = getNumber(key)?.toInt()

    @JvmStatic
    fun getLong(key: String): Long? = getNumber(key)?.toLong()

    @JvmStatic
    fun getDouble(key: String): Double? = getNumber(key)?.toDouble()

    @JvmStatic
    inline fun <reified T> getObj(key: String): T? =
        getString(key)?.jsonToObj()

    fun <T : Any> get(key: String): T? =
        RedisUtils.redisTemplate.opsForValue().get(key).asTo()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified E, KEY> getEnum(key: String): E?
        where E : EnumValue<KEY>, E : Enum<E>, KEY : Serializable {
        val value = RedisUtils.redisTemplate.opsForValue().get(key)
            ?: return null
        return getCreator(E::class.java).create(value as KEY)
    }
}
