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
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * 微信提现请求对象
 */
@Suppress("SpellCheckingInspection")
@XStreamAlias("xml")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
public data class WechatTransferReq(
    @XStreamAlias("mch_appid")
    @param:JsonProperty("mch_appid")
    val mchAppId: String?,
    @XStreamAlias("mchid")
    @param:JsonProperty("mchid")
    val mchId: String?,
    @XStreamAlias("nonce_str")
    val nonceStr: String,
    @XStreamAlias("partner_trade_no")
    val partnerTradeNo: String,
    @XStreamAlias("openid")
    @param:JsonProperty("openid")
    val openId: String,
    @XStreamAlias("check_name")
    val checkName: String = "NO_CHECK",
    val amount: Long,
    val desc: String,
    @XStreamAlias("spbill_create_ip")
    val spbillCreateIP: String,
) {
    var sign: String? = null
}
