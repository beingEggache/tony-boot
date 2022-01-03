@file:Suppress("unused", "FunctionName")
@file:JvmName("CoreUtils")

package com.tony.utils

import com.tony.exception.BizException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

fun <T> T?.println() = println(this)

@JvmSynthetic
fun <T> T.getLogger(): Logger where T : Any =
    LoggerFactory.getLogger(this::class.java)

fun getLogger(name: String?): Logger = LoggerFactory.getLogger(name)

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

/**
 * 参考SpringCloud获取IP的代码
 *
 * @return ip
 */
val localIp = NetworkInterface
    .getNetworkInterfaces()
    .asSequence()
    .filter { it.isUp }
    .minByOrNull { it.index }
    ?.inetAddresses
    ?.toList()
    ?.firstOrNull { it is Inet4Address && !it.isLoopbackAddress }
    ?.hostAddress
    ?: try {
        InetAddress.getLocalHost().hostAddress
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        null
    } ?: "127.0.0.1"
