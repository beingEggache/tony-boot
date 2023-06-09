package com.tony.redis.service.impl

import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService

internal class ProtostuffRedisService : RedisService {
    override val serializerMode: SerializerMode = SerializerMode.PROTOSTUFF
}
