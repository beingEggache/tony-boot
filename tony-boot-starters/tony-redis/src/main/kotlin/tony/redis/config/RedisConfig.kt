/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.redis.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import tony.core.jackson.InjectableValueSupplier
import tony.core.jackson.InjectableValuesBySupplier
import tony.core.misc.YamlPropertySourceFactory
import tony.core.utils.createObjectMapper
import tony.redis.aspect.JacksonRedisCacheAspect
import tony.redis.aspect.RedisCacheAspect
import tony.redis.serializer.SerializerMode
import tony.redis.service.RedisService
import tony.redis.service.impl.JacksonRedisService

/**
 * RedisCacheConfig
 *
 * @author tangli
 * @date 2023/05/25 19:31
 */
@PropertySource("classpath:redis.config.yml", factory = YamlPropertySourceFactory::class)
@EnableConfigurationProperties(RedisProperties::class)
@AutoConfigureBefore(RedisAutoConfiguration::class)
@Configuration(proxyBeanMethods = false)
private class RedisConfig(
    private val redisProperties: RedisProperties,
) {
    private val logger = LoggerFactory.getLogger(RedisConfig::class.java)

    @ConditionalOnMissingBean(RedisCacheAspect::class)
    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "JACKSON", matchIfMissing = true)
    @Bean
    private fun jacksonRedisCacheAspect(): RedisCacheAspect {
        logger.info("Annotation based redis cache with jackson enabled")
        return JacksonRedisCacheAspect()
    }

    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilder::class)
    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "JACKSON", matchIfMissing = true)
    @Bean
    private fun jackson2ObjectMapperBuilder(
        jackson2ObjectMapperBuilderCustomizer: Jackson2ObjectMapperBuilderCustomizer,
    ): Jackson2ObjectMapperBuilder =
        Jackson2ObjectMapperBuilder().apply {
            jackson2ObjectMapperBuilderCustomizer.customize(this)
        }

    @ConditionalOnMissingBean(ObjectMapper::class)
    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "JACKSON", matchIfMissing = true)
    @Bean
    private fun objectMapper(
        jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder,
        injectableValueSuppliers: List<InjectableValueSupplier>,
    ): ObjectMapper =
        createObjectMapper().apply {
            jackson2ObjectMapperBuilder.configure(this)
            injectableValues = InjectableValuesBySupplier(injectableValueSuppliers.associateBy { it.name })
        }

    @ConditionalOnMissingBean(RedisSerializer::class)
    @Bean
    private fun genericJackson2JsonRedisSerializer(objectMapper: ObjectMapper): RedisSerializer<Any?> =
        GenericJackson2JsonRedisSerializer(objectMapper).also {
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
    private fun jacksonRedisService(): RedisService =
        JacksonRedisService()

    @Bean("redisTemplate")
    private fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        redisSerializer: RedisSerializer<Any?>,
    ): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory
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
 * @date 2023/05/25 19:31
 */
@ConfigurationProperties(prefix = "redis")
private data class RedisProperties(
    @DefaultValue("")
    val keyPrefix: String,
    /**
     * redis 序列化/反序列化 方式
     */
    @DefaultValue("JACKSON")
    val serializerMode: SerializerMode,
)
