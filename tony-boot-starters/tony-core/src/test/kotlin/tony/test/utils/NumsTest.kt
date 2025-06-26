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
 * Nums工具类单元测试
 * 测试数字工具类的各种功能，包括类型转换、格式化、随机数生成等
 *
 * @author tangli
 * @date 2023/12/09 19:07
 * @since 1.0.0
 */
@DisplayName("Nums工具类测试")
object NumsTest {

    private val logger = getLogger()

    @Test
    @DisplayName("测试toBigDecimal方法 - 基本功能")
    fun testToBigDecimal() {
        logger.info("开始测试toBigDecimal方法的基本功能")

        // 测试各种数字类型转BigDecimal
        val byte: Byte = 1
        val short: Short = 2
        val int = 3
        val long = 4L
        val float = 5.1f
        val double = 6.2
        val bigInteger: BigInteger = BigInteger.valueOf(7)
        val bigDecimal: BigDecimal = BigDecimal.valueOf(8.3)
        val atomicInteger = AtomicInteger(9)
        val atomicLong = AtomicLong(10)

        // 验证转换结果
        assertEquals(BigDecimal("1.00"), byte.toBigDecimal())
        assertEquals(BigDecimal("2.00"), short.toBigDecimal())
        assertEquals(BigDecimal("3.00"), int.toBigDecimal())
        assertEquals(BigDecimal("4.00"), long.toBigDecimal())
        assertEquals(BigDecimal("5.10"), float.toBigDecimal())
        assertEquals(BigDecimal("6.20"), double.toBigDecimal())
        assertEquals(BigDecimal("7.00"), bigInteger.toBigDecimal())
        assertEquals(BigDecimal("8.30"), bigDecimal.toBigDecimal())
        assertEquals(BigDecimal("9.00"), atomicInteger.toBigDecimal())
        assertEquals(BigDecimal("10.00"), atomicLong.toBigDecimal())

        logger.info("基本类型转换测试通过")

        // 测试自定义小数位数和舍入模式
        val testNumber = 3.14159
        assertEquals(BigDecimal("3.14"), testNumber.toBigDecimal(2, RoundingMode.DOWN))
        assertEquals(BigDecimal("3.142"), testNumber.toBigDecimal(3, RoundingMode.UP))
        assertEquals(BigDecimal("3.1416"), testNumber.toBigDecimal(4, RoundingMode.HALF_UP))

        logger.info("自定义精度和舍入模式测试通过")
    }

    @Test
    @DisplayName("测试toBigDecimal方法 - 边界情况和异常")
    fun testToBigDecimalBoundaryAndException() {
        logger.info("开始测试toBigDecimal方法的边界情况和异常")

        // 测试null值
        val nil: Number? = null
        val exception = assertThrows(IllegalArgumentException::class.java) {
            nil.toBigDecimal()
        }
        assertEquals("number must not be null", exception.message)
        logger.info("null值异常测试通过: {}", exception.message)

        // 测试负数小数位数
        val negativeDecimalException = assertThrows(IllegalArgumentException::class.java) {
            123.toBigDecimal(-1)
        }
        assertEquals("decimal must >= 0", negativeDecimalException.message)
        logger.info("负数小数位数异常测试通过: {}", negativeDecimalException.message)

        // 测试极大数值
        val maxLong = Long.MAX_VALUE
        val maxLongBigDecimal = maxLong.toBigDecimal()
        assertNotNull(maxLongBigDecimal)
        assertEquals(BigDecimal(Long.MAX_VALUE.toString() + ".00"), maxLongBigDecimal)
        logger.info("极大数值测试通过: {}", maxLongBigDecimal)

        // 测试极小数值
        val minLong = Long.MIN_VALUE
        val minLongBigDecimal = minLong.toBigDecimal()
        assertNotNull(minLongBigDecimal)
        assertEquals(BigDecimal(Long.MIN_VALUE.toString() + ".00"), minLongBigDecimal)
        logger.info("极小数值测试通过: {}", minLongBigDecimal)
    }

