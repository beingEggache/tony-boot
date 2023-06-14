@file:JvmName("RedisUtils")

package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.serializer.SerializerMode
import com.tony.utils.asTo
import com.tony.utils.isNumberTypes
import com.tony.utils.rawClass
import com.tony.utils.toNumber

/**
 * RedisService is
 * @author tangli
 * @since 2023/06/09 13:30
 */
public interface RedisService :
    RedisValueOp,
    RedisMapOp,
    RedisListOp {

    /**
     * 序列化反序列化方式
     */
    public val serializerMode: SerializerMode
}

public sealed interface RedisValueTransformer {

    public fun <T : Any> Any?.outputTransformTo(type: Class<T>): T? {
        if (type.isNumberTypes()) {
            return this?.toNumber(type)
        }
        return this?.asTo()
    }

    public fun <T : Any> Any?.outputTransformTo(type: JavaType): T? = outputTransformTo(type.rawClass())

    public fun <T : Any> Any?.outputTransformTo(type: TypeReference<T>): T? = outputTransformTo(type.rawClass())

    public fun Any.inputTransformTo(): Any = this
}
