package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.enums.EnumCreator
import com.tony.enums.EnumValue
import com.tony.redis.RedisManager.trimQuotes
import com.tony.utils.asTo
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import org.springframework.data.redis.core.RedisTemplate
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * redis value 操作单例.
 *
 * @author tangli
 * @since 2023/5/25 9:24
 */
public object RedisValues {

    /**
     * 同 [RedisTemplate.hasKey]
     *
     * @param key
     * @return
     */
    @JvmStatic
    public fun hasKey(key: String): Boolean = RedisManager.redisTemplate.hasKey(key)

    /**
     * 同 RedisTemplate.boundValueOps.increment.
     *
     * 如果键不存在则创建 [initial] 初始值.
     *
     * @param key
     * @param delta
     * @param initial
     * @return
     */
    @JvmStatic
    @JvmOverloads
    public fun increment(key: String, delta: Long = 1L, initial: Long? = null): Long? {
        if (initial != null) {
            RedisManager.redisTemplate.boundValueOps(key).setIfAbsent(initial)
        }
        return RedisManager.redisTemplate.boundValueOps(key).increment(delta)
    }

    /**
     * 同 RedisTemplate.boundValueOps.increment.
     *
     * 如果键不存在则创建 [initial] 初始值.
     *
     * @param key
     * @param delta
     * @param initial
     * @return
     */
    @JvmStatic
    @JvmOverloads
    public fun increment(key: String, delta: Double = 1.0, initial: Double? = null): Double? {
        if (initial != null) {
            RedisManager.redisTemplate.boundValueOps(key).setIfAbsent(initial)
        }
        return RedisManager.redisTemplate.boundValueOps(key).increment(delta)
    }

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = if (timeout == 0L) {
        RedisManager.redisTemplate.opsForValue().set(key, value)
    } else {
        RedisManager.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)
    }

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> setObj(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = if (timeout == 0L) {
        RedisManager.redisTemplate.opsForValue().set(key, value.toJsonString())
    } else {
        RedisManager.redisTemplate.opsForValue().set(key, value, timeout, timeUnit)
    }

    @JvmStatic
    public fun getString(key: String): String? {
        val string = RedisManager.redisTemplate.opsForValue().get(key)?.toString()
        if (string.isNullOrBlank()) return string
        return string.trimQuotes()
    }

    @JvmStatic
    internal fun getNumber(key: String): Number? =
        RedisManager.redisTemplate.opsForValue().get(key).asTo()

    @JvmStatic
    public fun getInt(key: String): Int? = getNumber(key)?.toInt()

    @JvmStatic
    public fun getLong(key: String): Long? = getNumber(key)?.toLong()

    @JvmStatic
    public fun getDouble(key: String): Double? = getNumber(key)?.toDouble()

    @JvmStatic
    public inline fun <reified T> getObj(key: String): T? =
        RedisManager.redisTemplate.opsForValue().get(key)?.toJsonString()?.jsonToObj()

    @JvmStatic
    public fun <T> getObj(key: String, typeReference: TypeReference<T>): T? =
        getString(key)?.jsonToObj(typeReference)

    @JvmStatic
    public fun <T> getObj(key: String, javaType: JavaType): T? =
        getString(key)?.jsonToObj(javaType)

    @JvmStatic
    public fun <T : Any> get(key: String): T? =
        RedisManager.redisTemplate.opsForValue().get(key)?.asTo()

    @Suppress("UNCHECKED_CAST")
    public inline fun <reified E, KEY> getEnum(key: String): E?
        where E : EnumValue<KEY>, E : Enum<E>, KEY : Serializable {
        val value = RedisManager.redisTemplate.opsForValue().get(key)
            ?: return null
        return EnumCreator.getCreator(E::class.java).create(value as KEY)
    }
}
