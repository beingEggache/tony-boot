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

@file:JvmName("Cols")

package com.tony.utils

/**
 * 集合 工具类, 只提供给Java.
 * @author Tang Li
 * @date 2023/09/12 10:53
 * @since 1.0.0
 */

public fun isNullOrEmpty(cols: Collection<*>?): Boolean =
    cols.isNullOrEmpty()

public fun <C : Collection<T>, T : Any?> ifEmpty(
    cols: C?,
    ifEmpty: C,
): C =
    if (cols.isNullOrEmpty()) ifEmpty else cols

public fun <C : Collection<T>, T : Any?> orEmpty(
    cols: C?,
    ifEmpty: C,
): C =
    if (cols.isNullOrEmpty()) ifEmpty else cols

public fun <C : MutableList<T>, T : Any?> orEmpty(cols: C?): C =
    if (cols.isNullOrEmpty()) mutableListOf<T>().asToNotNull() else cols

public fun <C : MutableSet<T>, T : Any?> orEmpty(cols: C?): C =
    if (cols.isNullOrEmpty()) mutableSetOf<T>().asToNotNull() else cols
