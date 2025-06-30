package tony.test.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import tony.annotation.EnableTonyBoot
import tony.test.redis.config.RedisTestConfig

/**
 * Redis 测试专用 Spring Boot 主配置类
 *
 * @author tony
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableTonyBoot
@Import(RedisTestConfig::class)
class TestRedisApplication
