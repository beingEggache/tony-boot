package com.tony.extension

import com.tony.exception.BizException

@JvmSynthetic
inline fun <R> throwIfAndReturn(
    condition: Boolean,
    message: String,
    crossinline block: () -> R,
): R {
    if (condition) throw BizException(message)
    return block()
}

@JvmSynthetic
inline fun <T, R> T?.throwIfNullAndReturn(
    message: String,
    crossinline block: () -> R,
): R =
    throwIfAndReturn(this == null, message, block)
