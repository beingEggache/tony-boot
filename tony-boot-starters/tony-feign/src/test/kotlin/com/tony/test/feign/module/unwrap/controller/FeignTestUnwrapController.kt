package com.tony.test.feign.module.unwrap.controller

import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.unwrap.api.FeignTestUnwrapApi
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class FeignTestUnwrapController : FeignTestUnwrapApi {
    override fun boolean(): Boolean = true

    override fun booleanArray(): BooleanArray = booleanArrayOf(true, false)

    override fun byte(): Byte = Byte.MAX_VALUE

    override fun short(): Short = Short.MAX_VALUE

    override fun shortArray(): ShortArray = shortArrayOf(Short.MIN_VALUE, Short.MAX_VALUE)

    override fun int(): Int = Int.MAX_VALUE

    override fun intArray(): IntArray = intArrayOf(Int.MIN_VALUE, Int.MAX_VALUE)

    override fun long(): Long = Long.MAX_VALUE

    override fun longArray(): LongArray = longArrayOf(Long.MIN_VALUE, Long.MAX_VALUE)

    override fun float(): Float = Float.MAX_VALUE

    override fun floatArray(): FloatArray = floatArrayOf(Float.MIN_VALUE, Float.MAX_VALUE)

    override fun double(): Double = Double.MAX_VALUE

    override fun doubleArray(): DoubleArray = doubleArrayOf(Double.MIN_VALUE, Double.MAX_VALUE)

    override fun char(): Char = Char.MAX_VALUE

    override fun charArray(): CharArray = charArrayOf(Char.MIN_VALUE, Char.MAX_VALUE)

    override fun string(): String = "string"

    override fun array(): Array<*> = arrayOf(
        Person(null, null, null, null),
        "",
        1,
        listOf(1, 2, 3),
    )

    override fun list(): List<*> = listOf(
        Person(null, null, null, null),
        "",
        1,
        listOf(1, 2, 3),
    )

    override fun map(@RequestBody person: Person) = mapOf("test" to true)

    override fun person(@RequestBody person: Person) = person

    override fun validate(@Validated @RequestBody person: Person): Person = person

    override fun exception(@RequestBody person: Person): Person = throw Exception("go fuck yourself")

    override fun bizException(@RequestBody person: Person): Person = throw BizException("go fuck yourself")

    override fun apiException(@RequestBody person: Person): Person = throw ApiException("go fuck yourself")
}
