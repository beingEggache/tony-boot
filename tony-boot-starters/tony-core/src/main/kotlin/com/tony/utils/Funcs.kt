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

@file:JvmName("Funcs")

package com.tony.utils

/**
 * 核心工具类
 *
 * @author Tang Li
 * @date 2022/09/29 19:20
 */
import com.tony.ApiProperty
import com.tony.exception.BaseException
import com.tony.exception.BizException

/**
 * 如果为null, 提供默认值.
 * @receiver [T]?
 * @param [T] 自身类型
 * @param [default] 默认值
 * @return [T]
 * @author Tang Li
 * @date 2023/11/07 19:38
 * @since 1.0.0
 */
public fun <T> T?.ifNull(default: T): T =
    this ?: default

/**
 * 如果为null, 通过回调[block]提供默认值.
 * @receiver [T]?
 * @param [T] 自身类型
 * @param [block] 回调
 * @return [T]
 * @author Tang Li
 * @date 2023/11/07 19:39
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun <reified T> T?.ifNull(crossinline block: () -> T): T =
    this ?: block()

/**
 * 如果条件[this]为真,  执行回调[block]. 并返回回调函数值.
 * @receiver [Boolean]
 * @param [T] 返回类型
 * @param [block] 回调
 * @return [T]?
 * @author Tang Li
 * @date 2023/11/07 19:39
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun <T> Boolean.runIf(crossinline block: () -> T?): T? =
    if (this) {
        block()
    } else {
        null
    }

/**
 * 如果条件为真,  执行回调[block].
 * @receiver [Boolean] 条件.
 * @param [block] 回调.
 * @author Tang Li
 * @date 2023/11/07 19:41
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun Boolean.alsoIf(crossinline block: () -> Unit) {
    if (this) block()
}

/**
 * 如果条件[condition]为真,  执行回调[block]. 并返回回调函数值.
 * @receiver [T] 自身
 * @param [T] 自身类型
 * @param [R] 返回类型
 * @param [condition] 条件
 * @param [block] 块
 * @return [R]?
 * @author Tang Li
 * @date 2023/11/07 19:41
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun <T, R> T.runIf(
    condition: Boolean,
    crossinline block: T.() -> R,
): R? =
    if (condition) {
        block()
    } else {
        null
    }

/**
 * 如果条件[condition]为真,  执行回调[block]. 并返回回调函数值.
 * @receiver [T] 自身
 * @param [T] 自身类型
 * @param [R] 返回类型
 * @param [condition] 条件
 * @param [block] 块
 * @return [R]?
 * @author Tang Li
 * @date 2023/11/07 19:41
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun <T, R> T.letIf(
    condition: Boolean,
    crossinline block: (T) -> R,
): R? =
    if (condition) {
        block(this)
    } else {
        null
    }

/**
 * 如果条件[condition]为真,  执行回调[block]. 并返回自身.
 * @receiver [T] 自身
 * @param [T] 自身类型
 * @param [condition] 条件
 * @param [block] 块
 * @return [this]?
 * @author Tang Li
 * @date 2023/11/07 19:41
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun <T> T.applyIf(
    condition: Boolean,
    crossinline block: T.() -> Unit,
): T {
    if (condition) block()
    return this
}

/**
 * 如果条件[condition]为真,  执行回调[block]. 并返回自身.
 * @receiver [T] 自身
 * @param [T] 自身类型
 * @param [condition] 条件
 * @param [block] 块
 * @return [this]?
 * @author Tang Li
 * @date 2023/11/07 19:41
 * @since 1.0.0
 */
@JvmSynthetic
public inline fun <T> T.alsoIf(
    condition: Boolean,
    crossinline block: (T) -> Unit,
): T {
    if (condition) block(this)
    return this
}

/**
 * 当[condition]为真时,抛出[BizException]异常.
 *
 * 异常信息为[message], 异常代码为[code],默认为[ApiProperty.preconditionFailedCode]
 *
 * @param condition 异常条件
 * @param message 异常信息
 * @param code 异常代码
 * @param ex 异常类型
 */
@JvmOverloads
public fun throwIf(
    condition: Boolean,
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
    ex: (message: String, code: Int) -> BaseException = ::BizException,
) {
    if (condition) throw ex(message, code)
}

/**
 * 当 [this] [T]? 为 null 时, 抛出异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 *
 * @receiver [T]?
 * @param [T] 自身类型
 * @param message 异常信息
 * @param code 异常代码
 * @param ex 异常类型
 * @return [T] this
 *
 * @see throwIf
 */
@JvmOverloads
public fun <T> T?.throwIfNull(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
    ex: (message: String, code: Int) -> BaseException = ::BizException,
): T {
    throwIf(this == null, message, code, ex)
    return this!!
}

/**
 * 当 [this]? 为 null 或者 空时, 抛出异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 异常信息
 * @param [code] 异常代码
 * @param [ex] 异常类型构造函数
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmOverloads
public fun <C : Collection<T>, T : Any?> C?.throwIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
    ex: (message: String, code: Int) -> BaseException = ::BizException,
): C {
    throwIf(isNullOrEmpty(), message, code, ex)
    return this!!
}

/**
 * 当 [this]? 为 null 或者 空时, 抛出异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @receiver [C]
 * @param [C] Map类型
 * @param [message] 异常信息
 * @param [code] 异常代码
 * @param [ex] 异常类型构造函数
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmOverloads
public fun <C : Map<*, *>> C?.throwIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
    ex: (message: String, code: Int) -> BaseException = ::BizException,
): C {
    throwIf(isNullOrEmpty(), message, code, ex)
    return this!!
}

/**
 * 当 [this]? 为 null 或者 空时, 抛出异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @receiver [C]
 * @param [C] 字符串类型
 * @param [message] 异常信息
 * @param [code] 异常代码
 * @param [ex] 异常类型构造函数
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmOverloads
public fun <C : CharSequence> C?.throwIfNullOrEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
    ex: (message: String, code: Int) -> BaseException = ::BizException,
): C {
    throwIf(isNullOrEmpty(), message, code, ex)
    return this!!
}
