package com.tony.wechat.pay.req

import com.fasterxml.jackson.annotation.JsonProperty

sealed interface WechatPayReq

data class WechatAppPayReq(
    @JsonProperty("appid") val appId: String?,
    @JsonProperty("partnerid") val partnerId: String?,
    @JsonProperty("prepayid") val prepayId: String,
    @JsonProperty("noncestr") val nonceStr: String,
    val timestamp: String,
    @JsonProperty("package") val `package`: String = "Sign=WXPay",
    var sign: String? = null
) : WechatPayReq

data class WechatMiniProgramPayReq(
    @JsonProperty("appId") val appId: String?,
    val timeStamp: String,
    val nonceStr: String,
    val signType: String,
    @JsonProperty("package") val `package`: String = "Sign=WXPay",
    var paySign: String? = null
) : WechatPayReq
