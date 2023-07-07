@file:JvmName("JsonUtils")

package com.tony.utils

/**
 * Json工具类
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.tony.SpringContexts
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.util.Date
import java.util.TimeZone

public val globalObjectMapper: ObjectMapper by lazy {
    try {
        SpringContexts.getBean(ObjectMapper::class.java)
    } catch (e: Throwable) {
        // If not in spring env.
        getLogger("com.tony.utils.JsonUtils").warn("We don't have a spring context.")
        createObjectMapper()
    }
}

/**
 * 创建全局统一行为 jackson object mapper.
 *
 */
public fun createObjectMapper(): ObjectMapper =
    ObjectMapper()
        .apply {
            val kotlinModule = KotlinModule.Builder()
                .apply {
                    enable(KotlinFeature.NullIsSameAsDefault)
                    enable(KotlinFeature.NullToEmptyCollection)
                    enable(KotlinFeature.NullToEmptyMap)
                }.build()
            registerModules(kotlinModule, JavaTimeModule(), ParameterNamesModule())
            setTimeZone(TimeZone.getDefault())
            setSerializationInclusion(JsonInclude.Include.ALWAYS)
            configOverride(Date::class.java).setFormat(
                JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss"),
            )
            configOverride(LocalDateTime::class.java).setFormat(
                JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss"),
            )
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            enable(
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
                DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
            )
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            @Suppress("DEPRECATION")
            enable(
                JsonGenerator.Feature.IGNORE_UNKNOWN,
                JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN,
                JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS,
                JsonGenerator.Feature.USE_FAST_DOUBLE_WRITER,
            )
            enable(
                JsonParser.Feature.USE_FAST_BIG_NUMBER_PARSER,
                JsonParser.Feature.USE_FAST_DOUBLE_PARSER,
            )
        }

@Throws(IOException::class)
public inline fun <reified T> String.jsonToObj(): T =
    globalObjectMapper.readValue(this)

/**
 * Method to deserialize JSON content from given JSON content String.
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(clazz: Class<T>): T =
    globalObjectMapper.readValue(this, clazz)

/**
 * Method to deserialize JSON content from given JSON content String.
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(typeReference: TypeReference<T>): T =
    globalObjectMapper.readValue(this, typeReference)

/**
 * Method to deserialize JSON content from given JSON content String.
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(javaType: JavaType): T =
    globalObjectMapper.readValue(this, javaType)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun String.jsonNode(): JsonNode = globalObjectMapper.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun ByteArray.jsonNode(): JsonNode = globalObjectMapper.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun InputStream.jsonNode(): JsonNode = globalObjectMapper.readTree(this)

/**
 * Method that can be used to serialize any Java value as a String.
 */
public fun <T> T?.toJsonString(): String =
    if (this != null) {
        globalObjectMapper.writeValueAsString(this)
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
    globalObjectMapper.readValue(this)

@Throws(IOException::class)
public inline fun <reified T> InputStream.jsonToObj(): T =
    globalObjectMapper.readValue(this)
