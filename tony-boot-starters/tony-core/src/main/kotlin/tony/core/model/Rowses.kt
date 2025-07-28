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

@file:JvmName("Rowses")

package tony.core.model

import tony.core.utils.asTo

/**
 * 构造 [Rows]
 * @param [collection]  集合
 * @return [Rows]<[E]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun <E> ofRows(collection: Collection<E>?): Rows<E> =
    RowsImpl(collection)

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[E]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun <E> ofRows(array: Array<E>): Rows<E> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Byte]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: ByteArray): Rows<Byte> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Short]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: ShortArray): Rows<Short> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Int]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: IntArray): Rows<Int> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Long]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: LongArray): Rows<Long> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Float]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: FloatArray): Rows<Float> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Double]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: DoubleArray): Rows<Double> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Boolean]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: BooleanArray): Rows<Boolean> =
    RowsImpl(array.asList().asTo())

/**
 * 构造 [Rows]
 * @param [array] 数组
 * @return [Rows]<[Char]>
 * @author tangli
 * @date 2025/07/28 16:07
 */
public fun ofRows(array: CharArray): Rows<Char> =
    RowsImpl(array.asList().asTo())
