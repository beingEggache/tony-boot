package tony.test.redis.fury.service

import tony.redis.serializer.SerializerMode
import tony.redis.service.RedisService
import tony.redis.toNum
import tony.test.redis.fury.serializer.FurySerializer
import tony.utils.asTo
import tony.utils.isNumberTypes

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
