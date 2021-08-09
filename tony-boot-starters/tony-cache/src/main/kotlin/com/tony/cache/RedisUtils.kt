@file:Suppress("unused")

package com.tony.cache

import com.tony.core.exception.ApiException
import com.tony.core.utils.OBJECT_MAPPER
import org.springframework.context.annotation.Lazy
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import java.util.Collections
import java.util.Random
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Suppress("MemberVisibilityCanBePrivate")
@Component
object RedisUtils {

    val values = RedisValues

    val maps = RedisMaps

    val lists = RedisLists

    val keys = RedisKeys

    @JvmStatic
    private lateinit var factory: RedisConnectionFactory

    @Lazy
    @Resource
    private fun factory(factory: RedisConnectionFactory) {
        this.factory = factory
    }

    @JvmStatic
    lateinit var stringRedisTemplate: StringRedisTemplate

    val redisTemplate: RedisTemplate<String, Any> by lazy {
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

    @Lazy
    @Resource
    private fun stringRedisTemplate(stringRedisTemplate: StringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate
    }

    private val script: String =
        """ |if redis.call('setNx',KEYS[1],ARGV[1])==1 then
            |   if redis.call('get',KEYS[1])==ARGV[1] then
            |      return redis.call('expire',KEYS[1],ARGV[2])
            |   else
            |      return 0
            |   end
            |else
            |   return 0
            |end""".trimMargin()

    fun lockKey(key: String, timeout: Long): Boolean {
        if (timeout <= 0) throw ApiException("timeout must greater than 0")
        val redisScript = DefaultRedisScript(script, Long::class.java)
        return redisTemplate.execute(redisScript, Collections.singletonList(key), 1L, timeout) == 1L
    }

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
    fun getExpire(key: String, timeUnit: TimeUnit = TimeUnit.SECONDS): Long =
        redisTemplate.getExpire(key, timeUnit)

    @JvmStatic
    fun delete(key: String) = redisTemplate.delete(keys(key)) > 0

    @Suppress("MemberVisibilityCanBePrivate")
    fun keys(key: String): Collection<String> = redisTemplate.keys(key)
}
