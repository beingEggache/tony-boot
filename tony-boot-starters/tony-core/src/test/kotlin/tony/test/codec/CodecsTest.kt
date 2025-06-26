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
import org.slf4j.LoggerFactory
import tony.codec.decodeToByteArray
import tony.codec.decodeToString
import tony.codec.encodeToByteArray
import tony.codec.encodeToString
import tony.codec.enums.Encoding

/**
 * Codecs 工具类单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object CodecsTest {

    private val logger = LoggerFactory.getLogger(CodecsTest::class.java)

    /**
     * 测试 CharSequence 编码扩展函数
     */
    @Test
    fun testCharSequenceEncodeExtensions() {
        logger.info("开始测试 CharSequence 编码扩展函数")

        val testString = "Hello, World! 你好，世界！"

        // 测试 Base64 编码
        val base64Encoded = testString.encodeToString(Encoding.BASE64)
        val base64Decoded = base64Encoded.decodeToString(Encoding.BASE64)
        assertEquals(testString, base64Decoded, "CharSequence Base64 编码解码应该一致")
        logger.info("CharSequence Base64 编码解码测试通过: $testString -> $base64Encoded -> $base64Decoded")

        // 测试 Hex 编码
        val hexEncoded = testString.encodeToString(Encoding.HEX)
        val hexDecoded = hexEncoded.decodeToString(Encoding.HEX)
        assertEquals(testString, hexDecoded, "CharSequence Hex 编码解码应该一致")
        logger.info("CharSequence Hex 编码解码测试通过: $testString -> $hexEncoded -> $hexDecoded")

        // 测试编码到字节数组
        val base64Bytes = testString.encodeToByteArray(Encoding.BASE64)
        val hexBytes = testString.encodeToByteArray(Encoding.HEX)
        assertTrue(base64Bytes.isNotEmpty(), "Base64 编码字节数组不应为空")
        assertTrue(hexBytes.isNotEmpty(), "Hex 编码字节数组不应为空")
        logger.info("CharSequence 编码到字节数组测试通过")
    }

    /**
     * 测试 CharSequence 解码扩展函数
     */
    @Test
    fun testCharSequenceDecodeExtensions() {
        logger.info("开始测试 CharSequence 解码扩展函数")

        val testString = "Test Data 测试数据"

        // 先编码
        val base64Encoded = testString.encodeToString(Encoding.BASE64)
        val hexEncoded = testString.encodeToString(Encoding.HEX)

        // 测试解码
        val base64Decoded = base64Encoded.decodeToString(Encoding.BASE64)
        val hexDecoded = hexEncoded.decodeToString(Encoding.HEX)

        assertEquals(testString, base64Decoded, "CharSequence Base64 解码应该正确")
        assertEquals(testString, hexDecoded, "CharSequence Hex 解码应该正确")

        // 测试解码到字节数组
        val base64DecodedBytes = base64Encoded.decodeToByteArray(Encoding.BASE64)
        val hexDecodedBytes = hexEncoded.decodeToByteArray(Encoding.HEX)

        assertTrue(base64DecodedBytes.contentEquals(testString.toByteArray(Charsets.UTF_8)), "Base64 解码字节数组应该正确")
        assertTrue(hexDecodedBytes.contentEquals(testString.toByteArray(Charsets.UTF_8)), "Hex 解码字节数组应该正确")

        logger.info("CharSequence 解码扩展函数测试通过")
    }

    /**
     * 测试 ByteArray 编码扩展函数
     */
    @Test
    fun testByteArrayEncodeExtensions() {
        logger.info("开始测试 ByteArray 编码扩展函数")

        val testBytes = "Hello, World!".toByteArray(Charsets.UTF_8)

        // 测试 Base64 编码
        val base64Encoded = testBytes.encodeToString(Encoding.BASE64)
        val base64Decoded = base64Encoded.decodeToString(Encoding.BASE64)
        assertTrue(testBytes.contentEquals(base64Decoded.toByteArray(Charsets.UTF_8)), "ByteArray Base64 编码解码应该一致")
        logger.info("ByteArray Base64 编码解码测试通过")

        // 测试 Hex 编码
        val hexEncoded = testBytes.encodeToString(Encoding.HEX)
        val hexDecoded = hexEncoded.decodeToString(Encoding.HEX)
        assertTrue(testBytes.contentEquals(hexDecoded.toByteArray(Charsets.UTF_8)), "ByteArray Hex 编码解码应该一致")
        logger.info("ByteArray Hex 编码解码测试通过")

        // 测试编码到字节数组
        val base64Bytes = testBytes.encodeToByteArray(Encoding.BASE64)
        val hexBytes = testBytes.encodeToByteArray(Encoding.HEX)
        assertTrue(base64Bytes.isNotEmpty(), "Base64 编码字节数组不应为空")
        assertTrue(hexBytes.isNotEmpty(), "Hex 编码字节数组不应为空")
        logger.info("ByteArray 编码到字节数组测试通过")
    }

    /**
     * 测试 ByteArray 解码扩展函数
     */
    @Test
    fun testByteArrayDecodeExtensions() {
        logger.info("开始测试 ByteArray 解码扩展函数")

        val testBytes = "Test Data".toByteArray(Charsets.UTF_8)

        // 先编码
        val base64Encoded = testBytes.encodeToString(Encoding.BASE64)
        val hexEncoded = testBytes.encodeToString(Encoding.HEX)

        // 测试解码
        val base64Decoded = base64Encoded.toByteArray(Charsets.UTF_8).decodeToString(Encoding.BASE64)
        val hexDecoded = hexEncoded.toByteArray(Charsets.UTF_8).decodeToString(Encoding.HEX)

        assertTrue(testBytes.contentEquals(base64Decoded.toByteArray(Charsets.UTF_8)), "ByteArray Base64 解码应该正确")
        assertTrue(testBytes.contentEquals(hexDecoded.toByteArray(Charsets.UTF_8)), "ByteArray Hex 解码应该正确")

        // 测试解码到字节数组
        val base64DecodedBytes = base64Encoded.toByteArray(Charsets.UTF_8).decodeToByteArray(Encoding.BASE64)
        val hexDecodedBytes = hexEncoded.toByteArray(Charsets.UTF_8).decodeToByteArray(Encoding.HEX)

        assertTrue(testBytes.contentEquals(base64DecodedBytes), "Base64 解码字节数组应该正确")
        assertTrue(testBytes.contentEquals(hexDecodedBytes), "Hex 解码字节数组应该正确")

        logger.info("ByteArray 解码扩展函数测试通过")
    }

    /**
     * 测试空输入
     */
    @Test
    fun testEmptyInputs() {
        logger.info("开始测试空输入")

        val emptyString = ""
        val emptyBytes = ByteArray(0)

        // 测试空字符串
        val emptyBase64Encoded = emptyString.encodeToString(Encoding.BASE64)
        val emptyBase64Decoded = emptyBase64Encoded.decodeToString(Encoding.BASE64)
        assertEquals(emptyString, emptyBase64Decoded, "空字符串 Base64 编码解码应该一致")

        val emptyHexEncoded = emptyString.encodeToString(Encoding.HEX)
        val emptyHexDecoded = emptyHexEncoded.decodeToString(Encoding.HEX)
        assertEquals(emptyString, emptyHexDecoded, "空字符串 Hex 编码解码应该一致")

        // 测试空字节数组
        val emptyBytesBase64Encoded = emptyBytes.encodeToString(Encoding.BASE64)
        val emptyBytesBase64Decoded = emptyBytesBase64Encoded.decodeToByteArray(Encoding.BASE64)
        assertTrue(emptyBytes.contentEquals(emptyBytesBase64Decoded), "空字节数组 Base64 编码解码应该一致")

        val emptyBytesHexEncoded = emptyBytes.encodeToString(Encoding.HEX)
        val emptyBytesHexDecoded = emptyBytesHexEncoded.decodeToByteArray(Encoding.HEX)
        assertTrue(emptyBytes.contentEquals(emptyBytesHexDecoded), "空字节数组 Hex 编码解码应该一致")

        logger.info("空输入测试通过")
    }

    /**
     * 测试特殊字符
     */
    @Test
    fun testSpecialCharacters() {
        logger.info("开始测试特殊字符")

        val specialString = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~"

        // Base64 编码解码
        val base64Encoded = specialString.encodeToString(Encoding.BASE64)
        val base64Decoded = base64Encoded.decodeToString(Encoding.BASE64)
        assertEquals(specialString, base64Decoded, "特殊字符 Base64 编码解码应该一致")

        // Hex 编码解码
        val hexEncoded = specialString.encodeToString(Encoding.HEX)
        val hexDecoded = hexEncoded.decodeToString(Encoding.HEX)
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

        // Base64 编码解码
        val base64Encoded = chineseString.encodeToString(Encoding.BASE64)
        val base64Decoded = base64Encoded.decodeToString(Encoding.BASE64)
        assertEquals(chineseString, base64Decoded, "中文字符 Base64 编码解码应该一致")

        // Hex 编码解码
        val hexEncoded = chineseString.encodeToString(Encoding.HEX)
        val hexDecoded = hexEncoded.decodeToString(Encoding.HEX)
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

        // Base64 编码解码
        val base64Encoded = largeString.encodeToString(Encoding.BASE64)
        val base64Decoded = base64Encoded.decodeToString(Encoding.BASE64)
        assertEquals(largeString, base64Decoded, "大字符串 Base64 编码解码应该一致")

        // Hex 编码解码
        val hexEncoded = largeString.encodeToString(Encoding.HEX)
        val hexDecoded = hexEncoded.decodeToString(Encoding.HEX)
        assertEquals(largeString, hexDecoded, "大字符串 Hex 编码解码应该一致")

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

        // Base64 性能测试
        val base64StartTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = testString.encodeToString(Encoding.BASE64)
            encoded.decodeToString(Encoding.BASE64)
        }
        val base64EndTime = System.currentTimeMillis()
        val base64Duration = base64EndTime - base64StartTime
        logger.info("Base64 扩展函数性能测试完成，${iterations}次迭代耗时: ${base64Duration}ms")

        // Hex 性能测试
        val hexStartTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = testString.encodeToString(Encoding.HEX)
            encoded.decodeToString(Encoding.HEX)
        }
        val hexEndTime = System.currentTimeMillis()
        val hexDuration = hexEndTime - hexStartTime
        logger.info("Hex 扩展函数性能测试完成，${iterations}次迭代耗时: ${hexDuration}ms")

        assertTrue(base64Duration > 0, "Base64 扩展函数性能测试应该完成")
        assertTrue(hexDuration > 0, "Hex 扩展函数性能测试应该完成")
    }

    /**
     * 测试边界情况
     */
    @Test
    fun testBoundaryCases() {
        logger.info("开始测试边界情况")

        // 测试单个字符
        val singleChar = "A"
        val singleCharBase64 = singleChar.encodeToString(Encoding.BASE64)
        val singleCharBase64Decoded = singleCharBase64.decodeToString(Encoding.BASE64)
        assertEquals(singleChar, singleCharBase64Decoded, "单个字符 Base64 编码解码应该一致")

        val singleCharHex = singleChar.encodeToString(Encoding.HEX)
        val singleCharHexDecoded = singleCharHex.decodeToString(Encoding.HEX)
        assertEquals(singleChar, singleCharHexDecoded, "单个字符 Hex 编码解码应该一致")

        // 测试两个字符
        val twoChars = "AB"
        val twoCharsBase64 = twoChars.encodeToString(Encoding.BASE64)
        val twoCharsBase64Decoded = twoCharsBase64.decodeToString(Encoding.BASE64)
        assertEquals(twoChars, twoCharsBase64Decoded, "两个字符 Base64 编码解码应该一致")

        val twoCharsHex = twoChars.encodeToString(Encoding.HEX)
        val twoCharsHexDecoded = twoCharsHex.decodeToString(Encoding.HEX)
        assertEquals(twoChars, twoCharsHexDecoded, "两个字符 Hex 编码解码应该一致")

        logger.info("边界情况测试通过")
    }

    /**
     * 测试不同编码类型的比较
     */
    @Test
    fun testEncodingComparison() {
        logger.info("开始测试不同编码类型的比较")

        val testString = "Hello, World! 你好，世界！"

        // 获取不同编码的结果
        val base64Result = testString.encodeToString(Encoding.BASE64)
        val hexResult = testString.encodeToString(Encoding.HEX)

        // 验证编码结果不同
        assertTrue(base64Result != hexResult, "Base64 和 Hex 编码结果应该不同")

        // 验证都能正确解码
        val base64Decoded = base64Result.decodeToString(Encoding.BASE64)
        val hexDecoded = hexResult.decodeToString(Encoding.HEX)

        assertEquals(testString, base64Decoded, "Base64 解码应该正确")
        assertEquals(testString, hexDecoded, "Hex 解码应该正确")

        logger.info("编码类型比较测试通过")
        logger.info("Base64 编码结果: $base64Result")
        logger.info("Hex 编码结果: $hexResult")
    }
}
