@file:JvmName("JsonUtils")
@file:Suppress("unused")

package com.tony.core.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
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

@JvmField
val OBJECT_MAPPER: ObjectMapper = createObjectMapper()

fun createObjectMapper() = ObjectMapper().apply {
    registerModules(KotlinModule(), JavaTimeModule())
    setTimeZone(TimeZone.getDefault())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)
    enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
    enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

fun <T> T?.toJsonString(): String = if (this != null) OBJECT_MAPPER.writeValueAsString(this) else ""

@Throws(IOException::class)
inline fun <reified T> String.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
fun <T> String.jsonToObj(javaType: JavaType): T =
    OBJECT_MAPPER.readValue(this, javaType)

@Throws(IOException::class)
inline fun <reified T> ByteArray.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
inline fun <reified T> InputStream.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

fun JavaType.isDateTimeLikeType() =
    isTypeOrSubTypeOf(Date::class.java) || isTypeOrSubTypeOf(TemporalAccessor::class.java)

fun JavaType.isArrayLikeType() =
    isArrayType || isCollectionLikeType

fun JavaType.isBooleanType() =
    isTypeOrSubTypeOf(Boolean::class.java) || isTypeOrSubTypeOf(java.lang.Boolean::class.java)

fun JavaType.isNumberType() =
    isTypeOrSubTypeOf(Number::class.java)

fun JavaType.isObjLikeType() =
    isMapLikeType || (!isArrayLikeType() && !isNumberType() && !isStringLikeType())

fun JavaType.isStringLikeType() =
    isTypeOrSubTypeOf(CharSequence::class.java) ||
        isTypeOrSubTypeOf(Character::class.java) ||
        isTypeOrSubTypeOf(Char::class.java)
