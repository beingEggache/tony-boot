@file:JvmName("JsonUtils")
@file:Suppress("unused")

package com.tony.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException
import java.io.InputStream
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.TimeZone

@JvmSynthetic
public val OBJECT_MAPPER: ObjectMapper = createObjectMapper()

public fun createObjectMapper(): ObjectMapper = ObjectMapper().apply {
    registerModules(KotlinModule.Builder().build(), JavaTimeModule())
    setTimeZone(TimeZone.getDefault())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)
    enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
    enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

@Throws(IOException::class)
public inline fun <reified T> String.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
public fun <T> String.jsonToObj(clazz: Class<T>): T =
    OBJECT_MAPPER.readValue(this, clazz)

@Throws(IOException::class)
public fun <T> String.jsonToObj(typeReference: TypeReference<T>): T =
    OBJECT_MAPPER.readValue(this, typeReference)

@Throws(IOException::class)
public fun <T> String.jsonToObj(javaType: JavaType): T =
    OBJECT_MAPPER.readValue(this, javaType)

public fun <T> T?.toJsonString(): String = if (this != null) OBJECT_MAPPER.writeValueAsString(this) else ""

private val valueJsonToken = setOf(
    JsonToken.VALUE_NULL,
    JsonToken.VALUE_TRUE,
    JsonToken.VALUE_FALSE,
    JsonToken.VALUE_STRING,
    JsonToken.VALUE_NUMBER_INT,
    JsonToken.VALUE_NUMBER_FLOAT,
    JsonToken.VALUE_EMBEDDED_OBJECT,
)

private val jsonFactory = JsonFactory()
public fun String.getFromRootAsString(field: String): String? {
    jsonFactory.createParser(this).use {
        while (
            try {
                it.nextToken()
            } catch (e: JsonParseException) {
                return null
            } != null
        ) {
            if (it.currentToken == JsonToken.FIELD_NAME &&
                it.currentName == field &&
                it.parsingContext.parent.inRoot()
            ) {
                val token = it.nextToken()
                return if (token in valueJsonToken) it.text else null
            }
        }
    }
    return null
}

@Throws(IOException::class)
public inline fun <reified T> ByteArray.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
public inline fun <reified T> InputStream.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

public fun JavaType.isDateTimeLikeType(): Boolean =
    isTypeOrSubTypeOf(Date::class.java) || isTypeOrSubTypeOf(TemporalAccessor::class.java)

public fun JavaType.isArrayLikeType(): Boolean =
    isArrayType || isCollectionLikeType

public fun JavaType.isBooleanType(): Boolean =
    isTypeOrSubTypeOf(Boolean::class.java) || isTypeOrSubTypeOf(java.lang.Boolean::class.java)

public fun JavaType.isNumberType(): Boolean =
    isTypeOrSubTypeOf(Number::class.java)

public fun JavaType.isByteType(): Boolean =
    isTypeOrSubTypeOf(Byte::class.java) || isTypeOrSubTypeOf(java.lang.Byte::class.java)

public fun JavaType.isShortType(): Boolean =
    isTypeOrSubTypeOf(Short::class.java) || isTypeOrSubTypeOf(java.lang.Short::class.java)

public fun JavaType.isIntType(): Boolean =
    isTypeOrSubTypeOf(Int::class.java) || isTypeOrSubTypeOf(java.lang.Integer::class.java)

public fun JavaType.isLongType(): Boolean =
    isTypeOrSubTypeOf(Long::class.java) || isTypeOrSubTypeOf(java.lang.Long::class.java)

public fun JavaType.isFloatType(): Boolean =
    isTypeOrSubTypeOf(Float::class.java) || isTypeOrSubTypeOf(java.lang.Float::class.java)

public fun JavaType.isDoubleType(): Boolean =
    isTypeOrSubTypeOf(Double::class.java) || isTypeOrSubTypeOf(java.lang.Double::class.java)

public fun JavaType.isObjLikeType(): Boolean =
    isMapLikeType || (!isArrayLikeType() && !isNumberType() && !isStringLikeType())

public fun JavaType.isStringLikeType(): Boolean =
    isTypeOrSubTypeOf(CharSequence::class.java) ||
        isTypeOrSubTypeOf(Character::class.java) ||
        isTypeOrSubTypeOf(Char::class.java)
