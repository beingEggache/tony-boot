package com.tony.wechat.client.req

/**
 * ktwl-boot-dependencies
 * WechatMiniProgramReq
 *
 * @author wyf
 * @since 2022/8/31 16:26
 */
data class WechatMiniProgramQrCodeCreateReq(
    val scene: String,
    val page: String
)

data class WechatMiniProgramUserPhoneReq(
    val code: String
)
