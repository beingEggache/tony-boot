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
import tony.core.utils.isArrayLikeType
import tony.core.utils.isBooleanType
import tony.core.utils.isDateTimeLikeType
import tony.core.utils.isNumberTypes
import tony.core.utils.isStringLikeType

/**
 * 拉平对象成 [FlattenApiResultImpl], 将所有字段拉到最外层显示.
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
 * @return [ApiResult]<[T]>
 * @author tangli
 * @date 2023/09/13 19:31
 */
@JvmOverloads
public fun <T> T.flattenResult(
    code: Int = ApiProperty.okCode,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<T> =
    FlattenApiResultImpl(this, code, message)

/**
 * 只返回消息
 * @param apiMessage 默认为 [ApiProperty.defaultOkMessage]
 */
@JvmOverloads
public fun apiMessage(message: String = ApiProperty.defaultOkMessage): ApiResult<Unit> =
    ApiResultImpl(Unit, ApiProperty.okCode, message)

/**
 * 用 [MonoValue] 包装 [Boolean]
 * @param value Boolean
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    value: Boolean,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<MonoValue<Boolean>> =
    ApiResultImpl(value.wrap(), ApiProperty.okCode, message)

/**
 * 用 [MonoValue] 包装 [String]
 * @param value String
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    value: String,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<MonoValue<String>> =
    ApiResultImpl(value.wrap(), ApiProperty.okCode, message)

/**
 * 用 [MonoValue] 包装 [Number]
 * @param value Number
 * @param [message] 消息
 */
@JvmOverloads
public fun <E : Number> ofApiResult(
    value: E,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<MonoValue<Number>> =
    ApiResultImpl(value.wrap(), ApiProperty.okCode, message)

/**
 * 用 [MonoValue] 包装 [Enum]
 * @param value Enum
 * @param [message] 消息
 */
@JvmOverloads
public fun <E : Enum<*>> ofApiResult(
    value: E,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<MonoValue<Enum<*>>> =
    ApiResultImpl(value.wrap(), ApiProperty.okCode, message)

/**
 * 用 [MonoValue] 包装 [value]
 * @param value
 * @param [message] 消息
 */
@JvmOverloads
public fun <E : Temporal> ofApiResult(
    value: E,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<MonoValue<Temporal>> =
    ApiResultImpl(value.wrap(), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [value]
 * @param value
 * @param [message] 消息
 */
@JvmOverloads
public fun <E> ofApiResult(
    value: Collection<E>,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<E>> =
    ApiResultImpl(ofRows(value), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun <E> ofApiResult(
    array: Array<E>,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<E>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: ByteArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Byte>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: ShortArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Short>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: IntArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Int>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: LongArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Long>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: FloatArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Float>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: DoubleArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Double>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: BooleanArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Boolean>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 用 [Rows] 包装 [array]
 * @param array
 * @param [message] 消息
 */
@JvmOverloads
public fun ofApiResult(
    array: CharArray,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<Rows<Char>> =
    ApiResultImpl(ofRows(array), ApiProperty.okCode, message)

/**
 * 只用于 加密
 * @param [data] 数据
 * @param [code] 法典
 * @param [message] 消息
 * @return [ApiResult]<[String]>
 * @author tangli
 * @date 2025/07/29 16:57
 */
@JvmSynthetic
@JvmName("-encryptApiResult")
public fun encryptApiResult(
    data: String,
    code: Int = ApiProperty.okCode,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<String> =
    ApiResultImpl(data, code, message)

/**
 * 构造方法
 * @param [data] 数据
 * @param [code] 编码
 * @param [message] 消息
 * @return [ApiResult]<[T]>
 * @author tangli
 * @date 2025/07/23 16:20
 */
@JvmOverloads
public fun <T> ofApiResult(
    data: T?,
    code: Int = ApiProperty.okCode,
    message: String = ApiProperty.defaultOkMessage,
): ApiResult<T> {
    val template = "%s type can not be the first parameter.Please use ofApiResult(result) instead."
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
    return ApiResultImpl(data, code, message)
}
