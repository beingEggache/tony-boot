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

package tony.test.redis.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestPropertySource
import redis.embedded.RedisServer

/**
 * Redis 测试配置类
 *
 * 使用 embedded-redis 提供测试环境，确保在 Spring 容器初始化前启动 Redis 服务
 *
 * @author tony
 * @since 2024-01-01
 */
@TestConfiguration
@TestPropertySource(locations = ["classpath:application.yml"])
class RedisTestConfig {

    private val logger = LoggerFactory.getLogger(RedisTestConfig::class.java)

    @Value("\${spring.data.redis.port:6380}")
    private lateinit var redisPort: String


    private val redisServer: RedisServer by lazy {
        RedisServer(redisPort.toInt())
    }

    /**
     * 在 Spring 容器初始化前启动 embedded-redis
     */
    @PostConstruct
    fun startEmbeddedRedis() {
        val port = redisPort.toInt()
        logger.info("Starting embedded Redis server on port: {}", port)
        try {
            redisServer.start()
            logger.info("Embedded Redis server started successfully on port: {}", port)
        } catch (e: Exception) {
            logger.error("Failed to start embedded Redis server: {}", e.message, e)
            throw e
        }
    }

    /**
     * 在 Spring 容器销毁前停止 embedded-redis
     */
    @PreDestroy
    fun stopEmbeddedRedis() {
        try {
            redisServer.stop()
            logger.info("Embedded Redis server stopped")
        } catch (e: Exception) {
            logger.error("Failed to stop embedded Redis server: {}", e.message, e)
        }
    }
}
