/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("Jsons")

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
public fun createObjectMapper(): ObjectMapper =
    ObjectMapper().initialize()

/**
 * json字符串转对象.
 * @receiver [CharSequence]
 * @param [T] 返回类型
 * @return [T]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readValue
 */
@Throws(IOException::class)
public inline fun <reified T> CharSequence.jsonToObj(): T =
    globalObjectMapper.readValue(this.toString())

/**
 * Method to deserialize JSON content from given JSON content String.
 * @receiver [CharSequence]
 * @param [T] 对应类型
 * @param [clazz] clazz
 * @return [T]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readValue
 */
@Throws(IOException::class)
public fun <T> CharSequence.jsonToObj(clazz: Class<T>): T =
    globalObjectMapper.readValue(this.toString(), clazz)

/**
 * Method to deserialize JSON content from given JSON content String.
 * @receiver [CharSequence]
 * @param [T] 返回类型
 * @param [typeReference] 类型参考
 * @return [T]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readValue
 */
@Throws(IOException::class)
public fun <T> CharSequence.jsonToObj(typeReference: TypeReference<T>): T =
    globalObjectMapper.readValue(this.toString(), typeReference)

/**
 * Method to deserialize JSON content from given JSON content String.
 * @receiver [CharSequence]
 * @param [T] 返回类型
 * @param [javaType] java类型
 * @return [T]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:20
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readValue
 */
@Throws(IOException::class)
public fun <T> CharSequence.jsonToObj(javaType: JavaType): T =
    globalObjectMapper.readValue(this.toString(), javaType)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @receiver [CharSequence]
 * @return [JsonNode]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:21
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
@Throws(IOException::class)
public fun CharSequence.jsonNode(): JsonNode =
    globalObjectMapper.readTree(this.toString())

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @receiver [ByteArray]
 * @return [JsonNode]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:21
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
@Throws(IOException::class)
public fun ByteArray.jsonNode(): JsonNode =
    globalObjectMapper.readTree(this)

/**
 * Method to deserialize JSON content as tree expressed using set of JsonNode instances.
 * @receiver [InputStream]
 * @return [JsonNode]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:21
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readTree
 */
@Throws(IOException::class)
public fun InputStream.jsonNode(): JsonNode =
    globalObjectMapper.readTree(this)

/**
 * Method that can be used to serialize any Java value as a String.
 * @receiver [T]
 * @param [T] 自身类型
 * @return [String]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString
 */
@Throws(IOException::class)
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
 * @receiver [CharSequence]
 * @param [field] 领域
 * @return [String]?
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 */
@Throws(IOException::class)
public fun CharSequence.getFromRootAsString(field: String): String? {
    jsonFactory.createParser(this.toString()).use {
        while (
            try {
                it.nextToken()
            } catch (e: JsonParseException) {
                return null
            } != null
        ) {
            if (it.currentToken == JsonToken.FIELD_NAME &&
                it.currentName == field &&
                it
                    .parsingContext
                    .parent
                    .inRoot()
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
 * json到obj.
 * @receiver [ByteArray]
 * @param [T] 返回类型
 * @return [T]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readValue
 */
@Throws(IOException::class)
public inline fun <reified T> ByteArray.jsonToObj(): T =
    globalObjectMapper.readValue(this)

/**
 * json到obj
 * @receiver [InputStream]
 * @param [T] 返回类型
 * @return [T]
 * @throws [IOException]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 * @see com.fasterxml.jackson.databind.ObjectMapper.readValue
 */
@Throws(IOException::class)
public inline fun <reified T> InputStream.jsonToObj(): T =
    globalObjectMapper.readValue(this)
