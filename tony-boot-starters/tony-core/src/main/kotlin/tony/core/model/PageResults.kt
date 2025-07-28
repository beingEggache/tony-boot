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
