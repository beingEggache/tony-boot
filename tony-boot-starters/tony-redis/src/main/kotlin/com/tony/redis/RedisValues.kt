package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.service.RedisService
import com.tony.utils.asTo
import java.util.concurrent.TimeUnit

/**
 * redis value 操作单例.
 *
 * @author tangli
 * @since 2023/5/25 9:24
 */
public object RedisValues {

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
    public fun increment(key: String, delta: Long = 1L, initial: Long? = null): Long? =
        RedisManager.doInTransaction {
            if (initial != null) {
                redisTemplate.opsForValue().setIfAbsent(key, initial)
            }
            redisTemplate.opsForValue().increment(key, delta)
        }.last().asTo()

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
    public fun increment(key: String, delta: Double = 1.0, initial: Double? = null): Double? =
        RedisManager.doInTransaction {
            if (initial != null) {
                redisTemplate.boundValueOps(key).setIfAbsent(initial)
            }
            redisTemplate.boundValueOps(key).increment(delta)
        }.last().asTo()

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = redisService.set(key, value, timeout, timeUnit)

    /**
     * @see [RedisService.setIfAbsent]
     */
    @JvmStatic
    @JvmOverloads
    public fun <T : Any> setIfAbsent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? = redisService.setIfAbsent(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> setIfPresent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? = redisService.setIfPresent(key, value, timeout, timeUnit)

    public inline fun <reified T : Any> get(key: String): T? {
        return redisService.get(key, (object : TypeReference<T>() {}))
    }

    @JvmStatic
    public fun <T : Any> get(key: String, type: Class<T>): T? =
        redisService.get(key, type)

    @JvmStatic
    public fun <T : Any> get(key: String, javaType: JavaType): T? =
        redisService.get(key, javaType)

    @JvmStatic
    public fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? =
        redisService.get(key, typeReference)
}
