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
object RedisManager {

    @JvmField
    val values = RedisValues

    @JvmField
    val maps = RedisMaps

    @JvmField
    val lists = RedisLists

    @JvmField
    val keys = RedisKeys

    @JvmStatic
    val redisTemplate: RedisTemplate<String, Any> by lazy {
        val serializer = GenericJackson2JsonRedisSerializer(OBJECT_MAPPER)
        val stringRedisSerializer = RedisSerializer.string()
        RedisTemplate<String, Any>().apply {
            setConnectionFactory(Beans.getBean())
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
    fun doInTransaction(callback: () -> Unit) {
        RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        redisTemplate.setEnableTransactionSupport(true)
        redisTemplate.multi()
        callback()
        redisTemplate.exec()
    }

    @JvmStatic
    fun lockKey(key: String, timeout: Long): Boolean {
        if (timeout <= 0) throw ApiException("timeout must greater than 0")
        return redisTemplate.execute(script, Collections.singletonList(key), 1L, timeout) == 1L
    }

    @JvmStatic
    fun <T> executeScript(script: RedisScript<T>, keys: List<String>, args: List<Any?>): T? {
        return redisTemplate.execute(script, keys, *args.toTypedArray())
    }

    @JvmStatic
    fun lockKey(key: String, timeout: Long, waitTimeout: Long): Boolean {
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
    fun expire(
        key: String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Boolean = redisTemplate.expire(key, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun getExpire(
        key: String,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Long = redisTemplate.getExpire(key, timeUnit)

    @JvmStatic
    fun delete(key: String) =
        redisTemplate.delete(keys(key))

    @Suppress("MemberVisibilityCanBePrivate")
    fun keys(key: String): Collection<String> = redisTemplate.keys(key)
}
