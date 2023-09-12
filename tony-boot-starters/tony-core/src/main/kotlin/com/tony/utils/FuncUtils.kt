@file:JvmName("FuncUtils")

package com.tony.utils

/**
 * 核心工具类
 *
 * @author Tang Li
 * @date 2022/9/29 10:20
 */
import com.tony.ApiProperty
import com.tony.exception.BaseException
import com.tony.exception.BizException

@JvmSynthetic
public inline fun Boolean.doIf(crossinline block: () -> Unit) {
    if (this) block()
}

@JvmSynthetic
public inline fun <T> T.doIf(condition: Boolean, crossinline block: T.() -> Unit): T {
    if (condition) block()
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
 * @receiver [T]? this
 * @param [T]
 * @param message 异常信息
 * @param code 异常代码
 * @return [T] this
 *
 * @see throwIf
 */
@JvmOverloads
public fun <T> T?.throwIfNull(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T {
    throwIf(this == null, message, code)
    return this!!
}

@JvmSynthetic
public inline fun <R> throwIfAndReturn(
    condition: Boolean,
    message: String,
    crossinline block: () -> R,
): R {
    if (condition) throw BizException(message)
    return block()
}

@JvmSynthetic
public inline fun <T, R> T?.throwIfNullAndReturn(
    message: String,
    crossinline block: () -> R,
): R = throwIfAndReturn(this == null, message, block)

@JvmSynthetic
public inline fun <reified T> T?.returnIfNull(crossinline block: () -> T): T = this ?: block()
