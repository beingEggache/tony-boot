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

package tony.test.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.utils.getLogger
import tony.utils.toBigDecimal
import tony.utils.toNumber
import tony.utils.truncToBigDecimal
import tony.utils.truncToString
import tony.utils.formatToPercent
import tony.utils.genRandomInt
import tony.utils.genRandomLong
import tony.utils.secureRandom
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Nums工具类全量单元测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Nums工具类全量测试")
class NumsTest {
    @Nested
    @DisplayName("toBigDecimal相关")
    inner class ToBigDecimal {
        @Test
        @DisplayName("基本类型转BigDecimal")
        fun testBasicToBigDecimal() {
            assertEquals(BigDecimal("1.00"), 1.toBigDecimal())
            assertEquals(BigDecimal("2.00"), 2L.toBigDecimal())
            assertEquals(BigDecimal("3.10"), 3.1f.toBigDecimal())
            assertEquals(BigDecimal("4.20"), 4.2.toBigDecimal())
            assertEquals(BigDecimal("5.00"), BigInteger.valueOf(5).toBigDecimal())
            assertEquals(BigDecimal("6.30"), BigDecimal("6.3").toBigDecimal())
            assertEquals(BigDecimal("7.00"), AtomicInteger(7).toBigDecimal())
            assertEquals(BigDecimal("8.00"), AtomicLong(8).toBigDecimal())
        }
        @Test
        @DisplayName("自定义小数位和舍入模式")
        fun testCustomDecimalAndRounding() {
            assertEquals(BigDecimal("3.14"), 3.14159.toBigDecimal(2, RoundingMode.DOWN))
            assertEquals(BigDecimal("3.142"), 3.14159.toBigDecimal(3, RoundingMode.UP))
            assertEquals(BigDecimal("3.1416"), 3.14159.toBigDecimal(4, RoundingMode.HALF_UP))
        }
        @Test
        @DisplayName("异常情况：null和负小数位")
        fun testToBigDecimalException() {
            assertThrows(IllegalArgumentException::class.java) { (null as Number?).toBigDecimal() }
            assertThrows(IllegalArgumentException::class.java) { 1.toBigDecimal(-1) }
        }
    }

    @Nested
    @DisplayName("toNumber相关")
    inner class ToNumber {
        @Test
        @DisplayName("各种类型互转")
        fun testToNumber() {
            assertEquals(127L, 127.toByte().toNumber(Long::class.java))
            assertEquals(32767, 32767.toShort().toNumber(Int::class.java))
            assertEquals(2147483647.0, 2147483647.toNumber(Double::class.java))
            assertEquals((-1).toByte(), 2147483647.toNumber(Byte::class.java)) // 溢出
            assertEquals(BigInteger.valueOf(123), 123.toNumber(BigInteger::class.java))
            assertEquals(BigDecimal("123.45"), 123.45.toNumber(BigDecimal::class.java))
        }
        @Test
        @DisplayName("不支持类型抛异常")
        fun testToNumberUnsupported() {
            assertThrows(IllegalArgumentException::class.java) { 1.toNumber(String::class.java) }
        }
    }

    @Nested
    @DisplayName("truncToBigDecimal相关")
    inner class TruncToBigDecimal {
        @Test
        @DisplayName("基本截断功能")
        fun testTruncToBigDecimal() {
            assertEquals(BigDecimal("1234.56"), 123456.truncToBigDecimal(2))
            assertEquals(BigDecimal("123.456"), 123456.truncToBigDecimal(3))
            assertEquals(BigDecimal("12.3456"), 123456.truncToBigDecimal(4))
        }
        @Test
        @DisplayName("自定义精度和舍入模式")
        fun testTruncToBigDecimalCustom() {
            assertEquals(BigDecimal("12.34"), 123.456.truncToBigDecimal(1, 2, RoundingMode.DOWN))
            assertEquals(BigDecimal("12.35"), 123.456.truncToBigDecimal(1, 2, RoundingMode.UP))
            assertEquals(BigDecimal("12.35"), 123.456.truncToBigDecimal(1, 2, RoundingMode.HALF_UP))
        }
        @Test
        @DisplayName("边界情况")
        fun testTruncToBigDecimalBoundary() {
            assertEquals(BigDecimal("0.00"), 0.truncToBigDecimal())
            assertEquals(BigDecimal("-1234.56"), (-123456).truncToBigDecimal(2))
        }
    }

