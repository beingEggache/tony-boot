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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import tony.core.utils.antPathMatchAny
import tony.core.utils.camelToSnakeCase
import tony.core.utils.equalsIgnoreNullOrEmpty
import tony.core.utils.ifNullOrBlank
import tony.core.utils.isJson
import tony.core.utils.isMobileNumber
import tony.core.utils.isNumber
import tony.core.utils.queryStringToMap
import tony.core.utils.queryStringToObj
import tony.core.utils.removeLineBreak
import tony.core.utils.sanitizedPath
import tony.core.utils.snakeToLowerCamelCase
import tony.core.utils.snakeToUpperCamelCase
import tony.core.utils.toNumber
import tony.core.utils.toQueryString
import tony.core.utils.trimQuotes
import tony.core.utils.urlDecode
import tony.core.utils.urlEncode
import tony.core.utils.uuid
import java.math.BigDecimal
import java.math.BigInteger

/**
 * 字符串工具类单元测试
 * @author tangli
 *
 * @date 2025/06/27 17:00
 */
@DisplayName("Strs测试")
class StrsTest {

    // 测试数据类
    data class TestData(val name: String, val age: Int, val city: String?)

    @Nested
    @DisplayName("Strs.uuid()测试")
    inner class UuidTest {
        @Test
        @DisplayName("Strs.uuid():生成UUID")
        fun testUuid() {
            val uuid1 = uuid()
            val uuid2 = uuid()

            assertNotNull(uuid1)
            assertNotNull(uuid2)
            assertEquals(32, uuid1.length)
            assertEquals(32, uuid2.length)
            assertFalse(uuid1.contains("-"))
            assertFalse(uuid2.contains("-"))
            assertTrue(uuid1.all { it.isUpperCase() || it.isDigit() })
            assertTrue(uuid2.all { it.isUpperCase() || it.isDigit() })
            assertNotEquals(uuid1, uuid2)
        }

        @Test
        @DisplayName("Strs.uuid():多次生成不重复")
        fun testUuidUniqueness() {
            val uuids = (1..100).map { uuid() }.toSet()
            assertEquals(100, uuids.size)
        }
    }

