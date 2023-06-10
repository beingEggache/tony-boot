package com.tony.redis.service

import com.tony.redis.serializer.SerializerMode

/**
 * RedisService is
 * @author tangli
 * @since 2023/06/09 13:30
 */
public interface RedisService :
    RedisValueGetOp,
    RedisValueSetOp,
    RedisMapGetOp,
    RedisMapSetOp {

    public val serializerMode: SerializerMode
}
