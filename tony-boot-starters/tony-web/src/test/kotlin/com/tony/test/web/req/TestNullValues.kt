package com.tony.test.web.req

import com.fasterxml.jackson.databind.SerializationFeature
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.CharBuffer
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * TestDateTimesReq is
 * @author Tang Li
 * @date 2023/07/07 11:46
 */
data class TestNullValues(
    val boolean: Boolean? = null,
    val booleanArr: BooleanArray? = null,
    val byte: Byte? = null,
    val byteArray: ByteArray? = null,
    val short: Short? = null,
    val shortArray: ShortArray? = null,
    val int: Int? = null,
    val intArray: IntArray? = null,
    val long: Long? = null,
    val longArray: LongArray? = null,
    val bigInteger: BigInteger? = null,
    val float: Float? = null,
    val floatArray: FloatArray? = null,
    val double: Double? = null,
    val doubleArray: DoubleArray? = null,
    val char: Char? = null,
    val charArray: CharArray? = null,
    val bigDecimal: BigDecimal? = null,

    val stringBuffer: StringBuffer? = null,
    val stringBuilder: StringBuilder? = null,
    val charBuffer: CharBuffer? = null,
    val charSequence: CharSequence? = null,

    val date: Date? = null,
    val localDateTime: LocalDateTime? = null,
    val localDate: LocalDate? = null,

    val array: Array<*>? = null,
    val map: Map<String, Any>? = null,
    val list: List<Any>? = null,
    val set: Set<Any>? = null,
    val collection: Collection<*>? = null,

    val intEnum: TestIntEnum? = null,
    val stringEnum: TestStringEnum? = null,
    val enum: SerializationFeature? = null,
    val obj: Any? = null,
    )
