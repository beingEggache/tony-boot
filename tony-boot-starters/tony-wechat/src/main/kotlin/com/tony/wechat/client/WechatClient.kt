/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.wechat.client

import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatMiniProgramQrCodeCreateReq
import com.tony.wechat.client.req.WechatMiniProgramUserPhoneReq
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.req.WechatStableAccessTokenReq
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

/**
 * 微信 http client
 *
 * @author Tang Li
 * @date 2023/05/25 19:21
 */
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
            defaultValue = "client_credential"
        ) grantType: String? = "client_credential",
    ): WechatApiTokenResp

    /**
     * ## 获取稳定版接口调用凭据
     * - 获取小程序全局后台接口调用凭据，有效期最长为7200s，开发者需要进行妥善保存.
     * - 有两种调用模式:
     * - 该接口调用频率限制为 1万次 每分钟，每天限制调用 50万 次；
     * - 与 getAccessToken 获取的调用凭证完全隔离，互不影响。该接口仅支持 POST JSON 形式的调用；
     * - 如使用云开发，可通过云调用免维护 access_token 调用；
     * - 如使用云托管，也可以通过微信令牌/开放接口服务免维护 access_token 调用；
     * @param [req] 请求
     * @return [WechatApiTokenResp]
     * @author Tang Li
     * @date 2023/09/26 19:15
     * @since 1.0.0
     */
    @PostMapping("/cgi-bin/stable_token")
    public fun stableAccessToken(
        @Validated
        @RequestBody
        req: WechatStableAccessTokenReq,
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
            defaultValue = "authorization_code"
        ) grantType: String? = "authorization_code",
    ): WechatUserTokenResp

    /**
     * ## 获取调用微信JS接口的临时票据
     * @param accessToken
     * @param type
     * @return
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
    public fun createMenu(
        @PathVariable("accessToken") accessToken: String?,
        @RequestBody menu: WechatMenu,
    ): WechatResp

    @GetMapping("/cgi-bin/menu/delete")
    public fun deleteMenu(
        @RequestParam("access_token") accessToken: String?,
    ): WechatResp

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
