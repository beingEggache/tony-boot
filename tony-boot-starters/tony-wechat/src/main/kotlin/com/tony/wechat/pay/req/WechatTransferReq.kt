package com.tony.wechat.pay.req

import com.fasterxml.jackson.annotation.JsonProperty
import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * 微信提现请求对象
 */
@Suppress("unused")
@XStreamAlias("xml")
data class WechatTransferReq(

    @XStreamAlias("mch_appid")
    @JsonProperty("mch_appid")
    var mchAppId: String?,

    @XStreamAlias("mchid")
    @JsonProperty("mchid")
    var mchId: String?,

    @XStreamAlias("nonce_str")
    @JsonProperty("nonce_str")
    var nonceStr: String,

    var sign: String? = null,

    @XStreamAlias("partner_trade_no")
    @JsonProperty("partner_trade_no")
    var partnerTradeNo: String,

    @XStreamAlias("openid")
    @JsonProperty("openid")
    var openId: String,

    @XStreamAlias("check_name")
    @JsonProperty("check_name")
    var checkName: String = "NO_CHECK",

    var amount: Long,

    var desc: String,

    @XStreamAlias("spbill_create_ip")
    @JsonProperty("spbill_create_ip")
    var spbillCreateIP: String
)