    @Test
    @DisplayName("测试toNumber方法 - 数字类型互转")
    fun testToNumber() {
        logger.info("开始测试toNumber方法的数字类型互转")

        // 测试Byte类型转换
        val byte: Byte = 127
        assertEquals(127L, byte.toNumber(Long::class.java))
        assertEquals(127, byte.toNumber(Int::class.java))
        assertEquals(127.0, byte.toNumber(Double::class.java))
        assertEquals(127.toByte(), byte.toNumber(Byte::class.java))
        assertEquals(127.toShort(), byte.toNumber(Short::class.java))
        assertEquals(127.0f, byte.toNumber(Float::class.java))
        assertEquals(BigInteger.valueOf(127), byte.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("127"), byte.toNumber(BigDecimal::class.java))

        logger.info("Byte类型转换测试通过")

        // 测试Short类型转换
        val short: Short = 32767
        assertEquals(32767L, short.toNumber(Long::class.java))
        assertEquals(32767, short.toNumber(Int::class.java))
        assertEquals(32767.0, short.toNumber(Double::class.java))
        // 注意：32767转换为Byte会溢出，得到-1
        assertEquals((-1).toByte(), short.toNumber(Byte::class.java))
        assertEquals(32767.toShort(), short.toNumber(Short::class.java))
        assertEquals(32767.0f, short.toNumber(Float::class.java))
        assertEquals(BigInteger.valueOf(32767), short.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("32767"), short.toNumber(BigDecimal::class.java))

        logger.info("Short类型转换测试通过")

        // 测试Int类型转换
        val int = 2147483647
        assertEquals(2147483647L, int.toNumber(Long::class.java))
        assertEquals(2147483647, int.toNumber(Int::class.java))
        assertEquals(2147483647.0, int.toNumber(Double::class.java))
        // 注意：2147483647转换为Byte会溢出，得到-1
        assertEquals((-1).toByte(), int.toNumber(Byte::class.java))
        // 注意：2147483647转换为Short会溢出，得到-1
        assertEquals((-1).toShort(), int.toNumber(Short::class.java))
        assertEquals(2147483647.0f, int.toNumber(Float::class.java))
        assertEquals(BigInteger.valueOf(2147483647), int.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("2147483647"), int.toNumber(BigDecimal::class.java))

        logger.info("Int类型转换测试通过")

        // 测试Long类型转换
        val long: Long = 9223372036854775807L
        assertEquals(9223372036854775807L, long.toNumber(Long::class.java))
        // 注意：9223372036854775807L转换为Int会溢出，得到-1
        assertEquals(-1, long.toNumber(Int::class.java))
        assertEquals(9223372036854775807.0, long.toNumber(Double::class.java))
        // 注意：9223372036854775807L转换为Byte会溢出，得到-1
        assertEquals((-1).toByte(), long.toNumber(Byte::class.java))
        // 注意：9223372036854775807L转换为Short会溢出，得到-1
        assertEquals((-1).toShort(), long.toNumber(Short::class.java))
        assertEquals(9223372036854775807.0f, long.toNumber(Float::class.java))
        assertEquals(BigInteger.valueOf(9223372036854775807L), long.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("9223372036854775807"), long.toNumber(BigDecimal::class.java))

        logger.info("Long类型转换测试通过")

        // 测试Float类型转换
        val float = 3.14f
        assertEquals(3L, float.toNumber(Long::class.java))
        assertEquals(3, float.toNumber(Int::class.java))
        assertEquals(3.14, float.toNumber(Double::class.java), 0.001)
        assertEquals(3.toByte(), float.toNumber(Byte::class.java))
        assertEquals(3.toShort(), float.toNumber(Short::class.java))
        assertEquals(3.14f, float.toNumber(Float::class.java), 0.001f)
        assertEquals(BigInteger.valueOf(3), float.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("3.14"), float.toNumber(BigDecimal::class.java))

        logger.info("Float类型转换测试通过")

        // 测试Double类型转换
        val double = 3.14159
        assertEquals(3L, double.toNumber(Long::class.java))
        assertEquals(3, double.toNumber(Int::class.java))
        assertEquals(3.14159, double.toNumber(Double::class.java), 0.00001)
        assertEquals(3.toByte(), double.toNumber(Byte::class.java))
        assertEquals(3.toShort(), double.toNumber(Short::class.java))
        assertEquals(3.14159f, double.toNumber(Float::class.java), 0.00001f)
        assertEquals(BigInteger.valueOf(3), double.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("3.14159"), double.toNumber(BigDecimal::class.java))

        logger.info("Double类型转换测试通过")

        // 测试BigDecimal类型转换
        val bigDecimal: BigDecimal = BigDecimal.valueOf(123.456)
        assertEquals(123L, bigDecimal.toNumber(Long::class.java))
        assertEquals(123, bigDecimal.toNumber(Int::class.java))
        assertEquals(123.456, bigDecimal.toNumber(Double::class.java), 0.001)
        assertEquals(123.toByte(), bigDecimal.toNumber(Byte::class.java))
        assertEquals(123.toShort(), bigDecimal.toNumber(Short::class.java))
        assertEquals(123.456f, bigDecimal.toNumber(Float::class.java), 0.001f)
        assertEquals(BigInteger.valueOf(123), bigDecimal.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("123.456"), bigDecimal.toNumber(BigDecimal::class.java))

        logger.info("BigDecimal类型转换测试通过")

        // 测试BigInteger类型转换
        val bigInteger: BigInteger = BigInteger.valueOf(123456789)
        assertEquals(123456789L, bigInteger.toNumber(Long::class.java))
        assertEquals(123456789, bigInteger.toNumber(Int::class.java))
        assertEquals(123456789.0, bigInteger.toNumber(Double::class.java), 0.001)
        assertEquals(21.toByte(), bigInteger.toNumber(Byte::class.java))
        // 注意：123456789转换为Short会溢出，得到-13035
        assertEquals((-13035).toShort(), bigInteger.toNumber(Short::class.java))
        assertEquals(123456789.0f, bigInteger.toNumber(Float::class.java), 0.001f)
        assertEquals(BigInteger.valueOf(123456789), bigInteger.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("123456789"), bigInteger.toNumber(BigDecimal::class.java))

        logger.info("BigInteger类型转换测试通过")
    }

