@file:JvmName("JsonUtils")

package com.tony.utils

/**
 * Json工具类
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException
import java.io.InputStream
import java.util.TimeZone

@JvmSynthetic
public val OBJECT_MAPPER: ObjectMapper = createObjectMapper()

/**
 * 创建全局统一行为 jackson object mapper.
 *
 */
public fun createObjectMapper(): ObjectMapper =
    ObjectMapper()
        .apply {
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

/**
 * Method to deserialize JSON content from given JSON content String.
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(clazz: Class<T>): T =
    OBJECT_MAPPER.readValue(this, clazz)

/**
 * Method to deserialize JSON content from given JSON content String.
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(typeReference: TypeReference<T>): T =
    OBJECT_MAPPER.readValue(this, typeReference)

/**
 * Method to deserialize JSON content from given JSON content String.
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(javaType: JavaType): T =
    OBJECT_MAPPER.readValue(this, javaType)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun String.jsonNode(): JsonNode = OBJECT_MAPPER.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun ByteArray.jsonNode(): JsonNode = OBJECT_MAPPER.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun InputStream.jsonNode(): JsonNode = OBJECT_MAPPER.readTree(this)

/**
 * Method that can be used to serialize any Java value as a String.
 */
public fun <T> T?.toJsonString(): String =
    if (this != null) {
        OBJECT_MAPPER.writeValueAsString(this)
    } else {
        ""
    }

private val valueJsonToken =
    setOf(
        JsonToken.VALUE_NULL,
        JsonToken.VALUE_TRUE,
        JsonToken.VALUE_FALSE,
        JsonToken.VALUE_STRING,
        JsonToken.VALUE_NUMBER_INT,
        JsonToken.VALUE_NUMBER_FLOAT,
        JsonToken.VALUE_EMBEDDED_OBJECT,
    )

private val jsonFactory = JsonFactory()

/**
 * 流式获取Json 根节点的数据.
 */
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
                return if (token in valueJsonToken) {
                    it.text
                } else {
                    null
                }
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
