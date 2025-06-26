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
 * Cols 工具类单元测试
 * @author tangli
 * @date 2024/06/10
 */
object ColsTest {
    private val logger = LoggerFactory.getLogger(ColsTest::class.java)

    @Test
    @DisplayName("isNullOrEmpty 判空测试")
    fun testIsNullOrEmpty() {
        logger.info("测试 isNullOrEmpty 判空")
        assertTrue(isNullOrEmpty(null))
        assertTrue(isNullOrEmpty(emptyList<String>()))
        assertFalse(isNullOrEmpty(listOf(1, 2)))
    }

    @Test
    @DisplayName("ifEmpty 默认值测试")
    fun testIfEmpty() {
        logger.info("测试 ifEmpty 默认值")
        val list: List<Int>? = null
        val result = ifEmpty(list, listOf(1, 2))
        assertEquals(listOf(1, 2), result)
        val notEmpty = ifEmpty(listOf(3, 4), listOf(1, 2))
        assertEquals(listOf(3, 4), notEmpty)
    }

    @Test
    @DisplayName("orEmpty 空集合默认值测试")
    fun testOrEmpty() {
        logger.info("测试 orEmpty 空集合默认值")
        val list: MutableList<Int>? = null
        val result = orEmpty(list)
        assertTrue(result.isEmpty())
        val set: MutableSet<String>? = null
        val setResult = orEmpty(set)
        assertTrue(setResult.isEmpty())
    }

    @Test
    @DisplayName("alsoIfNotEmpty 链式操作测试")
    fun testAlsoIfNotEmpty() {
        logger.info("测试 alsoIfNotEmpty 链式操作")
        var called = false
        val list = listOf(1, 2, 3)
        val result = list.alsoIfNotEmpty {
            called = true
        }
        assertTrue(called)
        assertEquals(list, result)
        val empty: List<Int>? = emptyList()
        var notCalled = true
        val result2 = empty.alsoIfNotEmpty {
            notCalled = false
        }
        assertTrue(notCalled)
        assertEquals(empty, result2)
    }

    @Test
    @DisplayName("边界与异常测试")
    fun testEdgeCases() {
        logger.info("测试 Cols 工具类边界与异常")
        val nullList: List<String>? = null
        assertTrue(isNullOrEmpty(nullList))
        val emptySet: Set<Int>? = null
        assertTrue(isNullOrEmpty(emptySet))
        val notEmptySet = setOf(1)
        assertFalse(isNullOrEmpty(notEmptySet))
        val result = orEmpty(nullList as MutableList<String>?)
        assertTrue(result.isEmpty())
    }
} 