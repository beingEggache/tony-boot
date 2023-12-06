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

@file:JvmName("ApiResults")

/**
 * 全局响应统一结构.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */

package com.tony

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.tony.MonoResult.Companion.ofMonoResult
import com.tony.exception.ApiException
import com.tony.exception.BaseException
import com.tony.exception.BizException
import com.tony.utils.asTo

public typealias ApiMonoResult<T> = ApiResult<MonoResult<T>>

/**
 * 拉平对象成 [FlattenApiResult], 将所有字段拉到最外层显示.
 *
 * 比如
 * ```
 * {
 *   "code": 20000,
 *   "data": {
 *     "name": "Tony",
 *     "age": 18
 *   }
 * }
 * ```
 * 变成
 * ```
 * {
 *   "code": 20000,
 *   "name": "Tony",
 *   "age": 18
 * }
 * ```
 *
 * @return [ApiResultLike<T>]
 * @author Tang Li
 * @date 2023/09/13 10:31
 * @since 1.0.0
 */
public fun <T> T.flattenResult(): ApiResultLike<T> =
    FlattenApiResult(this, ApiProperty.okCode, ApiProperty.defaultOkMessage)

/**
 * 全局响应统一结构.
 *
 * @param T 响应体对象类型. 不支持 [Boolean] ,[CharSequence], [Number], [Enum].
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public data class ApiResult<T>
    @JvmOverloads
    constructor(
        private val data: T?,
        private val code: Int = ApiProperty.okCode,
        private val message: CharSequence = ApiProperty.defaultOkMessage,
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

        override fun getData(): T? =
            data

        override fun getCode(): Int =
            code

        override fun getMessage(): CharSequence =
            message

        /**
         * 将 data 的属性拉到根节点
         * @see [flattenResult]
         */
        public fun flatten(): ApiResultLike<T> =
            FlattenApiResult(data, code, message)

        /**
         * 如果返回码不成功, 则抛出异常.
         *
         * @param message 默认为 [ApiResult.message]
         * @param ex 异常类型构造函数, 得是 [BaseException]的子类.
         */
        @JvmOverloads
        public fun returnIfSuccessOrThrow(
            message: CharSequence = this.message,
            ex: (message: String, code: Int) -> BaseException = ::BizException,
        ): T? =
            if (code != ApiProperty.okCode) {
                throw ex.invoke(message.toString(), ApiProperty.preconditionFailedCode)
            } else {
                data
            }

        public companion object {
            /**
             * 只返回消息
             * @param message 默认为 [ApiProperty.defaultOkMessage]
             */
            @JvmOverloads
            @JvmStatic
            public fun message(message: CharSequence = ApiProperty.defaultOkMessage): ApiResult<Unit> =
                ApiResult(Unit, ApiProperty.okCode, message)

            /**
             * 用 [MonoResult] 包装 [Boolean]
             * @param value Boolean
             */
            @JvmStatic
            public fun of(
                value: Boolean,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiMonoResult<Boolean> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResult] 包装 [CharSequence]
             * @param value CharSequence
             */
            @JvmStatic
            public fun <E : CharSequence> of(
                value: E,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiMonoResult<E> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResult] 包装 [Number]
             * @param value Number
             */
            @JvmStatic
            public fun <E : Number> of(
                value: E,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiMonoResult<E> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResult] 包装 [Enum]
             * @param value Enum
             */
            @JvmStatic
            public fun <E : Enum<*>> of(
                value: E,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiMonoResult<E> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)
        }
    }

/**
 * 包装 [Boolean] ,[CharSequence], [Number], [Enum]
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public data class MonoResult<T> internal constructor(
    val value: T? = null,
) {
    public companion object {
        @JvmStatic
        public fun Boolean.ofMonoResult(): MonoResult<Boolean> =
            MonoResult(this)

        @JvmStatic
        public fun <E : CharSequence> E.ofMonoResult(): MonoResult<E> =
            MonoResult(this)

        @JvmStatic
        public fun <E : Number> E.ofMonoResult(): MonoResult<E> =
            MonoResult(this)

        @JvmStatic
        public fun <E : Enum<*>> E.ofMonoResult(): MonoResult<E> =
            MonoResult(this)
    }
}

/**
 * 全局响应统一列表结构.
 *
 * @param T
 * @param rows 列表
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public data class ListResult<T>(
    private val rows: Collection<T>?,
) : RowsWrapper<T> {
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

/**
 * 全局响应统一分页结构.
 * @param T
 * @param rows 列表
 * @param page 当前页
 * @param size 每页数量
 * @param pages 总页数
 * @param total 总个数
 * @param hasNext 是否有下页
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
@JsonPropertyOrder(value = ["page", "size", "total", "pages", "hasNext", "rows"])
public data class PageResult<T>(
    private val rows: Collection<T>?,
    private val page: Long,
    private val size: Long,
    private val pages: Long,
    private val total: Long,
    private val hasNext: Boolean,
) : PageResultLike<T> {
    override fun getRows(): Collection<T> =
        rows ?: emptyList()

    override fun getPage(): Long =
        page

    override fun getSize(): Long =
        size

    override fun getPages(): Long =
        pages

    override fun getTotal(): Long =
        total

    override fun getHasNext(): Boolean =
        hasNext

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
