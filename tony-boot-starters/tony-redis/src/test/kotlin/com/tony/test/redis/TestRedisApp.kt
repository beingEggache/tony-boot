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

package com.tony.test.redis

import com.tony.annotation.EnableTonyBoot
import com.tony.redis.aspect.ProtostuffRedisCacheAspect
import com.tony.redis.aspect.RedisCacheAspect
import com.tony.redis.service.RedisService
import com.tony.test.redis.fury.serializer.FurySerializer
import com.tony.test.redis.fury.service.FuryRedisService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.RedisSerializer


/**
 *
 * @author Tang Li
 * @date 2021-05-12 16:44
 */
@EnableTonyBoot
@SpringBootApplication
class TestRedisApp {


    @ConditionalOnProperty(prefix = "redis", name = ["serializer-mode"], havingValue = "FURY")
    @Configuration
    internal class FuryRedisConfig {
        private val logger = LoggerFactory.getLogger(FuryRedisConfig::class.java)

        @Bean
        internal fun redisCacheAspect(): RedisCacheAspect {
            logger.info("Annotation based redis cache with fury enabled")
            return ProtostuffRedisCacheAspect()
        }

        @Bean
        internal fun furySerializer(): RedisSerializer<Any?> =
            FurySerializer()
                .also {
                    logger.info("Redis serializer mode is Protostuff")
                }

        @Bean
        internal fun furyRedisService(): RedisService =
            FuryRedisService()
    }



//    @Bean
//    fun container(connectionFactory: RedisConnectionFactory): RedisMessageListenerContainer? {
//        val container = RedisMessageListenerContainer()
//        container.setConnectionFactory(connectionFactory)
//        return container
//    }
//
//    @Bean
//    fun redisKeyExpirationListener(listenerContainer: RedisMessageListenerContainer): RedisKeyExpirationListener {
//        return RedisKeyExpirationListener(listenerContainer)
//    }
}
