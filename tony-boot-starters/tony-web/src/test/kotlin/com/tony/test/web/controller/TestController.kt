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

package com.tony.test.web.controller

import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.enums.validate.RangedIntEnum
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.test.web.req.TestDateTimesReq
import com.tony.test.web.req.TestIntEnum
import com.tony.test.web.req.TestNullValues
import com.tony.test.web.req.TestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Tag(name = "测试")
@RestController
class TestController {

    @Operation(description = "index")
    @PostMapping("/")
    fun index() {
    }

    @Operation(description = "boolean")
    @GetMapping("/boolean")
    fun boolean(): Boolean = true

    @Operation(description = "boolean-array")
    @PostMapping("/boolean-array")
    fun booleanArray(): BooleanArray = BooleanArray(1) { true }

    @Operation(description = "byte")
    @PostMapping("/byte")
    fun byte(): Byte = Byte.MAX_VALUE

    @Operation(description = "byte-array")
    @PostMapping("/byte-array", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun byteArray(): ByteArray = ByteArray(1) { Byte.MIN_VALUE }

    @Operation(description = "short")
    @PostMapping("/short")
    fun short(): Short = Short.MAX_VALUE

    @Operation(description = "short-array")
    @PostMapping("/short-array")
    fun shortArray(): ShortArray = ShortArray(1) { Short.MIN_VALUE }

    @Operation(description = "int")
    @PostMapping("/int")
    fun int(): Int = Int.MAX_VALUE

    @Operation(description = "int-array")
    @PostMapping("/int-array")
    fun intArray(): IntArray = IntArray(1) { Int.MIN_VALUE }

    @Operation(description = "long")
    @PostMapping("/long")
    fun long(): Long = Long.MAX_VALUE

    @Operation(description = "long-array")
    @PostMapping("/long-array")
    fun longArray(): LongArray = LongArray(1) { Long.MIN_VALUE }

    @Operation(description = "big-integer")
    @PostMapping("/big-integer")
    fun bigInteger(): BigInteger = BigInteger.ONE

    @Operation(description = "big-integer-array")
    @PostMapping("/big-integer-array")
    fun bigIntegerArray(): Array<BigInteger> = arrayOf(BigInteger.ONE, BigInteger.TEN)

    @Operation(description = "float")
    @PostMapping("/float")
    fun float(): Float = Float.MAX_VALUE

    @Operation(description = "float-array")
    @PostMapping("/float-array")
    fun floatArray(): FloatArray = FloatArray(1) { Float.MIN_VALUE }

    @Operation(description = "double")
    @PostMapping("/double")
    fun double(): Double = Double.MAX_VALUE

    @Operation(description = "double-array")
    @PostMapping("/double-array")
    fun doubleArray(): DoubleArray = DoubleArray(1) { Double.MIN_VALUE }

    @Operation(description = "big-decimal")
    @PostMapping("/big-decimal")
    fun bigDecimal(): BigDecimal = BigDecimal.ONE

    @Operation(description = "big-decimal-array")
    @PostMapping("/big-decimal-array")
    fun bigDecimalArray(): Array<BigDecimal> = arrayOf(BigDecimal.ONE, BigDecimal.TEN)

    @Operation(description = "string")
    @PostMapping("/string")
    fun string(): String = "string"

    @Operation(description = "string-array")
    @PostMapping("/string-array")
    fun stringArray(): Array<String> = arrayOf("string")

    @Operation(description = "char")
    @PostMapping("/char")
    fun char(): Char = Char.MAX_VALUE

    @Operation(description = "char-array")
    @PostMapping("/char-array")
    fun charArray(): CharArray = CharArray(1) { Char.MIN_VALUE }

    @Operation(description = "map")
    @PostMapping("/map")
    fun map(): Map<String, Any?> = mapOf("name" to "张三")

    @Operation(description = "map-list")
    @PostMapping("/map-list")
    fun mapList(): List<Map<String, Any?>> = listOf(
        mapOf("name" to "张三"),
        mapOf("name" to "李四"),
    )

    @Operation(description = "api-exception")
    @PostMapping("/api-exception")
    fun apiException() {
        throw ApiException()
    }

    @Operation(description = "biz-exception")
    @PostMapping("/biz-exception")
    fun bizException() {
        throw BizException("bizException")
    }

    @Operation(description = "exception")
    @PostMapping("/exception")
    fun exception() {
        throw Exception()
    }

    @Operation(description = "string-enum")
    @PostMapping("/string-enum")
    fun stringEnum(@RequestParam testStringEnum: SymmetricCryptoAlgorithm): SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.DES

    @Operation(description = "int-enum")
    @PostMapping("/int-enum")
    fun intEnum(@RequestParam testIntEnum: TestIntEnum): TestIntEnum = TestIntEnum.TEST_2

    @Operation(description = "int-enum-validate")
    @PostMapping("/int-enum-validate")
    fun intEnumValidate(
        @RequestParam
        @RangedIntEnum(enums = [1], message = "不对", required = true)
        testIntEnum: TestIntEnum
    ): TestIntEnum = TestIntEnum.TEST_2

    @Operation(description = "form")
    @PostMapping("/form")
    fun form(
        @ParameterObject
        testReq: TestReq
    ): TestReq = testReq

    @Operation(description = "body")
    @PostMapping("/body")
    fun body(
        @Validated
        @RequestBody
        testReq: TestReq
    ): TestReq = testReq

    @Operation(description = "datetime")
    @PostMapping("/datetime")
    fun datetime(
        @Validated
        @RequestBody
        testReq: TestDateTimesReq
    ): TestDateTimesReq = testReq

    @Operation(description = "datetime")
    @PostMapping("/null-values")
    fun eachTypeNullValue(): TestNullValues? = TestNullValues()

    @Operation(description = "get-dates")
    @GetMapping("/get-dates")
    fun getDates(
        date: Date?,
        localDateTime: LocalDateTime?,
        localDate: LocalDate?,
    ) {
        println(date)
        println(localDateTime)
        println(localDate)
    }

    @Operation(description = "post-dates")
    @PostMapping("/post-dates")
    fun postDates(
        date: Date?,
        localDateTime: LocalDateTime?,
        localDate: LocalDate?,
    ) {
        println(date)
        println(localDateTime)
        println(localDate)
    }

    @Operation(description = "post-dates-form")
    @PostMapping("/post-dates-form")
    fun postDatesForm(
        @ParameterObject
        testReq: TestDateTimesReq
    ) {
        println(testReq)
    }
}
