/**
 * tony-boot-starters
 * WechatClient
 *
 * TODO
 *
 * @author tangli
 * @since 2021/9/26 13:21
 */
package com.tony.wechat.test

import com.tony.wechat.client.resp.WechatApiTokenResp
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient("wechatClient1", url = "https://api.weixin.qq.com", configuration = [TestWechatConfig1::class])
interface TestWechatClient1 {

    /**
     * ## 获取Access token
     * @param appId 第三方用户唯一凭证
     * @param secret 第三方用户唯一凭证密钥，即appsecret
     * @param grantType 获取access_token填写client_credential
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">获取Access token</a>
     */
    @GetMapping("/cgi-bin/token")
    fun accessToken(
        @RequestParam("appid", required = true) appId: String?,
        @RequestParam("secret", required = true) secret: String?,
        @RequestParam(
            "grant_type",
            required = true,
            defaultValue = "client_credential"
        ) grantType: String? = "client_credential"
    ): WechatApiTokenResp
}

@FeignClient("wechatClient2", url = "https://api.weixin.qq.com", configuration = [TestWechatConfig2::class])
interface TestWechatClient2 {

    /**
     * ## 获取Access token
     * @param appId 第三方用户唯一凭证
     * @param secret 第三方用户唯一凭证密钥，即appsecret
     * @param grantType 获取access_token填写client_credential
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">获取Access token</a>
     */
    @GetMapping("/cgi-bin/token")
    fun accessToken(
        @RequestParam("appid", required = true) appId: String?,
        @RequestParam("secret", required = true) secret: String?,
        @RequestParam(
            "grant_type",
            required = true,
            defaultValue = "client_credential"
        ) grantType: String? = "client_credential"
    ): WechatApiTokenResp
}
