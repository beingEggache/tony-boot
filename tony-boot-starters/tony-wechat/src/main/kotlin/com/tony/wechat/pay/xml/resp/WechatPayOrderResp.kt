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
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.XStreamCDataConverter

/**
 * 统一下单响应对象
 */
@XStreamAlias("xml")
public data class WechatPayOrderResp(

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_code")
    var returnCode: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_msg")
    var returnMsg: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("appid")
    var appId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("mch_id")
    var mchId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("nonce_str")
    var nonceStr: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("openid")
    var openId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("sign")
    var sign: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("result_code")
    var resultCode: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("err_code")
    var errCode: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("err_code_des")
    var errCodeDes: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("trade_type")
    var tradeType: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("prepay_id")
    var prePayId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("mweb_url")
    var webURL: String?,
)