    @Nested
    @DisplayName("Strs.isJson()测试")
    inner class IsJsonTest {
        @ParameterizedTest
        @ValueSource(strings = [
            """{"name": "Tony", "age": 30}""",
            """{"data": [1, 2, 3]}""",
            """{"nested": {"key": "value"}}""",
            """{"string": "test", "number": 123, "boolean": true, "null": null}"""
        ])
        @DisplayName("Strs.isJson():有效JSON")
        fun testIsJsonWithValidJson(value: String) {
            assertTrue(value.isJson())
        }

        @ParameterizedTest
        @ValueSource(strings = [
            "invalid json string",
            "{invalid}",
            "name: Tony",
            "undefined"
        ])
        @DisplayName("Strs.isJson():无效JSON")
        fun testIsJsonWithInvalidJson(value: String) {
            assertFalse(value.isJson())
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Strs.isJson():空值")
        fun testIsJsonWithEmptyValues(value: String) {
            assertFalse(value.isJson())
        }
    }

    @Nested
    @DisplayName("Strs.toQueryString()测试")
    inner class ToQueryStringTest {
        @Test
        @DisplayName("Strs.toQueryString():对象转查询字符串")
        fun testToQueryString() {
            val testData = TestData("Tony", 30, null)
            val queryString = testData.toQueryString()

            assertNotNull(queryString)
            assertTrue(queryString.contains("name=Tony"))
            assertTrue(queryString.contains("age=30"))
        }

        @Test
        @DisplayName("Strs.toQueryString():复杂对象")
        fun testToQueryStringWithComplexObject() {
            val complexData = mapOf(
                "user" to TestData("Alice", 25, "Shanghai"),
                "settings" to mapOf("theme" to "dark", "lang" to "zh")
            )
            val queryString = complexData.toQueryString()

            assertNotNull(queryString)
            assertTrue(queryString.isNotEmpty())
        }
    }

    @Nested
    @DisplayName("Strs.equalsIgnoreNullOrEmpty()测试")
    inner class EqualsIgnoreNullOrEmptyTest {
        @Test
        @DisplayName("Strs.equalsIgnoreNullOrEmpty():相同字符串")
        fun testEqualsIgnoreNullOrEmptyWithSameStrings() {
            val str1 = "test"
            val str2 = "test"
            assertTrue(str1.equalsIgnoreNullOrEmpty(str2))
        }

        @Test
        @DisplayName("Strs.equalsIgnoreNullOrEmpty():不同字符串")
        fun testEqualsIgnoreNullOrEmptyWithDifferentStrings() {
            val str1 = "test"
            val str2 = "different"
            assertFalse(str1.equalsIgnoreNullOrEmpty(str2))
        }

        @Test
        @DisplayName("Strs.equalsIgnoreNullOrEmpty():null与null")
        fun testEqualsIgnoreNullOrEmptyWithNullAndNull() {
            val nullStr: String? = null
            assertTrue(nullStr.equalsIgnoreNullOrEmpty(null))
        }

        @Test
        @DisplayName("Strs.equalsIgnoreNullOrEmpty():null与空字符串")
        fun testEqualsIgnoreNullOrEmptyWithNullAndEmpty() {
            val nullStr: String? = null
            val emptyStr = ""
            assertTrue(nullStr.equalsIgnoreNullOrEmpty(emptyStr))
            assertTrue(emptyStr.equalsIgnoreNullOrEmpty(nullStr))
        }

        @Test
        @DisplayName("Strs.equalsIgnoreNullOrEmpty():空字符串与空字符串")
        fun testEqualsIgnoreNullOrEmptyWithEmptyAndEmpty() {
            val emptyStr1 = ""
            val emptyStr2 = ""
            assertTrue(emptyStr1.equalsIgnoreNullOrEmpty(emptyStr2))
        }
    }

    @Nested
    @DisplayName("Strs.Map.toQueryString()测试")
    inner class MapToQueryStringTest {
        @Test
        @DisplayName("Strs.Map.toQueryString():基本Map转换")
        fun testMapToQueryString() {
            val map = mapOf("name" to "Tony", "age" to 30, "city" to "Beijing")
            val queryString = map.toQueryString()

            assertNotNull(queryString)
            assertTrue(queryString.isNotEmpty())
            assertTrue(queryString.contains("name=Tony"))
            assertTrue(queryString.contains("age=30"))
            assertTrue(queryString.contains("city=Beijing"))
        }

        @Test
        @DisplayName("Strs.Map.toQueryString():跳过null值")
        fun testMapToQueryStringSkipNull() {
            val mapWithNull = mapOf("name" to "Tony", "age" to null, "city" to "Beijing")
            val queryString = mapWithNull.toQueryString(skipNull = true)
            assertFalse(queryString.contains("age="))
        }

        @Test
        @DisplayName("Strs.Map.toQueryString():包含null值")
        fun testMapToQueryStringIncludeNull() {
            val mapWithNull = mapOf("name" to "Tony", "age" to null, "city" to "Beijing")
            val queryString = mapWithNull.toQueryString(skipNull = false)
            assertTrue(queryString.contains("age="))
        }

        @Test
        @DisplayName("Strs.Map.toQueryString():跳过空值")
        fun testMapToQueryStringSkipEmpty() {
            val mapWithEmpty = mapOf("name" to "Tony", "desc" to "", "city" to "Beijing")
            val queryString = mapWithEmpty.toQueryString(skipEmpty = true)
            assertFalse(queryString.contains("desc="))
        }

        @Test
        @DisplayName("Strs.Map.toQueryString():空Map")
        fun testMapToQueryStringWithEmptyMap() {
            val emptyMap = mapOf<String, Any>()
            val queryString = emptyMap.toQueryString()
            assertEquals("", queryString)
        }
    }

    @Nested
    @DisplayName("Strs.queryStringToMap()测试")
    inner class QueryStringToMapTest {
        @Test
        @DisplayName("Strs.queryStringToMap():基本查询字符串")
        fun testQueryStringToMap() {
            val queryString = "name=Tony&age=30&city=Beijing"
            val map = queryString.queryStringToMap()

            assertEquals("Tony", map["name"])
            assertEquals("30", map["age"])
            assertEquals("Beijing", map["city"])
        }

        @Test
        @DisplayName("Strs.queryStringToMap():包含空值的查询字符串")
        fun testQueryStringToMapWithEmptyValues() {
            val queryString = "name=Tony&desc=&city=Beijing"
            val map = queryString.queryStringToMap()

            assertEquals("Tony", map["name"])
            assertEquals("", map["desc"])
            assertEquals("Beijing", map["city"])
        }

        @Test
        @DisplayName("Strs.queryStringToMap():空查询字符串")
        fun testQueryStringToMapWithEmptyString() {
            val emptyQuery = ""
            val map = emptyQuery.queryStringToMap()
            assertTrue(map.isEmpty())
        }

        @Test
        @DisplayName("Strs.queryStringToMap():只有键没有值")
        fun testQueryStringToMapWithKeyOnly() {
            val queryString = "name=Tony&age=&city=Beijing"
            val map = queryString.queryStringToMap()

            assertEquals("Tony", map["name"])
            assertEquals("", map["age"])
            assertEquals("Beijing", map["city"])
        }
    }

    @Nested
    @DisplayName("Strs.queryStringToObj()测试")
    inner class QueryStringToObjTest {
        @Test
        @DisplayName("Strs.queryStringToObj():泛型转换")
        fun testQueryStringToObj() {
            val queryString = "name=Tony&age=30&city=Beijing"
            val data = queryString.queryStringToObj<TestData>()

            assertNotNull(data)
            assertEquals("Tony", data.name)
            assertEquals(30, data.age)
            assertEquals("Beijing", data.city)
        }

        @Test
        @DisplayName("Strs.queryStringToObj():Class转换")
        fun testQueryStringToObjWithClass() {
            val queryString = "name=Alice&age=25&city=Shanghai"
            val data = queryString.queryStringToObj(TestData::class.java)

            assertNotNull(data)
            assertEquals("Alice", data.name)
            assertEquals(25, data.age)
            assertEquals("Shanghai", data.city)
        }
    }

    @Nested
    @DisplayName("Strs.ifNullOrBlank()测试")
    inner class IfNullOrBlankTest {
        @Test
        @DisplayName("Strs.ifNullOrBlank():null值使用默认值")
        fun testIfNullOrBlankWithNull() {
            val nullStr: String? = null
            val result = nullStr.ifNullOrBlank("default")
            assertEquals("default", result)
        }

        @Test
        @DisplayName("Strs.ifNullOrBlank():空字符串使用默认值")
        fun testIfNullOrBlankWithEmpty() {
            val emptyStr = ""
            val result = emptyStr.ifNullOrBlank("default")
            assertEquals("default", result)
        }

        @Test
        @DisplayName("Strs.ifNullOrBlank():空白字符串使用默认值")
        fun testIfNullOrBlankWithBlank() {
            val blankStr = "   "
            val result = blankStr.ifNullOrBlank("default")
            assertEquals("default", result)
        }

        @Test
        @DisplayName("Strs.ifNullOrBlank():非空字符串返回原值")
        fun testIfNullOrBlankWithNonEmpty() {
            val nonEmptyStr = "test"
            val result = nonEmptyStr.ifNullOrBlank("default")
            assertEquals("test", result)
        }

        @Test
        @DisplayName("Strs.ifNullOrBlank():函数默认值")
        fun testIfNullOrBlankWithFunction() {
            val nullStr: String? = null
            val result = nullStr.ifNullOrBlank { "computed" }
            assertEquals("computed", result)
        }
    }

    @Nested
    @DisplayName("Strs.isMobileNumber()测试")
    inner class IsMobileNumberTest {
        @ParameterizedTest
        @ValueSource(strings = [
            "13800138000",
            "13912345678",
            "18612345678",
            "18912345678",
            "15012345678",
            "15112345678",
            "15212345678",
            "15312345678",
            "15512345678",
            "15612345678",
            "15712345678",
            "15812345678",
            "15912345678",
            "18012345678",
            "18112345678",
            "18212345678",
            "18312345678",
            "18412345678",
            "18512345678",
            "18712345678",
            "18812345678",
            "18912345678"
        ])
        @DisplayName("Strs.isMobileNumber():有效手机号")
        fun testIsMobileNumberWithValidNumbers(value: String) {
            assertTrue(value.isMobileNumber())
        }

        @ParameterizedTest
        @ValueSource(strings = [
            "12345678901",
            "1380013800",
            "138001380000",
            "1234567890",
            "abcdefghijk",
            "1380013800a",
            "1380013800@",
            "1380013800#"
        ])
        @DisplayName("Strs.isMobileNumber():无效手机号")
        fun testIsMobileNumberWithInvalidNumbers(value: String) {
            assertFalse(value.isMobileNumber())
        }
    }

    @Nested
    @DisplayName("Strs.isNumber()测试")
    inner class IsNumberTest {
        @ParameterizedTest
        @ValueSource(strings = [
            "123",
            "0",
            "-123",
            "123.456",
            "-123.456",
            "0.0",
            "12345678901234567890",
            "123.45678901234567890"
        ])
        @DisplayName("Strs.isNumber():有效数字")
        fun testIsNumberWithValidNumbers(value: String) {
            assertTrue(value.isNumber())
        }

        @ParameterizedTest
        @ValueSource(strings = [
            "abc",
            "123abc",
            "abc123",
            "12.34.56",
            "12-34",
            "12@34",
            "12 34",
            ""
        ])
        @DisplayName("Strs.isNumber():无效数字")
        fun testIsNumberWithInvalidNumbers(value: String) {
            assertFalse(value.isNumber())
        }
    }

    @Nested
    @DisplayName("Strs.toNumber()测试")
    inner class ToNumberTest {
        @Test
        @DisplayName("Strs.toNumber():转Long")
        fun testToNumberToLong() {
            val result = "123".toNumber(Long::class.java)
            assertEquals(123L, result)
        }

        @Test
        @DisplayName("Strs.toNumber():转Int")
        fun testToNumberToInt() {
            val result = "123".toNumber(Int::class.java)
            assertEquals(123, result)
        }

        @Test
        @DisplayName("Strs.toNumber():转Double")
        fun testToNumberToDouble() {
            val result = "123.456".toNumber(Double::class.java)
            assertEquals(123.456, result)
        }

        @Test
        @DisplayName("Strs.toNumber():转BigDecimal")
        fun testToNumberToBigDecimal() {
            val result = "123.456".toNumber(BigDecimal::class.java)
            assertEquals(BigDecimal("123.456"), result)
        }

        @Test
        @DisplayName("Strs.toNumber():转BigInteger")
        fun testToNumberToBigInteger() {
            val result = "12345678901234567890".toNumber(BigInteger::class.java)
            assertEquals(BigInteger("12345678901234567890"), result)
        }

        @Test
        @DisplayName("Strs.toNumber():不支持的类型")
        fun testToNumberWithUnsupportedType() {
            assertThrows(IllegalArgumentException::class.java) {
                "123".toNumber(String::class.java)
            }
        }
    }

    @Nested
    @DisplayName("Strs.urlEncode()测试")
    inner class UrlEncodeTest {
        @Test
        @DisplayName("Strs.urlEncode():基本编码")
        fun testUrlEncode() {
            val result = "Hello World".urlEncode()
            assertEquals("Hello+World", result)
        }

        @Test
        @DisplayName("Strs.urlEncode():特殊字符")
        fun testUrlEncodeWithSpecialChars() {
            val result = "Hello@World#123".urlEncode()
            assertEquals("Hello%40World%23123", result)
        }

        @Test
        @DisplayName("Strs.urlEncode():null值")
        fun testUrlEncodeWithNull() {
            val nullStr: String? = null
            val result = nullStr.urlEncode()
            assertEquals("", result)
        }

        @Test
        @DisplayName("Strs.urlEncode():空字符串")
        fun testUrlEncodeWithEmpty() {
            val result = "".urlEncode()
            assertEquals("", result)
        }
    }

    @Nested
    @DisplayName("Strs.urlDecode()测试")
    inner class UrlDecodeTest {
        @Test
        @DisplayName("Strs.urlDecode():基本解码")
        fun testUrlDecode() {
            val result = "Hello+World".urlDecode()
            assertEquals("Hello World", result)
        }

        @Test
        @DisplayName("Strs.urlDecode():特殊字符")
        fun testUrlDecodeWithSpecialChars() {
            val result = "Hello%40World%23123".urlDecode()
            assertEquals("Hello@World#123", result)
        }

        @Test
        @DisplayName("Strs.urlDecode():null值")
        fun testUrlDecodeWithNull() {
            val nullStr: String? = null
            val result = nullStr.urlDecode()
            assertEquals("", result)
        }

        @Test
        @DisplayName("Strs.urlDecode():空字符串")
        fun testUrlDecodeWithEmpty() {
            val result = "".urlDecode()
            assertEquals("", result)
        }
    }

    @Nested
    @DisplayName("Strs.removeLineBreak()测试")
    inner class RemoveLineBreakTest {
        @Test
        @DisplayName("Strs.removeLineBreak():去除换行符")
        fun testRemoveLineBreak() {
            val text = "Hello\nWorld\r\nTest"
            val result = text.removeLineBreak()
            assertEquals("HelloWorldTest", result)
        }

        @Test
        @DisplayName("Strs.removeLineBreak():无换行符")
        fun testRemoveLineBreakWithoutLineBreaks() {
            val text = "Hello World"
            val result = text.removeLineBreak()
            assertEquals("Hello World", result)
        }

        @Test
        @DisplayName("Strs.removeLineBreak():空字符串")
        fun testRemoveLineBreakWithEmpty() {
            val result = "".removeLineBreak()
            assertEquals("", result)
        }
    }

    @Nested
    @DisplayName("Strs.antPathMatchAny()测试")
    inner class AntPathMatchAnyTest {
        @Test
        @DisplayName("Strs.antPathMatchAny():匹配成功")
        fun testAntPathMatchAnySuccess() {
            val path = "/api/users/123"
            val patterns = listOf("/api/**", "/users/*", "/api/users/*")
            assertTrue(path.antPathMatchAny(patterns))
        }

        @Test
        @DisplayName("Strs.antPathMatchAny():匹配失败")
        fun testAntPathMatchAnyFailure() {
            val path = "/api/users/123"
            val patterns = listOf("/admin/**", "/users/*")
            assertFalse(path.antPathMatchAny(patterns))
        }

        @Test
        @DisplayName("Strs.antPathMatchAny():null路径")
        fun testAntPathMatchAnyWithNullPath() {
            val nullPath: String? = null
            val patterns = listOf("/api/**")
            assertFalse(nullPath.antPathMatchAny(patterns))
        }

        @Test
        @DisplayName("Strs.antPathMatchAny():null模式")
        fun testAntPathMatchAnyWithNullPatterns() {
            val path = "/api/users/123"
            assertFalse(path.antPathMatchAny(null))
        }
    }

    @Nested
    @DisplayName("Strs.camelToSnakeCase()测试")
    inner class CamelToSnakeCaseTest {
        @Test
        @DisplayName("Strs.camelToSnakeCase():基本转换")
        fun testCamelToSnakeCase() {
            val result = "userName".camelToSnakeCase()
            assertEquals("user_name", result)
        }

        @Test
        @DisplayName("Strs.camelToSnakeCase():多个大写字母")
        fun testCamelToSnakeCaseWithMultipleCaps() {
            val result = "userNameTest".camelToSnakeCase()
            assertEquals("user_name_test", result)
        }

        @Test
        @DisplayName("Strs.camelToSnakeCase():首字母大写")
        fun testCamelToSnakeCaseWithFirstCap() {
            val result = "UserName".camelToSnakeCase()
            assertEquals("user_name", result)
        }

        @Test
        @DisplayName("Strs.camelToSnakeCase():全小写")
        fun testCamelToSnakeCaseAllLower() {
            val result = "username".camelToSnakeCase()
            assertEquals("username", result)
        }
    }

    @Nested
    @DisplayName("Strs.snakeToLowerCamelCase()测试")
    inner class SnakeToLowerCamelCaseTest {
        @Test
        @DisplayName("Strs.snakeToLowerCamelCase():基本转换")
        fun testSnakeToLowerCamelCase() {
            val result = "user_name".snakeToLowerCamelCase()
            assertEquals("userName", result)
        }

        @Test
        @DisplayName("Strs.snakeToLowerCamelCase():多个下划线")
        fun testSnakeToLowerCamelCaseWithMultipleUnderscores() {
            val result = "user_name_test".snakeToLowerCamelCase()
            assertEquals("userNameTest", result)
        }

        @Test
        @DisplayName("Strs.snakeToLowerCamelCase():无下划线")
        fun testSnakeToLowerCamelCaseWithoutUnderscores() {
            val result = "username".snakeToLowerCamelCase()
            assertEquals("username", result)
        }
    }

    @Nested
    @DisplayName("Strs.snakeToUpperCamelCase()测试")
    inner class SnakeToUpperCamelCaseTest {
        @Test
        @DisplayName("Strs.snakeToUpperCamelCase():基本转换")
        fun testSnakeToUpperCamelCase() {
            val result = "user_name".snakeToUpperCamelCase()
            assertEquals("UserName", result)
        }

        @Test
        @DisplayName("Strs.snakeToUpperCamelCase():多个下划线")
        fun testSnakeToUpperCamelCaseWithMultipleUnderscores() {
            val result = "user_name_test".snakeToUpperCamelCase()
            assertEquals("UserNameTest", result)
        }

        @Test
        @DisplayName("Strs.snakeToUpperCamelCase():无下划线")
        fun testSnakeToUpperCamelCaseWithoutUnderscores() {
            val result = "username".snakeToUpperCamelCase()
            assertEquals("Username", result)
        }
    }

    @Nested
    @DisplayName("Strs.sanitizedPath()测试")
    inner class SanitizedPathTest {
        @Test
        @DisplayName("Strs.sanitizedPath():清理重复斜杠")
        fun testSanitizedPath() {
            val result = sanitizedPath("//api//users///123//")
            assertEquals("/api/users/123/", result)
        }

        @Test
        @DisplayName("Strs.sanitizedPath():无重复斜杠")
        fun testSanitizedPathWithoutDuplicates() {
            val result = sanitizedPath("/api/users/123")
            assertEquals("/api/users/123", result)
        }

        @Test
        @DisplayName("Strs.sanitizedPath():空字符串")
        fun testSanitizedPathWithEmpty() {
            val result = sanitizedPath("")
            assertEquals("", result)
        }
    }

    @Nested
    @DisplayName("Strs.trimQuotes()测试")
    inner class TrimQuotesTest {
        @Test
        @DisplayName("Strs.trimQuotes():去除单引号")
        fun testTrimQuotesWithSingleQuotes() {
            val result = "'Hello World'".trimQuotes()
            assertEquals("Hello World", result)
        }

        @Test
        @DisplayName("Strs.trimQuotes():去除双引号")
        fun testTrimQuotesWithDoubleQuotes() {
            val result = "\"Hello World\"".trimQuotes()
            assertEquals("Hello World", result)
        }

        @Test
        @DisplayName("Strs.trimQuotes():只有开始引号")
        fun testTrimQuotesWithStartQuoteOnly() {
            val result = "'Hello World".trimQuotes()
            assertEquals("'Hello World", result)
        }

        @Test
        @DisplayName("Strs.trimQuotes():只有结束引号")
        fun testTrimQuotesWithEndQuoteOnly() {
            val result = "Hello World'".trimQuotes()
            assertEquals("Hello World'", result)
        }

        @Test
        @DisplayName("Strs.trimQuotes():无引号")
        fun testTrimQuotesWithoutQuotes() {
            val result = "Hello World".trimQuotes()
            assertEquals("Hello World", result)
        }

        @Test
        @DisplayName("Strs.trimQuotes():短字符串")
        fun testTrimQuotesWithShortString() {
            val result = "A".trimQuotes()
            assertEquals("A", result)
        }

        @Test
        @DisplayName("Strs.trimQuotes():空字符串")
        fun testTrimQuotesWithEmpty() {
            val result = "".trimQuotes()
            assertEquals("", result)
        }
    }
}
