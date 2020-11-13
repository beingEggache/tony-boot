package com.tony.wechat.response

data class WechatJsSdkConfigResponse(
    var debug: Boolean,
    var appId: String,
    var timestamp: Long,
    var nonceStr: String,
    var signature: String
)
