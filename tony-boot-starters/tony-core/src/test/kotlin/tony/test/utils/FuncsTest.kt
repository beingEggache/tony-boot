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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.LoggerFactory
import tony.utils.*

/**
 * 函数式编程工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
@DisplayName("Funcs测试")
class FuncsTest {

    private val logger = LoggerFactory.getLogger(FuncsTest::class.java)

    // 测试用的数据类
    data class TestData(var value: String = "initial")
    data class Person(var name: String = "", var age: Int = 0)

    @Nested
    @DisplayName("Funcs.notEquals()测试")
    inner class NotEqualsTest {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test"])
        @DisplayName("Funcs.notEquals():不相等")
        fun testNotEqualsWhenDifferent(value: String) {
            val otherValue = if (value == "hello") "world" else "hello"
            assertTrue(value.notEquals(otherValue))
        }
        
        @ParameterizedTest
        @ValueSource(ints = [123, 456, 789])
        @DisplayName("Funcs.notEquals():数字不相等")
        fun testNotEqualsWhenDifferentNumbers(value: Int) {
            val otherValue = value + 1
            assertTrue(value.notEquals(otherValue))
        }
        
        @Test
        @DisplayName("Funcs.notEquals():null值")
        fun testNotEqualsWithNull() {
            assertTrue(null.notEquals("test"))
            assertTrue("test".notEquals(null))
        }
        
        @Test
        @DisplayName("Funcs.notEquals():相等")
        fun testNotEqualsWhenEqual() {
            assertFalse("hello".notEquals("hello"))
            assertFalse(123.notEquals(123))
            assertFalse(null.notEquals(null))
        }
    }

    @Nested
    @DisplayName("Funcs.ifNull()测试")
    inner class IfNullTest {
        @ParameterizedTest
        @ValueSource(strings = ["test", "hello", "world"])
        @DisplayName("Funcs.ifNull():非null值")
        fun testIfNullWithNonNullValue(value: String) {
            val result = value.ifNull("default")
            assertEquals(value, result)
        }
        
        @ParameterizedTest
        @ValueSource(ints = [100, 200, 300])
        @DisplayName("Funcs.ifNull():数字类型非null")
        fun testIfNullWithNonNullNumber(value: Int) {
            val result = value.ifNull(999)
            assertEquals(value, result)
        }
        
        @Test
        @DisplayName("Funcs.ifNull():null值")
        fun testIfNullWithNullValue() {
            val nullValue: String? = null
            val result = nullValue.ifNull("default")
            assertEquals("default", result)
        }
        
        @Test
        @DisplayName("Funcs.ifNull():数字类型null")
        fun testIfNullWithNumberType() {
            val nullInt: Int? = null
            val result = nullInt.ifNull(100)
            assertEquals(100, result)
        }
    }

    @Nested
    @DisplayName("Funcs.ifNull()回调测试")
    inner class IfNullCallbackTest {
        @ParameterizedTest
        @ValueSource(strings = ["test", "hello", "world"])
        @DisplayName("Funcs.ifNull():非null值回调")
        fun testIfNullCallbackWithNonNullValue(value: String) {
            var callbackCount = 0
            val result = value.ifNull { 
                callbackCount++
                "default"
            }
            assertEquals(value, result)
            assertEquals(0, callbackCount)
        }
        
        @Test
        @DisplayName("Funcs.ifNull():null值回调")
        fun testIfNullCallbackWithNullValue() {
            var callbackCount = 0
            val nullValue: String? = null
            val result = nullValue.ifNull { 
                callbackCount++
                "default"
            }
            assertEquals("default", result)
            assertEquals(1, callbackCount)
        }
    }

    @Nested
    @DisplayName("Funcs.runIf()测试")
    inner class RunIfTest {
        @Test
        @DisplayName("Funcs.runIf():条件为真")
        fun testRunIfWhenConditionTrue() {
            var executionCount = 0
            val result = true.runIf { 
                executionCount++
                "executed"
            }
            assertEquals("executed", result)
            assertEquals(1, executionCount)
        }
        @Test
        @DisplayName("Funcs.runIf():条件为假")
        fun testRunIfWhenConditionFalse() {
            var executionCount = 0
            val result = false.runIf { 
                executionCount++
                "not executed"
            }
            assertNull(result)
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.alsoIf()测试")
    inner class AlsoIfTest {
        @Test
        @DisplayName("Funcs.alsoIf():条件为真")
        fun testAlsoIfWhenConditionTrue() {
            var executionCount = 0
            true.alsoIf { 
                executionCount++
            }
            assertEquals(1, executionCount)
        }
        @Test
        @DisplayName("Funcs.alsoIf():条件为假")
        fun testAlsoIfWhenConditionFalse() {
            var executionCount = 0
            false.alsoIf { 
                executionCount++
            }
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.runIf()对象测试")
    inner class ObjectRunIfTest {
        @ParameterizedTest
        @ValueSource(strings = ["test", "hello", "world"])
        @DisplayName("Funcs.runIf():对象条件为真")
        fun testObjectRunIfWhenConditionTrue(value: String) {
            var executionCount = 0
            val result = value.runIf(true) { 
                executionCount++
                this.uppercase()
            }
            assertEquals(value.uppercase(), result)
            assertEquals(1, executionCount)
        }
        @Test
        @DisplayName("Funcs.runIf():对象条件为假")
        fun testObjectRunIfWhenConditionFalse() {
            val testObject = "test"
            var executionCount = 0
            val result = testObject.runIf(false) { 
                executionCount++
                this.uppercase()
            }
            assertNull(result)
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.letIf()测试")
    inner class LetIfTest {
        @ParameterizedTest
        @ValueSource(strings = ["test", "hello", "world"])
        @DisplayName("Funcs.letIf():条件为真")
        fun testLetIfWhenConditionTrue(value: String) {
            var executionCount = 0
            val result = value.letIf(true) { 
                executionCount++
                it.uppercase()
            }
            assertEquals(value.uppercase(), result)
            assertEquals(1, executionCount)
        }
        @Test
        @DisplayName("Funcs.letIf():条件为假")
        fun testLetIfWhenConditionFalse() {
            val testObject = "test"
            var executionCount = 0
            val result = testObject.letIf(false) { 
                executionCount++
                it.uppercase()
            }
            assertNull(result)
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.applyIf()测试")
    inner class ApplyIfTest {
        @ParameterizedTest
        @ValueSource(strings = ["modified", "changed", "updated"])
        @DisplayName("Funcs.applyIf():条件为真")
        fun testApplyIfWhenConditionTrue(newValue: String) {
            val testObject = TestData()
            var executionCount = 0
            val result = testObject.applyIf(true) { 
                executionCount++
                this.value = newValue
            }
            assertEquals(newValue, result.value)
            assertEquals(1, executionCount)
        }
        @Test
        @DisplayName("Funcs.applyIf():条件为假")
        fun testApplyIfWhenConditionFalse() {
            val testObject = TestData()
            var executionCount = 0
            val result = testObject.applyIf(false) { 
                executionCount++
                this.value = "not modified"
            }
            assertEquals("initial", result.value)
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.alsoIf()对象测试")
    inner class ObjectAlsoIfTest {
        @ParameterizedTest
        @ValueSource(strings = ["modified", "changed", "updated"])
        @DisplayName("Funcs.alsoIf():对象条件为真")
        fun testObjectAlsoIfWhenConditionTrue(newValue: String) {
            val testObject = TestData()
            var executionCount = 0
            val result = testObject.alsoIf(true) { 
                executionCount++
                it.value = newValue
            }
            assertEquals(newValue, result.value)
            assertEquals(1, executionCount)
        }
        @Test
        @DisplayName("Funcs.alsoIf():对象条件为假")
        fun testObjectAlsoIfWhenConditionFalse() {
            val testObject = TestData()
            var executionCount = 0
            val result = testObject.alsoIf(false) { 
                executionCount++
                it.value = "not modified"
            }
            assertEquals("initial", result.value)
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.throwIf()测试")
    inner class ThrowIfTest {
        @ParameterizedTest
        @ValueSource(strings = ["条件为真，应该抛出异常", "测试异常消息1", "测试异常消息2"])
        @DisplayName("Funcs.throwIfTrue():条件为真")
        fun testThrowIfTrueWhenConditionTrue(message: String) {
            assertThrows(RuntimeException::class.java) {
                true.throwIfTrue(message)
            }
        }
        @Test
        @DisplayName("Funcs.throwIfTrue():条件为假")
        fun testThrowIfTrueWhenConditionFalse() {
            assertDoesNotThrow {
                false.throwIfTrue("条件为假，不应该抛出异常")
            }
        }
        @ParameterizedTest
        @ValueSource(strings = ["条件为假，应该抛出异常", "测试异常消息3", "测试异常消息4"])
        @DisplayName("Funcs.throwIfFalse():条件为假")
        fun testThrowIfFalseWhenConditionFalse(message: String) {
            assertThrows(RuntimeException::class.java) {
                false.throwIfFalse(message)
            }
        }
        @Test
        @DisplayName("Funcs.throwIfFalse():条件为真")
        fun testThrowIfFalseWhenConditionTrue() {
            assertDoesNotThrow {
                true.throwIfFalse("条件为真，不应该抛出异常")
            }
        }
    }

    @Nested
    @DisplayName("Funcs.复杂条件组合测试")
    inner class ComplexConditionsTest {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test"])
        @DisplayName("Funcs.复杂条件组合:正常流程")
        fun testComplexConditionsNormalFlow(testString: String) {
            var executionCount = 0
            val result = testString
                .runIf(testString.length > 3) { 
                    executionCount++
                    this.uppercase()
                }
                ?.runIf(testString.contains("o")) { 
                    executionCount++
                    this + "!"
                }
            
            when {
                testString.length > 3 && testString.contains("o") -> {
                    assertEquals(testString.uppercase() + "!", result)
                    assertEquals(2, executionCount)
                }
                testString.length > 3 -> {
                    assertNull(result)
                    assertEquals(1, executionCount)
                }
                else -> {
                    assertNull(result)
                    assertEquals(0, executionCount)
                }
            }
        }
        
        @Test
        @DisplayName("Funcs.复杂条件组合:调试test参数")
        fun testComplexConditionsDebug() {
            val testString = "test"
            var executionCount = 0
            
            println("testString: $testString")
            println("testString.length: ${testString.length}")
            println("testString.length > 3: ${testString.length > 3}")
            println("testString.contains('o'): ${testString.contains("o")}")
            
            val firstResult = testString.runIf(testString.length > 3) { 
                executionCount++
                println("First runIf executed, executionCount: $executionCount")
                this.uppercase()
            }
            println("First result: $firstResult")
            
            val result = firstResult?.runIf(testString.contains("o")) { 
                executionCount++
                println("Second runIf executed, executionCount: $executionCount")
                this + "!"
            }
            println("Final result: $result")
            println("Final executionCount: $executionCount")
            
            assertNull(result)
            assertEquals(1, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.链式调用测试")
    inner class ChainedCallsTest {
        @ParameterizedTest
        @ValueSource(strings = ["Tony", "Alice", "Bob"])
        @DisplayName("Funcs.链式调用:条件为真")
        fun testChainedCallsWhenConditionsTrue(name: String) {
            val person = Person()
            var executionCount = 0
            val result = person
                .applyIf(true) { 
                    executionCount++
                    this.name = name
                }
                .applyIf(true) { 
                    executionCount++
                    this.age = 25
                }
            assertEquals(name, result.name)
            assertEquals(25, result.age)
            assertEquals(2, executionCount)
        }
        @Test
        @DisplayName("Funcs.链式调用:条件为假")
        fun testChainedCallsWhenConditionsFalse() {
            val person = Person()
            var executionCount = 0
            val result = person
                .applyIf(false) { 
                    executionCount++
                    this.name = "Tony"
                }
                .applyIf(false) { 
                    executionCount++
                    this.age = 25
                }
            assertEquals("", result.name)
            assertEquals(0, result.age)
            assertEquals(0, executionCount)
        }
    }

    @Nested
    @DisplayName("Funcs.边界情况测试")
    inner class EdgeCasesTest {
        @ParameterizedTest
        @ValueSource(strings = ["", "   ", "\t", "\n"])
        @DisplayName("Funcs.ifNullOrBlank():空字符串")
        fun testIfNullOrBlankWithEmptyString(value: String) {
            val result = value.ifNullOrBlank("default")
            assertEquals("default", result)
        }
        @ParameterizedTest
        @ValueSource(ints = [0, -1, 100])
        @DisplayName("Funcs.ifNull():零值")
        fun testIfNullWithZeroValue(value: Int) {
            val result = value.ifNull(999)
            assertEquals(value, result)
        }
        @Test
        @DisplayName("Funcs.ifNull():false值")
        fun testIfNullWithFalseValue() {
            val falseValue = false
            val result = falseValue.ifNull(true)
            assertEquals(false, result)
        }
    }

    @Nested
    @DisplayName("Funcs.throwIfEmpty()测试")
    inner class ThrowIfEmptyTest {
        @ParameterizedTest
        @ValueSource(strings = ["1,2,3", "a,b,c", "x,y,z"])
        @DisplayName("Funcs.throwIfEmpty():Collection非空")
        fun testThrowIfEmptyCollectionWithNonEmpty(value: String) {
            val list = value.split(",")
            assertEquals(list, list.throwIfEmpty("不应抛出"))
        }
        @Test
        @DisplayName("Funcs.throwIfEmpty():Collection空")
        fun testThrowIfEmptyCollectionWithEmpty() {
            val emptyList = emptyList<Int>()
            assertThrows(RuntimeException::class.java) {
                emptyList.throwIfEmpty("空集合应抛出")
            }
        }
        @Test
        @DisplayName("Funcs.throwIfEmpty():Collection null")
        fun testThrowIfEmptyCollectionWithNull() {
            val nullList: List<Int>? = null
            assertThrows(RuntimeException::class.java) {
                nullList.throwIfEmpty("null集合应抛出")
            }
        }
        @ParameterizedTest
        @ValueSource(strings = ["a:1", "b:2", "c:3"])
        @DisplayName("Funcs.throwIfEmpty():Map非空")
        fun testThrowIfEmptyMapWithNonEmpty(value: String) {
            val parts = value.split(":")
            val map = mapOf(parts[0] to parts[1].toInt())
            assertEquals(map, map.throwIfEmpty("不应抛出"))
        }
        @Test
        @DisplayName("Funcs.throwIfEmpty():Map空")
        fun testThrowIfEmptyMapWithEmpty() {
            val emptyMap = mapOf<String, Int>()
            assertThrows(RuntimeException::class.java) {
                emptyMap.throwIfEmpty("空Map应抛出")
            }
        }
        @Test
        @DisplayName("Funcs.throwIfEmpty():Map null")
        fun testThrowIfEmptyMapWithNull() {
            val nullMap: Map<String, Int>? = null
            assertThrows(RuntimeException::class.java) {
                nullMap.throwIfEmpty("nullMap应抛出")
            }
        }
    }

    @Nested
    @DisplayName("Funcs.throwIfNullOrEmpty()测试")
    inner class ThrowIfNullOrEmptyTest {
        @ParameterizedTest
        @ValueSource(strings = ["abc", "def", "ghi"])
        @DisplayName("Funcs.throwIfNullOrEmpty():CharSequence非空")
        fun testThrowIfNullOrEmptyCharSequenceWithNonEmpty(value: String) {
            assertEquals(value, value.throwIfNullOrEmpty("不应抛出"))
        }
        @Test
        @DisplayName("Funcs.throwIfNullOrEmpty():CharSequence空")
        fun testThrowIfNullOrEmptyCharSequenceWithEmpty() {
            val emptyStr = ""
            assertThrows(RuntimeException::class.java) {
                emptyStr.throwIfNullOrEmpty("空字符串应抛出")
            }
        }
        @Test
        @DisplayName("Funcs.throwIfNullOrEmpty():CharSequence null")
        fun testThrowIfNullOrEmptyCharSequenceWithNull() {
            val nullStr: String? = null
            assertThrows(RuntimeException::class.java) {
                nullStr.throwIfNullOrEmpty("null字符串应抛出")
            }
        }
    }
}
