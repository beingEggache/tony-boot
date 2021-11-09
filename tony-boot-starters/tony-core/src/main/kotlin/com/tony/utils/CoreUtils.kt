@file:Suppress("unused", "FunctionName")
@file:JvmName("CoreUtils")

package com.tony.utils

import com.tony.exception.BizException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T> T?.println() = println(this)

@JvmOverloads
@JvmSynthetic
fun <T> T.getLogger(name: String? = null): Logger where T : Any =
    if (name.isNullOrBlank()) LoggerFactory.getLogger(this::class.java.name)
    else LoggerFactory.getLogger(name)

@Suppress("UNCHECKED_CAST")
fun <E> Any?.asTo(): E? where E : Any = this as E?

inline fun Boolean.doIf(crossinline block: () -> Any) {
    if (this) block()
}

inline fun <T> T.doIf(condition: Boolean, crossinline block: T.() -> Unit): T {
    if (condition) block()
    return this
}

fun throwIf(condition: Boolean, message: String) {
    if (condition) throw BizException(message)
}

fun <T> T?.throwIfNull(message: String) {
    throwIf(this == null, message)
}

inline fun <R> throwIfAndReturn(condition: Boolean, message: String, crossinline block: () -> R): R {
    if (condition) throw BizException(message)
    return block()
}

inline fun <T, R> T?.throwIfNullAndReturn(message: String, crossinline block: () -> R) =
    throwIfAndReturn(this == null, message, block)

inline fun <reified T> T?.returnIfNull(crossinline block: () -> T) = this ?: block()
