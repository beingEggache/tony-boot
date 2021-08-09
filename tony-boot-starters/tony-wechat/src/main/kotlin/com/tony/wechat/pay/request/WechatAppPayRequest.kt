package com.tony.wechat.pay.request

import com.fasterxml.jackson.annotation.JsonProperty

data class WechatAppPayRequest(

    @JsonProperty("appid")
    var appId: String,

    @JsonProperty("partnerid")
    var partnerId: String,

    @JsonProperty("prepayid")
    var prepayId: String,

    @JsonProperty("package")
    var `package`: String = "Sign=WXPay",

    @JsonProperty("noncestr")
    var nonceStr: String,

    var timestamp: String,

    var sign: String? = null

) : WechatPayRequest
