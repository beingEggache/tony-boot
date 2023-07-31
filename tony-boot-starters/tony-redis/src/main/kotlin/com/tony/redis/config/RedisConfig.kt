package com.tony.redis.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.tony.misc.YamlPropertySourceFactory
import com.tony.redis.aspect.DefaultRedisCacheAspect
import com.tony.redis.serializer.ProtostuffSerializer
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.redis.service.impl.JacksonRedisService
import com.tony.redis.service.impl.ProtostuffRedisService
import com.tony.utils.getLogger
import io.protostuff.LinkedBuffer
import io.protostuff.runtime.RuntimeSchema
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
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
@PropertySource("classpath:redis.config.yml", factory = YamlPropertySourceFactory::class)
internal class RedisConfig(
    private val redisProperties: RedisProperties,
) {

    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "JACKSON", matchIfMissing = true)
    @Bean
    internal fun redisCacheAspect(): DefaultRedisCacheAspect {
        getLogger(DefaultRedisCacheAspect::class.java.name).info("Annotation based redis cache enabled")
        return DefaultRedisCacheAspect()
    }

    @ConditionalOnClass(value = [LinkedBuffer::class, RuntimeSchema::class])
    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "PROTOSTUFF")
    @Bean
    internal fun protostuffSerializer(): RedisSerializer<Any?> =
        ProtostuffSerializer()
            .also {
                getLogger(ProtostuffSerializer::class.java.name)
                    .info("Redis serializer mode is ${redisProperties.serializerMode}")
            }

    @ConditionalOnClass(value = [LinkedBuffer::class, RuntimeSchema::class])
    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "PROTOSTUFF")
    @Bean
    internal fun protostuffRedisService(): RedisService = ProtostuffRedisService()

    @ConditionalOnMissingBean(RedisSerializer::class)
    @Bean
    internal fun genericJackson2JsonRedisSerializer(
        objectMapper: ObjectMapper,
    ): RedisSerializer<Any?> =
        GenericJackson2JsonRedisSerializer(objectMapper).also {
            val logger = getLogger(GenericJackson2JsonRedisSerializer::class.java.name)
            if (redisProperties.serializerMode == SerializerMode.PROTOSTUFF) {
                logger.warn(
                    "Your serializer mode is ${SerializerMode.PROTOSTUFF}, but got ${SerializerMode.JACKSON}," +
                        " please check protostuff dependencies(protostuff-core, protostuff-runtime) are ready."
                )
            } else {
                logger.info("Redis serializer mode is ${redisProperties.serializerMode}")
            }
        }

    @ConditionalOnMissingBean(RedisService::class)
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
@ConfigurationProperties(prefix = "redis")
internal data class RedisProperties
    @ConstructorBinding
    constructor(
        @DefaultValue("")
        val keyPrefix: String,
        /**
         * redis 序列化/反序列化 方式
         */
        @DefaultValue("JACKSON")
        val serializerMode: SerializerMode,
    )
