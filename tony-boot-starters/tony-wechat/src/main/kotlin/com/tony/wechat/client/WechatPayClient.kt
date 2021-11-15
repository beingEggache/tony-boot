/**
 * tony-boot-starters
 * WechatPayClient
 *
 * TODO
 *
 * @author tangli
 * @since 2021/9/26 14:46
 */
package com.tony.wechat.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("wechatPayClient", url = "https://api.mch.weixin.qq.com")
interface WechatPayClient {

    @PostMapping("/pay/unifiedorder")
    fun unifiedOrder(@RequestBody req: String): String

    @PostMapping("/mmpaymkttransfers/promotion/transfers")
    fun transfers(): String
}
