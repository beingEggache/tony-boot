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

package com.tony.test.core

import com.tony.utils.dateOverlap
import com.tony.utils.overlap
import com.tony.utils.toDate
import java.time.LocalDate

fun main() {
    val start1 = LocalDate.of(2023, 12, 4)
    val end1 = LocalDate.of(2023, 12, 8)

    val start2 = LocalDate.of(2023, 12, 3)
    val end2 = LocalDate.of(2023, 12, 9)

    println((start1 to end1).overlap(start2 to end2))
    println((start1.toDate() to end1.toDate()).dateOverlap(start2.toDate() to end2.toDate()))

}

fun quickSort(list: List<Int>): List<Int> =
    if (list.size < 2) list
    else {
        val pivot = list[list.size / 2]
        val less = list.filter { it < pivot }
        val equal = list.filter { it == pivot }
        val greater = list.filter { it > pivot }
        quickSort(less) + equal + quickSort(greater)
    }
