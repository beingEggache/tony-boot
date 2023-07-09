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
    @JsonProperty("mch_appid")
    val mchAppId: String?,

    @XStreamAlias("mchid")
    @JsonProperty("mchid")
    val mchId: String?,

    @XStreamAlias("nonce_str")
    val nonceStr: String,

    @XStreamAlias("partner_trade_no")
    val partnerTradeNo: String,

    @XStreamAlias("openid")
    @JsonProperty("openid")
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
