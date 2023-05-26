package com.tony.cache

import com.tony.cache.RedisManager.trimQuotes
import com.tony.enums.EnumCreator
import com.tony.enums.EnumValue
import com.tony.utils.doIf
import com.tony.utils.getLogger
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import org.springframework.data.redis.core.RedisTemplate
import java.io.Serializable
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * redis hash操作单例.
 *
 * @author tangli
 * @since 2023/5/25 9:57
 */
public object RedisMaps {

    private val logger = getLogger()

    /**
     * 同 redisTemplate.boundHashOps(key).hasKey(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun hasKey(key: String, hashKey: String): Boolean =
        true == RedisManager.redisTemplate.boundHashOps<String, Any>(key).hasKey(hashKey)

    /**
     * 先进行 判断  redisTemplate.opsForHash().hasKey(key, hashKey).
     * 同 redisTemplate.opsForHash().delete(key, hashKey).
     *
     * @param key
     * @param hashKey
     */
    @JvmStatic
    public fun delete(key: String, vararg hashKey: String): Unit =
        RedisManager.redisTemplate.opsForHash<String, Any>()
            .hasKey(key, hashKey)
            .doIf {
                RedisManager.redisTemplate.opsForHash<String, Any>().delete(key, hashKey)
            }

    /**
     * 同 redisTemplate.boundHashOps(key).put(hashKey, value).
     *
     * 同时可设置过期时间.
     *
     * @param T
     * @param key
     * @param hashKey
     * @param value
     * @param timeout
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     */
    @JvmStatic
    @JvmOverloads
    public fun <T : Any> putObj(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    /**
     * 同 redisTemplate.boundHashOps(key).put(hashKey, value).
     *
     * 同时可设置过期时间.
     *
     * @param T
     * @param key
     * @param hashKey
     * @param value
     * @param date 过期时间
     */
    @JvmStatic
    public fun <T : Any> putObj(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, date)

    /**
     * 同 redisTemplate.boundHashOps(key).put(hashKey, value).
     *
     * 同时可设置过期时间.
     *
     * @param T
     * @param key
     * @param hashKey
     * @param value
     * @param timeout
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     */
    @JvmStatic
    @JvmOverloads
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, timeout, timeUnit)

    /**
     * 同 redisTemplate.boundHashOps(key).put(hashKey, value).
     *
     * 同时可设置过期时间.
     *
     * @param T
     * @param key
     * @param hashKey
     * @param value
     * @param date 过期时间
     */
    @JvmStatic
    public fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ): Unit = RedisManager.redisTemplate.put(key, hashKey, value, date)

    /**
     * 同 redisTemplate.boundHashOps(key).putAll(map).
     *
     * 同时可设置过期时间.
     *
     * @param key
     * @param map
     * @param timeout
     * @param timeUnit 默认为秒 [TimeUnit.SECONDS]
     */
    @JvmStatic
    @JvmOverloads
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ) {
        if (map == null) {
            logger.warn("Arg map is null.")
            return
        }
        if (timeout == 0L) {
            RedisManager.redisTemplate.boundHashOps<String, Any?>(key).putAll(map)
        } else {
            RedisManager.redisTemplate.boundHashOps<String, Any?>(key).apply {
                putAll(map)
                expire(timeout, timeUnit)
            }
        }
    }

    /**
     * 同 redisTemplate.boundHashOps(key).putAll(map).
     *
     * 同时可设置过期时间.
     *
     * @param key
     * @param map
     * @param date 过期时间
     */
    @JvmStatic
    public fun putAll(
        key: String,
        map: Map<String, Any?>?,
        date: Date,
    ) {
        if (map == null) {
            logger.warn("Arg map is null.")
            return
        }
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).apply {
            putAll(map)
            expireAt(date)
        }
    }

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun getString(key: String, hashKey: String): String? {
        val string = RedisManager.redisTemplate.boundHashOps<String, String>(key).get(hashKey)
        if (string.isNullOrBlank()) return string
        return string.trimQuotes()
    }

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun getNumber(key: String, hashKey: String): Number? =
        RedisManager.redisTemplate.boundHashOps<String, Number>(key).get(hashKey)

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun getInt(key: String, hashKey: String): Int? = getNumber(key, hashKey)?.toInt()

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun getLong(key: String, hashKey: String): Long? = getNumber(key, hashKey)?.toLong()

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun getDouble(key: String, hashKey: String): Double? = getNumber(key, hashKey)?.toDouble()

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param T
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public inline fun <reified T> getObj(key: String, hashKey: String): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey)?.toJsonString()?.jsonToObj()

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param T
     * @param key
     * @param hashKey
     * @return
     */
    @JvmStatic
    public fun <T : Any> get(key: String, hashKey: String): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey)

    /**
     * 同 redisTemplate.boundHashOp(key).get(hashKey)
     *
     * @param E  枚举类
     * @param KEY 枚举值类型
     * @param key
     * @param hashKey
     * @return
     */
    public inline fun <reified E, KEY> getEnum(key: String, hashKey: String): E?
        where E : EnumValue<KEY>, E : Enum<E>, KEY : Serializable {
        val value = RedisManager.redisTemplate.boundHashOps<String, KEY>(key).get(hashKey)
            ?: return null
        return EnumCreator.getCreator(E::class.java).create(value)
    }

    /**
     * 同 redisTemplate.opsForHash().entries(key).
     *
     * @param key
     * @return
     */
    @JvmStatic
    public fun getMap(key: String): Map<String, Any> =
        RedisManager.redisTemplate.opsForHash<String, Any>().entries(key)

    private fun <T : Any> RedisTemplate<String, T>.put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long = 0,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
    ) {
        if (timeout == 0L) {
            boundHashOps<String, T>(key).put(hashKey, value)
        } else {
            boundHashOps<String, T>(key).apply {
                put(hashKey, value)
                expire(timeout, timeUnit)
            }
        }
    }

    private fun <T : Any> RedisTemplate<String, T>.put(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ) {
        boundHashOps<String, T>(key).apply {
            put(hashKey, value)
            expireAt(date)
        }
    }
}
