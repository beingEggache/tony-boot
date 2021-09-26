/**
 * tony-boot-starters
 * WechatClient
 *
 * TODO
 *
 * @author tangli
 * @since 2021/9/26 13:21
 */
package com.tony.wechat.client

import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.resp.WechatApiTokenResp
import com.tony.wechat.client.resp.WechatJsApiTicketResp
import com.tony.wechat.client.resp.WechatJsCode2SessionResp
import com.tony.wechat.client.resp.WechatQrCodeResp
import com.tony.wechat.client.resp.WechatResp
import com.tony.wechat.client.resp.WechatUserInfoResp
import com.tony.wechat.client.resp.WechatUserTokenResp
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient("wechatClient", url = "https://api.weixin.qq.com")
interface WechatClient {

    @GetMapping("/cgi-bin/token")
    fun accessToken(
        @RequestParam("appid") appId: String?,
        @RequestParam("secret") secret: String?,
        @RequestParam("grant_type") grantType: String?
    ): WechatApiTokenResp

    @GetMapping("/sns/oauth2/access_token")
    fun userAccessToken(
        @RequestParam("appid") appId: String?,
        @RequestParam("secret") secret: String?,
        @RequestParam("code") code: String?,
        @RequestParam("grant_type") grantType: String?,
    ): WechatUserTokenResp

    @GetMapping("/cgi-bin/ticket/getticket")
    fun getTicket(
        @RequestParam("access_token") accessToken: String?,
        @RequestParam("type") type: String?
    ): WechatJsApiTicketResp

    @GetMapping("/sns/jscode2session")
    fun jsCode2Session(
        @RequestParam("appid") appId: String?,
        @RequestParam("secret") secret: String?,
        @RequestParam("grant_type") grantType: String?,
        @RequestParam("js_code") jsCode: String?
    ): WechatJsCode2SessionResp

    @GetMapping("/cgi-bin/user/info")
    fun userInfo(
        @RequestParam("access_token") accessToken: String?,
        @RequestParam("openid") openId: String?
    ): WechatUserInfoResp

    @PostMapping("/cgi-bin/menu/create?access_token={accessToken}")
    fun createMenu(
        @PathVariable("accessToken") accessToken: String?,
        @RequestBody menu: WechatMenu
    ): WechatResp

    @GetMapping("/cgi-bin/menu/delete")
    fun deleteMenu(
        @RequestParam("access_token") accessToken: String?
    ): WechatResp

    @PostMapping("/cgi-bin/qrcode/create?access_token={accessToken}")
    fun createQrCode(
        @Validated
        @RequestBody
        req: WechatQrCodeCreateReq,
        @PathVariable accessToken: String?
    ): WechatQrCodeResp
}
