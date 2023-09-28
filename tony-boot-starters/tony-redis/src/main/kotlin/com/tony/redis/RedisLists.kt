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
import com.tony.redis.service.RedisService
import java.util.concurrent.TimeUnit

/**
 * redis 列表操作单例.
 *
 * @author Tang Li
 * @date 2021-03-24 10:12
 */
public object RedisLists {

    /**
     * @see [RedisService.leftPush]
     */
    @JvmStatic
    public fun <T : Any> leftPush(key: String, value: T): Long? =
        redisService.leftPush(key, value)

    /**
     * @see [leftPush]
     */
    @JvmStatic
    public fun <T : Any> leftPush(key: String, pivot: Long, value: T): Long? =
        redisService.leftPush(key, pivot, value)

    /**
     * @see [RedisService.leftPushIfPresent]
     */
    @JvmStatic
    public fun <T : Any> leftPushIfPresent(key: String, value: T): Long? =
        redisService.leftPushIfPresent(key, value)

    /**
     * @see [RedisService.leftPushAll]
     */
    @JvmStatic
    public fun <T : Any> leftPushAll(key: String, vararg value: T): Long? =
        redisService.leftPushAll(key, *value)

    /**
     * @see [leftPushAll]
     */
    @JvmStatic
    public fun <T : Any> leftPushAll(key: String, value: Collection<T>): Long? =
        redisService.leftPushAll(key, value)

    /**
     * @see [RedisService.rightPush]
     */
    @JvmStatic
    public fun <T : Any> rightPush(key: String, value: T): Long? =
        redisService.rightPush(key, value)

    /**
     * @see [rightPush]
     */
    @JvmStatic
    public fun <T : Any> rightPush(key: String, pivot: Long, value: T): Long? =
        redisService.rightPush(key, pivot, value)

    /**
     * @see [RedisService.rightPushIfPresent]
     */
    @JvmStatic
    public fun <T : Any> rightPushIfPresent(key: String, value: T): Long? =
        redisService.rightPushIfPresent(key, value)

    /**
     * @see [RedisService.rightPushAll]
     */
    @JvmStatic
    public fun <T : Any> rightPushAll(key: String, vararg value: T): Long? =
        redisService.rightPushAll(key, *value)

    /**
     * @see [rightPushAll]
     */
    @JvmStatic
    public fun <T : Any> rightPushAll(key: String, value: Collection<T>): Long? =
        redisService.rightPushAll(key, value)

    /**
     * @see [RedisService.leftPop]
     */
    @JvmStatic
    public fun <T : Any> leftPop(key: String, type: Class<T>): T? =
        redisService.leftPop(key, type)

    /**
     * @see [leftPop]
     */
    @JvmStatic
    public fun <T : Any> leftPop(key: String, type: JavaType): T? =
        redisService.leftPop(key, type)

    /**
     * @see [leftPop]
     */
    @JvmStatic
    public fun <T : Any> leftPop(key: String, type: TypeReference<T>): T? =
        redisService.leftPop(key, type)

    /**
     * @see [leftPop]
     */
    @JvmStatic
    public fun <T : Any> leftPop(key: String, count: Long): List<Any>? =
        redisService.leftPop<T>(key, count)

    /**
     * @see [leftPop]
     */
    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> leftPop(key: String): T? =
        redisService.leftPop(key, T::class.java)

    /**
     * @see [RedisService.rightPop]
     */
    @JvmStatic
    public fun <T : Any> rightPop(key: String, type: Class<T>): T? =
        redisService.rightPop(key, type)

    /**
     * @see [rightPop]
     */
    @JvmStatic
    public fun <T : Any> rightPop(key: String, type: JavaType): T? =
        redisService.rightPop(key, type)

    /**
     * @see [rightPop]
     */
    @JvmStatic
    public fun <T : Any> rightPop(key: String, type: TypeReference<T>): T? =
        redisService.rightPop(key, type)

    /**
     * @see [rightPop]
     */
    @JvmStatic
    public fun <T : Any> rightPop(key: String, count: Long): List<Any>? =
        redisService.rightPop<T>(key, count)

    /**
     * @see [rightPop]
     */
    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> rightPop(key: String): T? =
        redisService.rightPop(key, T::class.java)

    /**
     * @see [RedisService.index]
     */
    @JvmStatic
    public fun <T : Any> index(key: String, index: Long, type: Class<T>): T? =
        redisService.index(key, index, type)

    /**
     * @see [index]
     */
    @JvmStatic
    public fun <T : Any> index(key: String, index: Long, type: JavaType): T? =
        redisService.index(key, index, type)

    /**
     * @see [index]
     */
    @JvmStatic
    public fun <T : Any> index(key: String, index: Long, type: TypeReference<T>): T? =
        redisService.index(key, index, type)

    /**
     * @see [index]
     */
    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> index(key: String, index: Long): T? =
        redisService.index(key, index, T::class.java)

    /**
     * @see [RedisService.range]
     */
    @JvmStatic
    public fun <T : Any> range(key: String, start: Long, end: Long): List<T>? =
        redisService.range(key, start, end)

    /**
     * @see [RedisService.size]
     */
    @JvmStatic
    public fun size(key: String): Long? =
        redisService.size(key)

    /**
     * @see [RedisService.rightPopAndLeftPush]
     */
    @JvmStatic
    public fun <T : Any> rightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        type: Class<T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? = redisService.rightPopAndLeftPush(sourceKey, destinationKey, type, timeout, timeUnit)

    /**
     * @see [rightPopAndLeftPush]
     */
    @JvmStatic
    public fun <T : Any> rightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        type: JavaType,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? = redisService.rightPopAndLeftPush(sourceKey, destinationKey, type, timeout, timeUnit)

    /**
     * @see [rightPopAndLeftPush]
     */
    @JvmStatic
    public fun <T : Any> rightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        type: TypeReference<T>,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): T? = redisService.rightPopAndLeftPush(sourceKey, destinationKey, type, timeout, timeUnit)

    /**
     * @see [RedisService.trim]
     */
    @JvmStatic
    public fun trim(key: String, start: Long, end: Long) {
        redisService.trim(key, start, end)
    }
}
