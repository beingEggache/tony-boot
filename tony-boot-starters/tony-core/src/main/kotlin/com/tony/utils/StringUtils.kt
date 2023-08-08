@file:JvmName("StringUtils")

package com.tony.utils

/**
 * 字符串工具类
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
import com.fasterxml.jackson.core.JsonProcessingException
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
 */
public fun uuid(): String = UUID.randomUUID().toString().uppercase().replace("-", "")

/**
 * 判断字符串是否是一个json
 */
public fun CharSequence.isJson(): Boolean = try {
    globalObjectMapper.readTree(this.toString())
    true
} catch (e: JsonProcessingException) {
    false
}

/**
 * 转为 queryString表示， 如a=1&b=2&c=3
 */
public fun <T> T.toQueryString(): String =
    toJsonString()
        .jsonToObj<Map<String, Any?>>()
        .toQueryString()

/**
 * 判断两字符串是否相等，null == "" -> true, "" == null -> true
 */
public fun CharSequence?.equalsIgnoreNullOrEmpty(str: String?): Boolean =
    if (this.isNullOrEmpty()) {
        str.isNullOrEmpty()
    } else {
        this == str
    }

/**
 * 转为 queryString表示， 如a=1&b=2&c=3
 */
public fun Map<String, Any?>.toQueryString(): String =
    asIterable()
        .filter { it.value != null }
        .joinToString("&") { "${it.key}=${it.value}" }

/**
 * 将queryString字符串转为map， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 */
public fun CharSequence.queryStringToMap(): Map<String, String> =
    toString()
        .split("&")
        .map { it.split("=") }
        .associate { it[0] to it[1] }

/**
 * 将queryString字符串转为对象， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 */
public inline fun <reified T> CharSequence.queryStringToObj(): T =
    toString()
        .queryStringToMap()
        .toJsonString()
        .jsonToObj()

/**
 * 字符串转为MD5并大写
 */
public fun CharSequence.md5Uppercase(): String =
    BigInteger(1, md5Digest().digest(toString().toByteArray()))
        .toString(16)
        .padStart(32, '0')
        .uppercase()

/**
 * 字符串base64表示
 */
public fun CharSequence.toBase64String(): String =
    toString()
        .toByteArray()
        .encodeToBase64()
        .toString(Charsets.UTF_8)

/**
 * 字符串base64表示
 */
public fun CharSequence.toBase64StringUrlSafe(): String =
    toString()
        .toByteArray()
        .encodeToBase64UrlSafe()
        .toString(Charsets.UTF_8)

/**
 * base64表示
 */
public fun CharSequence.toBase64ByteArray(): ByteArray =
    toString()
        .toByteArray()
        .encodeToBase64()

/**
 * base64表示
 */
public fun CharSequence.toBase64ByteArrayUrlSafe(): ByteArray =
    toString()
        .toByteArray()
        .encodeToBase64UrlSafe()

/**
 * base64表示转为实际字符串
 */
public fun CharSequence.base64ToString(): String =
    toString()
        .toByteArray()
        .decodeBase64()
        .toString(Charsets.UTF_8)

/**
 * base64表示转为实际字符串
 */
public fun CharSequence.base64ToStringUrlSafe(): String =
    toString()
        .toByteArray()
        .decodeBase64UrlSafe()
        .toString(Charsets.UTF_8)

/**
 * base64表示转为原二进制
 */
public fun CharSequence.base64ToByteArray(): ByteArray =
    toString()
        .toByteArray()
        .decodeBase64()

/**
 * base64表示转为原二进制
 */
public fun CharSequence.base64ToByteArrayUrlSafe(): ByteArray =
    toString()
        .toByteArray()
        .decodeBase64UrlSafe()

/**
 * encode Hex
 *
 * @receiver data        Data to Hex
 * @param lowerCase 是否小写,默认 true
 * @return bytes
 */
@JvmOverloads
public fun CharSequence.toHexByteArray(lowerCase: Boolean = true): ByteArray =
    toString()
        .toByteArray()
        .encodeToHex(lowerCase)

/**
 * encode Hex
 *
 * @receiver data        Data to Hex
 * @param lowerCase 是否小写,默认 true
 * @return bytes as a hex string
 */
@JvmOverloads
public fun CharSequence.toHexString(lowerCase: Boolean = true): String =
    toString()
        .toHexByteArray(lowerCase)
        .contentToString()

