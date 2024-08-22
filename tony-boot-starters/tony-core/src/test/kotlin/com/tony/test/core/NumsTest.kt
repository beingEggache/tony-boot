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

package com.tony.test.core

import com.tony.utils.getLogger
import com.tony.utils.toBigDecimal
import com.tony.utils.toNumber
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * NumsTest is
 * @author tangli
 * @date 2023/12/09 19:07
 * @since 1.0.0
 */
object NumsTest {

    val logger = getLogger()

    @Test
    fun testToBigDecimal() {
        val byte: Byte = 1
        val short: Short = 2
        val int = 3
        val long = 3L
        val float = 4.1f
        val double = 5.2
        val bigInteger: BigInteger = BigInteger.valueOf(6)
        val bigDecimal: BigDecimal = BigDecimal.valueOf(7.3)
        val atomicInteger = AtomicInteger(8)
        val atomicLong = AtomicLong(9)

        val nil: Number? = null

        logger.info("${byte.toBigDecimal()}")
        logger.info("${short.toBigDecimal()}")
        logger.info("${int.toBigDecimal()}")
        logger.info("${long.toBigDecimal()}")
        logger.info("${float.toBigDecimal()}")
        logger.info("${double.toBigDecimal()}")
        logger.info("${bigInteger.toBigDecimal()}")
        logger.info("${bigDecimal.toBigDecimal()}")
        logger.info("${atomicInteger.toBigDecimal()}")
        logger.info("${atomicLong.toBigDecimal()}")

        try{
            nil.toBigDecimal()
        } catch (e: IllegalArgumentException){
            logger.error(e.message)
        }
    }


