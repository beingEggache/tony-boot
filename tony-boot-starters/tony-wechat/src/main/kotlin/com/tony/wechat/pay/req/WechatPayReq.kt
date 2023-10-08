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

package com.tony.wechat.pay.req

import com.fasterxml.jackson.annotation.JsonProperty

public sealed interface WechatPayReq

@Suppress("SpellCheckingInspection")
public data class WechatAppPayReq(
    @JsonProperty("appid")
    val appId: String?,
    @JsonProperty("partnerid")
    val partnerId: String?,
    @JsonProperty("prepayid")
    val prepayId: String,
    @JsonProperty("noncestr")
    val nonceStr: String,
    val timestamp: String,
    @JsonProperty("package")
    val `package`: String = "Sign=WXPay",
    var sign: String = "",
) : WechatPayReq

public data class WechatMiniProgramPayReq(
    @JsonProperty("appId")
    val appId: String?,
    val timeStamp: String,
    val nonceStr: String,
    val signType: String,
    @JsonProperty("package")
    val `package`: String = "Sign=WXPay",
) : WechatPayReq {
    lateinit var paySign: String
}
