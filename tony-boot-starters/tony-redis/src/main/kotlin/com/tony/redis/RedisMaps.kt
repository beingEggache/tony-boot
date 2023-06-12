package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.SpringContexts
import com.tony.redis.service.RedisService
import com.tony.utils.doIf
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * redis hash操作单例.
 *
 * @author tangli
 * @since 2023/5/25 9:57
 */
public object RedisMaps {

    private val redisService: RedisService by SpringContexts.getBeanByLazy()

    /**
     * 同 redisTemplate.boundHashOps(key).hasKey(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun hasKey(key: String, hashKey: String): Boolean =
        true == RedisManager.redisTemplate.boundHashOps<String, Any>(key).hasKey(hashKey)

    /**
     * 先进行 判断  redisTemplate.opsForHash().hasKey(key, hashKey).
     * 同 redisTemplate.opsForHash().delete(key, hashKey).
     *
     * @param key
     * @param hashKey
     */
    @JvmStatic
    public fun delete(key: String, vararg hashKey: String): Unit =
        RedisManager.redisTemplate.opsForHash<String, Any>()
            .hasKey(key, hashKey)
            .doIf {
                RedisManager.redisTemplate.opsForHash<String, Any>().delete(key, hashKey)
            }

    /**
     * 同 redisTemplate.boundHashOps(key).put(hashKey, value).
     *
     * 同时可设置过期时间.
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
