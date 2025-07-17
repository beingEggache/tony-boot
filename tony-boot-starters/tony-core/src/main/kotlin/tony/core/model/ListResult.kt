package tony.core.model

import tony.core.utils.asTo

/**
 * 全局响应统一列表结构.
 *
 * @param T
 * @param rows 列表
 *
 * @author tangli
 * @date 2021/12/6 10:51
 */
public data class ListResult<T>(
    private val rows: Collection<T>?,
) : RowsLike<T> {
    public constructor(array: Array<*>) : this(array.asList().asTo())
    public constructor(byteArray: ByteArray) : this(byteArray.asList().asTo())
    public constructor(shortArray: ShortArray) : this(shortArray.asList().asTo())
    public constructor(intArray: IntArray) : this(intArray.asList().asTo())
    public constructor(longArray: LongArray) : this(longArray.asList().asTo())
    public constructor(floatArray: FloatArray) : this(floatArray.asList().asTo())
    public constructor(doubleArray: DoubleArray) : this(doubleArray.asList().asTo())
    public constructor(booleanArray: BooleanArray) : this(booleanArray.asList().asTo())
    public constructor(charArray: CharArray) : this(charArray.asList().asTo())

    override fun getRows(): Collection<T> =
        rows ?: emptyList()
}
