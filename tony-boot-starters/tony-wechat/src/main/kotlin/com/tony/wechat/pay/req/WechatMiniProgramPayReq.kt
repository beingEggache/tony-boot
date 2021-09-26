package com.tony.wechat.pay.req

import com.fasterxml.jackson.annotation.JsonProperty

data class WechatMiniProgramPayReq(
    @JsonProperty("appId") var appId: String?,
    var timeStamp: String,
    var nonceStr: String,
    var signType: String,
    @JsonProperty("package") var `package`: String = "Sign=WXPay",
    var paySign: String? = null
) : WechatPayReq
