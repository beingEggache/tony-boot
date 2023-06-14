package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.RedisManager

/**
 * RedisListOp is
 * @author tangli
 * @since 2023/06/14 13:38
 */

public interface RedisListGetOp : RedisValueTransformer {

    public fun <T : Any> leftPop(key: String, type: Class<T>): T? =
        RedisManager.redisTemplate.opsForList().leftPop(key).transformTo(type)

    public fun <T : Any> leftPop(key: String, type: JavaType): T? =
        RedisManager.redisTemplate.opsForList().leftPop(key).transformTo(type)

    public fun <T : Any> leftPop(key: String, type: TypeReference<T>): T? =
        RedisManager.redisTemplate.opsForList().leftPop(key).transformTo(type)
}

public interface RedisListSetOp
