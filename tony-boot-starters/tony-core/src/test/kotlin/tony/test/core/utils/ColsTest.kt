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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.core.model.ForestLike
import tony.core.utils.alsoIfNotEmpty
import tony.core.utils.ifEmpty
import tony.core.utils.isNullOrEmpty
import tony.core.utils.listToTree
import tony.core.utils.orEmpty
import tony.core.utils.treeToList

/**
 * 集合 工具类单元测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
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

    data class TestNode(
        override val code: String?,
        override val sort: Int? = null,
        override val children: MutableList<TestNode> = mutableListOf()
    ) : ForestLike<TestNode>

    @Nested
    @DisplayName("Cols.listToTree() & treeToList() 测试")
    inner class ListToTreeAndTreeToListTest {
        /**
         * 简单的测试节点实现，兼容Java调用
         */
        @Test
        @DisplayName("listToTree: 多节点组装树结构")
        fun testListToTree() {
            val nodes = mutableListOf(
                TestNode("1"),
                TestNode("1-1"),
                TestNode("1-2"),
                TestNode("1-1-1"),
                TestNode("2")
            )
            val tree = nodes.listToTree()
            assertEquals(2, tree.size)
            val root1 = tree.first { it.code == "1" }
            assertEquals(2, root1.children.size)
            val child11 = root1.children.first { it.code == "1-1" }
            assertEquals(1, child11.children.size)
            assertEquals("1-1-1", child11.children.first().code)
            val root2 = tree.first { it.code == "2" }
            assertTrue(root2.children.isEmpty())
        }

        @Test
        @DisplayName("treeToList: 树结构拍平")
        fun testTreeToList() {
            val root = TestNode("1", children = mutableListOf(
                TestNode("1-1", children = mutableListOf(
                    TestNode("1-1-1")
                )),
                TestNode("1-2")
            ))
            val flat = mutableListOf(root).treeToList()
            val codes = flat.map { it.code }.toSet()
            assertEquals(setOf("1", "1-1", "1-1-1", "1-2"), codes)
        }
    }
}
