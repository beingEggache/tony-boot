@file:Suppress("unused")

package com.tony

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.tony.exception.ApiException
import com.tony.utils.asTo

interface ApiResultLike<T> {
    val data: T?
    val code: Int
        get() = ApiProperty.successCode
    val message: String
        get() = ""
}

@JsonPropertyOrder(value = ["code", "message", "data"])
class ApiResult<T> @JvmOverloads constructor(
    override val data: T?,
    override val code: Int = ApiProperty.successCode,
    override val message: String = ""
) : ApiResultLike<T> {

    init {
        val template = "%s type can not be the first parameter.Please use ApiResult.of(result) instead."

        when (data) {
            is Boolean -> throw ApiException(String.format(template, "Boolean"))
            is CharSequence -> throw ApiException(String.format(template, "CharSequence"))
            is Number -> throw ApiException(String.format(template, "Number"))
            is Enum<*> -> throw ApiException(String.format(template, "Enum"))
        }
    }

    companion object {
        @JvmField
        val EMPTY_RESULT = emptyMap<Any?, Any?>()

        @JvmOverloads
        @JvmStatic
        fun message(message: String = ApiProperty.defaultSuccessMessage): ApiResult<Unit> =
            ApiResult(Unit, ApiProperty.successCode, message)

        @JvmStatic
        fun of(result: Boolean) = ApiResult(OneResult(result))

        @JvmStatic
        fun of(result: CharSequence) = ApiResult(OneResult(result))

        @JvmStatic
        fun of(result: Number) = ApiResult(OneResult(result))

        @JvmStatic
        fun of(result: Enum<*>) = ApiResult(OneResult(result))
    }
}

data class OneResult<T>(val result: T? = null)

interface ListResultLike<T> {
    val items: Collection<T>?
}

data class ListResult<T>
@JvmOverloads constructor(override val items: Collection<T>? = listOf()) :
    ListResultLike<T> {

    constructor(array: Array<*>) : this(array.asList().asTo())
    constructor(byteArray: ByteArray) : this(byteArray.asList().asTo())
    constructor(shortArray: ShortArray) : this(shortArray.asList().asTo())
    constructor(intArray: IntArray) : this(intArray.asList().asTo())
    constructor(longArray: LongArray) : this(longArray.asList().asTo())
    constructor(floatArray: FloatArray) : this(floatArray.asList().asTo())
    constructor(doubleArray: DoubleArray) : this(doubleArray.asList().asTo())
    constructor(booleanArray: BooleanArray) : this(booleanArray.asList().asTo())
    constructor(charArray: CharArray) : this(charArray.asList().asTo())
}

interface PageResultLike<T> {
    val items: Collection<T>?
    val page: Long
    val size: Long
    val pages: Long
    val total: Long
    val hasNext: Boolean

    fun <R> map(transform: (T) -> R) =
        PageResult(items?.map(transform), page, size, pages, total, hasNext)

    fun onEach(action: (T) -> Unit) =
        PageResult(items?.onEach(action), page, size, pages, total, hasNext)

    fun firstOrNull(predicate: (T) -> Boolean) = items?.firstOrNull(predicate)
}

@JsonPropertyOrder(value = ["page", "size", "total", "pages", "hasNext", "items"])
data class PageResult<T>(
    override val items: Collection<T>?,
    override val page: Long,
    override val size: Long,
    override val pages: Long,
    override val total: Long,
    override val hasNext: Boolean
) : PageResultLike<T> {

    constructor(
        array: Array<T>,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(array.asList(), page, size, pages, total, hasNext)

    constructor(
        byteArray: ByteArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(byteArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        shortArray: ShortArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(shortArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        intArray: IntArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(intArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        longArray: LongArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(longArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        floatArray: FloatArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(floatArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        doubleArray: DoubleArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(doubleArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        booleanArray: BooleanArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(booleanArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        charArray: CharArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(charArray.asList().asTo(), page, size, pages, total, hasNext)
}

interface Pageable {

    /**
     * get 页码
     * @return 页码
     */
    fun getPage(): Long? = 0L

    /**
     * set 页码
     * @param page 页码
     */
    fun setPage(page: Long?) {}

    /**
     * get 每页数量
     * @return 页码
     */
    fun getSize(): Long? = 0L

    /**
     * set 每页数量
     * @param size 页码
     */
    fun setSize(size: Long?) {}

    /**
     * get 倒序排序字段
     * @return 倒序排序字段
     */
    fun getDescs(): MutableCollection<String?>? = mutableListOf()

    /**
     * set 倒序排序字段
     * @param descs 倒序排序字段
     */
    fun setDescs(descs: List<String?>?) {}

    /**
     * get 顺序排序字段
     * @return 顺序排序字段
     */
    fun getAscs(): MutableCollection<String?>? = mutableListOf()

    /**
     * set 顺序排序字段
     * @param ascs 顺序排序字段
     */
    fun setAscs(ascs: List<String?>?) {}
}
