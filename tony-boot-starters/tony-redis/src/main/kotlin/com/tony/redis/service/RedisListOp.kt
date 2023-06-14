package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.RedisManager

/**
 * RedisListOp is
 * @author tangli
 * @since 2023/06/14 13:38
 */

public sealed interface RedisListGetOp : RedisValueTransformer {

    /**
     * Removes and returns first element in list stored at key.
     * @param T
     * @param key must not be null.
     * @param type
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.leftPop
     */
    public fun <T : Any> leftPop(key: String, type: Class<T>): T? =
        RedisManager.redisTemplate.opsForList().leftPop(key).outputTransformTo(type)

    /**
     * @see [leftPop]
     */
    public fun <T : Any> leftPop(key: String, type: JavaType): T? =
        RedisManager.redisTemplate.opsForList().leftPop(key).outputTransformTo(type)

    /**
     * @see [leftPop]
     */
    public fun <T : Any> leftPop(key: String, type: TypeReference<T>): T? =
        RedisManager.redisTemplate.opsForList().leftPop(key).outputTransformTo(type)

    /**
     * Removes and returns first elements in list stored at key.
     *
     * @param T
     * @param key must not be null.
     * @param count
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.leftPop
     */
    public fun <T : Any> leftPop(key: String, count: Long): List<Any>? =
        RedisManager.redisTemplate.opsForList().leftPop(key, count)

    /**
     * Get element at index form list at key.
     *
     * @param T
     * @param key  must not be null.
     * @param index
     * @param type
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.index
     */
    public fun <T : Any> index(key: String, index: Long, type: Class<T>): T? =
        RedisManager.redisTemplate.opsForList().index(key, index).outputTransformTo(type)

    /**
     * @see [RedisListGetOp.index]
     */
    public fun <T : Any> index(key: String, index: Long, type: JavaType): T? =
        RedisManager.redisTemplate.opsForList().index(key, index).outputTransformTo(type)

    /**
     * @see [RedisListGetOp.index]
     */
    public fun <T : Any> index(key: String, index: Long, type: TypeReference<T>): T? =
        RedisManager.redisTemplate.opsForList().index(key, index).outputTransformTo(type)

    /**
     * TODO
     *
     * @param T
     * @param key
     * @param start
     * @param end
     * @return null when used in pipeline / transaction.
     */
    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> range(key: String, start: Long, end: Long): List<T>? =
        RedisManager.redisTemplate.opsForList().range(key, start, end) as List<T>?
}

public sealed interface RedisListSetOp : RedisValueTransformer {
    public fun <T : Any> leftPush(key: String, value: T): Long? {
        return RedisManager.redisTemplate.opsForList().leftPush(key, value.inputTransformTo())
    }
}
