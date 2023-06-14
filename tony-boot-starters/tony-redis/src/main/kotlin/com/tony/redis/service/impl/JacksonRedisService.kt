package com.tony.redis.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import com.tony.redis.RedisManager
import com.tony.redis.RedisManager.trimQuotes
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.utils.asTo
import com.tony.utils.isNumberTypes
import com.tony.utils.isStringLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.jsonToObj
import com.tony.utils.rawClass
import com.tony.utils.toJsonString
import com.tony.utils.toNumber
import java.util.concurrent.TimeUnit

internal class JacksonRedisService : RedisService {

    override val serializerMode: SerializerMode = SerializerMode.JACKSON
    override fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit,
    ) {
        if (timeout == 0L) {
            RedisManager.redisTemplate.opsForValue().set(key, ifIsNotNumberThenToJson(value))
        } else {
            RedisManager.redisTemplate.opsForValue().set(key, ifIsNotNumberThenToJson(value), timeout, timeUnit)
        }
    }

    override fun <T : Any> setIfAbsent(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit,
    ): Boolean? =
        if (timeout == 0L) {
            RedisManager.redisTemplate.opsForValue().setIfAbsent(key, ifIsNotNumberThenToJson(value))
        } else {
            RedisManager.redisTemplate.opsForValue().setIfAbsent(key, ifIsNotNumberThenToJson(value), timeout, timeUnit)
        }

    override fun <T : Any> setIfPresent(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit,
    ): Boolean? =
        if (timeout == 0L) {
            RedisManager.redisTemplate.opsForValue().setIfPresent(key, ifIsNotNumberThenToJson(value))
        } else {
            RedisManager.redisTemplate.opsForValue()
                .setIfPresent(key, ifIsNotNumberThenToJson(value), timeout, timeUnit)
        }

    override fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
    ) {
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).put(hashKey, ifIsNotNumberThenToJson(value))
    }

    override fun <T : Any> putIfAbsent(key: String, hashKey: String, value: T) {
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).putIfAbsent(hashKey, ifIsNotNumberThenToJson(value))
    }

    private fun <T : Any> ifIsNotNumberThenToJson(value: T): Any =
        if (value::class.java.isNumberTypes()) {
            value
        } else {
            value.toJsonString()
        }

    override fun <T : Any> Any?.transformTo(type: Class<T>): T? =
        jsonToObjWithTypeClass(type) {
            it.trimQuotes().jsonToObj(type)
        }

    override fun <T : Any> Any?.transformTo(type: JavaType): T? =
        jsonToObjWithTypeClass(type.rawClass()) {
            it.trimQuotes().jsonToObj(type)
        }

    override fun <T : Any> Any?.transformTo(type: TypeReference<T>): T? =
        jsonToObjWithTypeClass(type.rawClass()) {
            it.trimQuotes().jsonToObj(type)
        }

    private fun <T : Any> Any?.jsonToObjWithTypeClass(type: Class<T>, func: (String) -> T): T? {
        if (this == null) {
            return null
        }
        return when {
            type.isNumberTypes() -> toNumber(type)
            type.isStringLikeType() -> toString().trimQuotes()
            type == EnumValue::class.java && this is EnumValue<*> -> this
            type.isTypesOrSubTypesOf(StringEnumValue::class.java) -> {
                StringEnumCreator.getCreator(type).create(toString().trimQuotes())
            }

            type.isTypesOrSubTypesOf(IntEnumValue::class.java) -> {
                IntEnumCreator.getCreator(type).create(toString().toInt())
            }

            this.isTypesOrSubTypesOf(type) -> this
            else -> func(toString().trimQuotes())
        }.asTo()
    }
}
