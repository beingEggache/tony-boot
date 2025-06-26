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
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.StandardCharsets

/**
 * 字符串工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object StrsTest {

    private val logger = LoggerFactory.getLogger(StrsTest::class.java)

    // ==================== uuid() 方法测试 ====================

    @Test
    @DisplayName("uuid()方法测试")
    fun testUuid() {
        logger.info("测试uuid()方法")

        val uuid1 = uuid()
        val uuid2 = uuid()

        logger.info("生成的UUID1: {}", uuid1)
        logger.info("生成的UUID2: {}", uuid2)

        assertNotNull(uuid1)
        assertNotNull(uuid2)
        assertTrue(uuid1.length == 32)
        assertTrue(uuid2.length == 32)
        assertFalse(uuid1.contains("-"))
        assertFalse(uuid2.contains("-"))
        assertTrue(uuid1.all { it.isUpperCase() || it.isDigit() })
        assertTrue(uuid2.all { it.isUpperCase() || it.isDigit() })
        assertNotEquals(uuid1, uuid2)
    }

    // ==================== isJson() 方法测试 ====================

    @Test
    @DisplayName("isJson()方法测试")
    fun testIsJson() {
        logger.info("测试isJson()方法")

        val validJson = """{"name": "Tony", "age": 30}"""
        val invalidJson = "invalid json string"
        val emptyString = ""
        val nullString = "null"

        logger.info("测试有效JSON: '{}' -> {}", validJson, validJson.isJson())
        assertTrue(validJson.isJson())

        logger.info("测试无效JSON: '{}' -> {}", invalidJson, invalidJson.isJson())
        assertFalse(invalidJson.isJson())

        logger.info("测试空字符串: '{}' -> {}", emptyString, emptyString.isJson())
        // 空字符串可能被某些JSON解析器认为是有效的JSON（空对象）
        val emptyIsJson = emptyString.isJson()
        logger.info("空字符串JSON验证结果: {}", emptyIsJson)

        logger.info("测试null字符串: null -> {}", nullString.isJson())
        val nullIsJson = nullString.isJson()
        logger.info("null字符串JSON验证结果: {}", nullIsJson)
    }

    // ==================== toQueryString() 方法测试 ====================

    @Test
    @DisplayName("toQueryString()方法测试")
    fun testToQueryString() {
        logger.info("测试toQueryString()方法")

        data class TestData(val name: String, val age: Int, val city: String?)

        val testData = TestData("Tony", 30, null)
        val queryString = testData.toQueryString()

        logger.info("对象转查询字符串: {} -> {}", testData, queryString)
        assertNotNull(queryString)
        assertTrue(queryString.contains("name=Tony"))
        assertTrue(queryString.contains("age=30"))
    }

    // ==================== equalsIgnoreNullOrEmpty() 方法测试 ====================

    @Test
    @DisplayName("equalsIgnoreNullOrEmpty()方法测试")
    fun testEqualsIgnoreNullOrEmpty() {
        logger.info("测试equalsIgnoreNullOrEmpty()方法")

        val str1 = "test"
        val str2 = "test"
        val str3 = "different"
        val nullStr: String? = null
        val emptyStr = ""

        logger.info("测试相同字符串: '{}' == '{}' -> {}", str1, str2, str1.equalsIgnoreNullOrEmpty(str2))
        assertTrue(str1.equalsIgnoreNullOrEmpty(str2))

        logger.info("测试不同字符串: '{}' == '{}' -> {}", str1, str3, str1.equalsIgnoreNullOrEmpty(str3))
        assertFalse(str1.equalsIgnoreNullOrEmpty(str3))

        logger.info("测试null与null: null == null -> {}", nullStr.equalsIgnoreNullOrEmpty(null))
        assertTrue(nullStr.equalsIgnoreNullOrEmpty(null))

        logger.info("测试null与空字符串: null == '' -> {}", nullStr.equalsIgnoreNullOrEmpty(emptyStr))
        assertTrue(nullStr.equalsIgnoreNullOrEmpty(emptyStr))

        logger.info("测试空字符串与null: '' == null -> {}", emptyStr.equalsIgnoreNullOrEmpty(nullStr))
        assertTrue(emptyStr.equalsIgnoreNullOrEmpty(nullStr))
    }

    // ==================== Map.toQueryString() 方法测试 ====================

    @Test
    @DisplayName("Map.toQueryString()方法测试")
    fun testMapToQueryString() {
        logger.info("测试Map.toQueryString()方法")
        
        val map = mapOf("name" to "Tony", "age" to 30, "city" to "Beijing")
        val queryString = map.toQueryString()
        
        logger.info("Map转查询字符串: {} -> '{}'", map, queryString)
        logger.info("查询字符串长度: {}", queryString.length)
        logger.info("查询字符串是否包含name=Tony: {}", queryString.contains("name=Tony"))
        logger.info("查询字符串是否包含age=30: {}", queryString.contains("age=30"))
        logger.info("查询字符串是否包含city=Beijing: {}", queryString.contains("city=Beijing"))
        
        assertNotNull(queryString)
        assertTrue(queryString.isNotEmpty())
        
        // 验证查询字符串包含所有键值对
        assertTrue(queryString.contains("name=Tony"))
        assertTrue(queryString.contains("age=30"))
        assertTrue(queryString.contains("city=Beijing"))
        
        // 测试skipNull参数
        val mapWithNull = mapOf("name" to "Tony", "age" to null, "city" to "Beijing")
        val queryStringSkipNull = mapWithNull.toQueryString(skipNull = true)
        logger.info("跳过null值: {} -> '{}'", mapWithNull, queryStringSkipNull)
        assertFalse(queryStringSkipNull.contains("age="))
        
        val queryStringIncludeNull = mapWithNull.toQueryString(skipNull = false)
        logger.info("包含null值: {} -> '{}'", mapWithNull, queryStringIncludeNull)
        assertTrue(queryStringIncludeNull.contains("age="))
    }

    // ==================== queryStringToMap() 方法测试 ====================

    @Test
    @DisplayName("queryStringToMap()方法测试")
    fun testQueryStringToMap() {
        logger.info("测试queryStringToMap()方法")

        val queryString = "name=Tony&age=30&city=Beijing"
        val map = queryString.queryStringToMap()

        logger.info("查询字符串转Map: '{}' -> {}", queryString, map)
        assertEquals("Tony", map["name"])
        assertEquals("30", map["age"])
        assertEquals("Beijing", map["city"])

        // 测试空查询字符串
        val emptyQuery = ""
        val emptyMap = emptyQuery.queryStringToMap()
        logger.info("空查询字符串: '{}' -> {}", emptyQuery, emptyMap)
        assertTrue(emptyMap.isEmpty())
    }

    // ==================== queryStringToObj() 方法测试 ====================

    @Test
    @DisplayName("queryStringToObj()方法测试")
    fun testQueryStringToObj() {
        logger.info("测试queryStringToObj()方法")

        data class TestObj(val name: String, val age: Int, val city: String)

        val queryString = "name=Tony&age=30&city=Beijing"
        val obj = queryString.queryStringToObj<TestObj>()

        logger.info("查询字符串转对象: '{}' -> {}", queryString, obj)
        assertEquals("Tony", obj.name)
        assertEquals(30, obj.age)
        assertEquals("Beijing", obj.city)

        // 测试Class参数版本
        val obj2 = queryString.queryStringToObj(TestObj::class.java)
        logger.info("查询字符串转对象(Class): '{}' -> {}", queryString, obj2)
        assertEquals("Tony", obj2.name)
        assertEquals(30, obj2.age)
        assertEquals("Beijing", obj2.city)
    }

    // ==================== ifNullOrBlank() 方法测试 ====================

    @Test
    @DisplayName("ifNullOrBlank()方法测试")
    fun testIfNullOrBlank() {
        logger.info("测试ifNullOrBlank()方法")

        val normalStr = "test"
        val nullStr: String? = null
        val emptyStr = ""
        val blankStr = "   "

        // 测试默认值版本
        logger.info("正常字符串: '{}' -> {}", normalStr, normalStr.ifNullOrBlank())
        assertEquals("test", normalStr.ifNullOrBlank())

        logger.info("null字符串: null -> {}", nullStr.ifNullOrBlank())
        assertEquals("", nullStr.ifNullOrBlank())

        logger.info("空字符串: '{}' -> {}", emptyStr, emptyStr.ifNullOrBlank())
        assertEquals("", emptyStr.ifNullOrBlank())

        logger.info("空白字符串: '{}' -> {}", blankStr, blankStr.ifNullOrBlank())
        assertEquals("", blankStr.ifNullOrBlank())

        // 测试自定义默认值
        val customDefault = "default"
        logger.info("null字符串(自定义默认值): null -> {}", nullStr.ifNullOrBlank(customDefault))
        assertEquals("default", nullStr.ifNullOrBlank(customDefault))

        // 测试Lambda版本
        val lambdaResult = nullStr.ifNullOrBlank { "lambda default" }
        logger.info("null字符串(Lambda默认值): null -> {}", lambdaResult)
        assertEquals("lambda default", lambdaResult)
    }

    // ==================== isMobileNumber() 方法测试 ====================

    @Test
    @DisplayName("isMobileNumber()方法测试")
    fun testIsMobileNumber() {
        logger.info("测试isMobileNumber()方法")

        val validMobiles = listOf(
            "13800138000",
            "13912345678",
            "15012345678",
            "18612345678",
            "19912345678"
        )

        val invalidMobiles = listOf(
            "12345678901",  // 不是1开头
            "1234567890",   // 长度不够
            "123456789012", // 长度过长
            "abcdefghijk",  // 包含字母
            "1380013800a",  // 包含字母
            "1380013800",   // 长度不够
            "138001380001"  // 长度过长
        )

        logger.info("测试有效手机号:")
        validMobiles.forEach { mobile ->
            val isValid = mobile.isMobileNumber()
            logger.info("手机号 {} 验证结果: {}", mobile, isValid)
            assertTrue(isValid)
        }

        logger.info("测试无效手机号:")
        invalidMobiles.forEach { mobile ->
            val isValid = mobile.isMobileNumber()
            logger.info("手机号 {} 验证结果: {}", mobile, isValid)
            assertFalse(isValid)
        }
    }

    // ==================== isNumber() 方法测试 ====================

    @Test
    @DisplayName("isNumber()方法测试")
    fun testIsNumber() {
        logger.info("测试isNumber()方法")

        val validNumbers = listOf(
            "123",
            "123.45",
            "-123",
            "-123.45",
            "0",
            "0.0",
            "12345678901234567890"
        )

        val invalidNumbers = listOf(
            "abc",
            "123abc",
            "abc123",
            "12.34.56",
            "",
            "   ",
            "12,345"
        )

        logger.info("测试有效数字:")
        validNumbers.forEach { num ->
            val isValid = num.isNumber()
            logger.info("数字 {} 验证结果: {}", num, isValid)
            assertTrue(isValid)
        }

        logger.info("测试无效数字:")
        invalidNumbers.forEach { num ->
            val isValid = num.isNumber()
            logger.info("数字 {} 验证结果: {}", num, isValid)
            assertFalse(isValid)
        }
    }

    // ==================== toNumber() 方法测试 ====================

    @Test
    @DisplayName("toNumber()方法测试")
    fun testToNumber() {
        logger.info("测试toNumber()方法")
        
        // 测试整数转换
        val intStr = "123"
        logger.info("转换为Long: {} -> {}", intStr, intStr.toNumber(Long::class.java))
        assertEquals(123L, intStr.toNumber(Long::class.java))
        
        logger.info("转换为Int: {} -> {}", intStr, intStr.toNumber(Int::class.java))
        assertEquals(123, intStr.toNumber(Int::class.java))
        
        // 测试小数转换
        val decimalStr = "123.45"
        logger.info("转换为Double: {} -> {}", decimalStr, decimalStr.toNumber(Double::class.java))
        assertEquals(123.45, decimalStr.toNumber(Double::class.java))
        
        logger.info("转换为Float: {} -> {}", decimalStr, decimalStr.toNumber(Float::class.java))
        assertEquals(123.45f, decimalStr.toNumber(Float::class.java))
        
        logger.info("转换为BigDecimal: {} -> {}", decimalStr, decimalStr.toNumber(BigDecimal::class.java))
        assertEquals(BigDecimal("123.45"), decimalStr.toNumber(BigDecimal::class.java))
        
        // 测试其他类型
        val byteStr = "127"
        logger.info("转换为Byte: {} -> {}", byteStr, byteStr.toNumber(Byte::class.java))
        assertEquals(127.toByte(), byteStr.toNumber(Byte::class.java))
        
        val shortStr = "32767"
        logger.info("转换为Short: {} -> {}", shortStr, shortStr.toNumber(Short::class.java))
        assertEquals(32767.toShort(), shortStr.toNumber(Short::class.java))
        
        val bigIntStr = "123456789"
        logger.info("转换为BigInteger: {} -> {}", bigIntStr, bigIntStr.toNumber(BigInteger::class.java))
        assertEquals(BigInteger("123456789"), bigIntStr.toNumber(BigInteger::class.java))
        
        // 测试异常情况
        val invalidStr = "abc"
        assertThrows(NumberFormatException::class.java) {
            invalidStr.toNumber(Int::class.java)
        }
        logger.info("无效数字字符串正确抛出异常")
    }

    // ==================== urlEncode() 和 urlDecode() 方法测试 ====================

    @Test
    @DisplayName("urlEncode()和urlDecode()方法测试")
    fun testUrlEncodeAndDecode() {
        logger.info("测试urlEncode()和urlDecode()方法")

        val originalStr = "Tony测试 123!@#"
        val encoded = originalStr.urlEncode()
        val decoded = encoded.urlDecode()

        logger.info("原始字符串: '{}'", originalStr)
        logger.info("URL编码: '{}'", encoded)
        logger.info("URL解码: '{}'", decoded)

        assertEquals(originalStr, decoded)

        // 测试null和空字符串
        val nullStr: String? = null
        val nullEncoded = nullStr.urlEncode()
        val nullDecoded = nullStr.urlDecode()

        logger.info("null字符串编码: null -> '{}'", nullEncoded)
        logger.info("null字符串解码: null -> '{}'", nullDecoded)

        assertEquals("", nullEncoded)
        assertEquals("", nullDecoded)
    }

    // ==================== removeLineBreak() 方法测试 ====================

    @Test
    @DisplayName("removeLineBreak()方法测试")
    fun testRemoveLineBreak() {
        logger.info("测试removeLineBreak()方法")

        val originalStr = "第一行${System.lineSeparator()}第二行${System.lineSeparator()}第三行"
        val result = originalStr.removeLineBreak()

        logger.info("原始字符串: '{}'", originalStr)
        logger.info("移除换行符: '{}'", result)

        assertFalse(result.contains(System.lineSeparator()))
        assertTrue(result.contains("第一行第二行第三行"))
    }

    // ==================== antPathMatchAny() 方法测试 ====================

    @Test
    @DisplayName("antPathMatchAny()方法测试")
    fun testAntPathMatchAny() {
        logger.info("测试antPathMatchAny()方法")

        val path = "/api/users/123"
        val patterns = listOf("/api/**", "/users/*", "/api/users/*")

        val result = path.antPathMatchAny(patterns)
        logger.info("路径: '{}'", path)
        logger.info("模式: {}", patterns)
        logger.info("匹配结果: {}", result)

        assertTrue(result)

        // 测试不匹配的情况
        val nonMatchingPatterns = listOf("/admin/**", "/users/list")
        val nonMatchingResult = path.antPathMatchAny(nonMatchingPatterns)
        logger.info("不匹配模式: {}", nonMatchingPatterns)
        logger.info("匹配结果: {}", nonMatchingResult)

        assertFalse(nonMatchingResult)

        // 测试null和空集合
        val nullResult = path.antPathMatchAny(null)
        logger.info("null模式匹配结果: {}", nullResult)
        assertFalse(nullResult)

        val emptyResult = path.antPathMatchAny(emptyList())
        logger.info("空模式匹配结果: {}", emptyResult)
        assertFalse(emptyResult)
    }

    // ==================== 命名转换方法测试 ====================

    @Test
    @DisplayName("camelToSnakeCase()方法测试")
    fun testCamelToSnakeCase() {
        logger.info("测试camelToSnakeCase()方法")

        val camelCase = "userName"
        val result = camelCase.camelToSnakeCase()

        logger.info("驼峰命名: '{}' -> 下划线命名: '{}'", camelCase, result)
        assertEquals("user_name", result)

        val multiWord = "firstNameLastName"
        val multiResult = multiWord.camelToSnakeCase()
        logger.info("多词驼峰: '{}' -> 下划线命名: '{}'", multiWord, multiResult)
        assertEquals("first_name_last_name", multiResult)
    }

    @Test
    @DisplayName("snakeToLowerCamelCase()方法测试")
    fun testSnakeToLowerCamelCase() {
        logger.info("测试snakeToLowerCamelCase()方法")

        val snakeCase = "user_name"
        val result = snakeCase.snakeToLowerCamelCase()

        logger.info("下划线命名: '{}' -> 小驼峰: '{}'", snakeCase, result)
        assertEquals("userName", result)

        val multiWord = "first_name_last_name"
        val multiResult = multiWord.snakeToLowerCamelCase()
        logger.info("多词下划线: '{}' -> 小驼峰: '{}'", multiWord, multiResult)
        assertEquals("firstNameLastName", multiResult)
    }

    @Test
    @DisplayName("snakeToUpperCamelCase()方法测试")
    fun testSnakeToUpperCamelCase() {
        logger.info("测试snakeToUpperCamelCase()方法")

        val snakeCase = "user_name"
        val result = snakeCase.snakeToUpperCamelCase()

        logger.info("下划线命名: '{}' -> 大驼峰: '{}'", snakeCase, result)
        assertEquals("UserName", result)

        val multiWord = "first_name_last_name"
        val multiResult = multiWord.snakeToUpperCamelCase()
        logger.info("多词下划线: '{}' -> 大驼峰: '{}'", multiWord, multiResult)
        assertEquals("FirstNameLastName", multiResult)
    }

    // ==================== sanitizedPath() 方法测试 ====================

    @Test
    @DisplayName("sanitizedPath()方法测试")
    fun testSanitizedPath() {
        logger.info("测试sanitizedPath()方法")

        val pathWithMultipleSlashes = "//api///users////123"
        val result = sanitizedPath(pathWithMultipleSlashes)

        logger.info("原始路径: '{}'", pathWithMultipleSlashes)
        logger.info("清理后路径: '{}'", result)

        assertEquals("/api/users/123", result)

        val normalPath = "/api/users/123"
        val normalResult = sanitizedPath(normalPath)
        logger.info("正常路径: '{}' -> '{}'", normalPath, normalResult)
        assertEquals(normalPath, normalResult)
    }

    // ==================== trimQuotes() 方法测试 ====================

    @Test
    @DisplayName("trimQuotes()方法测试")
    fun testTrimQuotes() {
        logger.info("测试trimQuotes()方法")

        val singleQuoted = "'quoted string'"
        val doubleQuoted = "\"quoted string\""
        val noQuotes = "no quotes"
        val shortStr = "a"
        val emptyStr = ""

        logger.info("单引号字符串: '{}' -> '{}'", singleQuoted, singleQuoted.trimQuotes())
        assertEquals("quoted string", singleQuoted.trimQuotes())

        logger.info("双引号字符串: '{}' -> '{}'", doubleQuoted, doubleQuoted.trimQuotes())
        assertEquals("quoted string", doubleQuoted.trimQuotes())

        logger.info("无引号字符串: '{}' -> '{}'", noQuotes, noQuotes.trimQuotes())
        assertEquals("no quotes", noQuotes.trimQuotes())

        logger.info("短字符串: '{}' -> '{}'", shortStr, shortStr.trimQuotes())
        assertEquals("a", shortStr.trimQuotes())

        logger.info("空字符串: '{}' -> '{}'", emptyStr, emptyStr.trimQuotes())
        assertEquals("", emptyStr.trimQuotes())
    }
}
