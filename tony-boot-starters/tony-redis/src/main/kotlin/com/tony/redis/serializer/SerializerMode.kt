@file:JvmName("Serializers")

package com.tony.redis.serializer

/**
 * redis 序列化/反序列化 方式
 *
 * @author Tang Li
 * @date 2023/6/5 18:10
 */
public enum class SerializerMode {
    JACKSON,
    PROTOSTUFF,
}
