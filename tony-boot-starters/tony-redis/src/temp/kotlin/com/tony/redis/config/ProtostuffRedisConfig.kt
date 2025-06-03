package tony.redis.config

import tony.redis.aspect.ProtostuffRedisCacheAspect
import tony.redis.aspect.RedisCacheAspect
import tony.redis.serializer.ProtostuffSerializer
import tony.redis.service.RedisService
import tony.redis.service.impl.ProtostuffRedisService
import io.protostuff.LinkedBuffer
import io.protostuff.runtime.RuntimeSchema
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.RedisSerializer

@ConditionalOnClass(value = [LinkedBuffer::class, RuntimeSchema::class])
@ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "PROTOSTUFF")
@Configuration
internal class ProtostuffRedisConfig {
    private val logger = LoggerFactory.getLogger(ProtostuffRedisConfig::class.java)

    @Bean
    internal fun protostuffRedisCacheAspect(): RedisCacheAspect {
        logger.info("Annotation based redis cache with protostuff enabled")
        return ProtostuffRedisCacheAspect()
    }

    @Bean
    internal fun protostuffSerializer(): RedisSerializer<Any?> =
        ProtostuffSerializer()
            .also {
                logger.info("Redis serializer mode is Protostuff")
            }

    @Bean
    internal fun protostuffRedisService(): RedisService =
        ProtostuffRedisService()
}
