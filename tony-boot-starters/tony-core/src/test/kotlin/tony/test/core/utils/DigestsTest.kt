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

package tony.test.core.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.core.utils.DigestAlgorithm
import tony.core.utils.md5
import tony.core.utils.sha1
import tony.core.utils.sha256

/**
 * 摘要工具类单元测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Digests测试")
class DigestsTest {

    @Nested
    @DisplayName("CharSequence.md5()测试")
    inner class Md5Test {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test", "Tony测试123!@#"])
        @DisplayName("CharSequence.md5():正常字符串")
        fun testMd5WithNormalString(value: String) {
            val md5Result = value.md5()

            assertNotNull(md5Result)
            assertEquals(32, md5Result.length)
            assertTrue(md5Result.all { it.isLetterOrDigit() })
        }

        @Test
        @DisplayName("CharSequence.md5():相同字符串")
        fun testMd5WithSameString() {
            val testString = "Tony测试123!@#"
            val md5Result1 = testString.md5()
            val md5Result2 = testString.md5()
            assertEquals(md5Result1, md5Result2)
        }

        @Test
        @DisplayName("CharSequence.md5():空字符串")
        fun testMd5WithEmptyString() {
            val emptyMd5 = "".md5()
            assertNotNull(emptyMd5)
            assertEquals(32, emptyMd5.length)
        }

        @Test
        @DisplayName("CharSequence.md5():StringBuilder")
        fun testMd5WithStringBuilder() {
            val stringBuilder = StringBuilder("Tony测试123!@#")
            val md5Result = stringBuilder.md5()

            assertNotNull(md5Result)
            assertEquals(32, md5Result.length)
        }

        @Test
        @DisplayName("CharSequence.md5():StringBuffer")
        fun testMd5WithStringBuffer() {
            val stringBuffer = StringBuffer("Tony测试123!@#")
            val md5Result = stringBuffer.md5()

            assertNotNull(md5Result)
            assertEquals(32, md5Result.length)
        }
    }

    @Nested
    @DisplayName("CharSequence.sha1()测试")
    inner class Sha1Test {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test", "Tony测试123!@#"])
        @DisplayName("CharSequence.sha1():正常字符串")
        fun testSha1WithNormalString(value: String) {
            val sha1Result = value.sha1()

            assertNotNull(sha1Result)
            assertEquals(40, sha1Result.length)
            assertTrue(sha1Result.all { it.isLetterOrDigit() })
        }

        @Test
        @DisplayName("CharSequence.sha1():相同字符串")
        fun testSha1WithSameString() {
            val testString = "Tony测试123!@#"
            val sha1Result1 = testString.sha1()
            val sha1Result2 = testString.sha1()
            assertEquals(sha1Result1, sha1Result2)
        }

        @Test
        @DisplayName("CharSequence.sha1():空字符串")
        fun testSha1WithEmptyString() {
            val emptySha1 = "".sha1()
            assertNotNull(emptySha1)
            assertEquals(40, emptySha1.length)
        }

        @Test
        @DisplayName("CharSequence.sha1():StringBuilder")
        fun testSha1WithStringBuilder() {
            val stringBuilder = StringBuilder("Tony测试123!@#")
            val sha1Result = stringBuilder.sha1()

            assertNotNull(sha1Result)
            assertEquals(40, sha1Result.length)
        }

        @Test
        @DisplayName("CharSequence.sha1():StringBuffer")
        fun testSha1WithStringBuffer() {
            val stringBuffer = StringBuffer("Tony测试123!@#")
            val sha1Result = stringBuffer.sha1()

            assertNotNull(sha1Result)
            assertEquals(40, sha1Result.length)
        }
    }

    @Nested
    @DisplayName("CharSequence.sha256()测试")
    inner class Sha256Test {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test", "Tony测试123!@#"])
        @DisplayName("CharSequence.sha256():正常字符串")
        fun testSha256WithNormalString(value: String) {
            val sha256Result = value.sha256()

            assertNotNull(sha256Result)
            assertEquals(64, sha256Result.length)
            assertTrue(sha256Result.all { it.isLetterOrDigit() })
        }

        @Test
        @DisplayName("CharSequence.sha256():相同字符串")
        fun testSha256WithSameString() {
            val testString = "Tony测试123!@#"
            val sha256Result1 = testString.sha256()
            val sha256Result2 = testString.sha256()
            assertEquals(sha256Result1, sha256Result2)
        }

        @Test
        @DisplayName("CharSequence.sha256():空字符串")
        fun testSha256WithEmptyString() {
            val emptySha256 = "".sha256()
            assertNotNull(emptySha256)
            assertEquals(64, emptySha256.length)
        }

        @Test
        @DisplayName("CharSequence.sha256():StringBuilder")
        fun testSha256WithStringBuilder() {
            val stringBuilder = StringBuilder("Tony测试123!@#")
            val sha256Result = stringBuilder.sha256()

            assertNotNull(sha256Result)
            assertEquals(64, sha256Result.length)
        }

        @Test
        @DisplayName("CharSequence.sha256():StringBuffer")
        fun testSha256WithStringBuffer() {
            val stringBuffer = StringBuffer("Tony测试123!@#")
            val sha256Result = stringBuffer.sha256()

            assertNotNull(sha256Result)
            assertEquals(64, sha256Result.length)
        }
    }

    @Nested
    @DisplayName("DigestAlgorithm枚举测试")
    inner class DigestAlgorithmTest {
        @Test
        @DisplayName("DigestAlgorithm.value():属性值")
        fun testDigestAlgorithmValue() {
            assertEquals("md5", DigestAlgorithm.MD5.value)
            assertEquals("sha1", DigestAlgorithm.SHA1.value)
            assertEquals("sha256", DigestAlgorithm.SHA256.value)
        }

        @Test
        @DisplayName("DigestAlgorithm.digest():MD5摘要")
        fun testDigestAlgorithmMd5Digest() {
            val testString = "Tony摘要测试"
            val md5Result = DigestAlgorithm.MD5.digest(testString)
            assertNotNull(md5Result)
            assertEquals(32, md5Result.length)
            assertTrue(md5Result.all { it.isLetterOrDigit() })
        }

        @Test
        @DisplayName("DigestAlgorithm.digest():SHA1摘要")
        fun testDigestAlgorithmSha1Digest() {
            val testString = "Tony摘要测试"
            val sha1Result = DigestAlgorithm.SHA1.digest(testString)
            assertNotNull(sha1Result)
            assertEquals(40, sha1Result.length)
            assertTrue(sha1Result.all { it.isLetterOrDigit() })
        }

        @Test
        @DisplayName("DigestAlgorithm.digest():SHA256摘要")
        fun testDigestAlgorithmSha256Digest() {
            val testString = "Tony摘要测试"
            val sha256Result = DigestAlgorithm.SHA256.digest(testString)
            assertNotNull(sha256Result)
            assertEquals(64, sha256Result.length)
            assertTrue(sha256Result.all { it.isLetterOrDigit() })
        }

        @Test
        @DisplayName("DigestAlgorithm.digest():空字符串")
        fun testDigestAlgorithmDigestEmptyString() {
            val emptyString = ""
            val md5Result = DigestAlgorithm.MD5.digest(emptyString)
            val sha1Result = DigestAlgorithm.SHA1.digest(emptyString)
            val sha256Result = DigestAlgorithm.SHA256.digest(emptyString)

            assertNotNull(md5Result)
            assertNotNull(sha1Result)
            assertNotNull(sha256Result)
            assertEquals(32, md5Result.length)
            assertEquals(40, sha1Result.length)
            assertEquals(64, sha256Result.length)
        }

        @Test
        @DisplayName("DigestAlgorithm.digest():相同输入相同输出")
        fun testDigestAlgorithmDigestConsistency() {
            val testString = "Tony一致性测试"
            val md5Result1 = DigestAlgorithm.MD5.digest(testString)
            val md5Result2 = DigestAlgorithm.MD5.digest(testString)
            assertEquals(md5Result1, md5Result2)

            val sha1Result1 = DigestAlgorithm.SHA1.digest(testString)
            val sha1Result2 = DigestAlgorithm.SHA1.digest(testString)
            assertEquals(sha1Result1, sha1Result2)

            val sha256Result1 = DigestAlgorithm.SHA256.digest(testString)
            val sha256Result2 = DigestAlgorithm.SHA256.digest(testString)
            assertEquals(sha256Result1, sha256Result2)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():MD5创建")
        fun testDigestAlgorithmCreateMd5() {
            val md5Result = DigestAlgorithm.create("md5")
            assertEquals(DigestAlgorithm.MD5, md5Result)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():SHA1创建")
        fun testDigestAlgorithmCreateSha1() {
            val sha1Result = DigestAlgorithm.create("sha1")
            assertEquals(DigestAlgorithm.SHA1, sha1Result)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():SHA256创建")
        fun testDigestAlgorithmCreateSha256() {
            val sha256Result = DigestAlgorithm.create("sha256")
            assertEquals(DigestAlgorithm.SHA256, sha256Result)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():大写创建")
        fun testDigestAlgorithmCreateUpperCase() {
            val upperCaseResult = DigestAlgorithm.create("MD5")
            assertEquals(DigestAlgorithm.MD5, upperCaseResult)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():混合大小写")
        fun testDigestAlgorithmCreateMixedCase() {
            val mixedCaseResult = DigestAlgorithm.create("Md5")
            assertEquals(DigestAlgorithm.MD5, mixedCaseResult)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():无效值")
        fun testDigestAlgorithmCreateInvalid() {
            val invalidResult = DigestAlgorithm.create("invalid")
            assertNull(invalidResult)
        }

        @Test
        @DisplayName("DigestAlgorithm.create():空值")
        fun testDigestAlgorithmCreateEmpty() {
            val emptyResult = DigestAlgorithm.create("")
            assertNull(emptyResult)
        }
    }

    @Nested
    @DisplayName("摘要算法一致性测试")
    inner class DigestConsistencyTest {
        @Test
        @DisplayName("不同算法结果不同")
        fun testDifferentAlgorithmsDifferentResults() {
            val testString = "Tony算法差异测试"
            val md5Result = testString.md5()
            val sha1Result = testString.sha1()
            val sha256Result = testString.sha256()

            assertNotEquals(md5Result, sha1Result)
            assertNotEquals(md5Result, sha256Result)
            assertNotEquals(sha1Result, sha256Result)
        }

        @Test
        @DisplayName("不同输入不同结果")
        fun testDifferentInputsDifferentResults() {
            val input1 = "Tony输入1"
            val input2 = "Tony输入2"

            val md5Result1 = input1.md5()
            val md5Result2 = input2.md5()
            assertNotEquals(md5Result1, md5Result2)

            val sha1Result1 = input1.sha1()
            val sha1Result2 = input2.sha1()
            assertNotEquals(sha1Result1, sha1Result2)

            val sha256Result1 = input1.sha256()
            val sha256Result2 = input2.sha256()
            assertNotEquals(sha256Result1, sha256Result2)
        }

        @Test
        @DisplayName("扩展方法与枚举方法一致性")
        fun testExtensionMethodConsistency() {
            val testString = "Tony一致性测试"

            // 扩展方法与枚举方法应该产生相同结果
            val extensionMd5 = testString.md5()
            val enumMd5 = DigestAlgorithm.MD5.digest(testString)
            assertEquals(extensionMd5, enumMd5)

            val extensionSha1 = testString.sha1()
            val enumSha1 = DigestAlgorithm.SHA1.digest(testString)
            assertEquals(extensionSha1, enumSha1)

            val extensionSha256 = testString.sha256()
            val enumSha256 = DigestAlgorithm.SHA256.digest(testString)
            assertEquals(extensionSha256, enumSha256)
        }
    }

    @Nested
    @DisplayName("边界情况测试")
    inner class BoundaryTest {
        @Test
        @DisplayName("特殊字符测试")
        fun testSpecialCharacters() {
            val specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?"
            val md5Result = specialChars.md5()
            val sha1Result = specialChars.sha1()
            val sha256Result = specialChars.sha256()

            assertNotNull(md5Result)
            assertNotNull(sha1Result)
            assertNotNull(sha256Result)
            assertEquals(32, md5Result.length)
            assertEquals(40, sha1Result.length)
            assertEquals(64, sha256Result.length)
        }

        @Test
        @DisplayName("Unicode字符测试")
        fun testUnicodeCharacters() {
            val unicodeString = "中文测试🌍🚀💻"
            val md5Result = unicodeString.md5()
            val sha1Result = unicodeString.sha1()
            val sha256Result = unicodeString.sha256()

            assertNotNull(md5Result)
            assertNotNull(sha1Result)
            assertNotNull(sha256Result)
            assertEquals(32, md5Result.length)
            assertEquals(40, sha1Result.length)
            assertEquals(64, sha256Result.length)
        }

        @Test
        @DisplayName("长字符串测试")
        fun testLongString() {
            val longString = "a".repeat(10000)
            val md5Result = longString.md5()
            val sha1Result = longString.sha1()
            val sha256Result = longString.sha256()

            assertNotNull(md5Result)
            assertNotNull(sha1Result)
            assertNotNull(sha256Result)
            assertEquals(32, md5Result.length)
            assertEquals(40, sha1Result.length)
            assertEquals(64, sha256Result.length)
        }
    }
}
