package com.tony.redis

import com.tony.SpringContexts
import com.tony.exception.ApiException
import com.tony.utils.secureRandom
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisConnectionUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import java.util.Collections
import java.util.concurrent.TimeUnit

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

    @JvmStatic
    public val redisTemplate: RedisTemplate<String, Any> by SpringContexts.getBeanByLazy("redisTemplate")

    /**
     * 简单锁脚本引用
     */
    private val lockScript: RedisScript<Long> =
        RedisScript.of(ClassPathResource("META-INF/scripts/lockKey.lua"), Long::class.java)

    /**
     * 批量删除脚本
     */
    private val deleteKeyByPatternScript: RedisScript<Long?> =
        RedisScript.of(ClassPathResource("META-INF/scripts/deleteByKeyPatterns.lua"), Long::class.java)

    /**
     * redis 事务操作.
     *
     * ## 注: 在事务中的redis 操作是获取不到值的. 只能在方法最终返回值中按顺序获取.
     *
     * @param callback
     */
    @JvmSynthetic
    @JvmStatic
    public fun doInTransaction(callback: () -> Unit): List<Any> {
        val redisConnection = RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        synchronized(redisConnection) {
            redisConnection.multi()
            try {
                callback()
                return redisConnection.exec()
            } catch (e: Throwable) {
                redisConnection.discard()
                throw e
            } finally {
                RedisConnectionUtils.unbindConnection(redisTemplate.requiredConnectionFactory)
            }
        }
    }

    /**
     * redis 事务操作.
     *
     * @see `RedisTemplate.execute(SessionCallback<T> session)`
     * @param callback
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
                redisConnection.discard()
                throw e
            } finally {
                RedisConnectionUtils.unbindConnection(redisTemplate.requiredConnectionFactory)
            }
        }
    }

    /**
     * redis 分布式锁简单实现.
     *
     * @param key
     * @param timeout
     * @return
     */
    @JvmStatic
    public fun lockKey(key: String, timeout: Long): Boolean {
        if (timeout <= 0) throw ApiException("timeout must greater than 0")
        return redisTemplate.execute(lockScript, Collections.singletonList(key), 1L, timeout) == 1L
    }

    public inline fun <reified T> String.toRedisScript(): RedisScript<T> = RedisScript.of(this, T::class.java)

    /**
     * 执行 redis lua 脚本
     *
     * @param T
     * @param script
     * @param keys
     * @param args
     * @return
     */
    @JvmStatic
    public fun <T> executeScript(script: RedisScript<T>, keys: List<String>, args: List<Any?>): T? {
        return redisTemplate.execute(script, keys, *args.toTypedArray())
    }

    /**
     * redis 分布式锁简单实现.
     *
     * @param key
     * @param timeout    锁过期时间
     * @param waitTimeout 等待 / 自旋时间
     * @return
     */
    @JvmStatic
    public fun lockKey(key: String, timeout: Long, waitTimeout: Long): Boolean {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < waitTimeout) {
            if (lockKey(key, timeout)) {
                return true
            }
            try {
                TimeUnit.MILLISECONDS.sleep(secureRandom.nextInt(100).toLong())
            } catch (e: InterruptedException) {
                logger.error(e.message, e)
            }
        }
        return false
    }

    /**
     * 同 redisTemplate.expire(key, timeout, timeUnit)
     *
     * @param key
     * @param timeout
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     * @return
     */
    @JvmStatic
    @JvmOverloads
    public fun expire(
        key: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Boolean = redisTemplate.expire(key, timeout, timeUnit)

    /**
     * 同 redisTemplate.getExpire(key, timeUnit)
     *
     * @param key
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     * @return
     */
    @JvmStatic
    @JvmOverloads
    public fun getExpire(
        key: String,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Long = redisTemplate.getExpire(key, timeUnit)

    /**
     * redis 根据 [keyPatterns] 批量删除.
     *
     * @param keyPatterns 可ant匹配
     * @return 删除的 key 个数
     */
    @JvmStatic
    public fun deleteByKeyPatterns(vararg keyPatterns: String): Long =
        deleteByKeyPatterns(keyPatterns.asList())

    /**
     * redis 根据 [keyPatterns] 批量删除.
     *
     * @param keyPatterns 可ant匹配
     * @return 删除的 key 个数
     */
    @JvmStatic
    public fun deleteByKeyPatterns(keyPatterns: List<String>): Long {
        if (keyPatterns.isEmpty()) throw ApiException("keyPatterns must not be empty.")
        if (keyPatterns.any { it.isBlank() }) throw ApiException("keyPattern must not be blank.")
        @Suppress("RedundantNullableReturnType")
        val result: Long? = redisTemplate.execute(deleteKeyByPatternScript, keyPatterns)
        return result ?: 0L
    }

    /**
     * 批量删除
     *
     * @param keys
     * @return
     */
    @JvmStatic
    public fun delete(vararg keys: String): Long =
        delete(keys.asList())

    /**
     * 批量删除
     *
     * @param keys
     * @return
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
     * 获取键
     *
     * @param keys
     * @return
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun keys(vararg keys: String): Collection<String> = keys(keys.asList())

    /**
     * 获取键
     *
     * @param keys
     * @return
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun keys(keys: Collection<String>): Collection<String> = keys.fold(HashSet()) { set, key ->
        set.addAll(redisTemplate.keys(key))
        set
    }

    internal fun String.trimQuotes() = substring(0, this.length)
}
