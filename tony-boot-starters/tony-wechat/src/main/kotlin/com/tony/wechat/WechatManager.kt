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

package com.tony.wechat

import com.tony.SpringContexts
import com.tony.utils.string
import com.tony.utils.urlEncode
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatMiniProgramQrCodeCreateReq
import com.tony.wechat.client.req.WechatMiniProgramUserPhoneReq
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.resp.WechatJsCode2SessionResp
import com.tony.wechat.client.resp.WechatJsSdkConfigResp
import com.tony.wechat.client.resp.WechatQrCodeResp
import com.tony.wechat.client.resp.WechatResp
import com.tony.wechat.client.resp.WechatUserInfoResp
import com.tony.wechat.client.resp.WechatUserTokenResp
import java.util.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.validation.annotation.Validated

/**
 * 微信 Manager
 * @author Tang Li
 * @date 2023/09/28 11:05
 * @since 1.0.0
 */
public object WechatManager {

    private val wechatClient: WechatClient by SpringContexts.getBeanByLazy()
    private val wechatPropProvider: WechatPropProvider by SpringContexts.getBeanByLazy()
    private val apiAccessTokenProvider: WechatApiAccessTokenProvider by SpringContexts.getBeanByLazy()

    /**
     * wechat 签名验证
     * @param [signature] 签名
     * @param [nonce] nonce
     * @param [timestamp] 时间戳
     * @param [app] 应用程序
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 11:05
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun checkSignature(
        signature: String,
        nonce: String,
        timestamp: String,
        app: String = "",
    ): Boolean =
        DigestUtils.sha1Hex(
            listOf(wechatPropProvider.getToken(app), timestamp, nonce)
                .sorted()
                .joinToString("")
        ) == signature

    @JvmStatic
    public fun checkSignatureDirect(
        signature: String,
        nonce: String,
        timestamp: String,
        token: String,
    ): Boolean =
        DigestUtils.sha1Hex(
            listOf(token, timestamp, nonce)
                .sorted()
                .joinToString("")
        ) == signature

    @JvmOverloads
    @JvmStatic
    public fun jsCode2Session(jsCode: String, app: String = ""): WechatJsCode2SessionResp =
        wechatClient.jsCode2Session(
            wechatPropProvider.getAppId(app),
            wechatPropProvider.getAppSecret(app),
            "authorization_code",
            jsCode
        ).check()

    @JvmOverloads
    @JvmStatic
    public fun createQrCode(
        @Validated
        req: WechatQrCodeCreateReq,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): WechatQrCodeResp = wechatClient.createQrCode(req, accessToken).check()

    @JvmOverloads
    @JvmStatic
    public fun createMenu(
        menu: WechatMenu,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): WechatResp =
        wechatClient.createMenu(accessToken, menu).check()

    @JvmOverloads
    @JvmStatic
    public fun deleteMenu(
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): WechatResp =
        wechatClient.deleteMenu(accessToken).check()

    @JvmOverloads
    @JvmStatic
    public fun userInfo(
        openId: String?,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): WechatUserInfoResp =
        wechatClient.userInfo(accessToken, openId).check()

    @JvmOverloads
    @JvmStatic
    public fun getTicket(app: String, accessToken: String? = accessTokenStr(app)): String? =
        wechatClient.getTicket(accessToken, "jsapi").check().ticket

    @JvmStatic
    public fun getJsApiSignature(nonceStr: String, timestamp: Long, url: String, app: String): String =
        DigestUtils.sha1Hex(
            listOf(
                "jsapi_ticket" to getTicket(app),
                "noncestr" to nonceStr,
                "timestamp" to timestamp,
                "url" to url
            ).joinToString("&") {
                "${it.first}=${it.second}"
            }
        )

    @JvmOverloads
    @JvmStatic
    public fun jsSdkConfig(url: String, app: String, debug: Boolean = false): WechatJsSdkConfigResp = run {
        val timestamp = genTimeStamp()
        val nonceStr = genNonceStr()
        WechatJsSdkConfigResp(
            debug = debug,
            appId = app,
            timestamp = timestamp,
            nonceStr = nonceStr,
            signature = getJsApiSignature(nonceStr, timestamp, url, app)
        )
    }

    @JvmStatic
    public fun showQrCode(ticket: String): String = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    @JvmStatic
    public fun userAccessToken(code: String?, app: String = ""): WechatUserTokenResp =
        apiAccessTokenProvider.userAccessToken(
            wechatPropProvider.getAppId(app),
            wechatPropProvider.getAppSecret(app),
            code
        )

    private fun accessTokenStr(app: String = "", forceRefresh: Boolean = false) = apiAccessTokenProvider.accessTokenStr(
        wechatPropProvider.getAppId(app),
        wechatPropProvider.getAppSecret(app),
        forceRefresh
    )

    @Suppress("unused")
    private fun wechatRedirect(url: String, app: String = ""): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=${wechatPropProvider.getAppId(app)}" +
            "&redirect_uri=${url.urlEncode()}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=#wechat_redirect"

    @JvmOverloads
    @JvmStatic
    public fun createMiniProgramQrCode(
        @Validated
        req: WechatMiniProgramQrCodeCreateReq,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): String = wechatClient
        .createMiniProgramQrcode(req, accessToken)
        .body()
        .asInputStream()
        .use {
            Base64.getEncoder().encode(it.readAllBytes()).string()
        }

    @JvmOverloads
    @JvmStatic
    public fun getUserPhoneNumber(
        @Validated
        code: String,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): String? = wechatClient
        .getUserPhoneNumber(WechatMiniProgramUserPhoneReq(code), accessToken)
        .check()
        .phoneInfo
        ?.purePhoneNumber
}
