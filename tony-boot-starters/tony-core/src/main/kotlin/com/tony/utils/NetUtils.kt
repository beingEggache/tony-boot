@file:JvmName("NetUtils")

package com.tony.utils

import com.tony.SpringContexts
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

/**
 * NetUtils is
 * @author Tang Li
 * @date 2023/06/13 17:13
 */
/**
 * 获取本地ip.
 *
 * 参考SpringCloud获取IP的代码.
 * @return ip
 */
public val localIp: String = NetworkInterface
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
        getLogger("com.tony.utils.NetUtils")
            .error(e.message, e)
        null
    } ?: "127.0.0.1"

/**
 * 判断地址是否是期望的域.
 */
@Suppress("UNCHECKED_CAST")
public fun isPreferredAddress(address: InetAddress): Boolean {
    val useOnlySiteLocalInterfaces =
        SpringContexts.Env.getProperty(
            "spring.cloud.inetutils.use-only-site-local-interfaces",
            Boolean::class.java,
            false
        )
    if (useOnlySiteLocalInterfaces) {
        return address.isSiteLocalAddress
    }

    val preferredNetworks =
        SpringContexts.Env.getProperty(
            "spring.cloud.inetutils.preferred-networks",
            List::class.java,
            emptyList<String>()
        ) as List<String>

    if (preferredNetworks.isEmpty()) {
        return true
    }
    return preferredNetworks.any {
        val hostAddress = address.hostAddress
        hostAddress.matches(Regex(it)) || hostAddress.startsWith(it)
    }
}
