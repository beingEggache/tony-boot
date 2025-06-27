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
 * Cols 工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
@DisplayName("Cols测试")
class ColsTest {

    @Nested
    @DisplayName("Cols.isNullOrEmpty()测试")
    inner class IsNullOrEmptyTest {
        @Test
        @DisplayName("Cols.isNullOrEmpty():null集合")
        fun testIsNullOrEmptyWithNull() {
            assertTrue(isNullOrEmpty(null))
        }

        @Test
        @DisplayName("Cols.isNullOrEmpty():空集合")
        fun testIsNullOrEmptyWithEmpty() {
            assertTrue(isNullOrEmpty(emptyList<String>()))
        }

        @Test
        @DisplayName("Cols.isNullOrEmpty():非空集合")
        fun testIsNullOrEmptyWithNotEmpty() {
            assertFalse(isNullOrEmpty(listOf(1, 2)))
        }
    }

    @Nested
    @DisplayName("Cols.ifEmpty()测试")
    inner class IfEmptyTest {
        @Test
        @DisplayName("Cols.ifEmpty():null集合")
        fun testIfEmptyWithNull() {
            val list: List<Int>? = null
            val result = ifEmpty(list, listOf(1, 2))
            assertEquals(listOf(1, 2), result)
        }

        @Test
        @DisplayName("Cols.ifEmpty():非空集合")
        fun testIfEmptyWithNotEmpty() {
            val notEmpty = ifEmpty(listOf(3, 4), listOf(1, 2))
            assertEquals(listOf(3, 4), notEmpty)
        }
    }

    @Nested
    @DisplayName("Cols.orEmpty()测试")
    inner class OrEmptyTest {
        @Test
        @DisplayName("Cols.orEmpty():null列表")
        fun testOrEmptyWithNullList() {
            val list: MutableList<Int>? = null
            val result = orEmpty(list)
            assertTrue(result.isEmpty())
        }

        @Test
        @DisplayName("Cols.orEmpty():null集合")
        fun testOrEmptyWithNullSet() {
            val set: MutableSet<String>? = null
            val setResult = orEmpty(set)
            assertTrue(setResult.isEmpty())
        }
    }

    @Nested
    @DisplayName("Cols.alsoIfNotEmpty()测试")
    inner class AlsoIfNotEmptyTest {
        @Test
        @DisplayName("Cols.alsoIfNotEmpty():非空集合")
        fun testAlsoIfNotEmptyWithNotEmpty() {
            var called = false
            val list = listOf(1, 2, 3)
            val result = list.alsoIfNotEmpty {
                called = true
            }
            assertTrue(called)
            assertEquals(list, result)
        }

        @Test
        @DisplayName("Cols.alsoIfNotEmpty():空集合")
        fun testAlsoIfNotEmptyWithEmpty() {
            val empty: List<Int>? = emptyList()
            var notCalled = true
            val result2 = empty.alsoIfNotEmpty {
                notCalled = false
            }
            assertTrue(notCalled)
            assertEquals(empty, result2)
        }
    }

    @Nested
    @DisplayName("Cols边界与异常测试")
    inner class EdgeCasesTest {
        @Test
        @DisplayName("Cols.isNullOrEmpty():null列表")
        fun testIsNullOrEmptyWithNullList() {
            val nullList: List<String>? = null
            assertTrue(isNullOrEmpty(nullList))
        }

        @Test
        @DisplayName("Cols.isNullOrEmpty():null集合")
        fun testIsNullOrEmptyWithNullSet() {
            val emptySet: Set<Int>? = null
            assertTrue(isNullOrEmpty(emptySet))
        }

        @Test
        @DisplayName("Cols.isNullOrEmpty():非空集合")
        fun testIsNullOrEmptyWithNotEmptySet() {
            val notEmptySet = setOf(1)
            assertFalse(isNullOrEmpty(notEmptySet))
        }

        @Test
        @DisplayName("Cols.orEmpty():null可变列表")
        fun testOrEmptyWithNullMutableList() {
            val result = orEmpty(null as MutableList<String>?)
            assertTrue(result.isEmpty())
        }
    }
} 