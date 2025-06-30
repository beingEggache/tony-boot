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

package tony.wechat

import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import tony.SpringContexts
import tony.codec.Base64Codec
import tony.utils.jsonToObj
import tony.utils.sha1
import tony.utils.string
import tony.utils.urlEncode
import tony.wechat.client.WechatClient
import tony.wechat.client.req.WechatMenu
import tony.wechat.client.req.WechatMiniProgramQrCodeCreateReq
import tony.wechat.client.req.WechatMiniProgramUserPhoneReq
import tony.wechat.client.req.WechatQrCodeCreateReq
import tony.wechat.client.resp.WechatJsCode2SessionResp
import tony.wechat.client.resp.WechatJsSdkConfigResp
import tony.wechat.client.resp.WechatQrCodeResp
import tony.wechat.client.resp.WechatResp
import tony.wechat.client.resp.WechatUserInfoResp
import tony.wechat.client.resp.WechatUserTokenResp

/**
 * 微信 Manager
 * @author tangli
 * @date 2023/09/28 19:05
 */
public data object WechatManager {
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
     * @author tangli
     * @date 2023/09/28 19:05
     */
    @JvmOverloads
    @JvmStatic
    public fun checkSignature(
        signature: String,
        nonce: String,
        timestamp: String,
        app: String = "",
    ): Boolean =
        listOf(wechatPropProvider.token(app), timestamp, nonce)
            .sorted()
            .joinToString("")
            .sha1() == signature

    @JvmStatic
    public fun checkSignatureDirect(
        signature: String,
        nonce: String,
        timestamp: String,
        token: String,
    ): Boolean =
        listOf(token, timestamp, nonce)
            .sorted()
            .joinToString("")
            .sha1() == signature

    @JvmOverloads
    @JvmStatic
    public fun jsCode2Session(
        jsCode: String,
        app: String = "",
    ): WechatJsCode2SessionResp =
        wechatClient
            .jsCode2Session(
                wechatPropProvider.appId(app),
                wechatPropProvider.appSecret(app),
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
    ): WechatQrCodeResp =
        wechatClient.createQrCode(req, accessToken).check()

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
    public fun getTicket(
        app: String,
        accessToken: String? = accessTokenStr(app),
    ): String? =
        wechatClient
            .getTicket(accessToken, "jsapi")
            .check()
            .ticket

    @JvmStatic
    public fun getJsApiSignature(
        nonceStr: String,
        timestamp: Long,
        url: String,
        app: String,
    ): String =
        listOf(
            "jsapi_ticket" to getTicket(app),
            "noncestr" to nonceStr,
            "timestamp" to timestamp,
            "url" to url
        ).joinToString("&") {
            "${it.first}=${it.second}"
        }.sha1()

    @JvmOverloads
    @JvmStatic
    public fun jsSdkConfig(
        url: String,
        app: String,
        debug: Boolean = false,
    ): WechatJsSdkConfigResp =
        run {
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
    public fun showQrCode(ticket: String): String =
        "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    @JvmStatic
    public fun userAccessToken(
        code: String?,
        app: String = "",
    ): WechatUserTokenResp =
        apiAccessTokenProvider.userAccessToken(
            wechatPropProvider.appId(app),
            wechatPropProvider.appSecret(app),
            code
        )

    private fun accessTokenStr(
        app: String = "",
        forceRefresh: Boolean = false,
    ) =
        apiAccessTokenProvider.accessTokenStr(
            wechatPropProvider.appId(app),
            wechatPropProvider.appSecret(app),
            forceRefresh
        )

    @Suppress("unused")
    private fun wechatRedirect(
        url: String,
        app: String = "",
    ): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=${wechatPropProvider.appId(app)}" +
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
    ): String =
        wechatClient
            .createMiniProgramQrcode(req, accessToken)
            .let { response ->
                response
                    .body()
                    .use { body ->
                        body.asInputStream().use { inputStream ->
                            val contentType =
                                response
                                    .headers()
                                    .get("Content-Type")
                                    .orEmpty()
                                    .first()
                            val bytes = inputStream.readAllBytes()
                            if (MediaType.parseMediaType(contentType).includes(MediaType.APPLICATION_JSON)) {
                                bytes.string().jsonToObj<WechatResp>().check()
                            }
                            Base64Codec.encodeToString(bytes)
                        }
                    }
            }

    @JvmOverloads
    @JvmStatic
    public fun getUserPhoneNumber(
        @Validated
        code: String,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): String? =
        wechatClient
            .getUserPhoneNumber(WechatMiniProgramUserPhoneReq(code), accessToken)
            .check()
            .phoneInfo
            ?.purePhoneNumber
}
