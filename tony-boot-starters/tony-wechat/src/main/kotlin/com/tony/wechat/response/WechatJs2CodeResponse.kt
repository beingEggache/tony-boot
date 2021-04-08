@file:Suppress("unused")

package com.tony.wechat.response

import com.fasterxml.jackson.annotation.JsonProperty

class WechatJs2CodeResponse(

    @JsonProperty("openid")
    var openId: String?,

    @JsonProperty("session_key")
    var sessionKey: String?,

    @JsonProperty("unionid")
    var unionId: String?,

    @JsonProperty("errcode")
    var errCode: String?,

    @JsonProperty("errmsg")
    var errMsg: String?

)
