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
 * @author tangli
 * @date 2021/12/6 10:51
 */

package tony.core.model

import java.time.temporal.Temporal
import tony.core.ApiProperty
import tony.core.exception.ApiException
import tony.core.exception.BaseException
import tony.core.exception.BizException
import tony.core.model.ApiResult.`-Companion`.message
import tony.core.model.MonoResult.`-Companion`.ofMonoResult
import tony.core.utils.isArrayLikeType
import tony.core.utils.isBooleanType
import tony.core.utils.isDateTimeLikeType
import tony.core.utils.isNumberTypes
import tony.core.utils.isStringLikeType

/**
 * 拉平对象成 [tony.core.model.FlattenApiResult], 将所有字段拉到最外层显示.
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
 * @param [code] 消息码
 * @param [message] 消息
 * @return [ApiResultLike]<[T]>
 * @author tangli
 * @date 2023/09/13 19:31
 */
@JvmOverloads
public fun <T> T.flattenResult(
    code: Int = ApiProperty.okCode,
    message: String = ApiProperty.defaultOkMessage,
): FlattenApiResult<T> =
    FlattenApiResult(this, code, message)

/**
 * 全局响应统一结构.
 *
 * @param T 响应体对象类型. 不支持 [Boolean] ,[CharSequence], [Number], [Enum].
 *
 * @author tangli
 * @date 2021/12/6 10:51
 */
public data class ApiResult<T>
    private constructor(
        private val data: T?,
        private val code: Int = ApiProperty.okCode,
        private val message: String = ApiProperty.defaultOkMessage,
    ) : ApiResultLike<T> {
        override fun getData(): T? =
            data

        override fun getCode(): Int =
            code

        override fun getMessage(): String =
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

        @Suppress("ClassName")
        public companion object `-Companion` {
            /**
             * 只返回消息
             * @param message 默认为 [ApiProperty.defaultOkMessage]
             */
            @JvmOverloads
            @JvmStatic
            public fun message(message: String = ApiProperty.defaultOkMessage): ApiResultLike<Unit> =
                ApiResult(Unit, ApiProperty.okCode, message)

            /**
             * 用 [MonoResultLike] 包装 [Boolean]
             * @param value Boolean
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                value: Boolean,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<MonoResultLike<Boolean>> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResultLike] 包装 [CharSequence]
             * @param value CharSequence
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                value: String,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<MonoResultLike<String>> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResultLike] 包装 [Number]
             * @param value Number
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun <E : Number> of(
                value: E,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<MonoResultLike<Number>> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResultLike] 包装 [Enum]
             * @param value Enum
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun <E : Enum<*>> of(
                value: E,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<MonoResultLike<Enum<*>>> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [MonoResultLike] 包装 [value]
             * @param value
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun <E : Temporal> of(
                value: E,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<MonoResultLike<Temporal>> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [value]
             * @param value
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun <E> of(
                value: Collection<E>,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<E>> =
                ApiResult(ListResult(value), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun <E> of(
                array: Array<E>,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<E>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: ByteArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Byte>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: ShortArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Short>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: IntArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Int>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: LongArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Long>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: FloatArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Float>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: DoubleArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Double>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: BooleanArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Boolean>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 用 [ListResult] 包装 [array]
             * @param array
             * @param [message] 消息
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                array: CharArray,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<ListResult<Char>> =
                ApiResult(ListResult(array), ApiProperty.okCode, message)

            /**
             * 构造方法
             * @param [data] 数据
             * @param [code] 编码
             * @param [message] 消息
             * @return [ApiResultLike]<[T]>
             * @author tangli
             * @date 2025/07/23 16:20
             */
            @JvmOverloads
            @JvmStatic
            public fun <T> of(
                data: T?,
                code: Int = ApiProperty.okCode,
                message: String = ApiProperty.defaultOkMessage,
            ): ApiResultLike<T> {
                val template = "%s type can not be the first parameter.Please use ApiResult.of(result) instead."
                if (data != null) {
                    val clazz = data::class.java
                    when {
                        clazz.isBooleanType() -> throw ApiException(String.format(template, "Boolean"))
                        clazz.isStringLikeType() -> throw ApiException(String.format(template, "CharSequence"))
                        clazz.isNumberTypes() -> throw ApiException(String.format(template, "Number"))
                        clazz.isEnum -> throw ApiException(String.format(template, "Enum"))
                        clazz.isDateTimeLikeType() -> throw ApiException(String.format(template, "Temporal"))
                        clazz.isArrayLikeType() -> throw ApiException(String.format(template, "Collection"))
                    }
                }
                return ApiResult(data, code, message)
            }
        }
    }
