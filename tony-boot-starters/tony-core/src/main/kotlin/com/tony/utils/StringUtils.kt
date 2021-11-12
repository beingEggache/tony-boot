@file:Suppress("unused")
@file:JvmName("StringUtils")

package com.tony.utils

import com.fasterxml.jackson.core.JsonProcessingException
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.util.AntPathMatcher
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64
import java.util.UUID

/**
 * 生成uuid并去掉横杠 “-”，并大写
 */
fun uuid() = UUID.randomUUID().toString().uppercase().replace("-", "")

fun String.isJson() = try {
    OBJECT_MAPPER.readTree(this)
    true
} catch (e: JsonProcessingException) {
    false
}

/**
 * 转为 deeplink表示， 如a=1&b=2&c=3
 */
fun <T> T.toDeepLink() =
    toJsonString()
        .jsonToObj<Map<String, Any?>>()
        .toDeepLink()

/**
 * 转为 deeplink表示， 如a=1&b=2&c=3
 */
fun Map<String, Any?>.toDeepLink() =
    asIterable()
        .filter { it.value != null }
        .joinToString("&") { "${it.key}=${it.value}" }

/**
 * 将deeplink字符串转为map， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 */
fun String.deepLinkToMap() =
    split("&")
        .map { it.split("=") }
        .associate { it[0] to it[1] }

/**
 * 将deeplink字符串转为对象， 如将a=1&b=2&c=3  转为 {a=1,b=2,c=3}
 */
inline fun <reified T> String.deepLinkToObj() =
    deepLinkToMap()
        .toJsonString()
        .jsonToObj<T>()

/**
 * 字符串转为MD5并大写
 */
fun String.toMd5UppercaseString(): String = DigestUtils.md5Hex(toByteArray()).uppercase()

/**
 * 字符串base64表示
 */
fun String.toBase64String(): String = Base64.getEncoder().encode(toByteArray()).toString(Charsets.UTF_8)

/**
 * base64表示转为实际字符串
 */
fun String.base64ToString(): String = Base64.getDecoder().decode(toByteArray()).toString(Charsets.UTF_8)

@JvmOverloads
fun String?.defaultIfBlank(default: String = ""): String = this ?: default

private val mobileRegex = Regex("^1[3-9][0-9]{9}$")

fun String.isMobileNumber() = mobileRegex.matches(this)

fun String.isInt() = toIntOrNull() != null

fun CharSequence.isInt() = toString().isInt()

@JvmOverloads
fun String?.urlEncode(charset: String = "UTF8"): String = URLEncoder.encode(defaultIfBlank(), charset)

@JvmOverloads
fun String?.urlDecode(charset: String = "UTF8"): String = URLDecoder.decode(defaultIfBlank(), charset)

private val lineBreakRegex = Regex("[\\n\\r]+")

fun String.removeLineBreak(): String = this.replace(lineBreakRegex, "")

private val antPathMatcher = AntPathMatcher()

fun String?.antPathMatchAny(patterns: List<String>?) =
    patterns?.any { antPathMatcher.match(it, defaultIfBlank()) } ?: false
