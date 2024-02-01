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

import com.tony.exception.ApiException
import com.tony.utils.alsoIf
import com.tony.utils.secureRandom
import java.util.Collections
import java.util.Date
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisConnectionUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript

/**
 * Redis 操作聚合类单例.
 *
 * @constructor Create empty Redis manager
 */
public object RedisManager {
    private val logger = LoggerFactory.getLogger(RedisManager::class.java)

    @JvmField
    public val values: RedisValues = RedisValues

    @JvmField
    public val maps: RedisMaps = RedisMaps

    @JvmField
    public val lists: RedisLists = RedisLists

    @JvmField
    public val keys: RedisKeys = RedisKeys

    /**
     * Redis 事务操作.
     * ## 注: 在事务中的redis 操作是获取不到值的. 只能在方法最终返回值中按顺序获取.
     *
     * @see `RedisTemplate.execute(SessionCallback<T> session)`
     * @param [callback] 回调
     * @return [List<Any?>]
     * @author Tang Li
     * @date 2023/09/28 19:58
     * @since 1.0.0
     */
    @JvmStatic
    public fun doInTransaction(callback: Runnable): List<Any?> {
        val redisConnection = RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        synchronized(redisConnection) {
            redisConnection.multi()
            try {
                callback.run()
                return redisConnection.exec()
            } catch (e: Throwable) {
                redisConnection
                    .isQueueing
                    .alsoIf { redisConnection.discard() }
                throw e
            } finally {
                RedisConnectionUtils.unbindConnection(redisTemplate.requiredConnectionFactory)
            }
        }
    }

    /**
     * 同 [RedisTemplate.hasKey]
     *
     * @param key
     * @return
     */
    @JvmStatic
    public fun hasKey(key: String): Boolean =
        redisTemplate.hasKey(key)

    /**
     * Redis 分布式锁简单实现.
     * @param [key] 钥匙
     * @param [timeout] 超时
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 19:59
     * @since 1.0.0
     */
    @JvmStatic
    public fun lockKey(
        key: String,
        timeout: Long,
    ): Boolean {
        if (timeout <= 0) throw ApiException("timeout must greater than 0")
        return redisTemplate.execute(lockScript, Collections.singletonList(key), 1L, timeout) == 1L
    }

    public inline fun <reified T> String.toRedisScript(): RedisScript<T> =
        RedisScript.of(this, T::class.java)

    /**
     * Executes the given RedisScript.
     *
     * @param T
     * @param script The script to execute
     * @param keys Any keys that need to be passed to the script
     * @param args Any args that need to be passed to the script
     * @return The return value of the script or null if RedisScript.getResultType() is null,
     * likely indicating a throw-away status reply (i.e. "OK")
     */
    @JvmStatic
    public fun <T> executeScript(
        script: RedisScript<T>,
        keys: List<String>,
        args: List<Any?>,
    ): T? =
        redisTemplate.execute(script, keys, *args.toTypedArray())

    /**
     * redis 分布式锁简单实现.
     *
     * @param key
     * @param timeout    锁过期时间
     * @param waitTimeout 等待 / 自旋时间
     * @return
     */
    @JvmStatic
    public fun lockKey(
        key: String,
        timeout: Long,
        waitTimeout: Long,
    ): Boolean {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < waitTimeout) {
            if (lockKey(key, timeout)) {
                return true
            }
            try {
                TimeUnit
                    .MILLISECONDS
                    .sleep(
                        secureRandom
                            .nextInt(100)
                            .toLong()
                    )
            } catch (e: InterruptedException) {
                logger.error(e.message, e)
            }
        }
        return false
    }

    /**
     * Set time to live for given key.
     *
     * @param key must not be null.
     * @param timeout
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]. must not be null.
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.RedisOperations.expire
     */
    @JvmStatic
    @JvmOverloads
    public fun expire(
        key: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean =
        redisTemplate.expire(key, timeout, timeUnit)

    /**
     * Set the expiration for given key as a date timestamp.
     *
     * @param key must not be null.
     * @param date must not be null.
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.RedisOperations.expireAt
     */
    @JvmStatic
    public fun expireAt(
        key: String,
        date: Date,
    ): Boolean =
        redisTemplate.expireAt(key, date)

    /**
     * Get the time to live for key in and convert it to the given TimeUnit.
     *
     * @param key
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     * @return null when used in pipeline / transaction.
     * @see org.springframework.data.redis.core.RedisOperations.getExpire
     */
    @JvmStatic
    @JvmOverloads
    public fun getExpire(
        key: String,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Long =
        redisTemplate.getExpire(key, timeUnit)

    /**
     * redis 根据 [keyPatterns] 批量删除.
     *
     * @param keyPatterns 可ant匹配
     * @return The number of keys that were removed. null when used in pipeline / transaction.
     */
    @JvmStatic
    public fun deleteByKeyPatterns(vararg keyPatterns: String): Long =
        deleteByKeyPatterns(keyPatterns.asList())

    /**
     * redis 根据 [keyPatterns] 批量删除.
     *
     * @param keyPatterns 可ant匹配
     * @return The number of keys that were removed. null when used in pipeline / transaction.
     */
    @JvmStatic
    public fun deleteByKeyPatterns(keyPatterns: Collection<String>): Long {
        if (keyPatterns.isEmpty()) throw ApiException("keyPatterns must not be empty.")
        if (keyPatterns.any { it.isBlank() }) throw ApiException("keyPattern must not be blank.")
        @Suppress("RedundantNullableReturnType")
        val result: Long? = redisTemplate.execute(deleteKeyByPatternScript, keyPatterns.toList())
        return result ?: 0L
    }

    /**
     * Delete given keys.
     *
     * @param keys must not be null.
     * @return The number of keys that were removed. null when used in pipeline / transaction.
     */
    @JvmStatic
    public fun delete(vararg keys: String): Long =
        delete(keys.asList())

    /**
     * Delete given keys.
     *
     * @param keys must not be null.
     * @return The number of keys that were removed. null when used in pipeline / transaction.
     */
    @JvmStatic
    public fun delete(keys: Collection<String>): Long {
        @Suppress("RedundantNullableReturnType")
        val result: Long? = redisTemplate.delete(keys)
        return result ?: 0L
    }

    /**
     * 清空redis, 也就是直接执行flushdb 命令
     *
     */
    @JvmStatic
    public fun flushdb() {
        redisTemplate.execute {
            it.execute("flushdb")
        }
    }

    /**
     * Find all keys matching the given pattern.
     *
     * @param keys  must not be null.
     * @return null when used in pipeline / transaction.
     */
    public fun keys(vararg keys: String): Collection<String> =
        keys(keys.asList())

    /**
     * Find all keys matching the given pattern.
     *
     * @param keys must not be null.
     * @return null when used in pipeline / transaction.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun keys(keys: Collection<String>): Collection<String> =
        keys.fold(HashSet()) { set, key ->
            set.addAll(redisTemplate.keys(key))
            set
        }
}
