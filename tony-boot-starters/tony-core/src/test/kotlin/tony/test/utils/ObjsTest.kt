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
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import tony.utils.*

/**
 * Objs 工具类单元测试
 * @author tangli
 * @date 2024/06/10
 */
object ObjsTest {
    private val logger = LoggerFactory.getLogger(ObjsTest::class.java)

    @Test
    @DisplayName("asTo 类型转换测试")
    fun testAsTo() {
        logger.info("测试 asTo 类型转换")
        val str: Any = "hello"
        val result: String? = str.asTo()
        assertEquals("hello", result)
        assertThrows<ClassCastException> {
            val intResult: Int? = str.asTo()
        }
    }

    @Test
    @DisplayName("asToDefault 默认值测试")
    fun testAsToDefault() {
        logger.info("测试 asToDefault 默认值")
        val str: Any = "hello"
        val result: String = str.asToDefault("default")
        assertEquals("hello", result)
        assertThrows<ClassCastException> {
            val intResult: Int = str.asToDefault(123)
        }
        val nullObj: Any? = null
        val defaultStr: String = nullObj.asToDefault("default")
        assertEquals("default", defaultStr)
    }

    @Test
    @DisplayName("asToNotNull 非空类型转换测试")
    fun testAsToNotNull() {
        logger.info("测试 asToNotNull 非空类型转换")
        val str: Any = "hello"
        val result: String = str.asToNotNull()
        assertEquals("hello", result)
        assertThrows<ClassCastException> {
            @Suppress("UnusedVariable", "unused")
            val asToNotNull = (123).asToNotNull<String>()
        }
    }

    @Test
    @DisplayName("notBlank 非空判断测试")
    fun testNotBlank() {
        logger.info("测试 notBlank 非空判断")
        assertFalse(null.notBlank())
        assertFalse("".notBlank())
        assertFalse("   ".notBlank())
        assertTrue("abc".notBlank())
        assertTrue(123.notBlank())
    }

    data class User(val name: String, val age: Int)

    @Test
    @DisplayName("copyTo 属性复制测试")
    fun testCopyTo() {
        logger.info("测试 copyTo 属性复制")
        val user = User("tony", 18)
        val map = mapOf("name" to "tony", "age" to 18)
        val user2 = map.copyTo<User>()
        assertEquals(user, user2)
        val user3 = map.copyTo(User::class.java)
        assertEquals(user, user3)
    }

    @Test
    @DisplayName("println 扩展测试")
    fun testPrintln() {
        logger.info("测试 println 扩展")
        val str: String? = "hello"
        str.println() // 仅保证不抛异常
        val nullStr: String? = null
        nullStr.println() // 仅保证不抛异常
    }
}
