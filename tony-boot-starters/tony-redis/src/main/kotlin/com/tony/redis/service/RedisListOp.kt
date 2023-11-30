/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.listOp
import com.tony.utils.asTo
import java.util.concurrent.TimeUnit

/**
 * RedisListOp is
 * @author Tang Li
 * @date 2023/06/14 13:38
 */
public sealed interface RedisListOp :
    RedisListGetOp,
    RedisListSetOp {
    /**
     * Remove the last element from list at sourceKey,
     * append it to destinationKey and return its value.
     *
     * Blocks connection until element available or timeout reached.
     *
     * @param sourceKey must not be null.
     * @param destinationKey must not be null.
     * @param type
     * @param timeout
     * @param timeUnit must not be null.
     * @return can be null.
     *
     * @see org.springframework.data.redis.core.ListOperations.rightPopAndLeftPush
     */
    public fun <T : Any> rightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        type: Class<T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? =
        listOp
            .rightPopAndLeftPush(sourceKey, destinationKey, timeout, timeUnit)
            .outputTransformTo(type)

    /**
     * @see [rightPopAndLeftPush]
     */
    public fun <T : Any> rightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        type: JavaType,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? =
        listOp
            .rightPopAndLeftPush(sourceKey, destinationKey, timeout, timeUnit)
            .outputTransformTo(type)

    /**
     * @see [rightPopAndLeftPush]
     */
    public fun <T : Any> rightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        type: TypeReference<T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? =
        listOp
            .rightPopAndLeftPush(sourceKey, destinationKey, timeout, timeUnit)
            .outputTransformTo(type)

    /**
     * Trim list at key to elements between start and end.
     * @param key must not be null.
     * @param start
     * @param end
     *
     * @see org.springframework.data.redis.core.ListOperations.trim
     */
    public fun trim(
        key: String,
        start: Long,
        end: Long,
    ) {
        listOp.trim(key, start, end)
    }
}

public sealed interface RedisListGetOp : RedisValueTransformer {
    /**
     * Removes and returns first element in list stored at key.
     * @param T
     * @param key must not be null.
     * @param type
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.leftPop
     */
    public fun <T : Any> leftPop(
        key: String,
        type: Class<T>,
    ): T? =
        listOp
            .leftPop(key)
            .outputTransformTo(type)

    /**
     * @see [leftPop]
     */
    public fun <T : Any> leftPop(
        key: String,
        type: JavaType,
    ): T? =
        listOp
            .leftPop(key)
            .outputTransformTo(type)

    /**
     * @see [leftPop]
     */
    public fun <T : Any> leftPop(
        key: String,
        type: TypeReference<T>,
    ): T? =
        listOp
            .leftPop(key)
            .outputTransformTo(type)

    /**
     * Removes and returns first elements in list stored at key.
     *
     * @param T
     * @param key must not be null.
     * @param count
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.leftPop
     */
    public fun <T : Any> leftPop(
        key: String,
        count: Long,
    ): List<Any>? =
        listOp.leftPop(key, count)

    /**
     * Removes and returns last element in list stored at key.
     * @param T
     * @param key must not be null.
     * @param type
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.rightPop
     */
    public fun <T : Any> rightPop(
        key: String,
        type: Class<T>,
    ): T? =
        listOp
            .rightPop(key)
            .outputTransformTo(type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(
        key: String,
        type: JavaType,
    ): T? =
        listOp
            .rightPop(key)
            .outputTransformTo(type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(
        key: String,
        type: TypeReference<T>,
    ): T? =
        listOp
            .rightPop(key)
            .outputTransformTo(type)

    /**
     * Removes and returns last elements in list stored at key.
     *
     * @param T
     * @param key must not be null.
     * @param count
     * @return can be null.
     * @see org.springframework.data.redis.core.ListOperations.rightPop
     */
    public fun <T : Any> rightPop(
        key: String,
        count: Long,
    ): List<Any>? =
        listOp.rightPop(key, count)

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
    public fun <T : Any> index(
        key: String,
        index: Long,
        type: Class<T>,
    ): T? =
        listOp
            .index(key, index)
            .outputTransformTo(type)

    /**
     * @see [RedisListGetOp.index]
     */
    public fun <T : Any> index(
        key: String,
        index: Long,
        type: JavaType,
    ): T? =
        listOp
            .index(key, index)
            .outputTransformTo(type)

    /**
     * @see [RedisListGetOp.index]
     */
    public fun <T : Any> index(
        key: String,
        index: Long,
        type: TypeReference<T>,
    ): T? =
        listOp
            .index(key, index)
            .outputTransformTo(type)

    /**
     * Get elements between begin and end from list at key.
     *
     * @param T
     * @param key must not be null.
     * @param start
     * @param end
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.range
     */
    public fun <T : Any> range(
        key: String,
        start: Long,
        end: Long,
    ): List<T>? =
        listOp.range(key, start, end).asTo()

    /**
     * Get the size of list stored at key.
     *
     * @param key must not be null.
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.size
     */
    public fun size(key: String): Long? =
        listOp.size(key)
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
    public fun <T : Any> leftPush(
        key: String,
        value: T,
    ): Long? =
        listOp.leftPush(key, value.inputTransformTo())

    /**
     * Insert value to key before pivot.
     * @param T
     * @param key must not be null.
     * @param pivot must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPush
     */
    public fun <T : Any> leftPush(
        key: String,
        pivot: Long,
        value: T,
    ): Long? =
        listOp.leftPush(key, pivot, value.inputTransformTo())

    /**
     * Prepend values to key only if the list exists.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPushIfPresent
     */
    public fun <T : Any> leftPushIfPresent(
        key: String,
        value: T,
    ): Long? =
        listOp.leftPushIfPresent(key, value.inputTransformTo())

    /**
     * Prepend values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPushAll
     */
    public fun <T : Any> leftPushAll(
        key: String,
        vararg value: T,
    ): Long? =
        listOp.leftPushAll(key, value.onEach { it.inputTransformTo() })

    /**
     * Prepend values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.leftPushAll
     */
    public fun <T : Any> leftPushAll(
        key: String,
        value: Collection<T>,
    ): Long? =
        listOp.leftPushAll(key, value.onEach { it.inputTransformTo() })

    /**
     * Append value to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPush
     */
    public fun <T : Any> rightPush(
        key: String,
        value: T,
    ): Long? =
        listOp.rightPush(key, value.inputTransformTo())

    /**
     * Insert value to key after pivot.
     * @param T
     * @param key must not be null.
     * @param pivot must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPush
     */
    public fun <T : Any> rightPush(
        key: String,
        pivot: Long,
        value: T,
    ): Long? =
        listOp.rightPush(key, pivot, value.inputTransformTo())

    /**
     * Append values to key only if the list exists.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPushIfPresent
     */
    public fun <T : Any> rightPushIfPresent(
        key: String,
        value: T,
    ): Long? =
        listOp.rightPushIfPresent(key, value.inputTransformTo())

    /**
     * Append values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPushAll
     */
    public fun <T : Any> rightPushAll(
        key: String,
        vararg value: T,
    ): Long? =
        listOp.rightPushAll(key, value.onEach { it.inputTransformTo() })

    /**
     * Append values to key.
     * @param T
     * @param key must not be null.
     * @param value
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.ListOperations.rightPushAll
     */
    public fun <T : Any> rightPushAll(
        key: String,
        value: Collection<T>,
    ): Long? =
        listOp.rightPushAll(key, value.onEach { it.inputTransformTo() })
}
