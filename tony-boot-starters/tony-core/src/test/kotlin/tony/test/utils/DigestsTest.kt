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
import org.slf4j.LoggerFactory
import tony.utils.*

/**
 * 摘要工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object DigestsTest {

    private val logger = LoggerFactory.getLogger(DigestsTest::class.java)

    // ==================== md5() 方法测试 ====================

    @Test
    @DisplayName("md5()方法测试")
    fun testMd5() {
        logger.info("测试md5()方法")
        
        val testString = "Tony测试123!@#"
        val md5Result = testString.md5()
        
        logger.info("原始字符串: '{}'", testString)
        logger.info("MD5结果: {}", md5Result)
        
        assertNotNull(md5Result)
        assertTrue(md5Result.length == 32)
        assertTrue(md5Result.all { it.isLetterOrDigit() })
        
        // 测试相同字符串产生相同MD5
        val md5Result2 = testString.md5()
        logger.info("第二次MD5结果: {}", md5Result2)
        assertEquals(md5Result, md5Result2)
        
        // 测试空字符串
        val emptyMd5 = "".md5()
        logger.info("空字符串MD5: '{}' -> {}", "", emptyMd5)
        assertNotNull(emptyMd5)
        assertTrue(emptyMd5.length == 32)
    }

    // ==================== sha1() 方法测试 ====================

    @Test
    @DisplayName("sha1()方法测试")
    fun testSha1() {
        logger.info("测试sha1()方法")
        
        val testString = "Tony测试123!@#"
        val sha1Result = testString.sha1()
        
        logger.info("原始字符串: '{}'", testString)
        logger.info("SHA1结果: {}", sha1Result)
        
        assertNotNull(sha1Result)
        assertTrue(sha1Result.length == 40)
        assertTrue(sha1Result.all { it.isLetterOrDigit() })
        
        // 测试相同字符串产生相同SHA1
        val sha1Result2 = testString.sha1()
        logger.info("第二次SHA1结果: {}", sha1Result2)
        assertEquals(sha1Result, sha1Result2)
        
        // 测试空字符串
        val emptySha1 = "".sha1()
        logger.info("空字符串SHA1: '{}' -> {}", "", emptySha1)
        assertNotNull(emptySha1)
        assertTrue(emptySha1.length == 40)
    }

    // ==================== sha256() 方法测试 ====================

    @Test
    @DisplayName("sha256()方法测试")
    fun testSha256() {
        logger.info("测试sha256()方法")
        
        val testString = "Tony测试123!@#"
        val sha256Result = testString.sha256()
        
        logger.info("原始字符串: '{}'", testString)
        logger.info("SHA256结果: {}", sha256Result)
        
        assertNotNull(sha256Result)
        assertTrue(sha256Result.length == 64)
        assertTrue(sha256Result.all { it.isLetterOrDigit() })
        
        // 测试相同字符串产生相同SHA256
        val sha256Result2 = testString.sha256()
        logger.info("第二次SHA256结果: {}", sha256Result2)
        assertEquals(sha256Result, sha256Result2)
        
        // 测试空字符串
        val emptySha256 = "".sha256()
        logger.info("空字符串SHA256: '{}' -> {}", "", emptySha256)
        assertNotNull(emptySha256)
        assertTrue(emptySha256.length == 64)
    }

    // ==================== DigestAlgorithm 枚举测试 ====================

    @Test
    @DisplayName("DigestAlgorithm枚举 - value属性测试")
    fun testDigestAlgorithmValue() {
        logger.info("测试DigestAlgorithm枚举的value属性")
        
        logger.info("DigestAlgorithm.MD5.value = {}", DigestAlgorithm.MD5.value)
        logger.info("DigestAlgorithm.SHA1.value = {}", DigestAlgorithm.SHA1.value)
        logger.info("DigestAlgorithm.SHA256.value = {}", DigestAlgorithm.SHA256.value)
        
        assertEquals("md5", DigestAlgorithm.MD5.value)
        assertEquals("sha1", DigestAlgorithm.SHA1.value)
        assertEquals("sha256", DigestAlgorithm.SHA256.value)
    }

    @Test
    @DisplayName("DigestAlgorithm枚举 - digest()方法测试")
    fun testDigestAlgorithmDigest() {
        logger.info("测试DigestAlgorithm枚举的digest()方法")
        
        val testString = "Tony摘要测试"
        
        // 测试MD5
        val md5Result = DigestAlgorithm.MD5.digest(testString)
        logger.info("MD5.digest('{}') = {}", testString, md5Result)
        assertNotNull(md5Result)
        assertTrue(md5Result.length == 32)
        
        // 测试SHA1
        val sha1Result = DigestAlgorithm.SHA1.digest(testString)
        logger.info("SHA1.digest('{}') = {}", testString, sha1Result)
        assertNotNull(sha1Result)
        assertTrue(sha1Result.length == 40)
        
        // 测试SHA256
        val sha256Result = DigestAlgorithm.SHA256.digest(testString)
        logger.info("SHA256.digest('{}') = {}", testString, sha256Result)
        assertNotNull(sha256Result)
        assertTrue(sha256Result.length == 64)
    }

    @Test
    @DisplayName("DigestAlgorithm枚举 - create()方法测试")
    fun testDigestAlgorithmCreate() {
        logger.info("测试DigestAlgorithm枚举的create()方法")
        
        val md5Result = DigestAlgorithm.create("md5")
        logger.info("DigestAlgorithm.create('md5') = {}", md5Result)
        assertEquals(DigestAlgorithm.MD5, md5Result)
        
        val sha1Result = DigestAlgorithm.create("sha1")
        logger.info("DigestAlgorithm.create('sha1') = {}", sha1Result)
        assertEquals(DigestAlgorithm.SHA1, sha1Result)
        
        val sha256Result = DigestAlgorithm.create("sha256")
        logger.info("DigestAlgorithm.create('sha256') = {}", sha256Result)
        assertEquals(DigestAlgorithm.SHA256, sha256Result)
        
        // 测试大写
        val upperCaseResult = DigestAlgorithm.create("MD5")
        logger.info("DigestAlgorithm.create('MD5') = {}", upperCaseResult)
        assertEquals(DigestAlgorithm.MD5, upperCaseResult)
        
        // 测试无效值
        val invalidResult = DigestAlgorithm.create("invalid")
        logger.info("DigestAlgorithm.create('invalid') = {}", invalidResult)
        assertNull(invalidResult)
    }

    @Test
    @DisplayName("DigestAlgorithm枚举 - values()测试")
    fun testDigestAlgorithmValues() {
        logger.info("测试DigestAlgorithm枚举的values()方法")
        
        val values = DigestAlgorithm.values()
        logger.info("DigestAlgorithm.values() = {}", values.contentToString())
        
        assertEquals(3, values.size)
        assertTrue(values.contains(DigestAlgorithm.MD5))
        assertTrue(values.contains(DigestAlgorithm.SHA1))
        assertTrue(values.contains(DigestAlgorithm.SHA256))
    }

    // ==================== 一致性测试 ====================

    @Test
    @DisplayName("摘要算法一致性测试")
    fun testDigestConsistency() {
        logger.info("测试摘要算法的一致性")
        
        val testString = "Tony一致性测试"
        
        // 测试MD5一致性
        val md5Result1 = testString.md5()
        val md5Result2 = DigestAlgorithm.MD5.digest(testString)
        logger.info("MD5一致性测试: {} == {}", md5Result1, md5Result2)
        assertEquals(md5Result1, md5Result2)
        
        // 测试SHA1一致性
        val sha1Result1 = testString.sha1()
        val sha1Result2 = DigestAlgorithm.SHA1.digest(testString)
        logger.info("SHA1一致性测试: {} == {}", sha1Result1, sha1Result2)
        assertEquals(sha1Result1, sha1Result2)
        
        // 测试SHA256一致性
        val sha256Result1 = testString.sha256()
        val sha256Result2 = DigestAlgorithm.SHA256.digest(testString)
        logger.info("SHA256一致性测试: {} == {}", sha256Result1, sha256Result2)
        assertEquals(sha256Result1, sha256Result2)
    }

    // ==================== 边界情况测试 ====================

    @Test
    @DisplayName("边界情况测试")
    fun testBoundaryCases() {
        logger.info("测试边界情况")
        
        // 测试特殊字符
        val specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?"
        val specialMd5 = specialChars.md5()
        val specialSha1 = specialChars.sha1()
        val specialSha256 = specialChars.sha256()
        
        logger.info("特殊字符MD5: '{}' -> {}", specialChars, specialMd5)
        logger.info("特殊字符SHA1: '{}' -> {}", specialChars, specialSha1)
        logger.info("特殊字符SHA256: '{}' -> {}", specialChars, specialSha256)
        
        assertNotNull(specialMd5)
        assertNotNull(specialSha1)
        assertNotNull(specialSha256)
        
        // 测试中文字符
        val chineseChars = "中文测试字符"
        val chineseMd5 = chineseChars.md5()
        val chineseSha1 = chineseChars.sha1()
        val chineseSha256 = chineseChars.sha256()
        
        logger.info("中文字符MD5: '{}' -> {}", chineseChars, chineseMd5)
        logger.info("中文字符SHA1: '{}' -> {}", chineseChars, chineseSha1)
        logger.info("中文字符SHA256: '{}' -> {}", chineseChars, chineseSha256)
        
        assertNotNull(chineseMd5)
        assertNotNull(chineseSha1)
        assertNotNull(chineseSha256)
        
        // 测试长字符串
        val longString = "a".repeat(1000)
        val longMd5 = longString.md5()
        val longSha1 = longString.sha1()
        val longSha256 = longString.sha256()
        
        logger.info("长字符串MD5: '{}' -> {}", longString.take(20) + "...", longMd5)
        logger.info("长字符串SHA1: '{}' -> {}", longString.take(20) + "...", longSha1)
        logger.info("长字符串SHA256: '{}' -> {}", longString.take(20) + "...", longSha256)
        
        assertNotNull(longMd5)
        assertNotNull(longSha1)
        assertNotNull(longSha256)
    }

    // ==================== 性能测试 ====================

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试摘要算法性能")
        
        val testString = "Tony性能测试字符串"
        val iterations = 1000
        
        // MD5性能测试
        val md5StartTime = System.currentTimeMillis()
        repeat(iterations) {
            testString.md5()
        }
        val md5EndTime = System.currentTimeMillis()
        val md5Duration = md5EndTime - md5StartTime
        
        logger.info("MD5性能测试: {}次迭代耗时 {}ms", iterations, md5Duration)
        assertTrue(md5Duration < 1000) // 应该在1秒内完成
        
        // SHA1性能测试
        val sha1StartTime = System.currentTimeMillis()
        repeat(iterations) {
            testString.sha1()
        }
        val sha1EndTime = System.currentTimeMillis()
        val sha1Duration = sha1EndTime - sha1StartTime
        
        logger.info("SHA1性能测试: {}次迭代耗时 {}ms", iterations, sha1Duration)
        assertTrue(sha1Duration < 1000) // 应该在1秒内完成
        
        // SHA256性能测试
        val sha256StartTime = System.currentTimeMillis()
        repeat(iterations) {
            testString.sha256()
        }
        val sha256EndTime = System.currentTimeMillis()
        val sha256Duration = sha256EndTime - sha256StartTime
        
        logger.info("SHA256性能测试: {}次迭代耗时 {}ms", iterations, sha256Duration)
        assertTrue(sha256Duration < 1000) // 应该在1秒内完成
    }
} 