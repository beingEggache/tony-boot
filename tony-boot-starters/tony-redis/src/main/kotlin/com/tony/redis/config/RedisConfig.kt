package com.tony.redis.config

import com.tony.redis.aspect.DefaultRedisCacheAspect
import com.tony.redis.serializer.ProtostuffSerializer
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.redis.service.impl.JacksonRedisService
import com.tony.redis.service.impl.ProtostuffRedisService
import com.tony.utils.OBJECT_MAPPER
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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

    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "PROTOSTUFF")
    @Bean
    internal fun protostuffSerializer(): ProtostuffSerializer = ProtostuffSerializer().also {
        logger.info("Redis serializer mode is ${redisProperties.serializerMode}")
    }

    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "PROTOSTUFF")
    @Bean
    internal fun protostuffRedisService(): ProtostuffRedisService = ProtostuffRedisService()

    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "JACKSON")
    @Bean
    internal fun genericJackson2JsonRedisSerializer() = GenericJackson2JsonRedisSerializer(OBJECT_MAPPER).also {
        if (redisProperties.serializerMode == SerializerMode.PROTOSTUFF) {
            logger.warn(
                "Your serializer mode is ${SerializerMode.PROTOSTUFF},but got ${SerializerMode.JACKSON}",
            )
        } else {
            logger.info("Redis serializer mode is ${redisProperties.serializerMode}")
        }
    }

    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "JACKSON")
    @Bean
    internal fun jacksonRedisService(): RedisService = JacksonRedisService()

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
