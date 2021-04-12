@file:JvmName("WechatUtils")

package com.tony.wechat

import com.tony.core.utils.jsonToObj
import com.tony.core.utils.toJsonString
import org.apache.commons.codec.digest.DigestUtils
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

internal fun genNonceStr() = UUID.randomUUID().toString().replace("-", "").toUpperCase()

internal fun genTimeStamp() = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

fun <T> T.toDeepLink(
    vararg params: Pair<String, Any>,
    filter: ((Map.Entry<String, Any>) -> Boolean)
) = toJsonString()
    .jsonToObj<Map<String, Any>>()
    .asSequence()
    .filter(filter)
    .sortedBy { it.key }
    .map { Pair(it.key, it.value) }
    .plus(params)
    .joinToString("&") {
        "${it.first}=${it.second}"
    }

internal fun <T> genMd5UpperCaseSign(obj: T, vararg params: Pair<String, Any>): String {

    val deepLink = obj.toDeepLink(*params) {
        it.value !is String && !(it.value.toString()).isBlank()
    }
    return DigestUtils.md5Hex(deepLink).toUpperCase()
}
