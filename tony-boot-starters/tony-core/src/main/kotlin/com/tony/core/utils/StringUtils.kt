@file:Suppress("unused")
@file:JvmName("StringUtils")

package com.tony.core.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.codec.digest.DigestUtils
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64
import java.util.UUID

fun uuid() = UUID.randomUUID().toString().uppercase().replace("-", "")

fun String.isJson() = try {
    OBJECT_MAPPER.readTree(this)
    true
} catch (e: JsonProcessingException) {
    false
}

@Throws(IOException::class)
inline fun <reified T> String.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
fun <T> String.jsonToObj(javaType: JavaType): T =
    OBJECT_MAPPER.readValue(this, javaType)

fun <T> T.toDeepLink() =
    toJsonString()
        .jsonToObj<Map<String, Any?>>()
        .toDeepLink()

fun Map<String, Any?>.toDeepLink() =
    asIterable()
        .filter { it.value != null }
        .joinToString("&") { "${it.key}=${it.value}" }

fun String.deepLinkToMap() =
    split("&")
        .map { it.split("=") }
        .associate { it[0] to it[1] }

inline fun <reified T> String.deepLinkToObj() =
    deepLinkToMap()
        .toJsonString()
        .jsonToObj<T>()

fun String.toMd5UppercaseString(): String = DigestUtils.md5Hex(toByteArray()).uppercase()
fun String.toBase64String(): String = Base64.getEncoder().encode(toByteArray()).toString(Charsets.UTF_8)
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

val lineBreakRegex = Regex("[\\n\\r]+")
fun String.removeLineBreak(): String = this.replace(lineBreakRegex, "")