    @Test
    @DisplayName("测试toNumber方法 - 字符串转数字")
    fun testStringToNumber() {
        logger.info("开始测试toNumber方法的字符串转数字功能")

        // 测试整数字符串
        val intString = "123"
        assertEquals(123L, intString.toNumber(Long::class.java))
        assertEquals(123, intString.toNumber(Int::class.java))
        assertEquals(123.0, intString.toNumber(Double::class.java), 0.001)
        assertEquals(123.toByte(), intString.toNumber(Byte::class.java))
        assertEquals(123.toShort(), intString.toNumber(Short::class.java))
        assertEquals(123.0f, intString.toNumber(Float::class.java), 0.001f)
        assertEquals(BigInteger.valueOf(123), intString.toNumber(BigInteger::class.java))
        assertEquals(BigDecimal("123"), intString.toNumber(BigDecimal::class.java))

        logger.info("整数字符串转换测试通过")

        // 测试小数字符串 - 注意：小数字符串无法转换为Long和Int
        val doubleString = "123.456"
        // 小数字符串转换为Long会抛出异常
        assertThrows(NumberFormatException::class.java) {
            doubleString.toNumber(Long::class.java)
        }
        // 小数字符串转换为Int会抛出异常
        assertThrows(NumberFormatException::class.java) {
            doubleString.toNumber(Int::class.java)
        }
        assertEquals(123.456, doubleString.toNumber(Double::class.java), 0.001)
        // 小数字符串转换为Byte会抛出异常
        assertThrows(NumberFormatException::class.java) {
            doubleString.toNumber(Byte::class.java)
        }
        // 小数字符串转换为Short会抛出异常
        assertThrows(NumberFormatException::class.java) {
            doubleString.toNumber(Short::class.java)
        }
        assertEquals(123.456f, doubleString.toNumber(Float::class.java), 0.001f)
        // 小数字符串转换为BigInteger会抛出异常
        assertThrows(NumberFormatException::class.java) {
            doubleString.toNumber(BigInteger::class.java)
        }
        assertEquals(BigDecimal("123.456"), doubleString.toNumber(BigDecimal::class.java))

        logger.info("小数字符串转换测试通过")

        // 测试边界值
        val maxByteString = "127"
        assertEquals(127.toByte(), maxByteString.toNumber(Byte::class.java))

        val maxShortString = "32767"
        assertEquals(32767.toShort(), maxShortString.toNumber(Short::class.java))

        val maxIntString = "2147483647"
        assertEquals(2147483647, maxIntString.toNumber(Int::class.java))

        val maxLongString = "9223372036854775807"
        assertEquals(9223372036854775807L, maxLongString.toNumber(Long::class.java))

        logger.info("边界值转换测试通过")
    }

