package tony.test.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import tony.core.annotation.EnableTonyBoot
import tony.test.redis.config.RedisTestConfig

/**
 * Redis 测试专用 Spring Boot 主配置类
 *
 * @author tony
 * @date 2025/07/01 17:00
 */
@SpringBootApplication
@EnableTonyBoot
@Import(RedisTestConfig::class)
class TestRedisApplication
