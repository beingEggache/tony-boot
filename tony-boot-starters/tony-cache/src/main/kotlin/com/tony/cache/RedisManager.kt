@file:Suppress("unused")

package com.tony.cache

import com.tony.Beans
import com.tony.exception.ApiException
import com.tony.utils.OBJECT_MAPPER
import org.springframework.beans.factory.getBean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisConnectionUtils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.scripting.support.ResourceScriptSource
import java.util.Collections
import java.util.Random
import java.util.concurrent.TimeUnit

@Suppress("MemberVisibilityCanBePrivate")
public object RedisManager {

    @JvmField
    public val values: RedisValues = RedisValues

    @JvmField
    public val maps: RedisMaps = RedisMaps

    @JvmField
    public val lists: RedisLists = RedisLists

    @JvmField
    public val keys: RedisKeys = RedisKeys

    @JvmStatic
    public val redisTemplate: RedisTemplate<String, Any> by lazy {
        val serializer = GenericJackson2JsonRedisSerializer(OBJECT_MAPPER)
        val stringRedisSerializer = RedisSerializer.string()
        RedisTemplate<String, Any>().apply {
            connectionFactory = Beans.getBean()
            keySerializer = stringRedisSerializer
            hashKeySerializer = stringRedisSerializer
            valueSerializer = serializer
            hashValueSerializer = serializer
            afterPropertiesSet()
        }
    }

    private val script: DefaultRedisScript<Long> = DefaultRedisScript<Long>().apply {
        setScriptSource(
            ResourceScriptSource(
                ClassPathResource("META-INF/scripts/lock_key.lua")
            )
        )
        resultType = Long::class.java
    }

    @JvmStatic
    public fun doInTransaction(callback: () -> Unit) {
        RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        redisTemplate.setEnableTransactionSupport(true)
        redisTemplate.multi()
        callback()
        redisTemplate.exec()
    }

    @JvmStatic
    public fun lockKey(key: String, timeout: Long): Boolean {
        if (timeout <= 0) throw ApiException("timeout must greater than 0")
        return redisTemplate.execute(script, Collections.singletonList(key), 1L, timeout) == 1L
    }

    @JvmStatic
    public fun <T> executeScript(script: RedisScript<T>, keys: List<String>, args: List<Any?>): T? {
        return redisTemplate.execute(script, keys, *args.toTypedArray())
    }

    @JvmStatic
    public fun lockKey(key: String, timeout: Long, waitTimeout: Long): Boolean {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < waitTimeout) {
            if (lockKey(key, timeout)) {
                return true
            }
            try {
                TimeUnit.MILLISECONDS.sleep(Random().nextInt(100).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return false
    }

    @JvmStatic
    @JvmOverloads
    public fun expire(
        key: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Boolean = redisTemplate.expire(key, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    public fun getExpire(
        key: String,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long = redisTemplate.getExpire(key, timeUnit)

    @JvmStatic
    public fun deleteWithKeyPatterns(vararg keys: String): Long? =
        redisTemplate.delete(keys(*keys))

    @JvmStatic
    public fun deleteWithKeyPatterns(keys: Collection<String>): Long? =
        redisTemplate.delete(keys(keys))

    @JvmStatic
    public fun delete(vararg keys: String): Long? =
        redisTemplate.delete(keys.asList())

    @JvmStatic
    public fun delete(keys: Collection<String>): Long? =
        redisTemplate.delete(keys)

    @Suppress("MemberVisibilityCanBePrivate")
    public fun keys(vararg keys: String): Collection<String> = keys.fold(HashSet()) { set, key ->
        set.addAll(redisTemplate.keys(key))
        set
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public fun keys(keys: Collection<String>): Collection<String> = keys.fold(HashSet()) { set, key ->
        set.addAll(redisTemplate.keys(key))
        set
    }

    internal fun String.trimQuotes() = substring(0, this.length)
}
