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

/**
 * Base64Codec 单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object Base64CodecTest {

    private val logger = LoggerFactory.getLogger(Base64CodecTest::class.java)

    /**
     * 测试 Base64Codec 实例
     */
    @Test
    fun testBase64CodecInstance() {
        logger.info("开始测试 Base64Codec 实例")

        val base64Codec = Base64Codec
        assertNotNull(base64Codec, "Base64Codec 实例不应为空")
        logger.info("Base64Codec 实例测试通过")
    }

    /**
     * 测试标准 Base64 编码解码
     */
    @Test
    fun testStandardBase64EncodeDecode() {
        logger.info("开始测试标准 Base64 编码解码")

        val testString = "Hello, World!"
        val testBytes = testString.toByteArray(Charsets.UTF_8)

        // 测试字符串编码解码
        val encodedString = Base64Codec.encodeToString(testString)
        val decodedString = Base64Codec.decodeToString(encodedString)
        assertEquals(testString, decodedString, "字符串 Base64 编码解码应该一致")
        logger.info("字符串 Base64 编码解码测试通过: $testString -> $encodedString -> $decodedString")

        // 测试字节数组编码解码
        val encodedBytes = Base64Codec.encodeToString(testBytes)
        val decodedBytes = Base64Codec.decodeToByteArray(encodedBytes)
        assertTrue(testBytes.contentEquals(decodedBytes), "字节数组 Base64 编码解码应该一致")
        logger.info("字节数组 Base64 编码解码测试通过")
    }

    /**
     * 测试 URL 安全的 Base64 编码解码
     */
    @Test
    fun testUrlSafeBase64EncodeDecode() {
        logger.info("开始测试 URL 安全的 Base64 编码解码")

        val testString = "Hello, World! 你好，世界！http://www.baidu.com?a=1&b=2"

        // 测试 URL 安全编码解码
        val urlSafeEncoded = Base64Codec.encodeToStringUrlSafe(testString)
        val urlSafeDecoded = Base64Codec.decodeToStringUrlSafe(urlSafeEncoded)
        assertEquals(testString, urlSafeDecoded, "URL 安全 Base64 编码解码应该一致")
        logger.info("URL 安全 Base64 编码解码测试通过: $testString -> $urlSafeEncoded -> $urlSafeDecoded")

        // 验证 URL 安全编码不包含特殊字符
        assertTrue(!urlSafeEncoded.contains('+'), "URL 安全编码不应包含 + 字符")
        assertTrue(!urlSafeEncoded.contains('/'), "URL 安全编码不应包含 / 字符")
        assertTrue(!urlSafeEncoded.contains('='), "URL 安全编码不应包含 = 字符")
        logger.info("URL 安全编码字符验证通过")
    }

    /**
     * 测试标准 Base64 和 URL 安全 Base64 的区别
     */
    @Test
    fun testStandardVsUrlSafeBase64() {
        logger.info("开始测试标准 Base64 和 URL 安全 Base64 的区别")

        val testString = "http://www.baidu.com?a=1&b=2 Hello, World! 你好，世界！"

        // 标准 Base64 编码
        val standardEncoded = Base64Codec.encodeToString(testString)
        logger.info("标准 Base64 编码: $standardEncoded")

        // URL 安全 Base64 编码
        val urlSafeEncoded = Base64Codec.encodeToStringUrlSafe(testString)
        logger.info("URL 安全 Base64 编码: $urlSafeEncoded")

        // 验证两种编码结果不同
        assertTrue(standardEncoded != urlSafeEncoded, "标准 Base64 和 URL 安全 Base64 编码结果应该不同")

        // 验证两种编码都能正确解码
        val standardDecoded = Base64Codec.decodeToString(standardEncoded)
        val urlSafeDecoded = Base64Codec.decodeToStringUrlSafe(urlSafeEncoded)
        assertEquals(testString, standardDecoded, "标准 Base64 解码应该正确")
        assertEquals(testString, urlSafeDecoded, "URL 安全 Base64 解码应该正确")

        logger.info("标准 Base64 和 URL 安全 Base64 区别测试通过")
    }

    /**
     * 测试空字符串和空字节数组
     */
    @Test
    fun testEmptyInputs() {
        logger.info("开始测试空输入")

        // 测试空字符串
        val emptyString = ""
        val emptyStringEncoded = Base64Codec.encodeToString(emptyString)
        val emptyStringDecoded = Base64Codec.decodeToString(emptyStringEncoded)
        assertEquals(emptyString, emptyStringDecoded, "空字符串 Base64 编码解码应该一致")

        val emptyStringUrlSafeEncoded = Base64Codec.encodeToStringUrlSafe(emptyString)
        val emptyStringUrlSafeDecoded = Base64Codec.decodeToStringUrlSafe(emptyStringUrlSafeEncoded)
        assertEquals(emptyString, emptyStringUrlSafeDecoded, "空字符串 URL 安全 Base64 编码解码应该一致")

        // 测试空字节数组
        val emptyBytes = ByteArray(0)
        val emptyBytesEncoded = Base64Codec.encodeToString(emptyBytes)
        val emptyBytesDecoded = Base64Codec.decodeToByteArray(emptyBytesEncoded)
        assertTrue(emptyBytes.contentEquals(emptyBytesDecoded), "空字节数组 Base64 编码解码应该一致")

        logger.info("空输入测试通过")
    }

    /**
     * 测试特殊字符
     */
    @Test
    fun testSpecialCharacters() {
        logger.info("开始测试特殊字符")

        val specialString = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~"

        // 标准 Base64
        val standardEncoded = Base64Codec.encodeToString(specialString)
        val standardDecoded = Base64Codec.decodeToString(standardEncoded)
        assertEquals(specialString, standardDecoded, "特殊字符标准 Base64 编码解码应该一致")

        // URL 安全 Base64
        val urlSafeEncoded = Base64Codec.encodeToStringUrlSafe(specialString)
        val urlSafeDecoded = Base64Codec.decodeToStringUrlSafe(urlSafeEncoded)
        assertEquals(specialString, urlSafeDecoded, "特殊字符 URL 安全 Base64 编码解码应该一致")

        logger.info("特殊字符测试通过: $specialString")
    }

    /**
     * 测试中文字符
     */
    @Test
    fun testChineseCharacters() {
        logger.info("开始测试中文字符")

        val chineseString = "中文测试：你好世界！"

        // 标准 Base64
        val standardEncoded = Base64Codec.encodeToString(chineseString)
        val standardDecoded = Base64Codec.decodeToString(standardEncoded)
        assertEquals(chineseString, standardDecoded, "中文字符标准 Base64 编码解码应该一致")

        // URL 安全 Base64
        val urlSafeEncoded = Base64Codec.encodeToStringUrlSafe(chineseString)
        val urlSafeDecoded = Base64Codec.decodeToStringUrlSafe(urlSafeEncoded)
        assertEquals(chineseString, urlSafeDecoded, "中文字符 URL 安全 Base64 编码解码应该一致")

        logger.info("中文字符测试通过: $chineseString")
    }

    /**
     * 测试大字符串
     */
    @Test
    fun testLargeString() {
        logger.info("开始测试大字符串")

        val largeString = "A".repeat(10000)

        // 标准 Base64
        val standardEncoded = Base64Codec.encodeToString(largeString)
        val standardDecoded = Base64Codec.decodeToString(standardEncoded)
        assertEquals(largeString, standardDecoded, "大字符串标准 Base64 编码解码应该一致")

        // URL 安全 Base64
        val urlSafeEncoded = Base64Codec.encodeToStringUrlSafe(largeString)
        val urlSafeDecoded = Base64Codec.decodeToStringUrlSafe(urlSafeEncoded)
        assertEquals(largeString, urlSafeDecoded, "大字符串 URL 安全 Base64 编码解码应该一致")

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

        // 标准 Base64 性能测试
        val standardStartTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = Base64Codec.encodeToString(testString)
            Base64Codec.decodeToString(encoded)
        }
        val standardEndTime = System.currentTimeMillis()
        val standardDuration = standardEndTime - standardStartTime
        logger.info("标准 Base64 性能测试完成，${iterations}次迭代耗时: ${standardDuration}ms")

        // URL 安全 Base64 性能测试
        val urlSafeStartTime = System.currentTimeMillis()
        repeat(iterations) {
            val encoded = Base64Codec.encodeToStringUrlSafe(testString)
            Base64Codec.decodeToStringUrlSafe(encoded)
        }
        val urlSafeEndTime = System.currentTimeMillis()
        val urlSafeDuration = urlSafeEndTime - urlSafeStartTime
        logger.info("URL 安全 Base64 性能测试完成，${iterations}次迭代耗时: ${urlSafeDuration}ms")

        assertTrue(standardDuration > 0, "标准 Base64 性能测试应该完成")
        assertTrue(urlSafeDuration > 0, "URL 安全 Base64 性能测试应该完成")
    }

    /**
     * 测试边界情况
     */
    @Test
    fun testBoundaryCases() {
        logger.info("开始测试边界情况")

        // 测试单个字符
        val singleChar = "A"
        val singleCharEncoded = Base64Codec.encodeToString(singleChar)
        val singleCharDecoded = Base64Codec.decodeToString(singleCharEncoded)
        assertEquals(singleChar, singleCharDecoded, "单个字符 Base64 编码解码应该一致")

        // 测试两个字符
        val twoChars = "AB"
        val twoCharsEncoded = Base64Codec.encodeToString(twoChars)
        val twoCharsDecoded = Base64Codec.decodeToString(twoCharsEncoded)
        assertEquals(twoChars, twoCharsDecoded, "两个字符 Base64 编码解码应该一致")

        // 测试三个字符
        val threeChars = "ABC"
        val threeCharsEncoded = Base64Codec.encodeToString(threeChars)
        val threeCharsDecoded = Base64Codec.decodeToString(threeCharsEncoded)
        assertEquals(threeChars, threeCharsDecoded, "三个字符 Base64 编码解码应该一致")

        logger.info("边界情况测试通过")
    }
}
