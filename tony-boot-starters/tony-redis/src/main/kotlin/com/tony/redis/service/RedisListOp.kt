package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.RedisManager

/**
 * RedisListOp is
 * @author tangli
 * @since 2023/06/14 13:38
 */
public sealed interface RedisListOp : RedisListGetOp, RedisListSetOp

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
     * Removes and returns last element in list stored at key.
     * @param T
     * @param key must not be null.
     * @param type
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.rightPop
     */
    public fun <T : Any> rightPop(key: String, type: Class<T>): T? =
        RedisManager.redisTemplate.opsForList().rightPop(key).outputTransformTo(type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(key: String, type: JavaType): T? =
        RedisManager.redisTemplate.opsForList().rightPop(key).outputTransformTo(type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(key: String, type: TypeReference<T>): T? =
        RedisManager.redisTemplate.opsForList().rightPop(key).outputTransformTo(type)

    /**
     * Removes and returns last elements in list stored at key.
     *
     * @param T
     * @param key must not be null.
     * @param count
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.rightPop
     */
    public fun <T : Any> rightPop(key: String, count: Long): List<Any>? =
        RedisManager.redisTemplate.opsForList().rightPop(key, count)

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
     * @see org.springframework.data.redis.core.ListOperations.range
     */
    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> range(key: String, start: Long, end: Long): List<T>? =
        RedisManager.redisTemplate.opsForList().range(key, start, end) as List<T>?

    /**
     * Get the size of list stored at key.
     *
     * @param key must not be null.
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.size
     */
    public fun size(key: String): Long? =
        RedisManager.redisTemplate.opsForList().size(key)
}

public sealed interface RedisListSetOp : RedisValueTransformer {
    /**
     * Prepend value to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPush
     */
    public fun <T : Any> leftPush(key: String, value: T): Long? =
        RedisManager.redisTemplate.opsForList().leftPush(key, value.inputTransformTo())

    /**
     * Insert value to key before pivot.
     * @param T
     * @param key must not be null.
     * @param pivot must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPush
     */
    public fun <T : Any> leftPush(key: String, pivot: Long, value: T): Long? =
        RedisManager.redisTemplate.opsForList().leftPush(key, pivot, value.inputTransformTo())

    /**
     * Prepend values to key only if the list exists.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPushIfPresent
     */
    public fun <T : Any> leftPushIfPresent(key: String, value: T): Long? =
        RedisManager.redisTemplate.opsForList().leftPushIfPresent(key, value.inputTransformTo())

    /**
     * Prepend values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPushAll
     */
    public fun <T : Any> leftPushAll(key: String, vararg value: T): Long? =
        RedisManager.redisTemplate.opsForList().leftPushAll(key, value.onEach { it.inputTransformTo() })

    /**
     * Prepend values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPushAll
     */
    public fun <T : Any> leftPushAll(key: String, value: Collection<T>): Long? =
        RedisManager.redisTemplate.opsForList().leftPushAll(key, value.onEach { it.inputTransformTo() })

    /**
     * Append value to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPush
     */
    public fun <T : Any> rightPush(key: String, value: T): Long? =
        RedisManager.redisTemplate.opsForList().rightPush(key, value.inputTransformTo())

    /**
     * Insert value to key after pivot.
     * @param T
     * @param key must not be null.
     * @param pivot must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPush
     */
    public fun <T : Any> rightPush(key: String, pivot: Long, value: T): Long? =
        RedisManager.redisTemplate.opsForList().rightPush(key, pivot, value.inputTransformTo())

    /**
     * Append values to key only if the list exists.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPushIfPresent
     */
    public fun <T : Any> rightPushIfPresent(key: String, value: T): Long? =
        RedisManager.redisTemplate.opsForList().rightPushIfPresent(key, value.inputTransformTo())

    /**
     * Append values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPushAll
     */
    public fun <T : Any> rightPushAll(key: String, vararg value: T): Long? =
        RedisManager.redisTemplate.opsForList().rightPushAll(key, value.onEach { it.inputTransformTo() })

    /**
     * Append values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPushAll
     */
    public fun <T : Any> rightPushAll(key: String, value: Collection<T>): Long? =
        RedisManager.redisTemplate.opsForList().rightPushAll(key, value.onEach { it.inputTransformTo() })
}
