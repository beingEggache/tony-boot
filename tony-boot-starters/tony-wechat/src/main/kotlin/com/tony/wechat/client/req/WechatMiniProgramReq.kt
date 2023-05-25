package com.tony.wechat.client.req

/**
 * WechatMiniProgramQrCodeCreateReq
 *
 * @author wyf
 * @since 2022/8/31 16:26
 */
public data class WechatMiniProgramQrCodeCreateReq(
    val scene: String,
    val page: String,
)

public data class WechatMiniProgramUserPhoneReq(
    val code: String,
)
