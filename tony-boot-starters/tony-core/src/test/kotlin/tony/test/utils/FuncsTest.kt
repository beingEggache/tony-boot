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
 * 函数式编程工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object FuncsTest {

    private val logger = LoggerFactory.getLogger(FuncsTest::class.java)

    @Test
    @DisplayName("不相等判断测试")
    fun testNotEquals() {
        logger.info("测试不相等判断")
        
        // 测试不相等
        assertTrue("hello".notEquals("world"))
        logger.info("'hello' != 'world': true")
        
        assertTrue(123.notEquals(456))
        logger.info("123 != 456: true")
        
        assertTrue(null.notEquals("test"))
        logger.info("null != 'test': true")
        
        // 测试相等
        assertFalse("hello".notEquals("hello"))
        logger.info("'hello' != 'hello': false")
        
        assertFalse(123.notEquals(123))
        logger.info("123 != 123: false")
        
        assertFalse(null.notEquals(null))
        logger.info("null != null: false")
    }

    @Test
    @DisplayName("null默认值测试")
    fun testIfNull() {
        logger.info("测试null默认值")
        
        // 测试非null值
        val nonNullValue = "test"
        val result1 = nonNullValue.ifNull("default")
        assertEquals("test", result1)
        logger.info("非null值 '{}' 使用默认值结果: {}", nonNullValue, result1)
        
        // 测试null值
        val nullValue: String? = null
        val result2 = nullValue.ifNull("default")
        assertEquals("default", result2)
        logger.info("null值使用默认值结果: {}", result2)
        
        // 测试数字类型
        val nullInt: Int? = null
        val result3 = nullInt.ifNull(100)
        assertEquals(100, result3)
        logger.info("null Int使用默认值结果: {}", result3)
    }

    @Test
    @DisplayName("null回调默认值测试")
    fun testIfNullCallback() {
        logger.info("测试null回调默认值")
        
        var callbackCount = 0
        
        // 测试非null值
        val nonNullValue = "test"
        val result1 = nonNullValue.ifNull { 
            callbackCount++
            "default"
        }
        assertEquals("test", result1)
        assertEquals(0, callbackCount)
        logger.info("非null值回调执行次数: {}", callbackCount)
        
        // 测试null值
        val nullValue: String? = null
        val result2 = nullValue.ifNull { 
            callbackCount++
            "default"
        }
        assertEquals("default", result2)
        assertEquals(1, callbackCount)
        logger.info("null值回调执行次数: {}", callbackCount)
    }

    @Test
    @DisplayName("条件执行测试")
    fun testRunIf() {
        logger.info("测试条件执行")
        
        var executionCount = 0
        
        // 测试条件为真
        val result1 = true.runIf { 
            executionCount++
            "executed"
        }
        assertEquals("executed", result1)
        assertEquals(1, executionCount)
        logger.info("条件为真时执行结果: {}, 执行次数: {}", result1, executionCount)
        
        // 测试条件为假
        val result2 = false.runIf { 
            executionCount++
            "not executed"
        }
        assertNull(result2)
        assertEquals(1, executionCount) // 没有增加
        logger.info("条件为假时执行结果: {}, 执行次数: {}", result2, executionCount)
    }

    @Test
    @DisplayName("条件执行无返回值测试")
    fun testAlsoIf() {
        logger.info("测试条件执行无返回值")
        
        var executionCount = 0
        
        // 测试条件为真
        true.alsoIf { 
            executionCount++
        }
        assertEquals(1, executionCount)
        logger.info("条件为真时执行次数: {}", executionCount)
        
        // 测试条件为假
        false.alsoIf { 
            executionCount++
        }
        assertEquals(1, executionCount) // 没有增加
        logger.info("条件为假时执行次数: {}", executionCount)
    }

    @Test
    @DisplayName("对象条件执行测试")
    fun testObjectRunIf() {
        logger.info("测试对象条件执行")
        
        val testObject = "test"
        var executionCount = 0
        
        // 测试条件为真
        val result1 = testObject.runIf(true) { 
            executionCount++
            this.uppercase()
        }
        assertEquals("TEST", result1)
        assertEquals(1, executionCount)
        logger.info("对象条件为真时执行结果: {}, 执行次数: {}", result1, executionCount)
        
        // 测试条件为假
        val result2 = testObject.runIf(false) { 
            executionCount++
            this.uppercase()
        }
        assertNull(result2)
        assertEquals(1, executionCount) // 没有增加
        logger.info("对象条件为假时执行结果: {}, 执行次数: {}", result2, executionCount)
    }

    @Test
    @DisplayName("对象条件let测试")
    fun testObjectLetIf() {
        logger.info("测试对象条件let")
        
        val testObject = "test"
        var executionCount = 0
        
        // 测试条件为真
        val result1 = testObject.letIf(true) { 
            executionCount++
            it.uppercase()
        }
        assertEquals("TEST", result1)
        assertEquals(1, executionCount)
        logger.info("对象条件为真时let结果: {}, 执行次数: {}", result1, executionCount)
        
        // 测试条件为假
        val result2 = testObject.letIf(false) { 
            executionCount++
            it.uppercase()
        }
        assertNull(result2)
        assertEquals(1, executionCount) // 没有增加
        logger.info("对象条件为假时let结果: {}, 执行次数: {}", result2, executionCount)
    }

    @Test
    @DisplayName("对象条件apply测试")
    fun testObjectApplyIf() {
        logger.info("测试对象条件apply")
        
        data class TestData(var value: String = "initial")
        
        val testObject = TestData()
        var executionCount = 0
        
        // 测试条件为真
        val result1 = testObject.applyIf(true) { 
            executionCount++
            this.value = "modified"
        }
        assertEquals("modified", result1.value)
        assertEquals(1, executionCount)
        logger.info("对象条件为真时apply结果: {}, 执行次数: {}", result1.value, executionCount)
        
        // 测试条件为假
        val result2 = testObject.applyIf(false) { 
            executionCount++
            this.value = "not modified"
        }
        assertEquals("modified", result2.value) // 值没有改变
        assertEquals(1, executionCount) // 没有增加
        logger.info("对象条件为假时apply结果: {}, 执行次数: {}", result2.value, executionCount)
    }

    @Test
    @DisplayName("对象条件also测试")
    fun testObjectAlsoIf() {
        logger.info("测试对象条件also")
        
        data class TestData(var value: String = "initial")
        
        val testObject = TestData()
        var executionCount = 0
        
        // 测试条件为真
        val result1 = testObject.alsoIf(true) { 
            executionCount++
            it.value = "modified"
        }
        assertEquals("modified", result1.value)
        assertEquals(1, executionCount)
        logger.info("对象条件为真时also结果: {}, 执行次数: {}", result1.value, executionCount)
        
        // 测试条件为假
        val result2 = testObject.alsoIf(false) { 
            executionCount++
            it.value = "not modified"
        }
        assertEquals("modified", result2.value) // 值没有改变
        assertEquals(1, executionCount) // 没有增加
        logger.info("对象条件为假时also结果: {}, 执行次数: {}", result2.value, executionCount)
    }

    @Test
    @DisplayName("异常抛出测试")
    fun testThrowIf() {
        logger.info("测试异常抛出")
        
        // 测试 throwIfTrue 条件为真时抛出异常
        assertThrows(RuntimeException::class.java) {
            true.throwIfTrue("条件为真，应该抛出异常")
        }
        logger.info("throwIfTrue: 条件为真时正确抛出异常")

        // 测试 throwIfTrue 条件为假时不抛出异常
        assertDoesNotThrow {
            false.throwIfTrue("条件为假，不应该抛出异常")
        }
        logger.info("throwIfTrue: 条件为假时不抛出异常")

        // 测试 throwIfFalse 条件为假时抛出异常
        assertThrows(RuntimeException::class.java) {
            false.throwIfFalse("条件为假，应该抛出异常")
        }
        logger.info("throwIfFalse: 条件为假时正确抛出异常")
        
        // 测试 throwIfFalse 条件为真时不抛出异常
        assertDoesNotThrow {
            true.throwIfFalse("条件为真，不应该抛出异常")
        }
        logger.info("throwIfFalse: 条件为真时不抛出异常")
    }

    @Test
    @DisplayName("复杂条件组合测试")
    fun testComplexConditions() {
        logger.info("测试复杂条件组合")
        
        val testString = "hello"
        var executionCount = 0
        
        // 测试多个条件组合
        val result1 = testString
            .runIf(testString.length > 3) { 
                executionCount++
                this.uppercase()
            }
            ?.runIf(testString.contains("o")) { 
                executionCount++
                this + "!"
            }
        
        assertEquals("HELLO!", result1)
        assertEquals(2, executionCount)
        logger.info("复杂条件组合结果: {}, 执行次数: {}", result1, executionCount)
    }

    @Test
    @DisplayName("链式调用测试")
    fun testChainedCalls() {
        logger.info("测试链式调用")
        
        data class Person(var name: String = "", var age: Int = 0)
        
        val person = Person()
        var executionCount = 0
        
        val result = person
            .applyIf(true) { 
                executionCount++
                this.name = "Tony"
            }
            .applyIf(true) { 
                executionCount++
                this.age = 25
            }
            .applyIf(false) { 
                executionCount++
                this.name = "Not Tony"
            }
        
        assertEquals("Tony", result.name)
        assertEquals(25, result.age)
        assertEquals(2, executionCount)
        logger.info("链式调用结果: name={}, age={}, 执行次数: {}", result.name, result.age, executionCount)
    }

    @Test
    @DisplayName("边界情况测试")
    fun testEdgeCases() {
        logger.info("测试边界情况")
        
        // 测试空字符串
        val emptyString = ""
        val result1 = emptyString.ifNullOrBlank("default")
        assertEquals("default", result1)
        logger.info("空字符串默认值结果: {}", result1)
        
        // 测试空白字符串
        val blankString = "   "
        val result2 = blankString.ifNullOrBlank("default")
        assertEquals("default", result2)
        logger.info("空白字符串默认值结果: {}", result2)
        
        // 测试零值
        val zeroValue = 0
        val result3 = zeroValue.ifNull(100)
        assertEquals(0, result3)
        logger.info("零值默认值结果: {}", result3)
        
        // 测试false值
        val falseValue = false
        val result4 = falseValue.ifNull(true)
        assertEquals(false, result4)
        logger.info("false值默认值结果: {}", result4)
    }

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试性能")
        
        val iterations = 10000
        var executionCount = 0
        
        val startTime = System.currentTimeMillis()
        
        repeat(iterations) {
            "test".runIf(true) { 
                executionCount++
                this.uppercase()
            }
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        assertEquals(iterations, executionCount)
        logger.info("执行{}次条件判断耗时: {}ms", iterations, duration)
        logger.info("平均每次执行耗时: {}ms", duration.toDouble() / iterations)
    }

    @Test
    @DisplayName("throwIfNull 方法测试")
    fun testThrowIfNull() {
        logger.info("测试 throwIfNull")
        // 非null时返回自身
        val value: String? = "abc"
        assertEquals("abc", value.throwIfNull("不应抛出"))
        // null时抛出异常
        val nullValue: String? = null
        assertThrows(RuntimeException::class.java) {
            nullValue.throwIfNull("为null应抛出")
        }
        logger.info("throwIfNull: null时正确抛出异常")
    }

    @Test
    @DisplayName("throwIfEmpty (Collection) 方法测试")
    fun testThrowIfEmptyCollection() {
        logger.info("测试 throwIfEmpty (Collection)")
        // 非空集合返回自身
        val list = listOf(1, 2, 3)
        assertEquals(list, list.throwIfEmpty("不应抛出"))
        // 空集合抛出异常
        val emptyList = listOf<Int>()
        assertThrows(RuntimeException::class.java) {
            emptyList.throwIfEmpty("空集合应抛出")
        }
        // null集合抛出异常
        val nullList: List<Int>? = null
        assertThrows(RuntimeException::class.java) {
            nullList.throwIfEmpty("null集合应抛出")
        }
        logger.info("throwIfEmpty(Collection): 空或null时正确抛出异常")
    }

    @Test
    @DisplayName("throwIfEmpty (Map) 方法测试")
    fun testThrowIfEmptyMap() {
        logger.info("测试 throwIfEmpty (Map)")
        // 非空Map返回自身
        val map = mapOf("a" to 1)
        assertEquals(map, map.throwIfEmpty("不应抛出"))
        // 空Map抛出异常
        val emptyMap = mapOf<String, Int>()
        assertThrows(RuntimeException::class.java) {
            emptyMap.throwIfEmpty("空Map应抛出")
        }
        // nullMap抛出异常
        val nullMap: Map<String, Int>? = null
        assertThrows(RuntimeException::class.java) {
            nullMap.throwIfEmpty("nullMap应抛出")
        }
        logger.info("throwIfEmpty(Map): 空或null时正确抛出异常")
    }

    @Test
    @DisplayName("throwIfNullOrEmpty (CharSequence) 方法测试")
    fun testThrowIfNullOrEmptyCharSequence() {
        logger.info("测试 throwIfNullOrEmpty (CharSequence)")
        // 非空字符串返回自身
        val str: String? = "abc"
        assertEquals("abc", str.throwIfNullOrEmpty("不应抛出"))
        // 空字符串抛出异常
        val emptyStr = ""
        assertThrows(RuntimeException::class.java) {
            emptyStr.throwIfNullOrEmpty("空字符串应抛出")
        }
        // null字符串抛出异常
        val nullStr: String? = null
        assertThrows(RuntimeException::class.java) {
            nullStr.throwIfNullOrEmpty("null字符串应抛出")
        }
        logger.info("throwIfNullOrEmpty(CharSequence): 空或null时正确抛出异常")
    }
}
