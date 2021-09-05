@file:Suppress("unused", "FunctionName")
@file:JvmName("CoreUtils")

package com.tony.core.utils

import com.tony.core.exception.BizException
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

inline fun <T> T.doIf(flag: Boolean, crossinline block: T.() -> Unit): T {
    if (flag) block()
    return this
}

inline fun Boolean.doUnless(crossinline block: () -> Any) {
    if (!this) block()
}

inline fun <reified T> T?.doIfNull(crossinline block: () -> T): T {
    return this ?: block()
}

inline fun checkBiz(value: Boolean, lazyMessage: () -> Any) {
    if (value) {
        throw BizException(lazyMessage().toString())
    }
}

inline fun <T : Any> checkBizNotNull(value: T?, lazyMessage: () -> Any): T {
    if (value == null) {
        throw BizException(lazyMessage().toString())
    } else {
        return value
    }
}

inline fun checkStringBizNotBlank(value: String?, lazyMessage: () -> Any) {
    if (value.isNullOrBlank()) {
        throw BizException(lazyMessage().toString())
    }
}
