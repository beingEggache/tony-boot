package tony.core.model

import tony.core.utils.asTo

/**
 * 全局响应统一分页结构.
 * @param T
 * @param rows 列表
 * @param page 当前页
 * @param size 每页数量
 * @param total 总个数
 *
 * @author tangli
 * @date 2021/12/6 10:51
 */
public data class PageResult<T>(
    private val rows: Collection<T>?,
    private val page: Long,
    private val size: Long,
    private val total: Long,
) : PageResultLike<T> {
    override fun getRows(): Collection<T> =
        rows ?: emptyList()

    override fun getPage(): Long =
        page

    override fun getSize(): Long =
        size

    override fun getTotal(): Long =
        total

    /**
     * @see [PageResult]
     */
    public constructor(
        array: Array<T>,
        page: Long,
        size: Long,
        total: Long,
    ) : this(array.asList(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        byteArray: ByteArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(byteArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        shortArray: ShortArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(shortArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        intArray: IntArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(intArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        longArray: LongArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(longArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        floatArray: FloatArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(floatArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        doubleArray: DoubleArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(doubleArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        booleanArray: BooleanArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(booleanArray.asList().asTo(), page, size, total)

    /**
     * @see [PageResult]
     */
    public constructor(
        charArray: CharArray,
        page: Long,
        size: Long,
        total: Long,
    ) : this(charArray.asList().asTo(), page, size, total)
}
