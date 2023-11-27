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
import com.tony.redis.valueOp
import java.util.concurrent.TimeUnit

public sealed interface RedisValueOp :
    RedisValueGetOp,
    RedisValueSetOp

/**
 * redis value 保存操作
 * @author Tang Li
 * @date 2023/06/06 11:01
 */
public sealed interface RedisValueSetOp : RedisValueTransformer {
    /**
     * Set the value and expiration timeout for key.
     *
     * @param T
     * @param key must not be null.
     * @param value must not be null.
     * @param timeout
     * @param timeUnit
     */
    public fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit =
        if (timeout == 0L) {
            valueOp.set(key, value.inputTransformTo())
        } else {
            valueOp.set(key, value.inputTransformTo(), timeout, timeUnit)
        }

    /**
     * 当键 **不存在** 时保存。
     *
     * @param T
     * @param key must not be null.
     * @param value
     * @param timeout must not be null.
     * @param timeUnit must not be null.
     * @return
     */
    public fun <T : Any> setIfAbsent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? =
        if (timeout == 0L) {
            valueOp.setIfAbsent(key, value.inputTransformTo())
        } else {
            valueOp.setIfAbsent(key, value.inputTransformTo(), timeout, timeUnit)
        }

    /**
     * 当键 **存在** 时保存。
     *
     * @param T
     * @param key must not be null.
     * @param value must not be null.
     * @param timeout the key expiration timeout.
     * @param timeUnit must not be null.
     * @return
     */
    public fun <T : Any> setIfPresent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? =
        if (timeout == 0L) {
            valueOp.setIfPresent(key, value.inputTransformTo())
        } else {
            valueOp.setIfPresent(key, value.inputTransformTo(), timeout, timeUnit)
        }

    /**
     * Set value of key and return its old value.
     * @param T
     * @param key must not be null.
     * @param type the type will transform to.
     * @return null when key does not exist or used in pipeline / transaction.
     */
    public fun <T : Any> getAndSet(
        key: String,
        value: T,
        type: Class<T>,
    ): T? =
        valueOp
            .getAndSet(key, value.inputTransformTo())
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueSetOp.getAndSet]
     */
    public fun <T : Any> getAndSet(
        key: String,
        value: T,
        type: JavaType,
    ): T? =
        valueOp
            .getAndSet(key, value.inputTransformTo())
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueSetOp.getAndSet]
     */
    public fun <T : Any> getAndSet(
        key: String,
        value: T,
        type: TypeReference<T>,
    ): T? =
        valueOp
            .getAndSet(key, value.inputTransformTo())
            .outputTransformTo(type)

    /**
     * Return the value at key and expire the key by applying timeout.
     * @param T
     * @param key must not be null.
     * @param type the type will transform to.
     * @param timeout
     * @param timeUnit must not be null.
     * @return
     */
    public fun <T : Any> getAndExpire(
        key: String,
        type: Class<T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? =
        valueOp
            .getAndExpire(key, timeout, timeUnit)
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueSetOp.getAndExpire]
     */
    public fun <T : Any> getAndExpire(
        key: String,
        type: JavaType,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? =
        valueOp
            .getAndExpire(key, timeout, timeUnit)
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueSetOp.getAndExpire]
     */
    public fun <T : Any> getAndExpire(
        key: String,
        type: TypeReference<T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? =
        valueOp
            .getAndExpire(key, timeout, timeUnit)
            .outputTransformTo(type)
}

/**
 * redis value 读取操作
 *
 * @author Tang Li
 * @date 2023/06/06 11:01
 */
public sealed interface RedisValueGetOp : RedisValueTransformer {
    /**
     * Get the value of key.
     *
     * @param T
     * @param key must not be null.
     * @param type the type will transform to.
     * @return null when key does not exist or used in pipeline / transaction.
     */
    public fun <T : Any> get(
        key: String,
        type: Class<T>,
    ): T? =
        valueOp
            .get(key)
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueGetOp.get]
     */
    public fun <T : Any> get(
        key: String,
        type: JavaType,
    ): T? =
        valueOp
            .get(key)
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson
     * @see [RedisValueGetOp.get]
     */
    public fun <T : Any> get(
        key: String,
        type: TypeReference<T>,
    ): T? =
        valueOp
            .get(key)
            .outputTransformTo(type)

    /**
     * Return the value at key and delete the key.
     *
     * @param T
     * @param key must not be null.
     * @param type the type will transform to.
     * @return null when key does not exist or used in pipeline / transaction.
     */
    public fun <T : Any> getAndDelete(
        key: String,
        type: Class<T>,
    ): T? =
        valueOp
            .getAndDelete(key)
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueGetOp.getAndDelete]
     */
    public fun <T : Any> getAndDelete(
        key: String,
        type: JavaType,
    ): T? =
        valueOp
            .getAndDelete(key)
            .outputTransformTo(type)

    /**
     * @param type 兼容jackson.
     * @see [RedisValueGetOp.getAndDelete]
     */
    public fun <T : Any> getAndDelete(
        key: String,
        type: TypeReference<T>,
    ): T? =
        valueOp
            .getAndDelete(key)
            .outputTransformTo(type)
}
