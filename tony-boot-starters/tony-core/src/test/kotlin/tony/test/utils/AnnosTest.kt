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
 * Annos 工具类单元测试
 * @author tangli
 * @date 2024/06/10
 */
object AnnosTest {
    private val logger = LoggerFactory.getLogger(AnnosTest::class.java)

    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class MyAnno(val value: String = "test")

    class Demo {
        @field:MyAnno("field")
        @get:MyAnno("field")
        @set:MyAnno("field")
        var name: String = "tony"
    }

    @Test
    @DisplayName("annotationFromSelfOrGetterOrSetter 注解查找测试")
    fun testAnnotationFromSelfOrGetterOrSetter() {
        logger.info("测试 annotationFromSelfOrGetterOrSetter 注解查找")
        val field = Demo::class.java.getDeclaredField("name")
        val anno = field.annotationFromSelfOrGetterOrSetter(MyAnno::class.java)
        assertNotNull(anno)
        assertEquals("field", anno?.value)
    }

    @Test
    @DisplayName("hasAnnotation 注解判定测试")
    fun testHasAnnotation() {
        logger.info("测试 hasAnnotation 注解判定")
        val field = Demo::class.java.getDeclaredField("name")
        assertTrue(field.hasAnnotation(MyAnno::class.java))
        val method = Demo::class.java.getMethod("getName")
        assertTrue(method.hasAnnotation(MyAnno::class.java))
        assertFalse(Demo::class.java.hasAnnotation(MyAnno::class.java))
    }

    @Test
    @DisplayName("annotation 注解获取测试")
    fun testAnnotation() {
        logger.info("测试 annotation 注解获取")
        val field = Demo::class.java.getDeclaredField("name")
        val anno = field.annotation(MyAnno::class.java)
        assertNotNull(anno)
        val method = Demo::class.java.getMethod("getName")
        val anno2 = method.annotation(MyAnno::class.java)
        assertNotNull(anno2)
        val anno3 = Demo::class.java.annotation(MyAnno::class.java)
        assertNull(anno3)
    }

    @Test
    @DisplayName("注解边界与异常测试")
    fun testAnnotationEdgeCases() {
        logger.info("测试注解工具类边界与异常")
        // 非注解字段/方法
        class NoAnno { var x: Int = 1; fun getter() = x }
        val field = NoAnno::class.java.getDeclaredField("x")
        assertNull(field.annotationFromSelfOrGetterOrSetter(MyAnno::class.java))
        assertFalse(field.hasAnnotation(MyAnno::class.java))
        val method = NoAnno::class.java.getMethod("getX")
        assertNull(method.annotation(MyAnno::class.java))
        assertFalse(method.hasAnnotation(MyAnno::class.java))
    }
}
