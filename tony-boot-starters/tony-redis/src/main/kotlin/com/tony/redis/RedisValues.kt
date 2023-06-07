package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.SpringContexts
import com.tony.redis.service.RedisValueService
import com.tony.utils.asTo
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

/**
 * redis value 操作单例.
 *
 * @author tangli
 * @since 2023/5/25 9:24
 */
public object RedisValues {

    public val redisValueService: RedisValueService
        by SpringContexts.getBeanByLazy<RedisValueService>()

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
    public fun increment(key: String, delta: Long = 1L, initial: Long? = null): Long? =
        RedisManager.doInTransaction {
            if (initial != null) {
                RedisManager.redisTemplate.boundValueOps(key).setIfAbsent(initial)
            }
            RedisManager.redisTemplate.boundValueOps(key).increment(delta)
        }.last().asTo()

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
    public fun increment(key: String, delta: Double = 1.0, initial: Double? = null): Double? =
        RedisManager.doInTransaction {
            if (initial != null) {
                RedisManager.redisTemplate.boundValueOps(key).setIfAbsent(initial)
            }
            RedisManager.redisTemplate.boundValueOps(key).increment(delta)
        }.last().asTo()

    @JvmStatic
    @JvmOverloads
    public fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = redisValueService.set(key, value, timeout, timeUnit)

    @JvmStatic
    public inline fun <reified T : Any> get(key: String): T? {
        return redisValueService.get(key, (object : TypeReference<T>() {}))
    }

    @JvmStatic
    public fun <T : Any> get(key: String, type: Class<T>): T? =
        redisValueService.get(key, type)

    @JvmStatic
    public fun <T : Any> get(key: String, javaType: JavaType): T? =
        redisValueService.get(key, javaType)

    @JvmStatic
    public fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? =
        redisValueService.get(key, typeReference)
}
