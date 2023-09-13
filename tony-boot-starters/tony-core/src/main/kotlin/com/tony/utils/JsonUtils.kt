@file:JvmName("JsonUtils")

package com.tony.utils

/**
 * Json工具类
 *
 * @author Tang Li
 * @date 2022/9/29 10:20
 */
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.tony.SpringContexts
import com.tony.jackson.initialize
import java.io.IOException
import java.io.InputStream
import org.springframework.beans.BeansException

/**
 * 全局ObjectMapper
 * 先从spring 环境获取, 否则创建一个.
 */
public val globalObjectMapper: ObjectMapper by lazy(LazyThreadSafetyMode.PUBLICATION) {
    try {
        SpringContexts.getBean(ObjectMapper::class.java)
    } catch (e: BeansException) {
        // If not in spring env.
        getLogger("com.tony.utils.JsonUtils").warn("We don't have a spring context.")
        createObjectMapper()
    }
}

/**
 * 创建全局统一行为 jackson object mapper.
 * @return [ObjectMapper]
 * @author Tang Li
 * @date 2023/09/13 10:19
 * @since 1.0.0
 */
public fun createObjectMapper(): ObjectMapper = ObjectMapper().initialize()

/**
 * json字符串转对象
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 */
@Throws(IOException::class)
public inline fun <reified T> String.jsonToObj(): T =
    globalObjectMapper.readValue(this)

/**
 * Method to deserialize JSON content from given JSON content String.
 * @param [clazz] clazz
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(clazz: Class<T>): T =
    globalObjectMapper.readValue(this, clazz)

/**
 * Method to deserialize JSON content from given JSON content String.
 * @param [typeReference] 类型参考
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(typeReference: TypeReference<T>): T =
    globalObjectMapper.readValue(this, typeReference)

/**
 * Method to deserialize JSON content from given JSON content String.
 * @param [javaType] java类型
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 */
@Throws(IOException::class)
public fun <T> String.jsonToObj(javaType: JavaType): T =
    globalObjectMapper.readValue(this, javaType)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @return [JsonNode]
 * @author Tang Li
 * @date 2023/09/13 10:21
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun String.jsonNode(): JsonNode = globalObjectMapper.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @return [JsonNode]
 * @author Tang Li
 * @date 2023/09/13 10:21
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun ByteArray.jsonNode(): JsonNode = globalObjectMapper.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @return [JsonNode]
 * @author Tang Li
 * @date 2023/09/13 10:21
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
public fun InputStream.jsonNode(): JsonNode = globalObjectMapper.readTree(this)

/**
 * Method that can be used to serialize any Java value as a String.
 * @return [String]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
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
        JsonToken.VALUE_EMBEDDED_OBJECT
    )

private val jsonFactory = JsonFactory()

/**
 * 流式获取Json 根节点的数据.
 * @param [field] 领域
 * @return [String]?
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
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

/**
 * json到obj
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 */
@Throws(IOException::class)
public inline fun <reified T> ByteArray.jsonToObj(): T =
    globalObjectMapper.readValue(this)

/**
 * json到obj
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 */
@Throws(IOException::class)
public inline fun <reified T> InputStream.jsonToObj(): T =
    globalObjectMapper.readValue(this)
