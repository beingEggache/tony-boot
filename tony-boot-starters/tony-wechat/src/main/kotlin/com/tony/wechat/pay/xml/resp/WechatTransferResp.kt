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

package com.tony.wechat.pay.xml.resp

import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * 微信提现响应对象
 */
@XStreamAlias("xml")
public data class WechatTransferResp(

    @XStreamAlias("return_code")
    var returnCode: String,
    @XStreamAlias("return_msg")
    var returnMsg: String,
    @XStreamAlias("mch_appid")
    var mchAppId: String,
    @XStreamAlias("mchid")
    var mchId: String,
    @XStreamAlias("nonce_str")
    var nonceStr: String,
    @XStreamAlias("result_code")
    var resultCode: String,
    @XStreamAlias("err_code")
    var errCode: String,
    @XStreamAlias("err_code_des")
    var errCodeDes: String,
    @XStreamAlias("partner_trade_no")
    var partnerTradeNo: String,
    @XStreamAlias("payment_no")
    var paymentNo: String,
    @XStreamAlias("payment_time")
    var paymentTime: String,

)
