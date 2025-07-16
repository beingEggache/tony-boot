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

package tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import java.util.Date
import java.util.concurrent.TimeUnit
import tony.core.utils.toCollectionJavaType
import tony.redis.hashOp
import tony.redis.redisTemplate

public sealed interface RedisMapOp :
    RedisMapGetOp,
    RedisMapSetOp

/**
 * redis map 值获取操作
 * @author tangli
 * @date 2023/06/09 19:20
 */
public sealed interface RedisMapGetOp : RedisValueTransformer {
    /**
     * 获取 map 值
     *
     * @param T   值类型
     * @param key  键
     * @param hashKey map健
     * @param type 值 class
     * @return
     */
    public fun <T : Any> get(
        key: String,
        hashKey: String,
        type: Class<T>,
    ): T? =
        hashOp
            .get(key, hashKey)
            .outputTransformTo(type)

    /**
     * 获取 map 值
     * @see [RedisMapGetOp.get]
     * @param type 兼容jackson
     * @return
     */
    public fun <T : Any> get(
        key: String,
        hashKey: String,
        type: JavaType,
    ): T? =
        hashOp
            .get(key, hashKey)
            .outputTransformTo(type)

    /**
     * 获取 map 值
     * @see [RedisMapGetOp.get]
     * @param type 兼容jackson
     * @return
     */
    public fun <T : Any> get(
        key: String,
        hashKey: String,
        type: TypeReference<T>,
    ): T? =
        hashOp
            .get(key, hashKey)
            .outputTransformTo(type)

    /**
     * Get values for given [hashKeys] from hash at [key]. Values are in the order of the requested keys
     * Absent field values are represented using null in the resulting [List].
     *
     * @param key must not be null.
     * @param hashKeys must not be null.
     * @return null when used in pipeline / transaction.
     */
    public fun multiGet(
        key: String,
        hashKeys: Collection<String>,
    ): List<*> =
        hashOp
            .multiGet(key, hashKeys)

    /**
     * @param type component type
     * @see [multiGet]
     */
    public fun <T> multiGet(
        key: String,
        hashKeys: Collection<String>,
        type: Class<T>,
    ): List<T> =
        hashOp
            .multiGet(key, hashKeys)
            .outputTransformTo(type.toCollectionJavaType(List::class.java))
            ?: listOf()

    /**
     * @param type component type
     * @see [multiGet]
     */
    public fun <T> multiGet(
        key: String,
        hashKeys: Collection<String>,
        type: JavaType,
    ): List<T> =
        hashOp
            .multiGet(key, hashKeys)
            .outputTransformTo(type.toCollectionJavaType(List::class.java))
            ?: listOf()

    /**
     * @param type component type
     * @see [multiGet]
     */
    public fun <T> multiGet(
        key: String,
        hashKeys: Collection<String>,
        type: TypeReference<T>,
    ): List<T> =
        hashOp
            .multiGet(key, hashKeys)
            .outputTransformTo(type.type.toCollectionJavaType(List::class.java))
            ?: listOf()

    /**
     * 根据key值获取整个map
     *
     * @param key
     * @return
     */
    public fun entries(key: String): Map<String, Any?> =
        hashOp.entries(key)
}

/**
 * redis map 值保存操作
 * @author tangli
 * @date 2023/06/09 19:20
 */
public sealed interface RedisMapSetOp : RedisValueTransformer {
    /**
     * Set multiple hash fields to multiple values using data provided in m at the [key] and expired at [date]
     *
     * @param key
     * @param map
     * @param date
     */
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ) {
        if (map == null) {
            return
        }
        redisTemplate.boundHashOps<String, Any>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

    /**
     * Set multiple hash fields to multiple values using data provided in m at the [key]
     * and expired in [timeout], [timeUnit]
     *
     * @param key
     * @param map
     * @param timeout
     * @param timeUnit
     */
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
            hashOp.putAll(key, map)
        } else {
            redisTemplate.boundHashOps<String, Any?>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    /**
     * Set the value of a hash key at the [key].
     * @param T
     * @param key
     * @param hashKey
     * @param value
     */
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
    ) {
        hashOp.put(key, hashKey, value.inputTransformTo())
    }

    /**
     * Set the value of a hash key only if key does not exist.
     * @param T
     * @param key
     * @param hashKey
     * @param value
     */
    public fun <T : Any> putIfAbsent(
        key: String,
        hashKey: String,
        value: T,
    ): Boolean =
        hashOp.putIfAbsent(key, hashKey, value.inputTransformTo())
}
