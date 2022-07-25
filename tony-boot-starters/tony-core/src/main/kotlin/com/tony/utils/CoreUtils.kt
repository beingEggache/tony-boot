@file:Suppress("unused", "FunctionName")
@file:JvmName("CoreUtils")

package com.tony.utils

import com.tony.ApiProperty
import com.tony.Env
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

@JvmOverloads
fun throwIf(condition: Boolean, message: String, code: Int = ApiProperty.bizErrorCode) {
    if (condition) throw BizException(message, code)
}

@JvmOverloads
fun <T> T?.throwIfNull(message: String = ApiProperty.notFoundMessage, code: Int = ApiProperty.notFoundCode): T {
    throwIf(this == null, message, code)
    return this!!
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
val localIp: String = NetworkInterface
    .getNetworkInterfaces()
    .asSequence()
    .filter { it.isUp && it.index != -1 && !it.isLoopback }
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

@Suppress("UNCHECKED_CAST")
fun isPreferredAddress(address: InetAddress): Boolean {
    if (Env.getProperty("spring.cloud.inetutils.use-only-site-local-interfaces", Boolean::class.java, false)) {
        return address.isSiteLocalAddress
    }

    val preferredNetworks =
        Env.getProperty("spring.cloud.inetutils.preferred-networks", List::class.java, emptyList<String>())
            as List<String>

    if (preferredNetworks.isEmpty()) {
        return true
    }
    return preferredNetworks.any {
        val hostAddress = address.hostAddress
        hostAddress.matches(Regex(it)) || hostAddress.startsWith(it)
    }
}
