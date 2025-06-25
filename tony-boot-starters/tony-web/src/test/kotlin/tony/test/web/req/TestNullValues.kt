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

package tony.test.web.req

import com.fasterxml.jackson.databind.SerializationFeature
import tony.codec.enums.Encoding
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * TestDateTimesReq is
 * @author tangli
 * @date 2023/07/07 19:46
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
    val stringEnum: Encoding? = null,
    val enum: SerializationFeature? = null,
    val obj: Any? = null,
    )
