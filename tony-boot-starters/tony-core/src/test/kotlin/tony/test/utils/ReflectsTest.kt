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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.utils.*
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Reflects 工具类单元测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Reflects测试")
class ReflectsTest {

    data class User(var name: String, var age: Int)

    @Nested
    @DisplayName("Reflects.descriptor()测试")
    inner class DescriptorTest {
        @Test
        @DisplayName("Reflects.descriptor():字段描述符")
        fun testDescriptorOnField() {
            val field: Field = User::class.java.getDeclaredField("name")
            assertNotNull(field.descriptor())
        }

        @Test
        @DisplayName("Reflects.descriptor():方法描述符")
        fun testDescriptorOnMethod() {
            val method: Method = User::class.java.getMethod("getName")
            assertNotNull(method.descriptor())
        }
    }

    @Nested
    @DisplayName("Reflects.getter()测试")
    inner class GetterTest {
        @Test
        @DisplayName("Reflects.getter():字段getter")
        fun testGetterOnField() {
            val field: Field = User::class.java.getDeclaredField("name")
            assertNotNull(field.getter())
        }

        @Test
        @DisplayName("Reflects.getter():方法getter")
        fun testGetterOnMethod() {
            val method: Method = User::class.java.getMethod("getName")
            assertNotNull(method.getter())
        }
    }

    @Nested
    @DisplayName("Reflects.setter()测试")
    inner class SetterTest {
        @Test
        @DisplayName("Reflects.setter():字段setter")
        fun testSetterOnField() {
            val field: Field = User::class.java.getDeclaredField("name")
            assertNotNull(field.setter())
        }

        @Test
        @DisplayName("Reflects.setter():方法setter")
        fun testSetterOnMethod() {
            val method: Method = User::class.java.getMethod("getName")
            assertNotNull(method.setter())
        }
    }

    @Nested
    @DisplayName("Reflects.field()测试")
    inner class FieldTest {
        @Test
        @DisplayName("Reflects.field():字段field")
        fun testFieldOnField() {
            val field: Field = User::class.java.getDeclaredField("name")
            assertEquals(field, field.field())
        }

        @Test
        @DisplayName("Reflects.field():方法field")
        fun testFieldOnMethod() {
            val method: Method = User::class.java.getMethod("getName")
            assertNotNull(method.field())
        }
    }

    @Nested
    @DisplayName("Reflects.setValueFirstUseSetter()测试")
    inner class SetValueFirstUseSetterTest {
        @Test
        @DisplayName("Reflects.setValueFirstUseSetter():设置字段值")
        fun testSetValueFirstUseSetter() {
            val user = User("tony", 18)
            val field: Field = User::class.java.getDeclaredField("name")
            field.setValueFirstUseSetter(user, "newName")
            assertEquals("newName", user.name)
        }
    }

    @Nested
    @DisplayName("Reflects.getValueFirstUseGetter()测试")
    inner class GetValueFirstUseGetterTest {
        @Test
        @DisplayName("Reflects.getValueFirstUseGetter():获取字段值")
        fun testGetValueFirstUseGetter() {
            val user = User("tony", 18)
            val field: Field = User::class.java.getDeclaredField("name")
            val value = field.getValueFirstUseGetter(user)
            assertEquals("tony", value)
        }
    }

    @Nested
    @DisplayName("Reflects边界与异常测试")
    inner class EdgeCasesTest {
        @Test
        @DisplayName("Reflects.descriptor():无属性类字段")
        fun testDescriptorWithNoPropField() {
            class NoProp
            val field = NoProp::class.java.getDeclaredFields().firstOrNull()
            if (field != null) {
                assertNull(field.descriptor())
            }
        }

        @Test
        @DisplayName("Reflects.getter():无属性类字段")
        fun testGetterWithNoPropField() {
            class NoProp
            val field = NoProp::class.java.getDeclaredFields().firstOrNull()
            if (field != null) {
                assertNull(field.getter())
            }
        }

        @Test
        @DisplayName("Reflects.setter():无属性类字段")
        fun testSetterWithNoPropField() {
            class NoProp
            val field = NoProp::class.java.getDeclaredFields().firstOrNull()
            if (field != null) {
                assertNull(field.setter())
            }
        }

        @Test
        @DisplayName("Reflects.field():无属性类字段")
        fun testFieldWithNoPropField() {
            class NoProp
            val field = NoProp::class.java.getDeclaredFields().firstOrNull()
            if (field != null) {
                assertEquals(field, field.field())
            }
        }

        @Test
        @DisplayName("Reflects.descriptor():无属性类方法")
        fun testDescriptorWithNoPropMethod() {
            class NoProp
            val method = NoProp::class.java.methods.firstOrNull()
            if (method != null) {
                assertNull(method.descriptor())
            }
        }

        @Test
        @DisplayName("Reflects.getter():无属性类方法")
        fun testGetterWithNoPropMethod() {
            class NoProp
            val method = NoProp::class.java.methods.firstOrNull()
            if (method != null) {
                assertNull(method.getter())
            }
        }

        @Test
        @DisplayName("Reflects.setter():无属性类方法")
        fun testSetterWithNoPropMethod() {
            class NoProp
            val method = NoProp::class.java.methods.firstOrNull()
            if (method != null) {
                assertNull(method.setter())
            }
        }

        @Test
        @DisplayName("Reflects.field():无属性类方法")
        fun testFieldWithNoPropMethod() {
            class NoProp
            val method = NoProp::class.java.methods.firstOrNull()
            if (method != null) {
                assertNull(method.field())
            }
        }
    }
}
