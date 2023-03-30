@file:JvmName("StringUtils")

package com.tony.utils

import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.util.AntPathMatcher
import java.math.BigInteger
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern

/**
 * 生成uuid并去掉横杠 “-”，并大写
 */
public fun uuid(): String = UUID.randomUUID().toString().uppercase().replace("-", "")

public fun String.isJson(): Boolean = try {
    OBJECT_MAPPER.readTree(this)
    true
} catch (e: JsonProcessingException) {
    false
}

/**
 * 转为 deeplink表示， 如a=1&b=2&c=3
 */
public fun <T> T.toDeepLink(): String =
    toJsonString()
        .jsonToObj<Map<String, Any?>>()
        .toDeepLink()

/**
 * 判断两字符串是否相等，null == "" -> true, "" == null -> true
 */
public fun String?.equalsIgnoreNullOrEmpty(str: String?): Boolean =
    if (this.isNullOrEmpty()) {
        str.isNullOrEmpty()
    } else {
        this == str
    }

/**
 * 转为 deeplink表示， 如a=1&b=2&c=3
 */
public fun Map<String, Any?>.toDeepLink(): String =
    asIterable()
        .filter { it.value != null }
        .joinToString("&") { "${it.key}=${it.value}" }

/**
 * 将deeplink字符串转为map， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 */
public fun String.deepLinkToMap(): Map<String, String> =
    split("&")
        .map { it.split("=") }
        .associate { it[0] to it[1] }

/**
 * 将deeplink字符串转为对象， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 */
public inline fun <reified T> String.deepLinkToObj(): T =
    deepLinkToMap()
        .toJsonString()
        .jsonToObj<T>()

/**
 * 字符串转为MD5并大写
 */
public fun String.md5Uppercase(): String =
    BigInteger(1, md5Digest().digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')
        .uppercase()

/**
 * 字符串base64表示
 */
public fun String.toBase64String(): String = Base64.getEncoder().encode(toByteArray()).toString(Charsets.UTF_8)

/**
 * base64表示转为实际字符串
 */
public fun String.base64ToString(): String = Base64.getDecoder().decode(toByteArray()).toString(Charsets.UTF_8)

@JvmOverloads
public fun String?.defaultIfBlank(default: String = ""): String = if (this.isNullOrBlank()) default else this

private val mobileRegex = Regex("^1[3-9][0-9]{9}$")

public fun String.isMobileNumber(): Boolean = mobileRegex.matches(this)

public fun String.isInt(): Boolean = toIntOrNull() != null

public fun CharSequence.isInt(): Boolean = toString().isInt()

@JvmOverloads
public fun String?.urlEncode(charset: String = "UTF8"): String = URLEncoder.encode(defaultIfBlank(), charset)

@JvmOverloads
public fun String?.urlDecode(charset: String = "UTF8"): String = URLDecoder.decode(defaultIfBlank(), charset)

private val lineBreakRegex = Regex("[\\n\\r]+")

public fun String.removeLineBreak(): String = this.replace(lineBreakRegex, "")

private val antPathMatcher = AntPathMatcher()

public fun String?.antPathMatchAny(patterns: Collection<String>?): Boolean =
    patterns?.any { antPathMatcher.match(it, defaultIfBlank()) } ?: false

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private val snakeRegex = "_[a-zA-Z]".toRegex()

public fun String.camelToSnakeCase(): String = camelRegex.replace(this) {
    "_${it.value}"
}.lowercase(Locale.getDefault())

public fun String.snakeToLowerCamelCase(): String = snakeRegex.replace(this) {
    it.value.replace("_", "").uppercase(Locale.getDefault())
}

public fun String.snakeToUpperCamelCase(): String =
    snakeToLowerCamelCase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

internal val duplicateSlash: Pattern = Pattern.compile("/{2,}")

public fun sanitizedPath(input: String): String =
    duplicateSlash.matcher(input).replaceAll("/")
