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

@file:JvmName("Strs")

package com.tony.utils

/**
 * 字符串工具类
 *
 * @author tangli
 * @date 2022/09/29 19:20
 */
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.convertValue
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern
import org.springframework.util.AntPathMatcher

/**
 * 生成uuid并去掉横杠 “-”，并大写
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:27
 * @since 1.0.0
 */
public fun uuid(): String =
    UUID
        .randomUUID()
        .toString()
        .uppercase()
        .replace("-", "")

/**
 * 判断字符串是否是一个json
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
public fun CharSequence.isJson(): Boolean =
    try {
        globalObjectMapper.readTree(this.toString())
        true
    } catch (e: JsonProcessingException) {
        false
    }

/**
 * 转为 queryString表示， 如a=1&b=2&c=3
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
public fun <T> T.toQueryString(): String =
    toJsonString()
        .jsonToObj<Map<String, Any?>>()
        .toQueryString()

/**
 * 判断两字符串是否相等，null == "" -> true, "" == null -> true
 * @param [str] str
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
public fun CharSequence?.equalsIgnoreNullOrEmpty(str: CharSequence?): Boolean =
    if (this.isNullOrEmpty()) {
        str.isNullOrEmpty()
    } else {
        this == str
    }

/**
 * 转为 queryString表示， 如a=1&b=2&c=3
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
public fun Map<String, Any?>.toQueryString(): String =
    asIterable()
        .filter { it.value != null }
        .joinToString("&") { "${it.key}=${it.value}" }

/**
 * 将queryString字符串转为map， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 * @return [Map<String, String>]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
public fun CharSequence.queryStringToMap(): Map<String, String> =
    toString()
        .split("&")
        .map { it.split("=") }
        .associate { it[0] to it[1] }

/**
 * 将queryString字符串转为对象， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 * @return [T]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
public inline fun <reified T> CharSequence.queryStringToObj(): T =
    globalObjectMapper.convertValue(
        toString()
            .queryStringToMap()
    )

/**
 * 当字符串为Null 或者空字符串时 提供默认值.
 * @param [default] 默认值
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:28
 * @since 1.0.0
 */
@JvmOverloads
public fun CharSequence?.ifNullOrBlank(default: CharSequence = ""): String =
    if (this.isNullOrBlank()) {
        default.toString()
    } else {
        this.toString()
    }

/**
 * 当字符串为Null 或者空字符串时 提供默认值.
 * @param [block] 默认值
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:29
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun CharSequence?.ifNullOrBlank(crossinline block: () -> String): String =
    if (this.isNullOrBlank()) {
        block()
    } else {
        this.toString()
    }

private val mobileRegex = Regex("^1[3-9][0-9]{9}$")

/**
 * 字符串是否手机号
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:29
 * @since 1.0.0
 */
public fun CharSequence.isMobileNumber(): Boolean =
    mobileRegex.matches(this)

/**
 * 字符串是否一个整形
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:29
 * @since 1.0.0
 */
public fun CharSequence.isInt(): Boolean =
    toString().toIntOrNull() != null

/**
 * 字符串转数字
 * @param [numberType] 数字类型
 * @return [T]
 * @author tangli
 * @date 2023/12/08 19:29
 * @since 1.0.0
 */
public fun <T : Number> CharSequence.toNumber(numberType: Class<in T>): T =
    when (numberType) {
        Long::class.javaObjectType, Long::class.javaPrimitiveType -> toString().toLong()
        Int::class.javaObjectType, Int::class.javaPrimitiveType -> toString().toInt()
        Double::class.javaObjectType, Double::class.javaPrimitiveType -> toString().toDouble()
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> toString().toByte()
        Short::class.javaObjectType, Short::class.javaPrimitiveType -> toString().toShort()
        Float::class.javaObjectType, Float::class.javaPrimitiveType -> toString().toFloat()
        BigInteger::class.java -> toString().toBigInteger()
        BigDecimal::class.java -> toString().toBigDecimal()
        else -> throw IllegalArgumentException("Not support input type: $numberType")
    }.asToNotNull()

/**
 * Translates a string into application/x-www-form-urlencoded format using a specific codec scheme.
 * @param [charset] 字符集
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:29
 * @since 1.0.0
 */
@JvmOverloads
public fun CharSequence?.urlEncode(charset: Charset = Charsets.UTF_8): String =
    URLEncoder.encode(ifNullOrBlank(), charset)

/**
 * Decodes an application/x-www-form-urlencoded string using a specific Charset.
 * The supplied charset is used to determine what characters are represented by any consecutive sequences of the form "%xy".
 * @param [charset] 字符集
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:29
 * @since 1.0.0
 */
@JvmOverloads
public fun CharSequence?.urlDecode(charset: Charset = Charsets.UTF_8): String =
    URLDecoder.decode(ifNullOrBlank(), charset)

/**
 * 去掉字符串的换行符[System.lineSeparator]
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 */
public fun CharSequence.removeLineBreak(): String =
    this.replace("[${System.lineSeparator()}]".toRegex(), "")

private val antPathMatcher = AntPathMatcher()

/**
 * 字符串 ant 匹配
 * @param [patterns] 图案
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 * @see AntPathMatcher.match
 */
public fun CharSequence?.antPathMatchAny(patterns: Collection<String>?): Boolean =
    patterns?.any { antPathMatcher.match(it, ifNullOrBlank()) } ?: false

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

/**
 * 字符串驼峰转 snake
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 */
public fun CharSequence.camelToSnakeCase(): String =
    camelRegex
        .replace(this) {
            "_${it.value}"
        }.lowercase(Locale.getDefault())

/**
 * snake 字符串 转驼峰
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 */
public fun CharSequence.snakeToLowerCamelCase(): String =
    snakeRegex
        .replace(this) {
            it
                .value
                .replace("_", "")
                .uppercase(Locale.getDefault())
        }

/**
 * snake 字符串 转驼峰
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 */
public fun CharSequence.snakeToUpperCamelCase(): String =
    snakeToLowerCamelCase()
        .replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase(Locale.getDefault())
            } else {
                it.toString()
            }
        }

@get:JvmSynthetic
internal val duplicateSlash: Pattern = Pattern.compile("/{2,}")

/**
 * 清理 路径.
 * 将多个重复的斜杠转为一个.
 * @param [input] 输入
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 */
public fun sanitizedPath(input: CharSequence): String =
    duplicateSlash
        .matcher(input)
        .replaceAll("/")

private val QUOTES_CHARS = arrayOf('\'', '\"')

/**
 * 去掉引号
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:30
 * @since 1.0.0
 */
public fun CharSequence.trimQuotes(): String =
    when {
        this.length < 2 -> this.toString()
        first() in QUOTES_CHARS && last() in QUOTES_CHARS -> substring(1, this.length - 1)
        else -> this.toString()
    }
