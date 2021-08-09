package com.tony.wechat.pay.request

import com.fasterxml.jackson.annotation.JsonProperty

data class WechatMiniProgramPayRequest(
    @JsonProperty("appId")
    var appId: String,

    var timeStamp: String,

    var nonceStr: String,

    @JsonProperty("package")
    var `package`: String = "Sign=WXPay",

    var signType: String,

    var paySign: String? = null
) : WechatPayRequest
