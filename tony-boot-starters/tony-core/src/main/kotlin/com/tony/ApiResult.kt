@file:Suppress("unused")

package com.tony

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.tony.exception.ApiException
import com.tony.exception.BaseException
import com.tony.exception.BizException
import com.tony.utils.asTo

public interface ApiResultLike<T> {
    public val data: T?
    public val code: Int
        get() = ApiProperty.successCode
    public val message: String
        get() = ""
}

@JsonPropertyOrder(value = ["code", "message", "data"])
public class ApiResult<T> @JvmOverloads constructor(
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

    @JvmOverloads
    public fun returnIfSuccessOrThrow(
        message: String = this.message,
        ex: (message: String, code: Int) -> BaseException = ::BizException
    ): T? = if (code != ApiProperty.successCode) {
        throw ex.invoke(message, ApiProperty.bizErrorCode)
    } else {
        data
    }

    public companion object {
        @JvmField
        public val EMPTY_RESULT: Map<Any?, Any?> = emptyMap()

        @JvmOverloads
        @JvmStatic
        public fun message(message: String = ApiProperty.defaultSuccessMessage): ApiResult<Unit> =
            ApiResult(Unit, ApiProperty.successCode, message)

        @JvmStatic
        public fun of(result: Boolean): ApiResult<OneResult<Boolean>> = ApiResult(OneResult(result))

        @JvmStatic
        public fun of(result: CharSequence): ApiResult<OneResult<CharSequence>> = ApiResult(OneResult(result))

        @JvmStatic
        public fun of(result: Number): ApiResult<OneResult<Number>> = ApiResult(OneResult(result))

        @JvmStatic
        public fun of(result: Enum<*>): ApiResult<OneResult<Enum<*>>> = ApiResult(OneResult(result))
    }
}

public data class OneResult<T>(val result: T? = null)

public interface ListResultLike<T> {
    public val items: Collection<T>?
}

public data class ListResult<T>
@JvmOverloads constructor(override val items: Collection<T>? = listOf()) :
    ListResultLike<T> {

    public constructor(array: Array<*>) : this(array.asList().asTo())
    public constructor(byteArray: ByteArray) : this(byteArray.asList().asTo())
    public constructor(shortArray: ShortArray) : this(shortArray.asList().asTo())
    public constructor(intArray: IntArray) : this(intArray.asList().asTo())
    public constructor(longArray: LongArray) : this(longArray.asList().asTo())
    public constructor(floatArray: FloatArray) : this(floatArray.asList().asTo())
    public constructor(doubleArray: DoubleArray) : this(doubleArray.asList().asTo())
    public constructor(booleanArray: BooleanArray) : this(booleanArray.asList().asTo())
    public constructor(charArray: CharArray) : this(charArray.asList().asTo())
}

public interface PageResultLike<T> {
    public val items: Collection<T>?
    public val page: Long
    public val size: Long
    public val pages: Long
    public val total: Long
    public val hasNext: Boolean

    public fun <R> map(transform: (T) -> R): PageResult<R> =
        PageResult(items?.map(transform), page, size, pages, total, hasNext)

    public fun onEach(action: (T) -> Unit): PageResult<T> =
        PageResult(items?.onEach(action), page, size, pages, total, hasNext)

    public fun firstOrNull(predicate: (T) -> Boolean): T? = items?.firstOrNull(predicate)
}

@JsonPropertyOrder(value = ["page", "size", "total", "pages", "hasNext", "items"])
public data class PageResult<T>(
    override val items: Collection<T>?,
    override val page: Long,
    override val size: Long,
    override val pages: Long,
    override val total: Long,
    override val hasNext: Boolean
) : PageResultLike<T> {

    public constructor(
        array: Array<T>,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(array.asList(), page, size, pages, total, hasNext)

    public constructor(
        byteArray: ByteArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(byteArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        shortArray: ShortArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(shortArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        intArray: IntArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(intArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        longArray: LongArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(longArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        floatArray: FloatArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(floatArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        doubleArray: DoubleArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(doubleArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        booleanArray: BooleanArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(booleanArray.asList().asTo(), page, size, pages, total, hasNext)

    public constructor(
        charArray: CharArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(charArray.asList().asTo(), page, size, pages, total, hasNext)
}

public interface Pageable {

    /**
     * get 页码
     * @return 页码
     */
    public fun getPage(): Long? = 0L

    /**
     * set 页码
     * @param page 页码
     */
    public fun setPage(page: Long?): Unit = Unit

    /**
     * get 每页数量
     * @return 页码
     */
    public fun getSize(): Long? = 0L

    /**
     * set 每页数量
     * @param size 页码
     */
    public fun setSize(size: Long?): Unit = Unit

    /**
     * get 倒序排序字段
     * @return 倒序排序字段
     */
    public fun getDescs(): MutableCollection<String?>? = mutableListOf()

    /**
     * set 倒序排序字段
     * @param descs 倒序排序字段
     */
    public fun setDescs(descs: List<String?>?): Unit = Unit

    /**
     * get 顺序排序字段
     * @return 顺序排序字段
     */
    public fun getAscs(): MutableCollection<String?>? = mutableListOf()

    /**
     * set 顺序排序字段
     * @param ascs 顺序排序字段
     */
    public fun setAscs(ascs: List<String?>?): Unit = Unit
}
