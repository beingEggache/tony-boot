package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.RedisManager
import com.tony.utils.asTo
import com.tony.utils.asToNumber
import com.tony.utils.isNumberTypes
import com.tony.utils.rawClass
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * RedisMapOp is
 * @author tangli
 * @since 2023/06/09 18:20
 */
public sealed interface RedisMapGetOp {

    public fun <T : Any> get(key: String, hashKey: String, type: Class<T>): T? {
        val value = RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey)
        if (type.isNumberTypes()) {
            return value.asToNumber(type)
        }
        return value.asTo()
    }

    public fun <T : Any> get(key: String, hashKey: String, javaType: JavaType): T? =
        get(key, hashKey, javaType.rawClass).asTo()

    public fun <T : Any> get(key: String, hashKey: String, typeReference: TypeReference<T>): T? =
        get(key, hashKey, typeReference.rawClass())

    public fun entries(key: String): Map<String, Any?> =
        RedisManager.redisTemplate.opsForHash<String, Any?>().entries(key)
}

public sealed interface RedisMapSetOp {

    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ) {
        if (map == null) {
            return
        }
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

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
            RedisManager.redisTemplate.boundHashOps<String, Any?>(key).putAll(map)
        } else {
            RedisManager.redisTemplate.boundHashOps<String, Any?>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ) {
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).apply {
            put(hashKey, value)
            expireAt(date)
        }
    }

    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ) {
        if (timeout == 0L) {
            RedisManager.redisTemplate.boundHashOps<String, T>(key).put(hashKey, value)
        } else {
            RedisManager.redisTemplate.boundHashOps<String, T>(key).apply {
                put(hashKey, value)
                expire(timeout, timeUnit)
            }
        }
    }
}
