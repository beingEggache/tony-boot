package com.tony.db.service

import com.tony.redis.RedisManager
import org.springframework.stereotype.Service

/**
 * RedisService is
 * @author tangli
 * @since 2023/07/10 14:28
 */
@Service
class RedisService {

    val delegate = RedisManager
}
