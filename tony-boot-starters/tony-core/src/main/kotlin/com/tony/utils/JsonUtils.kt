@file:JvmName("JsonUtils")
@file:Suppress("unused")

package com.tony.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
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
val OBJECT_MAPPER: ObjectMapper = createObjectMapper()

fun createObjectMapper() = ObjectMapper().apply {
    registerModules(KotlinModule.Builder().build(), JavaTimeModule())
    setTimeZone(TimeZone.getDefault())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)
    enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
    enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

@Throws(IOException::class)
inline fun <reified T> String.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
fun <T> String.jsonToObj(javaType: JavaType): T =
    OBJECT_MAPPER.readValue(this, javaType)

fun <T> T?.toJsonString(): String = if (this != null) OBJECT_MAPPER.writeValueAsString(this) else ""

private val valueJsonToken = setOf(
    JsonToken.VALUE_NULL,
    JsonToken.VALUE_TRUE,
    JsonToken.VALUE_FALSE,
    JsonToken.VALUE_STRING,
    JsonToken.VALUE_NUMBER_INT,
    JsonToken.VALUE_NUMBER_FLOAT,
    JsonToken.VALUE_EMBEDDED_OBJECT
)

private val jsonFactory = JsonFactory()
fun String.getFromRootAsString(field: String): String? {
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
