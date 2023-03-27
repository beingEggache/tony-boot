@file:JvmName("WechatUtils")

package com.tony.wechat

import com.tony.utils.jsonToObj
import com.tony.utils.md5Uppercase
import com.tony.utils.toJsonString
import com.tony.wechat.client.resp.WechatResp
import com.tony.wechat.exception.WechatException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

internal fun genNonceStr() = UUID.randomUUID().toString().replace("-", "").uppercase()

internal fun genTimeStamp() = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

public fun <T> T.toDeepLink(vararg params: Pair<String, Any?>, filter: ((Map.Entry<String, Any>) -> Boolean)): String =
    toJsonString()
        .jsonToObj<Map<String, Any>>()
        .asSequence()
        .filter(filter)
        .sortedBy { it.key }
        .map { Pair(it.key, it.value) }
        .plus(params)
        .joinToString("&") {
            "${it.first}=${it.second}"
        }

internal fun <T> genMd5UpperCaseSign(obj: T, vararg params: Pair<String, Any?>): String {
    val deepLink = obj.toDeepLink(*params) {
        it.value !is String && it.value.toString().isNotBlank()
    }
    return deepLink.md5Uppercase()
}

public fun <T : WechatResp> T.check(): T {
    if (!success()) {
        throw WechatException("errcode: $errCode, errmsg: $errMsg")
    }
    return this
}
