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

package com.tony.test.feign.module.enums.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue
import com.tony.utils.toJsonString
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@NoLoginCheck
@Validated
@RestController
class FeignEnumTestController {

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
