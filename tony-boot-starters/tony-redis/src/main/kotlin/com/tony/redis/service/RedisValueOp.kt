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
 * RedisValueService is
 * @author tangli
 * @since 2023/06/06 11:01
 */
public sealed interface RedisValueSetOp {
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
}

public sealed interface RedisValueGetOp {
    public fun <T : Any> get(key: String, type: Class<T>): T? {
        val value = RedisManager.redisTemplate.opsForValue().get(key)
        if (type.isNumberTypes()) {
            return value?.asToNumber(type)
        }
        return value?.asTo()
    }

    public fun <T : Any> get(key: String, javaType: JavaType): T? = get(key, javaType.rawClass).asTo()

    public fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? = get(key, typeReference.rawClass())
}
