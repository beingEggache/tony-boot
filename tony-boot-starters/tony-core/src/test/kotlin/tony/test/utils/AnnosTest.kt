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

/**
 * Annos 工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
@DisplayName("Annos测试")
class AnnosTest {

    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class MyAnno(val value: String = "test")

    class Demo {
        @field:MyAnno("field")
        @get:MyAnno("field")
        @set:MyAnno("field")
        var name: String = "tony"
    }

    @Nested
    @DisplayName("Annos.annotationFromSelfOrGetterOrSetter()测试")
    inner class AnnotationFromSelfOrGetterOrSetterTest {
        @Test
        @DisplayName("Annos.annotationFromSelfOrGetterOrSetter():注解查找")
        fun testAnnotationFromSelfOrGetterOrSetter() {
            val field = Demo::class.java.getDeclaredField("name")
            val anno = field.annotationFromSelfOrGetterOrSetter(MyAnno::class.java)
            assertNotNull(anno)
            assertEquals("field", anno?.value)
        }
    }

    @Nested
    @DisplayName("Annos.hasAnnotation()测试")
    inner class HasAnnotationTest {
        @Test
        @DisplayName("Annos.hasAnnotation():字段注解判定")
        fun testHasAnnotationOnField() {
            val field = Demo::class.java.getDeclaredField("name")
            assertTrue(field.hasAnnotation(MyAnno::class.java))
        }

        @Test
        @DisplayName("Annos.hasAnnotation():方法注解判定")
        fun testHasAnnotationOnMethod() {
            val method = Demo::class.java.getMethod("getName")
            assertTrue(method.hasAnnotation(MyAnno::class.java))
        }

        @Test
        @DisplayName("Annos.hasAnnotation():类注解判定")
        fun testHasAnnotationOnClass() {
            assertFalse(Demo::class.java.hasAnnotation(MyAnno::class.java))
        }
    }

    @Nested
    @DisplayName("Annos.annotation()测试")
    inner class AnnotationTest {
        @Test
        @DisplayName("Annos.annotation():字段注解获取")
        fun testAnnotationOnField() {
            val field = Demo::class.java.getDeclaredField("name")
            val anno = field.annotation(MyAnno::class.java)
            assertNotNull(anno)
        }

        @Test
        @DisplayName("Annos.annotation():方法注解获取")
        fun testAnnotationOnMethod() {
            val method = Demo::class.java.getMethod("getName")
            val anno = method.annotation(MyAnno::class.java)
            assertNotNull(anno)
        }

        @Test
        @DisplayName("Annos.annotation():类注解获取")
        fun testAnnotationOnClass() {
            val anno = Demo::class.java.annotation(MyAnno::class.java)
            assertNull(anno)
        }
    }

    @Nested
    @DisplayName("Annos边界与异常测试")
    inner class EdgeCasesTest {
        @Test
        @DisplayName("Annos.annotationFromSelfOrGetterOrSetter():无注解字段")
        fun testAnnotationFromSelfOrGetterOrSetterWithNoAnnotation() {
            class NoAnno { var x: Int = 1 }
            val field = NoAnno::class.java.getDeclaredField("x")
            assertNull(field.annotationFromSelfOrGetterOrSetter(MyAnno::class.java))
        }

        @Test
        @DisplayName("Annos.hasAnnotation():无注解字段")
        fun testHasAnnotationWithNoAnnotation() {
            class NoAnno { var x: Int = 1 }
            val field = NoAnno::class.java.getDeclaredField("x")
            assertFalse(field.hasAnnotation(MyAnno::class.java))
        }

        @Test
        @DisplayName("Annos.annotation():无注解方法")
        fun testAnnotationWithNoAnnotation() {
            class NoAnno { fun getter() = 1 }
            val method = NoAnno::class.java.getMethod("getter")
            assertNull(method.annotation(MyAnno::class.java))
        }

        @Test
        @DisplayName("Annos.hasAnnotation():无注解方法")
        fun testHasAnnotationWithNoAnnotationMethod() {
            class NoAnno { fun getter() = 1 }
            val method = NoAnno::class.java.getMethod("getter")
            assertFalse(method.hasAnnotation(MyAnno::class.java))
        }
    }
} 