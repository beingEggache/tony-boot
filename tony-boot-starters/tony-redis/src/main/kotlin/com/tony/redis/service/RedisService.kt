@file:JvmName("RedisUtils")

package com.tony.redis.service

import com.tony.redis.serializer.SerializerMode
import com.tony.utils.asTo
import com.tony.utils.asToNumber
import com.tony.utils.isNumberTypes

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

    /**
     * 序列化反序列化方式
     */
    public val serializerMode: SerializerMode
}

internal fun <T : Any> Any?.transformTo(type: Class<T>): T? {
    if (type.isNumberTypes()) {
        return this?.asToNumber(type)
    }
    return this?.asTo()
}
