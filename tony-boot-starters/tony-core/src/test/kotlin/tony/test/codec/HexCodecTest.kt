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

package tony.test.codec

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.slf4j.LoggerFactory
import tony.codec.HexCodec

/**
 * HexCodec 单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object HexCodecTest {

    private val logger = LoggerFactory.getLogger(HexCodecTest::class.java)

    /**
     * 测试 HexCodec 实例
     */
    @Test
    fun testHexCodecInstance() {
        logger.info("开始测试 HexCodec 实例")

        val hexCodec = HexCodec
        assertNotNull(hexCodec, "HexCodec 实例不应为空")
        logger.info("HexCodec 实例测试通过")
    }

    /**
     * 测试十六进制编码解码
     */
    @Test
    fun testHexEncodeDecode() {
        logger.info("开始测试十六进制编码解码")

        val testString = "Hello, World!"
        val testBytes = testString.toByteArray(Charsets.UTF_8)

        // 测试字符串编码解码
        val encodedString = HexCodec.encodeToString(testString)
        val decodedString = HexCodec.decodeToString(encodedString)
        assertEquals(testString, decodedString, "字符串十六进制编码解码应该一致")
        logger.info("字符串十六进制编码解码测试通过: $testString -> $encodedString -> $decodedString")

        // 测试字节数组编码解码
        val encodedBytes = HexCodec.encodeToString(testBytes)
        val decodedBytes = HexCodec.decodeToByteArray(encodedBytes)
        assertTrue(testBytes.contentEquals(decodedBytes), "字节数组十六进制编码解码应该一致")
        logger.info("字节数组十六进制编码解码测试通过")
    }

    /**
     * 测试十六进制格式验证
     */
    @Test
    fun testHexFormatValidation() {
        logger.info("开始测试十六进制格式验证")

        val testString = "Hello, World!"
        val hexEncoded = HexCodec.encodeToString(testString)

        // 验证十六进制字符串只包含十六进制字符
        val hexChars = "0123456789ABCDEFabcdef"
        val isValidHex = hexEncoded.all { it in hexChars }
        assertTrue(isValidHex, "十六进制编码应该只包含十六进制字符")
        logger.info("十六进制格式验证通过: $hexEncoded")

        // 验证十六进制字符串长度为偶数
        assertTrue(hexEncoded.length % 2 == 0, "十六进制字符串长度应该为偶数")
        logger.info("十六进制字符串长度验证通过: ${hexEncoded.length}")
    }

    /**
     * 测试空字符串和空字节数组
     */
    @Test
    fun testEmptyInputs() {
        logger.info("开始测试空输入")

        // 测试空字符串
        val emptyString = ""
        val emptyStringEncoded = HexCodec.encodeToString(emptyString)
        val emptyStringDecoded = HexCodec.decodeToString(emptyStringEncoded)
        assertEquals(emptyString, emptyStringDecoded, "空字符串十六进制编码解码应该一致")

        // 测试空字节数组
        val emptyBytes = ByteArray(0)
        val emptyBytesEncoded = HexCodec.encodeToString(emptyBytes)
        val emptyBytesDecoded = HexCodec.decodeToByteArray(emptyBytesEncoded)
        assertTrue(emptyBytes.contentEquals(emptyBytesDecoded), "空字节数组十六进制编码解码应该一致")

        logger.info("空输入测试通过")
    }

    /**
     * 测试特殊字符
     */
    @Test
    fun testSpecialCharacters() {
        logger.info("开始测试特殊字符")

        val specialString = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~"

        val hexEncoded = HexCodec.encodeToString(specialString)
        val hexDecoded = HexCodec.decodeToString(hexEncoded)
        assertEquals(specialString, hexDecoded, "特殊字符十六进制编码解码应该一致")

        logger.info("特殊字符测试通过: $specialString -> $hexEncoded -> $hexDecoded")
    }

    /**
     * 测试中文字符
     */
    @Test
    fun testChineseCharacters() {
        logger.info("开始测试中文字符")

        val chineseString = "中文测试：你好世界！"

        val hexEncoded = HexCodec.encodeToString(chineseString)
        val hexDecoded = HexCodec.decodeToString(hexEncoded)
        assertEquals(chineseString, hexDecoded, "中文字符十六进制编码解码应该一致")

        logger.info("中文字符测试通过: $chineseString -> $hexEncoded -> $hexDecoded")
    }

    /**
     * 测试大字符串
     */
    @Test
    fun testLargeString() {
        logger.info("开始测试大字符串")

        val largeString = "A".repeat(10000)

        val hexEncoded = HexCodec.encodeToString(largeString)
        val hexDecoded = HexCodec.decodeToString(hexEncoded)
        assertEquals(largeString, hexDecoded, "大字符串十六进制编码解码应该一致")

        logger.info("大字符串测试通过，长度: ${largeString.length}")
    }

    /**
     * 测试性能
     */
    @Test
    fun testPerformance() {
        logger.info("开始性能测试")

        val testString = "Performance test data"
        val iterations = 1000

        val startTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = HexCodec.encodeToString(testString)
            HexCodec.decodeToString(encoded)
        }
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        logger.info("十六进制性能测试完成，${iterations}次迭代耗时: ${duration}ms")

        assertTrue(duration > 0, "十六进制性能测试应该完成")
    }

    /**
     * 测试边界情况
     */
    @Test
    fun testBoundaryCases() {
        logger.info("开始测试边界情况")

        // 测试单个字符
        val singleChar = "A"
        val singleCharEncoded = HexCodec.encodeToString(singleChar)
        val singleCharDecoded = HexCodec.decodeToString(singleCharEncoded)
        assertEquals(singleChar, singleCharDecoded, "单个字符十六进制编码解码应该一致")

        // 测试两个字符
        val twoChars = "AB"
        val twoCharsEncoded = HexCodec.encodeToString(twoChars)
        val twoCharsDecoded = HexCodec.decodeToString(twoCharsEncoded)
        assertEquals(twoChars, twoCharsDecoded, "两个字符十六进制编码解码应该一致")

        // 测试三个字符
        val threeChars = "ABC"
        val threeCharsEncoded = HexCodec.encodeToString(threeChars)
        val threeCharsDecoded = HexCodec.decodeToString(threeCharsEncoded)
        assertEquals(threeChars, threeCharsDecoded, "三个字符十六进制编码解码应该一致")

        logger.info("边界情况测试通过")
    }

    /**
     * 测试字节数组边界情况
     */
    @Test
    fun testByteArrayBoundaryCases() {
        logger.info("开始测试字节数组边界情况")

        // 测试单个字节
        val singleByte = byteArrayOf(0x41) // 'A'
        val singleByteEncoded = HexCodec.encodeToString(singleByte)
        val singleByteDecoded = HexCodec.decodeToByteArray(singleByteEncoded)
        assertTrue(singleByte.contentEquals(singleByteDecoded), "单个字节十六进制编码解码应该一致")

        // 测试两个字节
        val twoBytes = byteArrayOf(0x41, 0x42) // 'AB'
        val twoBytesEncoded = HexCodec.encodeToString(twoBytes)
        val twoBytesDecoded = HexCodec.decodeToByteArray(twoBytesEncoded)
        assertTrue(twoBytes.contentEquals(twoBytesDecoded), "两个字节十六进制编码解码应该一致")

        // 测试三个字节
        val threeBytes = byteArrayOf(0x41, 0x42, 0x43) // 'ABC'
        val threeBytesEncoded = HexCodec.encodeToString(threeBytes)
        val threeBytesDecoded = HexCodec.decodeToByteArray(threeBytesEncoded)
        assertTrue(threeBytes.contentEquals(threeBytesDecoded), "三个字节十六进制编码解码应该一致")

        logger.info("字节数组边界情况测试通过")
    }

    /**
     * 测试十六进制字符串长度
     */
    @Test
    fun testHexStringLength() {
        logger.info("开始测试十六进制字符串长度")

        val testCases = listOf(
            "A" to 2,
            "AB" to 4,
            "ABC" to 6,
            "ABCD" to 8,
            "Hello" to 10,
            "Hello, World!" to 26
        )

        for ((input, expectedLength) in testCases) {
            val hexEncoded = HexCodec.encodeToString(input)
            assertEquals(expectedLength, hexEncoded.length, "输入 '$input' 的十六进制编码长度应该为 $expectedLength")
            logger.info("输入 '$input' 的十六进制编码长度验证通过: ${hexEncoded.length}")
        }

        logger.info("十六进制字符串长度测试通过")
    }

    /**
     * 测试字节数组到十六进制的转换
     */
    @Test
    fun testByteArrayToHexConversion() {
        logger.info("开始测试字节数组到十六进制的转换")

        // 测试各种字节值
        val testBytes = byteArrayOf(0x00, 0x01, 0x0F, 0x10, 0x7F)
        val hexEncoded = HexCodec.encodeToString(testBytes)
        val hexDecoded = HexCodec.decodeToByteArray(hexEncoded)

        assertTrue(testBytes.contentEquals(hexDecoded), "字节数组到十六进制转换应该一致")
        assertEquals("00010f107f", hexEncoded.lowercase(), "十六进制编码应该正确")

        logger.info("字节数组到十六进制转换测试通过: ${testBytes.contentToString()} -> $hexEncoded")
    }
}
