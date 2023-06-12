package com.tony.redis.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import com.tony.exception.ApiException
import com.tony.redis.RedisManager
import com.tony.redis.RedisManager.trimQuotes
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.utils.asTo
import com.tony.utils.asToNotNull
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

    override fun <T : Any> get(key: String, type: Class<T>): T? =
        RedisManager.redisTemplate.opsForValue().get(key).transformTo(type)

    override fun <T : Any> get(key: String, javaType: JavaType): T? =
        RedisManager.redisTemplate.opsForValue().get(key).transformTo(javaType)

    override fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? =
        RedisManager.redisTemplate.opsForValue().get(key).transformTo(typeReference)

    override fun <T : Any> get(key: String, hashKey: String, type: Class<T>): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey).transformTo(type)

    override fun <T : Any> get(key: String, hashKey: String, javaType: JavaType): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey).transformTo(javaType)

    override fun <T : Any> get(key: String, hashKey: String, typeReference: TypeReference<T>): T? =
        RedisManager.redisTemplate.boundHashOps<String, T>(key).get(hashKey).transformTo(typeReference)

    private fun <T : Any> ifIsNotNumberThenToJson(value: T): Any =
        if (value::class.java.isNumberTypes()) {
            value
        } else {
            value.toJsonString()
        }

    private fun <T : Any> Any?.transformTo(typeClass: Any): T? {
        if (this == null) {
            return null
        }
        val type = when (typeClass) {
            is Class<*> -> typeClass
            is TypeReference<*> -> typeClass.asToNotNull<TypeReference<T>>().rawClass()
            is JavaType -> typeClass.asToNotNull<JavaType>().rawClass()
            else -> throw IllegalStateException("Ain't gonna happen")
        }

        return when {
            type.isNumberTypes() -> asToNumber(type)
            type.isStringLikeType() -> toString().trimQuotes()
            type == EnumValue::class.java && this is EnumValue<*> -> this
            type.isTypeOrSubTypesOf(StringEnumValue::class.java) -> {
                StringEnumCreator.getCreator(type).create(toString().trimQuotes())
            }

            type.isTypeOrSubTypeOf(IntEnumValue::class.java) -> {
                IntEnumCreator.getCreator(type).create(toString().toInt())
            }

            this::class.java.isTypeOrSubTypesOf(type) -> this
            else -> when (typeClass) {
                is Class<*> -> toString().trimQuotes().jsonToObj(typeClass)
                is JavaType -> toString().trimQuotes().jsonToObj(typeClass)
                is TypeReference<*> -> toString().trimQuotes().jsonToObj(typeClass)
                else -> throw ApiException("Ain't gonna happen")
            }
        }.asTo()
    }
}
