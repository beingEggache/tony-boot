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
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Reflects 工具类单元测试
 * @author tangli
 * @date 2024/06/10
 */
object ReflectsTest {
    private val logger = LoggerFactory.getLogger(ReflectsTest::class.java)

    data class User(var name: String, var age: Int)

    @Test
    @DisplayName("descriptor/getter/setter/field 测试")
    fun testDescriptorGetterSetterField() {
        logger.info("测试 descriptor/getter/setter/field")
        val field: Field = User::class.java.getDeclaredField("name")
        val method: Method = User::class.java.getMethod("getName")
        assertNotNull(field.descriptor())
        assertNotNull(method.descriptor())
        assertNotNull(field.getter())
        assertNotNull(field.setter())
        assertNotNull(method.getter())
        assertNotNull(method.setter())
        assertEquals(field, field.field())
        assertEquals(field, method.field())
    }

    @Test
    @DisplayName("setValueFirstUseSetter/getValueFirstUseGetter 测试")
    fun testSetValueFirstUseSetterAndGetValueFirstUseGetter() {
        logger.info("测试 setValueFirstUseSetter/getValueFirstUseGetter")
        val user = User("tony", 18)
        val field: Field = User::class.java.getDeclaredField("name")
        field.setValueFirstUseSetter(user, "newName")
        assertEquals("newName", user.name)
        val value = field.getValueFirstUseGetter(user)
        assertEquals("newName", value)
    }

    @Test
    @DisplayName("边界与异常测试")
    fun testEdgeCases() {
        logger.info("测试 Reflects 工具类边界与异常")
        class NoProp
        val field = NoProp::class.java.getDeclaredFields().firstOrNull()
        if (field != null) {
            assertNull(field.descriptor())
            assertNull(field.getter())
            assertNull(field.setter())
            assertEquals(field, field.field())
        }
        val method = NoProp::class.java.methods.firstOrNull()
        if (method != null) {
            assertNull(method.descriptor())
            assertNull(method.getter())
            assertNull(method.setter())
            assertNull(method.field())
        }
    }
} 