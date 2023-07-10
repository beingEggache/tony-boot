package com.tony.redis.serializer.wrapper

/**
 * Redis protoStuff 序列化, 反序列化对象包装.
 *
 * 有 [com.tony.redis.RedisValues.increment]和 [com.tony.redis.RedisValues.get] [com.tony.redis.RedisValues.set] 需求的[Long], [Double] 类型不建议使用!
 * 因为 [com.tony.redis.RedisValues.increment]不会走 [org.springframework.data.redis.serializer.RedisSerializer.serialize].
 *
 * 但经过特殊处理, 所有 [Number] 类型都直接原生处理, 没走 [com.tony.redis.serializer.ProtostuffSerializer.serialize].
 * @author tangli
 * @since 2023/5/24 18:12
 */
internal class ProtoWrapper
    @JvmOverloads
    constructor(var data: Any? = null)
