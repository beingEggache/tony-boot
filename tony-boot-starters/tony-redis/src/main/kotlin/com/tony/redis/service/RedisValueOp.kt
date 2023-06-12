package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.RedisManager
import com.tony.utils.asTo
import com.tony.utils.asToNumber
import com.tony.utils.isNumberTypes
import com.tony.utils.rawClass
import java.util.concurrent.TimeUnit

/**
 * redis value 保存操作
 * @author tangli
 * @since 2023/06/06 11:01
 */
public sealed interface RedisValueSetOp {
    /**
     * Set the value and expiration timeout for key.
     *
     * @param T
     * @param key must not be null.
     * @param value must not be null.
     * @param timeout
     * @param timeUnit
     */
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

    /**
     * 当键 **不存在** 时保存。
     *
     * @param T
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @return
     */
    public fun <T : Any> setIfAbsent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? = if (timeout == 0L) {
        RedisManager.redisTemplate.opsForValue().setIfAbsent(key, value)
    } else {
        RedisManager.redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit)
    }

    /**
     * 当键 **存在** 时保存。
     *
     * @param T
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @return
     */
    public fun <T : Any> setIfPresent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? = if (timeout == 0L) {
        RedisManager.redisTemplate.opsForValue().setIfPresent(key, value)
    } else {
        RedisManager.redisTemplate.opsForValue().setIfPresent(key, value, timeout, timeUnit)
    }
}
/**
 * redis value 读取操作
 *
 * @author tangli
 * @since 2023/06/06 11:01
 */
public sealed interface RedisValueGetOp {
    /**
     * 获取 redis value
     *
     * @param T
     * @param key
     * @param type
     * @return
     */
    public fun <T : Any> get(key: String, type: Class<T>): T? {
        val value = RedisManager.redisTemplate.opsForValue().get(key)
        if (type.isNumberTypes()) {
            return value?.asToNumber(type)
        }
        return value?.asTo()
    }

    /**
     * 获取 redis value
     * @param javaType 兼容jackson.
     * @see [RedisValueGetOp.get]
     */
    public fun <T : Any> get(key: String, javaType: JavaType): T? = get(key, javaType.rawClass).asTo()

    /**
     * 获取 redis value
     * @param typeReference 兼容jackson
     * @see [RedisValueGetOp.get]
     */
    public fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? = get(key, typeReference.rawClass())
}
