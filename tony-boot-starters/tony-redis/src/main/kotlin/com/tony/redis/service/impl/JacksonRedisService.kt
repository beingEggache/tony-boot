package com.tony.redis.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.enums.EnumIntValue
import com.tony.enums.EnumStringValue
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.StringEnumCreator
import com.tony.exception.ApiException
import com.tony.redis.RedisManager
import com.tony.redis.RedisManager.trimQuotes
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.utils.asTo
import com.tony.utils.asToNumber
import com.tony.utils.isNumberTypes
import com.tony.utils.isStringLikeType
import com.tony.utils.isTypeOrSubTypeOf
import com.tony.utils.isTypeOrSubTypesOf
import com.tony.utils.jsonToObj
import com.tony.utils.rawClass
import com.tony.utils.toJsonString
import java.util.concurrent.TimeUnit

internal class JacksonRedisService : RedisService {

    override val serializerMode: SerializerMode = SerializerMode.JACKSON
    override fun <T : Any> set(
        key: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit,
    ) {
        val beSetValue: Any = if (value::class.java.isNumberTypes()) {
            value
        } else {
            value.toJsonString()
        }
        if (timeout == 0L) {
            RedisManager.redisTemplate.opsForValue().set(key, beSetValue)
        } else {
            RedisManager.redisTemplate.opsForValue().set(key, beSetValue, timeout, timeUnit)
        }
    }

    override fun <T : Any> get(key: String, type: Class<T>): T? {
        val value = RedisManager.redisTemplate.opsForValue().get(key) ?: return null
        val (result, transformedValue) = transform(value, type)
        return if (result) {
            transformedValue
        } else {
            transformedValue?.toString()?.trimQuotes()?.jsonToObj(type)
        }
    }

    override fun <T : Any> get(key: String, javaType: JavaType): T? {
        val value = RedisManager.redisTemplate.opsForValue().get(key) ?: return null
        val (result, transformedValue) = transform(value, javaType.rawClass)
        return if (result) {
            transformedValue
        } else {
            transformedValue?.toString()?.trimQuotes()?.jsonToObj(javaType)
        }.asTo()
    }

    override fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? {
        val value = RedisManager.redisTemplate.opsForValue().get(key) ?: return null
        val type = typeReference.rawClass()
        val (result, transformedValue) = transform(value, type)
        return if (result) {
            transformedValue
        } else {
            transformedValue?.toString()?.trimQuotes()?.jsonToObj(typeReference)
        }.asTo()
    }

    private fun <T : Any> transform(value: Any, type: Class<T>): Pair<Boolean, T?> {
        return when {
            type.isNumberTypes() -> true to value.asToNumber(type)
            type.isStringLikeType() -> true to value.toString().trimQuotes()
            type == EnumValue::class.java -> {
                if (value is EnumValue<*>) true to value
                throw ApiException("Please use more specific type.Like EnumStringValue or EnumIntValue")
            }

            type.isTypeOrSubTypesOf(EnumStringValue::class.java) -> {
                true to StringEnumCreator.getCreator(type).create(value.toString().trimQuotes())
            }

            type.isTypeOrSubTypeOf(EnumIntValue::class.java) -> {
                true to IntEnumCreator.getCreator(type).create(value.toString().toInt())
            }

            value::class.java.isTypeOrSubTypesOf(type) -> true to value
            else -> false to value
        }.asTo()!!
    }
}
