package com.tony.cache

import com.tony.SpringContexts
import com.tony.exception.ApiException
import com.tony.utils.secureRandom
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisConnectionUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.scripting.support.ResourceScriptSource
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

    private val script: DefaultRedisScript<Long> = DefaultRedisScript<Long>().apply {
        setScriptSource(
            ResourceScriptSource(
                ClassPathResource("META-INF/scripts/lock_key.lua"),
            ),
        )
        resultType = Long::class.java
    }

    /**
     * redis 事务操作.
     * ## 注意, 事务回调 [callback] 中读取操作是取不到值的.
     *
     * @param callback
     */
    @JvmSynthetic
    @JvmStatic
    public fun doInTransaction(callback: () -> Unit) {
        RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        redisTemplate.setEnableTransactionSupport(true)
        redisTemplate.multi()
        callback()
        redisTemplate.exec()
    }

    /**
     * redis 事务操作.
     * ## 注意, 事务回调 [callback] 中读取操作是取不到值的.
     *
     * @param callback
     */
    @JvmStatic
    public fun doInTransaction(callback: Runnable) {
        RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        redisTemplate.setEnableTransactionSupport(true)
        redisTemplate.multi()
        callback.run()
        redisTemplate.exec()
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
        return redisTemplate.execute(script, Collections.singletonList(key), 1L, timeout) == 1L
    }

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
     * 根据匹配键 批量删除
     *
     * @param keys 可ant匹配
     * @return
     */
    @JvmStatic
    public fun deleteWithKeyPatterns(vararg keys: String): Long? =
        redisTemplate.delete(keys(*keys))

    /**
     * 根据匹配键 批量删除
     *
     * @param keys 可ant匹配
     * @return
     */
    @JvmStatic
    public fun deleteWithKeyPatterns(keys: Collection<String>): Long? =
        redisTemplate.delete(keys(keys))

    /**
     * 批量删除
     *
     * @param keys
     * @return
     */
    @JvmStatic
    public fun delete(vararg keys: String): Long? =
        redisTemplate.delete(keys.asList())

    /**
     * 批量删除
     *
     * @param keys
     * @return
     */
    @JvmStatic
    public fun delete(keys: Collection<String>): Long? =
        redisTemplate.delete(keys)

    /**
     * 获取键
     *
     * @param keys
     * @return
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public fun keys(vararg keys: String): Collection<String> = keys.fold(HashSet()) { set, key ->
        set.addAll(redisTemplate.keys(key))
        set
    }

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