    @Test
    @DisplayName("测试toNumber方法 - 异常情况")
    fun testToNumberException() {
        logger.info("开始测试toNumber方法的异常情况")

        // 测试不支持的类型
        val unsupportedException = assertThrows(IllegalArgumentException::class.java) {
            123.toNumber(String::class.java)
        }
        assertEquals("Not support input type: class java.lang.String", unsupportedException.message)
        logger.info("不支持类型异常测试通过: {}", unsupportedException.message)

        // 测试无效字符串
        val invalidString = "abc"
        assertThrows(NumberFormatException::class.java) {
            invalidString.toNumber(Int::class.java)
        }
        logger.info("无效字符串异常测试通过")

        // 测试空字符串
        val emptyString = ""
        assertThrows(NumberFormatException::class.java) {
            emptyString.toNumber(Int::class.java)
        }
        logger.info("空字符串异常测试通过")

        // 测试null字符串
        val nullString: String? = null
        assertThrows(NullPointerException::class.java) {
            nullString!!.toNumber(Int::class.java)
        }
        logger.info("null字符串异常测试通过")
    }

    @Test
    @DisplayName("测试truncToBigDecimal方法")
    fun testTruncToBigDecimal() {
        logger.info("开始测试truncToBigDecimal方法")

        // 测试基本功能 - digit参数表示除以10的digit次方
        val number = 123456
        assertEquals(BigDecimal("1234.56"), number.truncToBigDecimal(2))
        assertEquals(BigDecimal("123.456"), number.truncToBigDecimal(3))
        assertEquals(BigDecimal("12.3456"), number.truncToBigDecimal(4))

        logger.info("基本截断功能测试通过")

        // 测试自定义精度和舍入模式
        val decimalNumber = 123.456789
        // digit=1表示除以10，decimal=2表示保留2位小数
        assertEquals(BigDecimal("12.34"), decimalNumber.truncToBigDecimal(1, 2, RoundingMode.DOWN))
        assertEquals(BigDecimal("12.35"), decimalNumber.truncToBigDecimal(1, 2, RoundingMode.UP))
        assertEquals(BigDecimal("12.35"), decimalNumber.truncToBigDecimal(1, 2, RoundingMode.HALF_UP))

        logger.info("自定义精度和舍入模式测试通过")

        // 测试边界情况
        val zero = 0
        assertEquals(BigDecimal("0.00"), zero.truncToBigDecimal())

        val negative = -123456
        assertEquals(BigDecimal("-1234.56"), negative.truncToBigDecimal(2))

        logger.info("边界情况测试通过")
    }

    @Test
    @DisplayName("测试truncToString方法")
    fun testTruncToString() {
        logger.info("开始测试truncToString方法")

        // 测试基本功能 - digit参数表示除以10的digit次方
        val number = 123456
        assertEquals("1234.56", number.truncToString(2))
        assertEquals("123.456", number.truncToString(3))
        assertEquals("12.3456", number.truncToString(4))

        logger.info("基本截断转字符串功能测试通过")

        // 测试自定义精度和舍入模式
        val decimalNumber = 123.456789
        // digit=1表示除以10，decimal=2表示保留2位小数
        assertEquals("12.34", decimalNumber.truncToString(1, 2, RoundingMode.DOWN))
        assertEquals("12.35", decimalNumber.truncToString(1, 2, RoundingMode.UP))
        assertEquals("12.35", decimalNumber.truncToString(1, 2, RoundingMode.HALF_UP))

        logger.info("自定义精度和舍入模式测试通过")

        // 测试边界情况
        val zero = 0
        assertEquals("0.00", zero.truncToString())

        val negative = -123456
        assertEquals("-1234.56", negative.truncToString(2))

        logger.info("边界情况测试通过")
    }

