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

package tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import java.util.Date
import java.util.concurrent.TimeUnit
import tony.core.utils.asTo

/**
 * redis hash操作单例.
 *
 * @author tangli
 * @date 2023/5/25 9:57
 */
public data object RedisMaps {
    /**
     * Determine if given hash hashKey exists.
     *
     * @param key must not be null.
     * @param hashKey must not be null.
     * @return null when used in pipeline / transaction.
     */
    @Suppress("SimplifyBooleanWithConstants")
    @JvmStatic
    public fun hasKey(
        key: String,
        hashKey: String,
    ): Boolean =
        true == hashOp.hasKey(key, hashKey)

    /**
     * Delete given hash hashKeys.
     *
     * @param key must not be null.
     * @param hashKeys must not be null.
     * @return null when used in pipeline / transaction.
     */
    @Suppress("RedundantNullableReturnType")
    @JvmStatic
    public fun delete(
        key: String,
        vararg hashKeys: String,
    ): Long? =
        hashOp.delete(key, *hashKeys)

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
        hashKey: String,
        delta: Long = 1L,
        initial: Long? = null,
    ): Long? =
        RedisManager
            .doInTransaction {
                if (initial != null) {
                    hashOp.putIfAbsent(key, hashKey, initial)
                }
                hashOp.increment(key, hashKey, delta)
            }.last()
            .asTo()

    /**
     * 同 RedisTemplate.boundValueOps.increment.
     *
     * 如果键不存在则创建 [initial] 初始值.
     *
     * @param key must not be null.
     * @param delta
     * @param hashKey must not be null.
     * @param initial
     * @return null when used in pipeline / transaction.
     */
    @JvmStatic
    @JvmOverloads
    public fun increment(
        key: String,
        hashKey: String,
        delta: Double = 1.0,
        initial: Double? = null,
    ): Double? =
        RedisManager
            .doInTransaction {
                if (initial != null) {
                    hashOp.putIfAbsent(key, hashKey, initial)
                }
                hashOp.increment(key, hashKey, delta)
            }.last()
            .asTo()

    /**
     * @see [tony.redis.service.RedisMapSetOp.put]
     */
    @JvmStatic
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
    ): Unit =
        redisService.put(key, hashKey, value)

    /**
     * @see [tony.redis.service.RedisMapSetOp.putIfAbsent]
     */
    @JvmStatic
    public fun <T : Any> putIfAbsent(
        key: String,
        hashKey: String,
        value: T,
    ): Boolean =
        redisService.putIfAbsent(key, hashKey, value)

    /**
     * @see [tony.redis.service.RedisMapSetOp.putAll]
     */
    @JvmStatic
    @JvmOverloads
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit =
        redisService.putAll(key, map, timeout, timeUnit)

    /**
     * @see [tony.redis.service.RedisMapSetOp.putAll]
     */
    @JvmStatic
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ): Unit =
        redisService.putAll(key, map, date)

    /**
     * @see [tony.redis.service.RedisMapGetOp.get]
     */
    @JvmStatic
    public inline fun <reified T : Any> get(
        key: String,
        hashKey: String,
    ): T? =
        get(key, hashKey, T::class.java)

    /**
     * @see [tony.redis.service.RedisMapGetOp.get]
     */
    @JvmStatic
    public fun <T : Any> get(
        key: String,
        hashKey: String,
        clazz: Class<T>,
    ): T? =
        redisService.get(key, hashKey, clazz)

    /**
     * @see [tony.redis.service.RedisMapGetOp.get]
     */
    @JvmStatic
    public fun <T : Any> get(
        key: String,
        hashKey: String,
        javaType: JavaType,
    ): T? =
        redisService.get(key, hashKey, javaType)

    /**
     * @see [tony.redis.service.RedisMapGetOp.get]
     */
    @JvmStatic
    public fun <T : Any> get(
        key: String,
        hashKey: String,
        typeReference: TypeReference<T>,
    ): T? =
        redisService.get(key, hashKey, typeReference)

    /**
     * @see [tony.redis.service.RedisMapGetOp.entries]
     */
    @JvmStatic
    public fun entries(key: String): Map<String, Any?> =
        redisService.entries(key)

    /**
     * @see [tony.redis.service.RedisMapGetOp.multiGet]
     */
    @JvmStatic
    public fun multiGet(
        key: String,
        hashKeys: Collection<String>,
    ): List<*> =
        redisService.multiGet(key, hashKeys)

    /**
     * @see [tony.redis.service.RedisMapGetOp.multiGet]
     */
    @JvmStatic
    public fun <T> multiGet(
        key: String,
        hashKeys: Collection<String>,
        type: Class<T>,
    ): List<T> =
        redisService.multiGet(key, hashKeys, type)

    /**
     * @see [tony.redis.service.RedisMapGetOp.multiGet]
     */
    @JvmStatic
    public fun <T> multiGet(
        key: String,
        hashKeys: Collection<String>,
        type: JavaType,
    ): List<T> =
        redisService.multiGet(key, hashKeys, type)

    /**
     * @see [tony.redis.service.RedisMapGetOp.multiGet]
     */
    @JvmStatic
    public fun <T> multiGet(
        key: String,
        hashKeys: Collection<String>,
        type: TypeReference<T>,
    ): List<T> =
        redisService.multiGet(key, hashKeys, type)
}
