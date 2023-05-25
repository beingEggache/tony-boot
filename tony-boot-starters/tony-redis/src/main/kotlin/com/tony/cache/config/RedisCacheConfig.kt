package com.tony.cache.config

import com.tony.cache.aspect.DefaultRedisCacheAspect
import com.tony.utils.OBJECT_MAPPER
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer

/**
 * RedisCacheConfig
 *
 * @author tangli
 * @since 2023/5/25 10:31
 */
@Configuration
@EnableConfigurationProperties(RedisCacheProperties::class)
internal class RedisCacheConfig {

    private val logger = LoggerFactory.getLogger(RedisCacheConfig::class.java)

    @Bean
    internal fun redisCacheAspect(): DefaultRedisCacheAspect {
        logger.info("Annotation based redis cache enabled.")
        return DefaultRedisCacheAspect()
    }

    @Bean("redisTemplate")
    internal fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> =
        run {
            val serializer = GenericJackson2JsonRedisSerializer(OBJECT_MAPPER)
            val stringRedisSerializer = RedisSerializer.string()
            RedisTemplate<String, Any>().apply {
                setConnectionFactory(redisConnectionFactory)
                keySerializer = stringRedisSerializer
                hashKeySerializer = stringRedisSerializer
                valueSerializer = serializer
                hashValueSerializer = serializer
                afterPropertiesSet()
            }
        }
}

/**
 * RedisCacheProperties
 *
 * @author tangli
 * @since 2023/5/25 10:31
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "cache")
private data class RedisCacheProperties(
    val keyPrefix: String?,
)
