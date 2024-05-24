package com.tony.redis.aspect

import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.RedisManager

/**
 * Protostuff RedisCache实现.
 *
 * 给常规的 @Cacheable 加了过期时间.
 * @author tangli
 * @date 2023/09/28 19:55
 * @since 1.0.0
 */
internal class ProtostuffRedisCacheAspect : RedisCacheAspect() {
    override fun getCachedValueByType(
        cacheKey: String,
        javaType: JavaType,
    ): Any? =
        RedisManager
            .values
            .get(cacheKey, javaType.rawClass)
}
