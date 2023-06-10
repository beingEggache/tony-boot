package com.tony.feign.test.enums.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue
import com.tony.utils.toJsonString
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class OpenFeignEnumTestController {

    @GetMapping("/test-int-enum")
    fun testIntEnum(
        myIntEnum: MyIntEnum
    ) {
        println(myIntEnum)
    }

    @GetMapping("/test-string-enum")
    fun testStringEnum(
        myStringEnum: MyStringEnum
    ) {
        println(myStringEnum)
    }

    @PostMapping("/test-post-enum")
    fun testPostEnum(
        @RequestBody
        enumTest: EnumTest
    ) {
        println(enumTest.toJsonString())
    }
}

class EnumTest {
    var myIntEnum: Int? = null
    var myStringEnum: String? = null
}

enum class MyIntEnum(
    override val value: Int
) : IntEnumValue {

    @JsonEnumDefaultValue
    ZERO(0),
    ONE(1),
    ;

    companion object : EnumCreator<MyIntEnum, Int>(MyIntEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}

enum class MyStringEnum(
    override val value: String
) : StringEnumValue {

    @JsonEnumDefaultValue
    YES("yes"),
    NO("NO"),
    ;

    companion object : EnumCreator<MyStringEnum, String>(MyStringEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String) =
            super.create(value)
    }
}
