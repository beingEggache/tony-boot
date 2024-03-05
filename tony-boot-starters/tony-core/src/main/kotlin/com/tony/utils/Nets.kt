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

@file:JvmName("Nets")

package com.tony.utils

import com.tony.SpringContexts
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

/**
 * NetUtils is
 * @author tangli
 * @date 2023/06/13 19:13
 */

/**
 * 获取本地ip.
 * 参考SpringCloud获取IP的代码.
 */
@get:JvmName("localIp")
public val localIp: String =
    NetworkInterface
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
 * @param [address] 地址
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:23
 * @since 1.0.0
 */
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
        SpringContexts
            .Env
            .getProperty(
                "spring.cloud.inetutils.preferred-networks",
                List::class.java,
                emptyList<String>()
            ).asToNotNull<List<String>>()

    if (preferredNetworks.isEmpty()) {
        return true
    }
    return preferredNetworks.any {
        val hostAddress = address.hostAddress
        hostAddress.matches(Regex(it)) || hostAddress.startsWith(it)
    }
}
