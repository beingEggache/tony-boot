@file:Suppress("unused")

package com.tony.cache

import com.tony.cache.config.RedisCacheProperties
import com.tony.core.utils.OBJECT_MAPPER
import com.tony.core.utils.doIf
import com.tony.core.utils.jsonToObj
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component

@Component
@Suppress("TooManyFunctions")
object RedisUtils {

    @JvmStatic
    private lateinit var factory: RedisConnectionFactory
    private val keyPrefix: String by lazy {
        redisCacheProperties.keyPrefix
    }
    private lateinit var redisCacheProperties: RedisCacheProperties

    @JvmStatic
    private val redisTemplate: RedisTemplate<String, Any> by lazy {
        val serializer = GenericJackson2JsonRedisSerializer(OBJECT_MAPPER)
        val stringRedisSerializer = StringRedisSerializer()
        RedisTemplate<String, Any>().apply {
            connectionFactory = factory
            keySerializer = stringRedisSerializer
            hashKeySerializer = stringRedisSerializer
            valueSerializer = serializer
            hashValueSerializer = serializer
            afterPropertiesSet()
        }
    }

    @JvmStatic
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @JvmStatic
    fun delete(key: String): Boolean = redisTemplate.delete(keys(key)) > 0

    @JvmStatic
    fun delete(key: String, hashKey: String) =
        redisTemplate.opsForHash<String, Any>()
            .hasKey(key, hashKey)
            .doIf {
                redisTemplate.opsForHash<String, Any>().delete(key, hashKey)
            }

    @JvmStatic
    @JvmOverloads
    fun expire(key: String,
               timeout: Long,
               timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Boolean? =
        redisTemplate.expire(key, timeout, timeUnit)

    @Resource
    private fun factory(factory: RedisConnectionFactory) {
        this.factory = factory
    }

    fun genKey(template: String, vararg args: Any?) =
        "$keyPrefix:${String.format(template, *args)}"

    @JvmStatic
    fun <T> get(key: String, hashKey: String): T? =
        stringRedisTemplate.boundHashOps<String, T>(key).get(hashKey)

    @JvmStatic
    @JvmOverloads
    fun getExpire(key: String, timeUnit: TimeUnit = TimeUnit.SECONDS): Long? =
        redisTemplate.getExpire(key, timeUnit)

    @JvmStatic
    fun getMap(key: String): Map<String, Any>? =
        redisTemplate.opsForHash<String, Any>().entries(key)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> getNumber(key: String): T? where T : Number =
        redisTemplate.opsForValue().get(key) as T?

    @JvmStatic
    inline fun <reified T> getObject(key: String): T? =
        getString(key)?.jsonToObj()

    @JvmStatic
    inline fun <reified T> getObject(key: String, hashKey: String): T? =
        get<String>(key, hashKey)?.jsonToObj()

    @JvmStatic
    fun getString(key: String): String? =
        stringRedisTemplate.opsForValue().get(key)

    @JvmStatic
    fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)

    @JvmStatic
    fun hasKey(key: String, hashKey: String): Boolean =
        true == stringRedisTemplate.boundHashOps<String, Any>(key).hasKey(hashKey)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Long = 1L): Long? =
        redisTemplate.boundValueOps(key).increment(delta)

    @JvmStatic
    @JvmOverloads
    fun increment(key: String, delta: Double = 1.0): Double? =
        redisTemplate.boundValueOps(key).increment(delta)

    @Suppress("unused")
    @Resource
    private fun prop(redisCacheProperties: RedisCacheProperties) {
        RedisUtils.redisCacheProperties = redisCacheProperties
    }

    @JvmStatic
    @JvmOverloads
    fun <T> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        if (timeout == 0L) {
            redisTemplate.boundHashOps<String, T>(key).put(hashKey, value)
        } else {
            redisTemplate.boundHashOps<String, T>(key).apply {
                put(hashKey, value)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    fun <T> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date
    ) {
        redisTemplate.boundHashOps<String, T>(key).apply {
            put(hashKey, value)
            expireAt(date)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun <T> putAll(key: String,
                   map: Map<String, T>,
                   timeout: Long = 0,
                   timeUnit: TimeUnit = TimeUnit.SECONDS) {
        if (timeout == 0L) {
            redisTemplate.boundHashOps<String, T>(key).putAll(map)
        } else {
            redisTemplate.boundHashOps<String, T>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    @JvmStatic
    fun <T> putAll(
        key: String,
        map: Map<String, T>,
        date: Date
    ) {
        redisTemplate.boundHashOps<String, T>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun set(key: String,
            value: String,
            timeout: Long = 0,
            timeUnit: TimeUnit = TimeUnit.SECONDS) =
        if (timeout == 0L) stringRedisTemplate.opsForValue().set(key, value)
        else stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun set(key: String,
            value: Number,
            timeout: Long = 0,
            timeUnit: TimeUnit = TimeUnit.SECONDS) =
        if (timeout == 0L) redisTemplate.opsForValue().set(key, value)
        else redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @JvmStatic
    @JvmOverloads
    fun <T> set(key: String,
                value: T,
                timeout: Long = 0,
                timeUnit: TimeUnit = TimeUnit.SECONDS) =
        if (timeout == 0L) redisTemplate.opsForValue().set(key, value)
        else redisTemplate.opsForValue().set(key, value, timeout, timeUnit)

    @Resource
    private fun stringRedisTemplate(stringRedisTemplate: StringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate
    }

    fun keys(key: String): Collection<String> {
        return redisTemplate.keys(key)
    }
}
