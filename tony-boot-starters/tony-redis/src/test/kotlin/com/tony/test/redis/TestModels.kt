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

package com.tony.test.redis

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import java.math.BigDecimal
import java.math.BigInteger

/**
 * TestModels is
 * @author Tang Li
 * @date 2023/06/05 19:14
 */
class SimpleObj(val name: String, val age: Int)

class ObjWithNumberTypes(
    val byte: Byte,
    val short: Short,
    val int: Int,
    val long: Long,
    val bigInteger: BigInteger,
    val float: Float,
    val double: Double,
    val bigDecimal: BigDecimal,
)

class ObjWithList(val name: String, val list: List<String>)

class ObjWithMap(val name: String, val map: Map<String, SimpleObj>)

class ObjWithObjList(val name: String, val list: List<SimpleObj>)

class ObjWithObjMap(val name: String, val map: Map<String, ObjWithObjList>)

enum class RedisTestIntEnum(
    override val value: Int
) : IntEnumValue {

    @JsonEnumDefaultValue
    ZERO(0),
    ONE(1),
    ;

    companion object : IntEnumCreator(RedisTestIntEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}

enum class RedisTestStringEnum(
    override val value: String
) : StringEnumValue {

    @JsonEnumDefaultValue
    YES("yes"),
    NO("NO"),
    ;

    companion object : StringEnumCreator(RedisTestStringEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String) =
            super.create(value)
    }
}
