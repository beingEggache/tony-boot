package com.tony.wechat.pay.req

import com.fasterxml.jackson.annotation.JsonProperty

data class WechatAppPayReq(
    @JsonProperty("appid") val appId: String?,
    @JsonProperty("partnerid") val partnerId: String?,
    @JsonProperty("prepayid") val prepayId: String,
    @JsonProperty("noncestr") val nonceStr: String,
    val timestamp: String,
    @JsonProperty("package") val `package`: String = "Sign=WXPay",
    var sign: String? = null
) : WechatPayReq
