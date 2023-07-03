package com.tony.db.service

import com.tony.redis.RedisManager
import org.springframework.stereotype.Service

/**
 * TestService is
 * @author tangli
 * @since 2023/07/03 16:33
 */
@Service
class TestService {

    fun testRedis() {
        RedisManager.values.set("test", "test")
    }
}
