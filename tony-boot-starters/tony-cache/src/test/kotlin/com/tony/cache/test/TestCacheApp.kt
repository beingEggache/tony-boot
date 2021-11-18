package com.tony.cache.test

import com.tony.cache.annotation.RedisCacheEvict
import com.tony.cache.annotation.RedisCacheable
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Service

/**
 *
 * @author tangli
 * @since 2021-05-12 16:44
 */
@SpringBootApplication(
    scanBasePackages = ["com.tony.cache"]
)
class TestCacheApp

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
    @RedisCacheEvict(cacheKey = "test2")
    fun rTestCache() {
        println("yeah")
    }
}


