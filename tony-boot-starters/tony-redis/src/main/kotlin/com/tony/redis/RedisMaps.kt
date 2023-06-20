package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.utils.asTo
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * redis hash操作单例.
 *
 * @author tangli
 * @since 2023/5/25 9:57
 */
public object RedisMaps {

    /**
     * Determine if given hash hashKey exists.
     *
     * @param key must not be null.
     * @param hashKey must not be null.
     * @return null when used in pipeline / transaction.
     */
    @Suppress("SimplifyBooleanWithConstants")
    @JvmStatic
    public fun hasKey(key: String, hashKey: String): Boolean =
        true == hashOp.hasKey(key, hashKey)

    /**
     * Delete given hash hashKeys.
     *
     * @param key must not be null.
     * @param hashKeys must not be null.
     * @return null when used in pipeline / transaction.
     */
    @Suppress("RedundantNullableReturnType")
    @JvmStatic
    public fun delete(key: String, vararg hashKeys: String): Long? =
        hashOp.delete(key, *hashKeys)

    /**
     * 同 RedisTemplate.boundValueOps.increment.
     *
     * 如果键不存在则创建 [initial] 初始值.
     *
     * @param key
     * @param delta
     * @param initial
     * @return null when used in pipeline / transaction.
     */
    @JvmStatic
    @JvmOverloads
    public fun increment(key: String, hashKey: String, delta: Long = 1L, initial: Long? = null): Long? =
        RedisManager.doInTransaction {
            if (initial != null) {
                hashOp.putIfAbsent(key, hashKey, initial)
            }
            hashOp.increment(key, hashKey, delta)
        }.last().asTo()

    /**
     * 同 RedisTemplate.boundValueOps.increment.
     *
     * 如果键不存在则创建 [initial] 初始值.
     *
     * @param key must not be null.
     * @param delta
     * @param hashKey must not be null.
     * @param initial
     * @return null when used in pipeline / transaction.
     */
    @JvmStatic
    @JvmOverloads
    public fun increment(key: String, hashKey: String, delta: Double = 1.0, initial: Double? = null): Double? =
        RedisManager.doInTransaction {
            if (initial != null) {
                hashOp.putIfAbsent(key, hashKey, initial)
            }
            hashOp.increment(key, hashKey, delta)
        }.last().asTo()

    /**
     * 同 redisTemplate.boundHashOps(key).put(hashKey, value).
     *
     * @param T
     * @param key
     * @param hashKey
     * @param value
     */
    @JvmStatic
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
    ): Unit = redisService.put(key, hashKey, value)

    /**
     * 同 redisTemplate.boundHashOps(key).putIfAbsent(hashKey, value).
     *
     * @param T
     * @param key
     * @param hashKey
     * @param value
     */
    @JvmStatic
    public fun <T : Any> putIfAbsent(
        key: String,
        hashKey: String,
        value: T,
    ): Unit = redisService.putIfAbsent(key, hashKey, value)

    /**
     * 同 redisTemplate.boundHashOps(key).putAll(map).
     *
     * 同时可设置过期时间.
     *
     * @param key
     * @param map
     * @param timeout
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     */
    @JvmStatic
    @JvmOverloads
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = redisService.putAll(key, map, timeout, timeUnit)

    /**
     * 同 redisTemplate.boundHashOps(key).putAll(map).
     *
     * 同时可设置过期时间.
     *
     * @param key
     * @param map
     * @param date 过期时间
     */
    @JvmStatic
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ): Unit = redisService.putAll(key, map, date)

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param T
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public inline fun <reified T : Any> get(key: String, hashKey: String): T? =
        get(key, hashKey, T::class.java)

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param T
     * @param key
     * @param hashKey
     * @param clazz
     * @see [com.tony.redis.service.RedisMapGetOp.get]
     * @return
     */
    @JvmStatic
    public fun <T : Any> get(key: String, hashKey: String, clazz: Class<T>): T? =
        redisService.get(key, hashKey, clazz)

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param T
     * @param key
     * @param hashKey
     * @param javaType
     * @see [com.tony.redis.service.RedisMapGetOp.get]
     * @return
     */
    @JvmStatic
    public fun <T : Any> get(key: String, hashKey: String, javaType: JavaType): T? =
        redisService.get(key, hashKey, javaType)

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param T
     * @param key
     * @param hashKey
     * @param typeReference
     * @see [com.tony.redis.service.RedisMapGetOp.get]
     * @return
     */
    @JvmStatic
    public fun <T : Any> get(key: String, hashKey: String, typeReference: TypeReference<T>): T? =
        redisService.get(key, hashKey, typeReference)

    /**
     * 同 redisTemplate.opsForHash().entries(key).
     *
     * @param key
     * @return
     */
    @JvmStatic
    public fun entries(key: String): Map<String, Any?> =
        redisService.entries(key)
}