/**
 * decode Hex
 *
 * @receiver data Hex data
 * @return decode hex to bytes
 */
public fun CharSequence.hexToByteArray(): ByteArray =
    toString()
        .toByteArray()
        .decodeHex()

/**
 * decode Hex
 *
 * @receiver data Hex data
 * @return bytes as a hex string
 */
public fun CharSequence.hexToString(): String =
    toString()
        .toByteArray()
        .decodeHex()
        .contentToString()

/**
 * 当字符串为Null 或者空字符串时 提供默认值.
 *
 * @param default
 * @return
 */
@JvmOverloads
public fun CharSequence?.defaultIfBlank(default: String = ""): String =
    if (this.isNullOrBlank()) {
        default
    } else {
        this.toString()
    }

private val mobileRegex = Regex("^1[3-9][0-9]{9}$")

/**
 * 字符串是否手机号
 */
public fun CharSequence.isMobileNumber(): Boolean = mobileRegex.matches(this)

/**
 * 字符串是否一个整形
 */
public fun CharSequence.isInt(): Boolean = toString().toIntOrNull() != null

public fun <T : Number> CharSequence.toNumber(numberType: Class<in T>): T =
    when (numberType) {
        Long::class.javaObjectType, Long::class.javaPrimitiveType -> toString().toLong()
        Int::class.javaObjectType, Int::class.javaPrimitiveType -> toString().toInt()
        Double::class.javaObjectType, Double::class.javaPrimitiveType -> toString().toDouble()
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> toString().toByte()
        Short::class.javaObjectType, Short::class.javaPrimitiveType -> toString().toShort()
        Float::class.javaObjectType, Float::class.javaPrimitiveType -> toString().toFloat()
        BigInteger::class.java -> BigInteger.valueOf(toString().toLong())
        BigDecimal::class.java -> BigDecimal(toString())
        else -> throw IllegalArgumentException("Not support input type: $numberType")
    }.asToNotNull()

/**
 * Translates a string into application/x-www-form-urlencoded format using a specific encoding scheme.
 */
@JvmOverloads
public fun CharSequence?.urlEncode(charset: Charset = Charsets.UTF_8): String =
    URLEncoder.encode(defaultIfBlank(), charset)

/**
 * Decodes an application/x-www-form-urlencoded string using a specific Charset.
 *
 * The supplied charset is used to determine what characters are represented by any consecutive sequences of the form "%xy".
 */
@JvmOverloads
public fun CharSequence?.urlDecode(charset: Charset = Charsets.UTF_8): String =
    URLDecoder.decode(defaultIfBlank(), charset)

private val lineBreakRegex = Regex("[\\n\\r]+")

/**
 * 去掉字符串的换行符, 比如 \n, \r
 */
public fun CharSequence.removeLineBreak(): String = this.replace(lineBreakRegex, "")

private val antPathMatcher = AntPathMatcher()

/**
 * 字符串 ant 匹配
 * @see AntPathMatcher.match
 */
public fun CharSequence?.antPathMatchAny(patterns: Collection<String>?): Boolean =
    patterns?.any { antPathMatcher.match(it, defaultIfBlank()) } ?: false

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

/**
 * 字符串驼峰转 snake
 */
public fun CharSequence.camelToSnakeCase(): String =
    camelRegex
        .replace(this) {
            "_${it.value}"
        }
        .lowercase(Locale.getDefault())

/**
 * snake 字符串 转驼峰
 */
public fun CharSequence.snakeToLowerCamelCase(): String =
    snakeRegex
        .replace(this) {
            it.value
                .replace("_", "")
                .uppercase(Locale.getDefault())
        }

/**
 * snake 字符串 转驼峰
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

@JvmSynthetic
internal val duplicateSlash: Pattern = Pattern.compile("/{2,}")

/**
 * 清理 路径.
 *
 * 将多个重复的斜杠转为一个.
 */
public fun sanitizedPath(input: CharSequence): String =
    duplicateSlash
        .matcher(input)
        .replaceAll("/")

private val QUOTES_CHARS = arrayOf('\'', '\"')
public fun String.trimQuotes(): String = when {
    this.length < 2 -> this
    first() in QUOTES_CHARS && last() in QUOTES_CHARS -> substring(1, this.length - 1)
    else -> this
}
