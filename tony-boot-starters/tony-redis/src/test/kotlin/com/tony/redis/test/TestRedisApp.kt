package com.tony.redis.test

import com.tony.annotation.EnableTonyBoot
import com.tony.annotation.redis.RedisCacheEvict
import com.tony.annotation.redis.RedisCacheable
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Service


/**
 *
 * @author Tang Li
 * @date 2021-05-12 16:44
 */
@EnableTonyBoot
@SpringBootApplication
class TestRedisApp {

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

class RedisKeyExpirationListener(listenerContainer: RedisMessageListenerContainer) :
    KeyExpirationEventMessageListener(listenerContainer) {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        println("${String(pattern ?: ByteArray(0))}:$message")
    }
}

@Service
class TestCacheService {

    @RedisCacheable(cacheKey = "test1")
    fun testCache1(): String {
        return "test1"
    }

    @RedisCacheable(cacheKey = "test2")
    fun testCache2(): String {
        return "test2"
    }

    @RedisCacheEvict(cacheKey = "test1")
    fun rTestCache() {
        println("yeah")
    }
}


