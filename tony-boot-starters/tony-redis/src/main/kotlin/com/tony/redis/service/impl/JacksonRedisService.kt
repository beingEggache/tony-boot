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
import com.tony.utils.asToNotNull
import com.tony.utils.asToNumber
import com.tony.utils.isNumberTypes
import com.tony.utils.isStringLikeType
import com.tony.utils.isTypeOrSubTypeOf
import com.tony.utils.isTypeOrSubTypesOf
import com.tony.utils.jsonToObj
import com.tony.utils.rawClass
import com.tony.utils.toJsonString
import java.util.Date
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

    override fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        date: Date,
    ) {
        RedisManager.redisTemplate.boundHashOps<String, Any>(key).apply {
            put(hashKey, ifIsNotNumberThenToJson(value))
            expireAt(date)
        }
    }

    override fun <T : Any> put(
        key: String,
        hashKey: String,
        value: T,
        timeout: Long,
        timeUnit: TimeUnit,
    ) {
        if (timeout == 0L) {
            RedisManager.redisTemplate.boundHashOps<String, Any>(key).put(hashKey, ifIsNotNumberThenToJson(value))
        } else {
            RedisManager.redisTemplate.boundHashOps<String, Any>(key).apply {
                put(hashKey, ifIsNotNumberThenToJson(value))
                expire(timeout, timeUnit)
            }
        }
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
            type.isNumberTypes() -> this.asToNumber(type)
            type.isStringLikeType() -> this.toString().trimQuotes()
            type == EnumValue::class.java && this is EnumValue<*> -> this
            type.isTypeOrSubTypesOf(EnumStringValue::class.java) -> {
                StringEnumCreator.getCreator(type).create(this.toString().trimQuotes())
            }

            type.isTypeOrSubTypeOf(EnumIntValue::class.java) -> {
                IntEnumCreator.getCreator(type).create(this.toString().toInt())
            }

            this::class.java.isTypeOrSubTypesOf(type) -> this
            else -> when (typeClass) {
                is Class<*> -> this.toString().trimQuotes().jsonToObj(typeClass)
                is JavaType -> this.toString().trimQuotes().jsonToObj(typeClass)
                is TypeReference<*> -> this.toString().trimQuotes().jsonToObj(typeClass)
                else -> throw ApiException("Ain't gonna happen")
            }
        }.asTo()
    }
}
