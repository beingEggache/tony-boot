package com.tony

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.tony.exception.ApiException
import com.tony.exception.BaseException
import com.tony.exception.BizException
import com.tony.utils.asTo
import java.util.Collections

/**
 * 全局响应统一结构.
 * @param T 响应体对象类型.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public interface ApiResultLike<T> {
    /**
     * 响应体
     */
    public val data: T?

    /**
     * 返回码
     */
    public val code: Int
        get() = ApiProperty.okCode

    /**
     * 返回消息
     */
    public val message: String
        get() = ApiProperty.defaultOkMessage
}

/**
 * 全局响应统一结构.
 *
 * @param T 响应体对象类型. 不支持 [Boolean] ,[CharSequence], [Number], [Enum].
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
@JsonPropertyOrder(value = ["code", "message", "data"])
public class ApiResult<T> @JvmOverloads constructor(
    override val data: T?,
    override val code: Int = ApiProperty.okCode,
    override val message: String = ApiProperty.defaultOkMessage,
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

    /**
     * 如果返回码不成功, 则抛出异常.
     *
     * @param message 默认为 [ApiResult.message]
     * @param ex 异常类型构造函数, 得是 [BaseException]的子类.
     */
    @JvmOverloads
    public fun returnIfSuccessOrThrow(
        message: String = this.message,
        ex: (message: String, code: Int) -> BaseException = ::BizException,
    ): T? = if (code != ApiProperty.okCode) {
        throw ex.invoke(message, ApiProperty.preconditionFailedCode)
    } else {
        data
    }

    public companion object {
        @JvmField
        public val EMPTY_RESULT: Map<Any?, Any?> = emptyMap()

        /**
         * 只返回消息
         * @param message 默认为 [ApiProperty.defaultOkMessage]
         */
        @JvmOverloads
        @JvmStatic
        public fun message(message: String = ApiProperty.defaultOkMessage): ApiResult<Unit> =
            ApiResult(Unit, ApiProperty.okCode, message)

        /**
         * 用 [OneResult] 包装 [Boolean]
         * @param result Boolean
         */
        @JvmStatic
        public fun of(result: Boolean): ApiResult<OneResult<Boolean>> = ApiResult(OneResult(result))

        /**
         * 用 [OneResult] 包装 [CharSequence]
         * @param result CharSequence
         */
        @JvmStatic
        public fun of(result: CharSequence): ApiResult<OneResult<CharSequence>> = ApiResult(OneResult(result))

        /**
         * 用 [OneResult] 包装 [Number]
         * @param result Number
         */
        @JvmStatic
        public fun of(result: Number): ApiResult<OneResult<Number>> = ApiResult(OneResult(result))

        /**
         * 用 [OneResult] 包装 [Enum]
         * @param result Enum
         */
        @JvmStatic
        public fun of(result: Enum<*>): ApiResult<OneResult<Enum<*>>> = ApiResult(OneResult(result))
    }
}

/**
 * 包装 [Boolean] ,[CharSequence], [Number], [Enum]
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public data class OneResult<T>(val result: T? = null)

/**
 * 全局响应统一列表结构.
 * @param T
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public interface ListResultLike<T> {
    /**
     * 列表
     */
    public val items: Collection<T>?
}

/**
 * 全局响应统一列表结构.
 *
 * @param T
 * @param items 列表
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
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

/**
 * 全局响应统一分页结构.
 * @param T
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public interface PageResultLike<T> {
    /**
     * 列表
     */
    public val items: Collection<T>?

    /**
     * 当前页
     */
    public val page: Long

    /**
     * 每页数量
     */
    public val size: Long

    /**
     * 总页数
     */
    public val pages: Long

    /**
     * 总个数
     */
    public val total: Long

    /**
     * 是否有下一页
     */
    public val hasNext: Boolean

    /**
     * map, 和 [List.map] 行为一致, 将返回类型改为自身.
     * @see [List.map]
     */
    public fun <R> map(transform: (T) -> R): PageResult<R> =
        PageResult(items?.map(transform), page, size, pages, total, hasNext)

    /**
     * onEach, 和 [List.onEach] 行为一致, 将返回类型改为自身.
     * @see [List.onEach]
     */
    public fun onEach(action: (T) -> Unit): PageResult<T> =
        PageResult(items?.onEach(action), page, size, pages, total, hasNext)

    public fun firstOrNull(predicate: (T) -> Boolean): T? = items?.firstOrNull(predicate)
}

/**
 * 全局响应统一分页结构.
 * @param T
 * @param items 列表
 * @param page 当前页
 * @param size 每页数量
 * @param pages 总页数
 * @param total 总个数
 * @param hasNext 是否有下页
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
@JsonPropertyOrder(value = ["page", "size", "total", "pages", "hasNext", "items"])
public data class PageResult<T>(
    override val items: Collection<T>?,
    override val page: Long,
    override val size: Long,
    override val pages: Long,
    override val total: Long,
    override val hasNext: Boolean,
) : PageResultLike<T> {

    /**
     * @see [PageResult]
     */
    public constructor(
        array: Array<T>,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(array.asList(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        byteArray: ByteArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(byteArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        shortArray: ShortArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(shortArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        intArray: IntArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(intArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        longArray: LongArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(longArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        floatArray: FloatArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(floatArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        doubleArray: DoubleArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(doubleArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        booleanArray: BooleanArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(booleanArray.asList().asTo(), page, size, pages, total, hasNext)

    /**
     * @see [PageResult]
     */
    public constructor(
        charArray: CharArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean,
    ) : this(charArray.asList().asTo(), page, size, pages, total, hasNext)
}

/**
 * 全局统一请求分页结构.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public interface Pageable {

    /**
     * 页码,当前页
     */
    public var page: Long?

    /**
     * 每页数量
     */
    public var size: Long?

    /**
     * 升序字段
     */
    public var ascs: MutableCollection<out CharSequence?>?

    /**
     * 降序字段
     */
    public var descs: MutableCollection<out CharSequence?>?
}

public class PageQuery @JvmOverloads constructor(
    override var page: Long?,
    override var size: Long? = 10L,
    override var descs: MutableCollection<out CharSequence?>? = Collections.emptyList(),
    override var ascs: MutableCollection<out CharSequence?>? = Collections.emptyList(),
) : Pageable
