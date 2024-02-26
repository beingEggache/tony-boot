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

@file:JvmName("Wechats")

package com.tony.wechat

import com.tony.utils.jsonToObj
import com.tony.utils.md5
import com.tony.utils.toJsonString
import com.tony.wechat.client.resp.WechatResp
import com.tony.wechat.exception.WechatException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@JvmSynthetic
internal fun genNonceStr() =
    UUID
        .randomUUID()
        .toString()
        .replace("-", "")
        .uppercase()

@JvmSynthetic
internal fun genTimeStamp() =
    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

@JvmSynthetic
internal fun <T> T.toQueryString(
    vararg params: Pair<String, Any?>,
    filter: ((Map.Entry<String, Any>) -> Boolean),
): String =
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

@JvmSynthetic
internal fun <T> genMd5UpperCaseSign(
    obj: T,
    vararg params: Pair<String, Any?>,
): String =
    obj
        .toQueryString(*params) {
            it.value !is String &&
                it
                    .value
                    .toString()
                    .isNotBlank()
        }.md5().uppercase()

@JvmSynthetic
internal fun <T : WechatResp> T.check(): T {
    if (!success) {
        throw WechatException("errcode: $errCode, errmsg: $errMsg")
    }
    return this
}