    @Nested
    @DisplayName("truncToString相关")
    inner class TruncToString {
        @Test
        @DisplayName("基本功能")
        fun testTruncToString() {
            assertEquals("1234.56", 123456.truncToString(2))
            assertEquals("123.456", 123456.truncToString(3))
        }
        @Test
        @DisplayName("自定义精度和舍入模式")
        fun testTruncToStringCustom() {
            assertEquals("12.34", 123.456.truncToString(1, 2, RoundingMode.DOWN))
            assertEquals("12.35", 123.456.truncToString(1, 2, RoundingMode.UP))
        }
    }

    @Nested
    @DisplayName("formatToPercent相关")
    inner class FormatToPercent {
        @Test
        @DisplayName("基本百分比格式化")
        fun testFormatToPercent() {
            assertEquals("12.34%", 0.1234.formatToPercent())
            assertEquals("12.3%", 0.1234.formatToPercent(1, 1))
            assertEquals("12.340%", 0.1234.formatToPercent(3, 3))
        }
        @Test
        @DisplayName("不同舍入模式")
        fun testFormatToPercentRounding() {
            assertEquals("12.34%", 0.123456.formatToPercent(2, 2, RoundingMode.DOWN))
            assertEquals("12.35%", 0.123456.formatToPercent(2, 2, RoundingMode.UP))
            assertEquals("12.35%", 0.123456.formatToPercent(2, 2, RoundingMode.HALF_UP))
        }
        @Test
        @DisplayName("边界和null情况")
        fun testFormatToPercentBoundary() {
            assertEquals("0.00%", 0.formatToPercent())
            assertEquals("100.00%", 1.formatToPercent())
            assertEquals("-12.34%", (-0.1234).formatToPercent())
            val nullNumber: Number? = null
            assertEquals("0.00%", nullNumber.formatToPercent())
        }
    }

    @Nested
    @DisplayName("secureRandom相关")
    inner class SecureRandomTest {
        @Test
        @DisplayName("secureRandom属性")
        fun testSecureRandom() {
            assertNotNull(secureRandom)
            val randomInt = secureRandom.nextInt(100)
            assertTrue(randomInt in 0 until 100)
            val randomLong = secureRandom.nextLong(1000)
            assertTrue(randomLong in 0 until 1000)
        }
    }

    @Nested
    @DisplayName("genRandomInt相关")
    inner class GenRandomIntTest {
        @Test
        @DisplayName("各位数随机数生成与异常")
        fun testGenRandomInt() {
            assertTrue(genRandomInt(1) in 0..9)
            assertTrue(genRandomInt(2) in 10..99)
            assertTrue(genRandomInt(3) in 100..999)
            assertThrows(IllegalArgumentException::class.java) { genRandomInt(0) }
            assertThrows(IllegalArgumentException::class.java) { genRandomInt(-1) }
        }
        @Test
        @DisplayName("随机性测试")
        fun testGenRandomIntRandomness() {
            val numbers = mutableSetOf<Int>()
            repeat(100) { numbers.add(genRandomInt(3)) }
            assertTrue(numbers.size > 50)
        }
    }

    @Nested
    @DisplayName("genRandomLong相关")
    inner class GenRandomLongTest {
        @Test
        @DisplayName("各位数随机长整数生成与异常")
        fun testGenRandomLong() {
            assertTrue(genRandomLong(1) in 0..9)
            assertTrue(genRandomLong(2) in 10..99)
            assertTrue(genRandomLong(3) in 100..999)
            assertThrows(IllegalArgumentException::class.java) { genRandomLong(0) }
            assertThrows(IllegalArgumentException::class.java) { genRandomLong(-1) }
        }
        @Test
        @DisplayName("随机性测试")
        fun testGenRandomLongRandomness() {
            val numbers = mutableSetOf<Long>()
            repeat(100) { numbers.add(genRandomLong(3)) }
            assertTrue(numbers.size > 50)
        }
    }
}
