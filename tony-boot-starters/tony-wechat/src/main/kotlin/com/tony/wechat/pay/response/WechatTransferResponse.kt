@file:Suppress("unused")

package com.tony.wechat.pay.response

import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * 微信提现响应对象
 */
@XStreamAlias("xml")
data class WechatTransferResponse(

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
    var paymentTime: String

)
