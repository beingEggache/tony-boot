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

import tony.core.ApiProperty
import tony.core.exception.ApiException
import tony.core.exception.BaseException
import tony.core.exception.BizException
import tony.core.model.ApiResult.Companion.message
import tony.core.model.MonoResultLike.Companion.ofMonoResult

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
 *
 * @return [ApiResultLike]<[T]>
 * @author tangli
 * @date 2023/09/13 19:31
 */
public fun <T> T.flattenResult(): ApiResultLike<T> =
    FlattenApiResult(this, ApiProperty.okCode, ApiProperty.defaultOkMessage)

/**
 * 全局响应统一结构.
 *
 * @param T 响应体对象类型. 不支持 [Boolean] ,[CharSequence], [Number], [Enum].
 *
 * @author tangli
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
             * 用 [BooleanMonoResult] 包装 [Boolean]
             * @param value Boolean
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                value: Boolean,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiResult<BooleanMonoResult> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [StringMonoResult] 包装 [CharSequence]
             * @param value CharSequence
             */
            @JvmOverloads
            @JvmStatic
            public fun of(
                value: String,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiResult<StringMonoResult> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [NumberMonoResult] 包装 [Number]
             * @param value Number
             */
            @JvmOverloads
            @JvmStatic
            public fun <E : Number> of(
                value: E,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiResult<NumberMonoResult> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)

            /**
             * 用 [EnumMonoResult] 包装 [Enum]
             * @param value Enum
             */
            @JvmOverloads
            @JvmStatic
            public fun <E : Enum<*>> of(
                value: E,
                message: CharSequence = ApiProperty.defaultOkMessage,
            ): ApiResult<EnumMonoResult> =
                ApiResult(value.ofMonoResult(), ApiProperty.okCode, message)
        }
    }
