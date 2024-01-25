package com.tony.test.redis.fury.service

import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.redis.toNum
import com.tony.test.redis.fury.serializer.FurySerializer
import com.tony.utils.asTo
import com.tony.utils.isNumberTypes

/**
 * FuryRedisService is
 * @author tangli
 * @date 2024/01/25 10:04
 * @since 1.0.0
 */
class FuryRedisService : RedisService {
    override val serializerMode: SerializerMode = SerializerMode.FURY
    override fun Any.inputTransformTo(): Any {
        FurySerializer.fury.register(this::class.java)
        return this
    }

    override fun <T : Any> Any?.outputTransformTo(type: Class<T>): T? {
        if (type.isNumberTypes()) {
            return toNum(type)
        }
        if (this != null) {
            FurySerializer.fury.register(this::class.java)
        }
        return this?.asTo()
    }
}
