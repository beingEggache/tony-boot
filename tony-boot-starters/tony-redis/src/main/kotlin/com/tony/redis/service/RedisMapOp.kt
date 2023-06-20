package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.hashOp
import com.tony.redis.redisTemplate
import java.util.Date
import java.util.concurrent.TimeUnit

public sealed interface RedisMapOp : RedisMapGetOp, RedisMapSetOp

/**
 * redis map 值获取操作
 * @author tangli
 * @since 2023/06/09 18:20
 */
public sealed interface RedisMapGetOp : RedisValueTransformer {

    /**
     * 获取 map 值
     *
     * @param T   值类型
     * @param key  键
     * @param hashKey map健
     * @param type 值 class
     * @return
     */
    public fun <T : Any> get(key: String, hashKey: String, type: Class<T>): T? =
        hashOp.get(key, hashKey).outputTransformTo(type)

    /**
     * 获取 map 值
     * @see [RedisMapGetOp.get]
     * @param type 兼容jackson
     * @return
     */
    public fun <T : Any> get(key: String, hashKey: String, type: JavaType): T? =
        hashOp.get(key, hashKey).outputTransformTo(type)

    /**
     * 获取 map 值
     * @see [RedisMapGetOp.get]
     * @param type 兼容jackson
     * @return
     */
    public fun <T : Any> get(key: String, hashKey: String, type: TypeReference<T>): T? =
        hashOp.get(key, hashKey).outputTransformTo(type)

    /**
     * 根据key值获取整个map
     *
     * @param key
     * @return
     */
    public fun entries(key: String): Map<String, Any?> =
        hashOp.entries(key)
}

/**
 * redis map 值保存操作
 * @author tangli
 * @since 2023/06/09 18:20
 */
public sealed interface RedisMapSetOp : RedisValueTransformer {

    /**
     * Set multiple hash fields to multiple values using data provided in m at the [key] and expired at [date]
     *
     * @param key
     * @param map
     * @param date
     */
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ) {
        if (map == null) {
            return
        }
        redisTemplate.boundHashOps<String, Any>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

    /**
     * Set multiple hash fields to multiple values using data provided in m at the [key]
     * and expired in [timeout], [timeUnit]
     *
     * @param key
     * @param map
     * @param timeout
     * @param timeUnit
     */
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ) {
        if (map == null) {
            return
        }
        if (timeout == 0L) {
            hashOp.putAll(key, map)
        } else {
            redisTemplate.boundHashOps<String, Any?>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    /**
     * Set the value of a hash key at the [key].
     * @param T
     * @param key
     * @param hashKey
     * @param value
     */
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
    ) {
        hashOp.put(key, hashKey, value.inputTransformTo())
    }

    /**
     * Set the value of a hash key only if key does not exist.
     * @param T
     * @param key
     * @param hashKey
     * @param value
     */
    public fun <T : Any> putIfAbsent(
        key: String,
        hashKey: String,
        value: T,
    ) {
        hashOp.putIfAbsent(key, hashKey, value.inputTransformTo())
    }
}