    @Test
    @DisplayName("测试formatToPercent方法")
    fun testFormatToPercent() {
        logger.info("开始测试formatToPercent方法")

        // 测试基本功能
        val number = 0.1234
        assertEquals("12.34%", number.formatToPercent())
        assertEquals("12.3%", number.formatToPercent(1, 1))
        assertEquals("12.340%", number.formatToPercent(3, 3))

        logger.info("基本百分比格式化测试通过")

        // 测试不同舍入模式
        val decimalNumber = 0.123456
        assertEquals("12.34%", decimalNumber.formatToPercent(2, 2, RoundingMode.DOWN))
        assertEquals("12.35%", decimalNumber.formatToPercent(2, 2, RoundingMode.UP))
        assertEquals("12.35%", decimalNumber.formatToPercent(2, 2, RoundingMode.HALF_UP))

        logger.info("不同舍入模式测试通过")

        // 测试边界情况
        val zero = 0
        assertEquals("0.00%", zero.formatToPercent())

        val one = 1
        assertEquals("100.00%", one.formatToPercent())

        val negative = -0.1234
        assertEquals("-12.34%", negative.formatToPercent())

        logger.info("边界情况测试通过")

        // 测试null值
        val nullNumber: Number? = null
        assertEquals("0.00%", nullNumber.formatToPercent())
        logger.info("null值测试通过")
    }

    @Test
    @DisplayName("测试secureRandom属性")
    fun testSecureRandom() {
        logger.info("开始测试secureRandom属性")

        // 验证secureRandom不为null
        assertNotNull(secureRandom)
        logger.info("secureRandom不为null测试通过")

        // 验证生成随机数的范围
        val randomInt = secureRandom.nextInt(100)
        assertTrue(randomInt >= 0 && randomInt < 100)
        logger.info("随机数范围测试通过: {}", randomInt)

        val randomLong = secureRandom.nextLong(1000)
        assertTrue(randomLong >= 0 && randomLong < 1000)
        logger.info("随机长整数范围测试通过: {}", randomLong)
    }

    @Test
    @DisplayName("测试genRandomInt方法")
    fun testGenRandomInt() {
        logger.info("开始测试genRandomInt方法")

        // 测试1位数随机数
        val oneDigit = genRandomInt(1)
        assertTrue(oneDigit >= 0 && oneDigit < 10)
        logger.info("1位数随机数测试通过: {}", oneDigit)

        // 测试2位数随机数
        val twoDigit = genRandomInt(2)
        assertTrue(twoDigit >= 10 && twoDigit < 100)
        logger.info("2位数随机数测试通过: {}", twoDigit)

        // 测试3位数随机数
        val threeDigit = genRandomInt(3)
        assertTrue(threeDigit >= 100 && threeDigit < 1000)
        logger.info("3位数随机数测试通过: {}", threeDigit)

        // 测试0位数（应该抛出异常）
        val exception = assertThrows(IllegalArgumentException::class.java) {
            genRandomInt(0)
        }
        assertEquals("digit must be positive.", exception.message)
        logger.info("0位数异常测试通过: {}", exception.message)

        // 测试负数位数（应该抛出异常）
        val negativeException = assertThrows(IllegalArgumentException::class.java) {
            genRandomInt(-1)
        }
        assertEquals("digit must be positive.", negativeException.message)
        logger.info("负数位数异常测试通过: {}", negativeException.message)

        // 测试多次生成确保随机性
        val numbers = mutableSetOf<Int>()
        repeat(100) {
            numbers.add(genRandomInt(3))
        }
        assertTrue(numbers.size > 50) // 至少应该有50个不同的数字
        logger.info("随机性测试通过，生成了{}个不同的3位数", numbers.size)
    }

    @Test
    @DisplayName("测试genRandomLong方法")
    fun testGenRandomLong() {
        logger.info("开始测试genRandomLong方法")

        // 测试1位数随机数
        val oneDigit = genRandomLong(1)
        assertTrue(oneDigit >= 0 && oneDigit < 10)
        logger.info("1位数随机长整数测试通过: {}", oneDigit)

        // 测试2位数随机数
        val twoDigit = genRandomLong(2)
        assertTrue(twoDigit >= 10 && twoDigit < 100)
        logger.info("2位数随机长整数测试通过: {}", twoDigit)

        // 测试3位数随机数
        val threeDigit = genRandomLong(3)
        assertTrue(threeDigit >= 100 && threeDigit < 1000)
        logger.info("3位数随机长整数测试通过: {}", threeDigit)

        // 测试0位数（应该抛出异常）
        val exception = assertThrows(IllegalArgumentException::class.java) {
            genRandomLong(0)
        }
        assertEquals("digit must be positive.", exception.message)
        logger.info("0位数异常测试通过: {}", exception.message)

        // 测试负数位数（应该抛出异常）
        val negativeException = assertThrows(IllegalArgumentException::class.java) {
            genRandomLong(-1)
        }
        assertEquals("digit must be positive.", negativeException.message)
        logger.info("负数位数异常测试通过: {}", negativeException.message)

        // 测试多次生成确保随机性
        val numbers = mutableSetOf<Long>()
        repeat(100) {
            numbers.add(genRandomLong(3))
        }
        assertTrue(numbers.size > 50) // 至少应该有50个不同的数字
        logger.info("随机性测试通过，生成了{}个不同的3位数", numbers.size)
    }

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("开始性能测试")

