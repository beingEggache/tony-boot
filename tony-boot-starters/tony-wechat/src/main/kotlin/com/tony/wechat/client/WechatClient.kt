/**
 * tony-boot-starters
 * WechatClient
 *
 * @author tangli
 * @since 2021/9/26 13:21
 */
package com.tony.wechat.client

import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatMiniProgramQrCodeCreateReq
import com.tony.wechat.client.req.WechatMiniProgramUserPhoneReq
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.resp.WechatApiTokenResp
import com.tony.wechat.client.resp.WechatJsApiTicketResp
import com.tony.wechat.client.resp.WechatJsCode2SessionResp
import com.tony.wechat.client.resp.WechatMiniProgramUserPhoneResp
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
public interface WechatClient {

    /**
     * ## 获取Access token
     * @param appId 第三方用户唯一凭证
     * @param secret 第三方用户唯一凭证密钥，即appsecret
     * @param grantType 获取access_token填写client_credential
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">获取Access token</a>
     */
    @GetMapping("/cgi-bin/token")
    public fun accessToken(
        @RequestParam("appid", required = true) appId: String?,
        @RequestParam("secret", required = true) secret: String?,
        @RequestParam(
            "grant_type",
            required = true,
            defaultValue = "client_credential",
        ) grantType: String? = "client_credential",
    ): WechatApiTokenResp

    /**
     * ## 网页授权
     * @param appId 公众号的唯一标识
     * @param secret 公众号的appsecret
     * @param code 填写第一步获取的code参数
     * @param grantType 填写为authorization_code
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html">网页授权</a>
     */
    @GetMapping("/sns/oauth2/access_token")
    public fun userAccessToken(
        @RequestParam("appid", required = true) appId: String?,
        @RequestParam("secret", required = true) secret: String?,
        @RequestParam("code", required = true) code: String?,
        @RequestParam(
            "grant_type",
            required = true,
            defaultValue = "authorization_code",
        ) grantType: String? = "authorization_code",
    ): WechatUserTokenResp

    /**
     * ## 获取调用微信JS接口的临时票据
     */
    @GetMapping("/cgi-bin/ticket/getticket")
    public fun getTicket(
        @RequestParam("access_token") accessToken: String?,
        @RequestParam("type") type: String?,
    ): WechatJsApiTicketResp

    @GetMapping("/sns/jscode2session")
    public fun jsCode2Session(
        @RequestParam("appid") appId: String?,
        @RequestParam("secret") secret: String?,
        @RequestParam("grant_type") grantType: String?,
        @RequestParam("js_code") jsCode: String?,
    ): WechatJsCode2SessionResp

    @GetMapping("/cgi-bin/user/info")
    public fun userInfo(
        @RequestParam("access_token") accessToken: String?,
        @RequestParam("openid") openId: String?,
    ): WechatUserInfoResp

    @PostMapping("/cgi-bin/menu/create?access_token={accessToken}")
    public fun createMenu(@PathVariable("accessToken") accessToken: String?, @RequestBody menu: WechatMenu,): WechatResp

    @GetMapping("/cgi-bin/menu/delete")
    public fun deleteMenu(@RequestParam("access_token") accessToken: String?,): WechatResp

    @PostMapping("/cgi-bin/qrcode/create?access_token={accessToken}")
    public fun createQrCode(
        @Validated
        @RequestBody
        req: WechatQrCodeCreateReq,
        @PathVariable accessToken: String?,
    ): WechatQrCodeResp

    @PostMapping("/wxa/getwxacodeunlimit?access_token={accessToken}")
    public fun createMiniProgramQrcode(
        @RequestBody
        req: WechatMiniProgramQrCodeCreateReq,
        @PathVariable accessToken: String?,
    ): feign.Response

    @PostMapping("/wxa/business/getuserphonenumber?access_token={accessToken}")
    public fun getUserPhoneNumber(
        @RequestBody
        req: WechatMiniProgramUserPhoneReq,
        @PathVariable accessToken: String?,
    ): WechatMiniProgramUserPhoneResp
}
