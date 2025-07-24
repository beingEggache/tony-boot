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
public data class PageResult<T> private constructor(
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

    @Suppress("ClassName")
    public companion object `-Companion` {
        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun <T> of(
            rows: Collection<T>,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<T> =
            PageResult(rows, page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun <T> of(
            array: Array<T>,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<T> =
            PageResult(array.asList(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            byteArray: ByteArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Byte> =
            PageResult(byteArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            shortArray: ShortArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Short> =
            PageResult(shortArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            intArray: IntArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Int> =
            PageResult(intArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            longArray: LongArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Long> =
            PageResult(longArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            floatArray: FloatArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Float> =
            PageResult(floatArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            doubleArray: DoubleArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Double> =
            PageResult(doubleArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            booleanArray: BooleanArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Boolean> =
            PageResult(booleanArray.asList().asTo(), page, size, total)

        /**
         * @see [PageResult]
         */
        @JvmStatic
        public fun of(
            charArray: CharArray,
            page: Long,
            size: Long,
            total: Long,
        ): PageResultLike<Char> =
            PageResult(charArray.asList().asTo(), page, size, total)
    }
}