        val iterations = 10000

        // 测试toBigDecimal性能
        val startTime1 = System.currentTimeMillis()
        repeat(iterations) {
            123.456.toBigDecimal()
        }
        val endTime1 = System.currentTimeMillis()
        val duration1 = endTime1 - startTime1
        logger.info("toBigDecimal性能测试: {}次操作耗时{}ms", iterations, duration1)
        assertTrue(duration1 < 1000) // 应该在1秒内完成

        // 测试toNumber性能
        val startTime2 = System.currentTimeMillis()
        repeat(iterations) {
            123.toNumber(Long::class.java)
        }
        val endTime2 = System.currentTimeMillis()
        val duration2 = endTime2 - startTime2
        logger.info("toNumber性能测试: {}次操作耗时{}ms", iterations, duration2)
        assertTrue(duration2 < 1000) // 应该在1秒内完成

        // 测试随机数生成性能
        val startTime3 = System.currentTimeMillis()
        repeat(iterations) {
            genRandomInt(3)
        }
        val endTime3 = System.currentTimeMillis()
        val duration3 = endTime3 - startTime3
        logger.info("随机数生成性能测试: {}次操作耗时{}ms", iterations, duration3)
        assertTrue(duration3 < 2000) // 应该在2秒内完成

        logger.info("性能测试通过")
    }

    @Test
    @DisplayName("并发安全测试")
    fun testConcurrency() {
        logger.info("开始并发安全测试")

        val threadCount = 10
        val operationsPerThread = 1000
        val executor = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)
        val results = mutableListOf<BigDecimal>()
        val exceptions = mutableListOf<Exception>()

        try {
            // 并发测试toBigDecimal
            repeat(threadCount) { threadId ->
                executor.submit {
                    try {
                        repeat(operationsPerThread) { operationId ->
                            val number = threadId * 1000 + operationId
                            val result = number.toBigDecimal()
                            synchronized(results) {
                                results.add(result)
                            }
                        }
                    } catch (e: Exception) {
                        synchronized(exceptions) {
                            exceptions.add(e)
                        }
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await(10, TimeUnit.SECONDS)

            // 验证结果
            assertEquals(threadCount * operationsPerThread, results.size)
            assertTrue(exceptions.isEmpty())
            logger.info("并发安全测试通过，共执行{}次操作，无异常", results.size)

            // 并发测试随机数生成
            val randomResults = mutableListOf<Int>()
            val randomLatch = CountDownLatch(threadCount)
            val randomExceptions = mutableListOf<Exception>()

            repeat(threadCount) {
                executor.submit {
                    try {
                        repeat(operationsPerThread) {
                            val result = genRandomInt(3)
                            synchronized(randomResults) {
                                randomResults.add(result)
                            }
                        }
                    } catch (e: Exception) {
                        synchronized(randomExceptions) {
                            randomExceptions.add(e)
                        }
                    } finally {
                        randomLatch.countDown()
                    }
                }
            }

            randomLatch.await(10, TimeUnit.SECONDS)

            // 验证随机数结果
            assertEquals(threadCount * operationsPerThread, randomResults.size)
            assertTrue(randomExceptions.isEmpty())

            // 验证随机数的范围
            randomResults.forEach { number ->
                assertTrue(number >= 100 && number < 1000)
            }

            logger.info("随机数并发安全测试通过，共生成{}个随机数", randomResults.size)
        } finally {
            executor.shutdown()
        }
    }
}