    @Test
    fun testNumberToNumber() {
        val byte: Byte = 1
        logger.info("byte to short: ${byte.toNumber(Short::class.java)}")
        logger.info("byte to int: ${byte.toNumber(Int::class.java)}")
        logger.info("byte to long: ${byte.toNumber(Long::class.java)}")
        logger.info("byte to float: ${byte.toNumber(Float::class.java)}")
        logger.info("byte to double: ${byte.toNumber(Double::class.java)}")
        logger.info("byte to BigDecimal: ${byte.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("byte to BigInteger: ${byte.toNumber(java.math.BigInteger::class.java)}")

        val short: Short = 1
        logger.info("short to byte: ${short.toNumber(Byte::class.java)}")
        logger.info("short to int: ${short.toNumber(Int::class.java)}")
        logger.info("short to long: ${short.toNumber(Long::class.java)}")
        logger.info("short to float: ${short.toNumber(Float::class.java)}")
        logger.info("short to double: ${short.toNumber(Double::class.java)}")
        logger.info("short to BigDecimal: ${short.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("short to BigInteger: ${short.toNumber(java.math.BigInteger::class.java)}")

        val int = 1
        logger.info("int to byte: ${int.toNumber(Byte::class.java)}")
        logger.info("int to short: ${int.toNumber(Short::class.java)}")
        logger.info("int to long: ${int.toNumber(Long::class.java)}")
        logger.info("int to float: ${int.toNumber(Float::class.java)}")
        logger.info("int to double: ${int.toNumber(Double::class.java)}")
        logger.info("int to BigDecimal: ${int.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("int to BigInteger: ${int.toNumber(java.math.BigInteger::class.java)}")

        val long: Long = 1
        logger.info("long to byte: ${long.toNumber(Byte::class.java)}")
        logger.info("long to short: ${long.toNumber(Short::class.java)}")
        logger.info("long to int: ${long.toNumber(Int::class.java)}")
        logger.info("long to float: ${long.toNumber(Float::class.java)}")
        logger.info("long to double: ${long.toNumber(Double::class.java)}")
        logger.info("long to BigDecimal: ${long.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("long to BigInteger: ${long.toNumber(java.math.BigInteger::class.java)}")

        val float = 1.0f
        logger.info("float to byte: ${float.toNumber(Byte::class.java)}")
        logger.info("float to short: ${float.toNumber(Short::class.java)}")
        logger.info("float to int: ${float.toNumber(Int::class.java)}")
        logger.info("float to long: ${float.toNumber(Long::class.java)}")
        logger.info("float to double: ${float.toNumber(Double::class.java)}")
        logger.info("float to BigDecimal: ${float.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("float to BigInteger: ${float.toNumber(java.math.BigInteger::class.java)}")

        val double = 1.0
        logger.info("double to byte: ${double.toNumber(Byte::class.java)}")
        logger.info("double to short: ${double.toNumber(Short::class.java)}")
        logger.info("double to int: ${double.toNumber(Int::class.java)}")
        logger.info("double to long: ${double.toNumber(Long::class.java)}")
        logger.info("double to float: ${double.toNumber(Float::class.java)}")
        logger.info("double to BigDecimal: ${double.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("double to BigInteger: ${double.toNumber(java.math.BigInteger::class.java)}")

        val bigDecimal: java.math.BigDecimal = java.math.BigDecimal.valueOf(1.0)
        logger.info("bigDecimal to byte: ${bigDecimal.toNumber(Byte::class.java)}")
        logger.info("bigDecimal to short: ${bigDecimal.toNumber(Short::class.java)}")
        logger.info("bigDecimal to int: ${bigDecimal.toNumber(Int::class.java)}")
        logger.info("bigDecimal to long: ${bigDecimal.toNumber(Long::class.java)}")
        logger.info("bigDecimal to float: ${bigDecimal.toNumber(Float::class.java)}")
        logger.info("bigDecimal to BigDecimal: ${bigDecimal.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("bigDecimal to BigInteger: ${bigDecimal.toNumber(java.math.BigInteger::class.java)}")

        val bigInteger: java.math.BigInteger = java.math.BigInteger.valueOf(1)
        logger.info("bigInteger to byte: ${bigInteger.toNumber(Byte::class.java)}")
        logger.info("bigInteger to short: ${bigInteger.toNumber(Short::class.java)}")
        logger.info("bigInteger to int: ${bigInteger.toNumber(Int::class.java)}")
        logger.info("bigInteger to long: ${bigInteger.toNumber(Long::class.java)}")
        logger.info("bigInteger to float: ${bigInteger.toNumber(Float::class.java)}")
        logger.info("bigInteger to BigDecimal: ${bigInteger.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("bigInteger to BigInteger: ${bigInteger.toNumber(java.math.BigInteger::class.java)}")
    }

    @Test
    fun testStringToNumber() {
        val byteString = "123"
        logger.info("byteString to byte: ${byteString.toNumber(Byte::class.java)}")
        logger.info("byteString to short: ${byteString.toNumber(Short::class.java)}")
        logger.info("byteString to int: ${byteString.toNumber(Int::class.java)}")
        logger.info("byteString to long: ${byteString.toNumber(Long::class.java)}")
        logger.info("byteString to float: ${byteString.toNumber(Float::class.java)}")
        logger.info("byteString to double: ${byteString.toNumber(Double::class.java)}")
        logger.info("byteString to BigDecimal: ${byteString.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("byteString to BigInteger: ${byteString.toNumber(java.math.BigInteger::class.java)}")

        val shortString = "123"
        logger.info("shortString to byte: ${shortString.toNumber(Byte::class.java)}")
        logger.info("shortString to short: ${shortString.toNumber(Short::class.java)}")
        logger.info("shortString to int: ${shortString.toNumber(Int::class.java)}")
        logger.info("shortString to long: ${shortString.toNumber(Long::class.java)}")
        logger.info("shortString to float: ${shortString.toNumber(Float::class.java)}")
        logger.info("shortString to double: ${shortString.toNumber(Double::class.java)}")
        logger.info("shortString to BigDecimal: ${shortString.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("shortString to BigInteger: ${shortString.toNumber(java.math.BigInteger::class.java)}")

        val intString = "127"
        logger.info("intString to byte: ${intString.toNumber(Byte::class.java)}")
        logger.info("intString to short: ${intString.toNumber(Short::class.java)}")
        logger.info("intString to int: ${intString.toNumber(Int::class.java)}")
        logger.info("intString to long: ${intString.toNumber(Long::class.java)}")
        logger.info("intString to float: ${intString.toNumber(Float::class.java)}")
        logger.info("intString to double: ${intString.toNumber(Double::class.java)}")
        logger.info("intString to BigDecimal: ${intString.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("intString to BigInteger: ${intString.toNumber(java.math.BigInteger::class.java)}")


        val longString = "127"
        logger.info("longString to byte: ${longString.toNumber(Byte::class.java)}")
        logger.info("longString to short: ${longString.toNumber(Short::class.java)}")
        logger.info("longString to int: ${longString.toNumber(Int::class.java)}")
        logger.info("longString to long: ${longString.toNumber(Long::class.java)}")
        logger.info("longString to float: ${longString.toNumber(Float::class.java)}")
        logger.info("longString to double: ${longString.toNumber(Double::class.java)}")
        logger.info("longString to BigDecimal: ${longString.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("longString to BigInteger: ${longString.toNumber(java.math.BigInteger::class.java)}")


        val floatString = "123.456"
        logger.info("floatString to float: ${floatString.toNumber(Float::class.java)}")
        logger.info("floatString to double: ${floatString.toNumber(Double::class.java)}")
        logger.info("floatString to BigDecimal: ${floatString.toNumber(java.math.BigDecimal::class.java)}")

        val doubleString = "123.456"
        logger.info("doubleString to float: ${doubleString.toNumber(Float::class.java)}")
        logger.info("doubleString to double: ${doubleString.toNumber(Double::class.java)}")
        logger.info("doubleString to BigDecimal: ${doubleString.toNumber(java.math.BigDecimal::class.java)}")

        val bigDecimalString = "127"
        logger.info("bigDecimalString to byte: ${bigDecimalString.toNumber(Byte::class.java)}")
        logger.info("bigDecimalString to short: ${bigDecimalString.toNumber(Short::class.java)}")
        logger.info("bigDecimalString to int: ${bigDecimalString.toNumber(Int::class.java)}")
        logger.info("bigDecimalString to long: ${bigDecimalString.toNumber(Long::class.java)}")
        logger.info("bigDecimalString to float: ${bigDecimalString.toNumber(Float::class.java)}")
        logger.info("bigDecimalString to double: ${bigDecimalString.toNumber(Double::class.java)}")
        logger.info("bigDecimalString to BigDecimal: ${bigDecimalString.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("bigDecimalString to BigInteger: ${bigDecimalString.toNumber(java.math.BigInteger::class.java)}")

        val bigIntegerString = "127"
        logger.info("bigIntegerString to byte: ${bigIntegerString.toNumber(Byte::class.java)}")
        logger.info("bigIntegerString to short: ${bigIntegerString.toNumber(Short::class.java)}")
        logger.info("bigIntegerString to int: ${bigIntegerString.toNumber(Int::class.java)}")
        logger.info("bigIntegerString to long: ${bigIntegerString.toNumber(Long::class.java)}")
        logger.info("bigIntegerString to float: ${bigIntegerString.toNumber(Float::class.java)}")
        logger.info("bigIntegerString to double: ${bigIntegerString.toNumber(Double::class.java)}")
        logger.info("bigIntegerString to BigDecimal: ${bigIntegerString.toNumber(java.math.BigDecimal::class.java)}")
        logger.info("bigIntegerString to BigInteger: ${bigIntegerString.toNumber(java.math.BigInteger::class.java)}")
    }
}
