@file:Suppress("unused", "FunctionName")
@file:JvmName("CoreUtils")

package com.tony.core.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.tony.core.exception.ApiException
import java.io.IOException
import java.io.InputStream
import java.security.SecureRandom
import java.text.DecimalFormat
import java.util.TimeZone
import kotlin.math.pow
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@JvmField
val logger: Logger = LoggerFactory.getLogger("web_extension")

fun <T> T?.println() = println(this)

@JvmOverloads
fun <T> T.getLogger(name: String? = null): Logger where T : Any =
    if (name.isNullOrBlank()) LoggerFactory.getLogger(this::class.java.name)
    else LoggerFactory.getLogger(name)

@JvmField
val OBJECT_MAPPER: ObjectMapper = createObjectMapper()

fun createObjectMapper() = ObjectMapper().apply {
    registerModules(KotlinModule(), JavaTimeModule())
    setTimeZone(TimeZone.getDefault())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)
    enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
    enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

@Throws(IOException::class)
inline fun <reified T> ByteArray.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
inline fun <reified T> InputStream.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Suppress("UNCHECKED_CAST")
fun <E> Any?.asTo(): E? where E : Any = this as E?

private val decimalFormat = DecimalFormat("#,####.##")

@JvmOverloads
fun Long?.trunc(digit: Double = 100.00): String =
    decimalFormat.format(this?.div(digit))

private val secureRandom = SecureRandom()

/**
 * 生成指定位数随机数
 * @param digit 位数
 */
fun genRandomNumber(digit: Int): Int {
    if (digit < 1) throw ApiException("随机数至少是一位数")
    if (digit == 1) {
        return secureRandom.nextInt(10)
    }
    val bound1 = (9 * 10).toDouble().pow((digit - 1).toDouble()).toInt()
    val bound2 = 10.toDouble().pow((digit - 1).toDouble()).toInt()

    return secureRandom.nextInt(bound1) + bound2
}

inline fun Boolean.doIf(crossinline block: () -> Any) {
    if (this) block()
}

inline fun <T> T.doIf(flag: Boolean, crossinline block: T.() -> Unit): T {
    if (flag) block()
    return this
}

inline fun Boolean.doUnless(crossinline block: () -> Any) {
    if (!this) block()
}

inline fun <reified T> T?.doIfNull(crossinline block: () -> T): T {
    return this ?: block()
}