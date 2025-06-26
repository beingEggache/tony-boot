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
import tony.codec.Base64Codec
import tony.codec.Codec
import tony.codec.HexCodec

/**
 * Codec 接口单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object CodecTest {

    private val logger = LoggerFactory.getLogger(CodecTest::class.java)

    /**
     * 测试编解码器实例
     */
    @Test
    fun testCodecInstances() {
        logger.info("开始测试编解码器实例")

        // 测试 Base64Codec 实例
        val base64Codec = Base64Codec
        assertNotNull(base64Codec, "Base64Codec 实例不应为空")
        assertTrue(base64Codec is Codec, "Base64Codec 应该实现 Codec 接口")
        logger.info("Base64Codec 实例测试通过")

        // 测试 HexCodec 实例
        val hexCodec = HexCodec
        assertNotNull(hexCodec, "HexCodec 实例不应为空")
        assertTrue(hexCodec is Codec, "HexCodec 应该实现 Codec 接口")
        logger.info("HexCodec 实例测试通过")
    }

    /**
     * 测试字符串编码解码
     */
    @Test
    fun testStringEncodeDecode() {
        logger.info("开始测试字符串编码解码")

        val testString = "Hello, World! 你好，世界！"
        val testBytes = testString.toByteArray(Charsets.UTF_8)

        // 测试 Base64Codec
        val base64Codec = Base64Codec
        val base64Encoded = base64Codec.encodeToString(testString)
        val base64Decoded = base64Codec.decodeToString(base64Encoded)
        assertEquals(testString, base64Decoded, "Base64 字符串编码解码应该一致")
        logger.info("Base64 字符串编码解码测试通过: $testString -> $base64Encoded -> $base64Decoded")

        // 测试 HexCodec
        val hexCodec = HexCodec
        val hexEncoded = hexCodec.encodeToString(testString)
        val hexDecoded = hexCodec.decodeToString(hexEncoded)
        assertEquals(testString, hexDecoded, "Hex 字符串编码解码应该一致")
        logger.info("Hex 字符串编码解码测试通过: $testString -> $hexEncoded -> $hexDecoded")
    }

    /**
     * 测试字节数组编码解码
     */
    @Test
    fun testByteArrayEncodeDecode() {
        logger.info("开始测试字节数组编码解码")

        val testBytes = "Test Data 测试数据".toByteArray(Charsets.UTF_8)

        // 测试 Base64Codec
        val base64Codec = Base64Codec
        val base64Encoded = base64Codec.encodeToString(testBytes)
        val base64Decoded = base64Codec.decodeToByteArray(base64Encoded)
        assertTrue(testBytes.contentEquals(base64Decoded), "Base64 字节数组编码解码应该一致")
        logger.info("Base64 字节数组编码解码测试通过")

        // 测试 HexCodec
        val hexCodec = HexCodec
        val hexEncoded = hexCodec.encodeToString(testBytes)
        val hexDecoded = hexCodec.decodeToByteArray(hexEncoded)
        assertTrue(testBytes.contentEquals(hexDecoded), "Hex 字节数组编码解码应该一致")
        logger.info("Hex 字节数组编码解码测试通过")
    }

    /**
     * 测试空字符串和空字节数组
     */
    @Test
    fun testEmptyInputs() {
        logger.info("开始测试空输入")

        val base64Codec = Base64Codec
        val hexCodec = HexCodec

        // 测试空字符串
        val emptyString = ""
        val base64EmptyEncoded = base64Codec.encodeToString(emptyString)
        val base64EmptyDecoded = base64Codec.decodeToString(base64EmptyEncoded)
        assertEquals(emptyString, base64EmptyDecoded, "空字符串 Base64 编码解码应该一致")

        val hexEmptyEncoded = hexCodec.encodeToString(emptyString)
        val hexEmptyDecoded = hexCodec.decodeToString(hexEmptyEncoded)
        assertEquals(emptyString, hexEmptyDecoded, "空字符串 Hex 编码解码应该一致")

        // 测试空字节数组
        val emptyBytes = ByteArray(0)
        val base64EmptyBytesEncoded = base64Codec.encodeToString(emptyBytes)
        val base64EmptyBytesDecoded = base64Codec.decodeToByteArray(base64EmptyBytesEncoded)
        assertTrue(emptyBytes.contentEquals(base64EmptyBytesDecoded), "空字节数组 Base64 编码解码应该一致")

        val hexEmptyBytesEncoded = hexCodec.encodeToString(emptyBytes)
        val hexEmptyBytesDecoded = hexCodec.decodeToByteArray(hexEmptyBytesEncoded)
        assertTrue(emptyBytes.contentEquals(hexEmptyBytesDecoded), "空字节数组 Hex 编码解码应该一致")

        logger.info("空输入测试通过")
    }

    /**
     * 测试特殊字符
     */
    @Test
    fun testSpecialCharacters() {
        logger.info("开始测试特殊字符")

        val specialString = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~"
        val base64Codec = Base64Codec
        val hexCodec = HexCodec

        // 测试 Base64Codec
        val base64Encoded = base64Codec.encodeToString(specialString)
        val base64Decoded = base64Codec.decodeToString(base64Encoded)
        assertEquals(specialString, base64Decoded, "特殊字符 Base64 编码解码应该一致")

        // 测试 HexCodec
        val hexEncoded = hexCodec.encodeToString(specialString)
        val hexDecoded = hexCodec.decodeToString(hexEncoded)
        assertEquals(specialString, hexDecoded, "特殊字符 Hex 编码解码应该一致")

        logger.info("特殊字符测试通过: $specialString")
    }

    /**
     * 测试中文字符
     */
    @Test
    fun testChineseCharacters() {
        logger.info("开始测试中文字符")

        val chineseString = "中文测试：你好世界！"
        val base64Codec = Base64Codec
        val hexCodec = HexCodec

        // 测试 Base64Codec
        val base64Encoded = base64Codec.encodeToString(chineseString)
        val base64Decoded = base64Codec.decodeToString(base64Encoded)
        assertEquals(chineseString, base64Decoded, "中文字符 Base64 编码解码应该一致")

        // 测试 HexCodec
        val hexEncoded = hexCodec.encodeToString(chineseString)
        val hexDecoded = hexCodec.decodeToString(hexEncoded)
        assertEquals(chineseString, hexDecoded, "中文字符 Hex 编码解码应该一致")

        logger.info("中文字符测试通过: $chineseString")
    }

    /**
     * 测试大字符串
     */
    @Test
    fun testLargeString() {
        logger.info("开始测试大字符串")

        val largeString = "A".repeat(10000)
        val base64Codec = Base64Codec
        val hexCodec = HexCodec

        // 测试 Base64Codec
        val base64Encoded = base64Codec.encodeToString(largeString)
        val base64Decoded = base64Codec.decodeToString(base64Encoded)
        assertEquals(largeString, base64Decoded, "大字符串 Base64 编码解码应该一致")

        // 测试 HexCodec
        val hexEncoded = hexCodec.encodeToString(largeString)
        val hexDecoded = hexCodec.decodeToString(hexEncoded)
        assertEquals(largeString, hexDecoded, "大字符串 Hex 编码解码应该一致")

        logger.info("大字符串测试通过，长度: ${largeString.length}")
    }

    /**
     * 测试性能
     */
    @Test
    fun testPerformance() {
        logger.info("开始性能测试")

        val testData = "Performance test data".toByteArray(Charsets.UTF_8)
        val iterations = 1000

        val base64Codec = Base64Codec
        val hexCodec = HexCodec

        // Base64 性能测试
        val base64StartTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = base64Codec.encodeToString(testData)
            base64Codec.decodeToByteArray(encoded)
        }
        val base64EndTime = System.currentTimeMillis()
        val base64Duration = base64EndTime - base64StartTime
        logger.info("Base64 性能测试完成，${iterations}次迭代耗时: ${base64Duration}ms")

        // Hex 性能测试
        val hexStartTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = hexCodec.encodeToString(testData)
            hexCodec.decodeToByteArray(encoded)
        }
        val hexEndTime = System.currentTimeMillis()
        val hexDuration = hexEndTime - hexStartTime
        logger.info("Hex 性能测试完成，${iterations}次迭代耗时: ${hexDuration}ms")

        assertTrue(base64Duration > 0, "Base64 性能测试应该完成")
        assertTrue(hexDuration > 0, "Hex 性能测试应该完成")
    }
}
