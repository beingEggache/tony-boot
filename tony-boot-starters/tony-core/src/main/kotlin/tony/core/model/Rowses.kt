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
