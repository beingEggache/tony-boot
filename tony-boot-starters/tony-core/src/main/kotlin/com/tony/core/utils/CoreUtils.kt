@file:Suppress("unused", "FunctionName")
@file:JvmName("CoreUtils")

package com.tony.core.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@JvmField
val logger: Logger = LoggerFactory.getLogger("web_extension")

fun <T> T?.println() = println(this)

@JvmOverloads
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
