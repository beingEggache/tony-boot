package com.tony.redis.config

import com.tony.redis.aspect.DefaultRedisCacheAspect
import com.tony.redis.serializer.ProtostuffSerializer
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.redis.service.impl.JacksonRedisService
import com.tony.redis.service.impl.ProtostuffRedisService
import com.tony.utils.OBJECT_MAPPER
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * RedisCacheConfig
 *
 * @author tangli
 * @since 2023/5/25 10:31
 */
@Configuration
@EnableConfigurationProperties(RedisProperties::class)
internal class RedisConfig(
    private val redisProperties: RedisProperties,
) {

    private val logger = LoggerFactory.getLogger(RedisConfig::class.java)

    @Bean
    internal fun redisCacheAspect(): DefaultRedisCacheAspect {
        logger.info("Annotation based redis cache enabled.")
        return DefaultRedisCacheAspect()
    }

    @Bean
    internal fun redisService(): RedisService =
        if (redisProperties.serializerMode == SerializerMode.PROTOSTUFF) {
            ProtostuffRedisService()
        } else {
            JacksonRedisService()
        }

    @Bean
    internal fun redisSerializer(): RedisSerializer<Any?> {
        logger.info("Redis serializer mode is ${redisProperties.serializerMode}")
        return if (redisProperties.serializerMode == SerializerMode.PROTOSTUFF) {
            ProtostuffSerializer()
        } else {
            GenericJackson2JsonRedisSerializer(OBJECT_MAPPER)
        }
    }

    @Bean("redisTemplate")
    internal fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        redisSerializer: RedisSerializer<Any?>,
    ): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            setConnectionFactory(redisConnectionFactory)
            setDefaultSerializer(redisSerializer)
            keySerializer = StringRedisSerializer.UTF_8
            hashKeySerializer = StringRedisSerializer.UTF_8
            valueSerializer = redisSerializer
            hashValueSerializer = redisSerializer
            afterPropertiesSet()
        }
}

/**
 * RedisCacheProperties
 *
 * @author tangli
 * @since 2023/5/25 10:31
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "redis")
internal data class RedisProperties(
    val keyPrefix: String?,
    /**
     * redis 序列化/反序列化 方式
     */
    @DefaultValue("JACKSON")
    val serializerMode: SerializerMode = SerializerMode.JACKSON,
)
