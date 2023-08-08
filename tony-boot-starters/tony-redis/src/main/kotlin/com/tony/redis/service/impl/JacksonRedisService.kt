package com.tony.redis.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
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
import com.tony.utils.trimQuotes

internal class JacksonRedisService : RedisService {

    override val serializerMode: SerializerMode = SerializerMode.JACKSON

    override fun Any.inputTransformTo(): Any =
        if (this::class.java.isNumberTypes()) {
            this
        } else {
            this.toJsonString().trimQuotes()
        }

    override fun <T : Any> Any?.outputTransformTo(type: Class<T>): T? =
        jsonToObjWithTypeClass(type) {
            it.trimQuotes().jsonToObj(type)
        }

    override fun <T : Any> Any?.outputTransformTo(type: JavaType): T? =
        jsonToObjWithTypeClass(type.rawClass()) {
            it.trimQuotes().jsonToObj(type)
        }

    override fun <T : Any> Any?.outputTransformTo(type: TypeReference<T>): T? =
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
            else -> func(toString())
        }.asTo()
    }
}
