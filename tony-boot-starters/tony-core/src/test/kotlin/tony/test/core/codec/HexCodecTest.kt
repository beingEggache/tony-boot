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

package tony.test.core.codec

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.core.codec.HexCodec

/**
 * HexCodec 单元测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("HexCodec全量测试")
class HexCodecTest {

    @Nested
    @DisplayName("编码与解码功能")
    inner class EncodeDecode {
        @ParameterizedTest
        @ValueSource(strings = ["Hello, World!", "", "!@#$%^&*()_+-=[]{}|;':\",./<>?`~", "中文测试：你好世界！", "A", "AB", "ABC"])
        @DisplayName("字符串编码解码一致性")
        fun testStringEncodeDecode(str: String) {
            val encoded = HexCodec.encodeToString(str)
            val decoded = HexCodec.decodeToString(encoded)
            assertEquals(str, decoded)
        }
        @Test
        @DisplayName("字节数组编码解码一致性")
        fun testByteArrayEncodeDecode() {
            val cases = listOf(
                "Hello, World!".toByteArray(),
                ByteArray(0),
                byteArrayOf(0x41), // 'A'
                byteArrayOf(0x41, 0x42), // 'AB'
                byteArrayOf(0x41, 0x42, 0x43), // 'ABC'
                "中文测试".toByteArray()
            )
            for (bytes in cases) {
                val encoded = HexCodec.encodeToString(bytes)
                val decoded = HexCodec.decodeToByteArray(encoded)
                assertTrue(bytes.contentEquals(decoded))
            }
        }
    }

    @Nested
    @DisplayName("格式与边界")
    inner class FormatBoundary {
        @Test
        @DisplayName("编码结果为偶数长度且仅含十六进制字符")
        fun testHexFormat() {
            val encoded = HexCodec.encodeToString("Hello, World!")
            assertTrue(encoded.length % 2 == 0)
            assertTrue(encoded.all { it in "0123456789ABCDEFabcdef" })
        }
        @Test
        @DisplayName("空字符串和空字节数组")
        fun testEmptyInputs() {
            val emptyStr = ""
            val encodedStr = HexCodec.encodeToString(emptyStr)
            val decodedStr = HexCodec.decodeToString(encodedStr)
            assertEquals(emptyStr, decodedStr)
            val emptyBytes = ByteArray(0)
            val encodedBytes = HexCodec.encodeToString(emptyBytes)
            val decodedBytes = HexCodec.decodeToByteArray(encodedBytes)
            assertTrue(emptyBytes.contentEquals(decodedBytes))
        }
        @Test
        @DisplayName("大字符串和大字节数组")
        fun testLargeInputs() {
            val largeStr = "A".repeat(10000)
            val encoded = HexCodec.encodeToString(largeStr)
            val decoded = HexCodec.decodeToString(encoded)
            assertEquals(largeStr, decoded)
            val largeBytes = ByteArray(10000) { (it % 256).toByte() }
            val encodedBytes = HexCodec.encodeToString(largeBytes)
            val decodedBytes = HexCodec.decodeToByteArray(encodedBytes)
            assertTrue(largeBytes.contentEquals(decodedBytes))
        }
    }

    @Nested
    @DisplayName("特殊字符与多语言")
    inner class SpecialCases {
        @ParameterizedTest
        @ValueSource(strings = ["!@#$%^&*()_+-=[]{}|;':\",./<>?`~", "中文测试：你好世界！", "\uD83D\uDE00\uD83C\uDF1F"]) // emoji
        @DisplayName("特殊字符和多语言编码解码")
        fun testSpecialAndUnicode(str: String) {
            val encoded = HexCodec.encodeToString(str)
            val decoded = HexCodec.decodeToString(encoded)
            assertEquals(str, decoded)
        }
    }

    @Nested
    @DisplayName("性能测试")
    inner class Performance {
        @Test
        @DisplayName("多次编码解码性能")
        fun testPerformance() {
            val testString = "Performance test data"
            val iterations = 1000
            val start = System.currentTimeMillis()
            repeat(iterations) {
                val encoded = HexCodec.encodeToString(testString)
                HexCodec.decodeToString(encoded)
            }
            val duration = System.currentTimeMillis() - start
            assertTrue(duration >= 0)
        }
    }
}
