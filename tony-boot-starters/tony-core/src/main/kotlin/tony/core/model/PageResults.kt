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

@file:JvmName("PageResults")

package tony.core.model

import tony.core.utils.asTo

/**
 * @see [PageResultImpl]
 */
public fun <T> ofPageResult(
    rows: Collection<T>,
    page: Long,
    size: Long,
    total: Long,
): PageResult<T> =
    PageResultImpl(rows, page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun <T> ofPageResult(
    array: Array<T>,
    page: Long,
    size: Long,
    total: Long,
): PageResult<T> =
    PageResultImpl(array.asList(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    byteArray: ByteArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Byte> =
    PageResultImpl(byteArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    shortArray: ShortArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Short> =
    PageResultImpl(shortArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    intArray: IntArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Int> =
    PageResultImpl(intArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    longArray: LongArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Long> =
    PageResultImpl(longArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    floatArray: FloatArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Float> =
    PageResultImpl(floatArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    doubleArray: DoubleArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Double> =
    PageResultImpl(doubleArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    booleanArray: BooleanArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Boolean> =
    PageResultImpl(booleanArray.asList().asTo(), page, size, total)

/**
 * @see [PageResultImpl]
 */
public fun ofPageResult(
    charArray: CharArray,
    page: Long,
    size: Long,
    total: Long,
): PageResult<Char> =
    PageResultImpl(charArray.asList().asTo(), page, size, total)
