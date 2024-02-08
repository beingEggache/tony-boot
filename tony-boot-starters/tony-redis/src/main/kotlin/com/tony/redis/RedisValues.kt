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

package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.utils.asTo
import java.util.concurrent.TimeUnit

/**
 * redis value 操作单例.
 *
 * @author Tang Li
 * @date 2023/5/25 9:24
 */
public data object RedisValues {
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
    public fun increment(
        key: String,
        delta: Long = 1L,
        initial: Long? = null,
    ): Long? =
        RedisManager
            .doInTransaction {
                if (initial != null) {
                    redisTemplate
                        .opsForValue()
                        .increment(key, initial)
                }
                redisTemplate
                    .opsForValue()
                    .increment(key, delta)
            }.last()
            .asTo()

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
    public fun increment(
        key: String,
        delta: Double = 1.0,
        initial: Double? = null,
    ): Double? =
        RedisManager
            .doInTransaction {
                if (initial != null) {
                    redisTemplate
                        .opsForValue()
                        .increment(key, initial)
                }
                redisTemplate
                    .opsForValue()
                    .increment(key, delta)
            }.last()
            .asTo()

    /**
     * @see [com.tony.redis.service.RedisValueOp.set]
     */
    @JvmStatic
    @JvmOverloads
    public fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit =
        redisService.set(key, value, timeout, timeUnit)

    /**
     * @see [com.tony.redis.service.RedisValueOp.setIfAbsent]
     */
    @JvmStatic
    @JvmOverloads
    public fun <T : Any> setIfAbsent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? =
        redisService.setIfAbsent(key, value, timeout, timeUnit)

    /**
     * @see [com.tony.redis.service.RedisValueOp.setIfPresent]
     */
    @JvmStatic
    @JvmOverloads
    public fun <T : Any> setIfPresent(
        key: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean? =
        redisService.setIfPresent(key, value, timeout, timeUnit)

    /**
     * @see [com.tony.redis.service.RedisValueOp.multiSet]
     */
    @JvmStatic
    public fun multiSet(keyValues: Map<String, Any>): Unit =
        redisService.multiSet(keyValues)

    /**
     * @see [com.tony.redis.service.RedisValueOp.multiSetIfAbsent]
     */
    @JvmStatic
    public fun multiSetIfAbsent(keyValues: Map<String, Any>): Boolean? =
        redisService.multiSetIfAbsent(keyValues)

    /**
     * @see [com.tony.redis.service.RedisValueOp.get]
     */
    public inline fun <reified T : Any> get(key: String): T? =
        redisService.get(key, (object : TypeReference<T>() {}))

    /**
     * @see [com.tony.redis.service.RedisValueOp.get]
     */
    @JvmStatic
    public fun <T : Any> get(
        key: String,
        type: Class<T>,
    ): T? =
        redisService.get(key, type)

    /**
     * @see [com.tony.redis.service.RedisValueOp.get]
     */
    @JvmStatic
    public fun <T : Any> get(
        key: String,
        type: JavaType,
    ): T? =
        redisService.get(key, type)

    /**
     * @see [com.tony.redis.service.RedisValueOp.get]
     */
    @JvmStatic
    public fun <T : Any> get(
        key: String,
        type: TypeReference<T>,
    ): T? =
        redisService.get(key, type)

    /**
     * @see [com.tony.redis.service.RedisValueOp.multiGet]
     */
    @JvmStatic
    public fun multiGet(keys: Collection<String>): List<*> =
        redisService.multiGet(keys)

    /**
     * @see [com.tony.redis.service.RedisValueOp.multiGet]
     */
    @JvmStatic
    public fun <T> multiGet(
        keys: Collection<String>,
        type: Class<T>,
    ): List<T> =
        redisService.multiGet(keys, type)

    /**
     * @see [com.tony.redis.service.RedisValueOp.multiGet]
     */
    @JvmStatic
    public fun <T> multiGet(
        keys: Collection<String>,
        type: JavaType,
    ): List<T> =
        redisService.multiGet(keys, type)

    /**
     * @see [com.tony.redis.service.RedisValueOp.multiGet]
     */
    @JvmStatic
    public fun <T> multiGet(
        keys: Collection<String>,
        type: TypeReference<T>,
    ): List<T> =
        redisService.multiGet(keys, type)
}
