package com.tony.web.auth.test.controller

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport
import com.tony.enums.validate.SimpleIntEnum
import com.tony.enums.validate.SimpleStringEnum
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.web.interceptor.NoLoginCheck
import com.tony.web.test.req.TestIntEnum
import com.tony.web.test.req.TestReq
import com.tony.web.test.req.TestStringEnum
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.BigInteger

@NoLoginCheck
@Tag(name = "测试")
@RestController
class TestController {

    @ApiOperationSupport(order = 0)
    @Operation(description = "boolean")
    @PostMapping("/boolean")
    fun boolean(): Boolean = true

    @ApiOperationSupport(order = 1)
    @Operation(description = "boolean-array")
    @PostMapping("/boolean-array")
    fun booleanArray(): BooleanArray = BooleanArray(1) { true }

    @ApiOperationSupport(order = 2)
    @Operation(description = "byte")
    @PostMapping("/byte")
    fun byte(): Byte = Byte.MAX_VALUE

    @ApiOperationSupport(order = 2)
    @Operation(description = "byte-array")
    @PostMapping("/byte-array", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun byteArray(): ByteArray = ByteArray(1) { Byte.MIN_VALUE }

    @ApiOperationSupport(order = 4)
    @Operation(description = "short")
    @PostMapping("/short")
    fun short(): Short = Short.MAX_VALUE

    @ApiOperationSupport(order = 5)
    @Operation(description = "short-array")
    @PostMapping("/short-array")
    fun shortArray(): ShortArray = ShortArray(1) { Short.MIN_VALUE }

    @ApiOperationSupport(order = 6)
    @Operation(description = "int")
    @PostMapping("/int")
    fun int(): Int = Int.MAX_VALUE

    @ApiOperationSupport(order = 7)
    @Operation(description = "int-array")
    @PostMapping("/int-array")
    fun intArray(): IntArray = IntArray(1) { Int.MIN_VALUE }

    @ApiOperationSupport(order = 8)
    @Operation(description = "long")
    @PostMapping("/long")
    fun long(): Long = Long.MAX_VALUE

    @ApiOperationSupport(order = 9)
    @Operation(description = "long-array")
    @PostMapping("/long-array")
    fun longArray(): LongArray = LongArray(1) { Long.MIN_VALUE }

    @ApiOperationSupport(order = 10)
    @Operation(description = "big-integer")
    @PostMapping("/big-integer")
    fun bigInteger(): BigInteger = BigInteger.ONE

    @ApiOperationSupport(order = 11)
    @Operation(description = "big-integer-array")
    @PostMapping("/big-integer-array")
    fun bigIntegerArray(): Array<BigInteger> = arrayOf(BigInteger.ONE, BigInteger.TEN)

    @ApiOperationSupport(order = 12)
    @Operation(description = "float")
    @PostMapping("/float")
    fun float(): Float = Float.MAX_VALUE

    @ApiOperationSupport(order = 13)
    @Operation(description = "float-array")
    @PostMapping("/float-array")
    fun floatArray(): FloatArray = FloatArray(1) { Float.MIN_VALUE }

    @ApiOperationSupport(order = 14)
    @Operation(description = "double")
    @PostMapping("/double")
    fun double(): Double = Double.MAX_VALUE

    @ApiOperationSupport(order = 15)
    @Operation(description = "double-array")
    @PostMapping("/double-array")
    fun doubleArray(): DoubleArray = DoubleArray(1) { Double.MIN_VALUE }

    @ApiOperationSupport(order = 16)
    @Operation(description = "big-decimal")
    @PostMapping("/big-decimal")
    fun bigDecimal(): BigDecimal = BigDecimal.ONE

    @ApiOperationSupport(order = 17)
    @Operation(description = "big-decimal-array")
    @PostMapping("/big-decimal-array")
    fun bigDecimalArray(): Array<BigDecimal> = arrayOf(BigDecimal.ONE, BigDecimal.TEN)

    @ApiOperationSupport(order = 18)
    @Operation(description = "string")
    @PostMapping("/string")
    fun string(): String = "string"

    @ApiOperationSupport(order = 19)
    @Operation(description = "string-array")
    @PostMapping("/string-array")
    fun stringArray(): Array<String> = arrayOf("string")

    @ApiOperationSupport(order = 20)
    @Operation(description = "char")
    @PostMapping("/char")
    fun char(): Char = Char.MAX_VALUE

    @ApiOperationSupport(order = 20)
    @Operation(description = "char-array")
    @PostMapping("/char-array")
    fun charArray(): CharArray = CharArray(1) { Char.MIN_VALUE }

    @ApiOperationSupport(order = 21)
    @Operation(description = "map")
    @PostMapping("/map")
    fun map(): Map<String, Any?> = mapOf("name" to "张三")

    @ApiOperationSupport(order = 22)
    @Operation(description = "map-list")
    @PostMapping("/map-list")
    fun mapList(): List<Map<String, Any?>> = listOf(
        mapOf("name" to "张三"),
        mapOf("name" to "李四"),
    )

    @ApiOperationSupport(order = 23)
    @Operation(description = "api-exception")
    @PostMapping("/api-exception")
    fun apiException() {
        throw ApiException()
    }

    @ApiOperationSupport(order = 24)
    @Operation(description = "biz-exception")
    @PostMapping("/biz-exception")
    fun bizException() {
        throw BizException("bizException")
    }

    @ApiOperationSupport(order = 25)
    @Operation(description = "exception")
    @PostMapping("/exception")
    fun exception() {
        throw Exception()
    }

    @ApiOperationSupport(order = 26)
    @Operation(description = "string-enum")
    @PostMapping("/string-enum")
    fun stringEnum(@RequestParam testStringEnum: TestStringEnum): TestStringEnum = TestStringEnum.TEST_1

    @ApiOperationSupport(order = 27)
    @Operation(description = "int-enum")
    @PostMapping("/int-enum")
    fun intEnum(@RequestParam testIntEnum: TestIntEnum): TestIntEnum = TestIntEnum.TEST_2

    @ApiOperationSupport(order = 28)
    @Operation(description = "string-enum-validate")
    @PostMapping("/string-enum-validate")
    fun stringEnumValidate(
        @RequestParam
        @SimpleStringEnum(enums = ["2"], message = "不对", required = true)
        testStringEnum: TestStringEnum
    ): TestStringEnum = TestStringEnum.TEST_1

    @ApiOperationSupport(order = 29)
    @Operation(description = "int-enum-validate")
    @PostMapping("/int-enum-validate")
    fun intEnumValidate(
        @RequestParam
        @SimpleIntEnum(enums = [1], message = "不对", required = true)
        testIntEnum: TestIntEnum
    ): TestIntEnum = TestIntEnum.TEST_2

    @ApiOperationSupport(order = 30)
    @Operation(description = "form")
    @PostMapping("/form")
    fun form(
        @ParameterObject
        testReq: TestReq
    ): TestReq = testReq

    @ApiOperationSupport(order = 31)
    @Operation(description = "body")
    @PostMapping("/body")
    fun body(
        @RequestBody
        testReq: TestReq
    ): TestReq = testReq
}
