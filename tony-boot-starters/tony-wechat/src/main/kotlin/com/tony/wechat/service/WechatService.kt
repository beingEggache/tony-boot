package com.tony.wechat.service

import com.tony.core.exception.ApiException
import com.tony.core.utils.getLogger
import com.tony.core.utils.urlEncode
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.resp.WechatJsSdkConfigResp
import com.tony.wechat.client.resp.WechatResp
import com.tony.wechat.config.WechatProperties
import com.tony.wechat.genNonceStr
import com.tony.wechat.genTimeStamp
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.validation.annotation.Validated

@Suppress("unused")
class WechatService(
    private val wechatProperties: WechatProperties,
    private val wechatClient: WechatClient
) {

    private val logger = getLogger()

    fun checkSignature(token: String, signature: String, nonce: String, timestamp: String) =
        DigestUtils
            .sha1Hex(
                listOf(token, timestamp, nonce)
                    .sorted()
                    .joinToString("")
            ) == signature

    fun jsCode2Session(jsCode: String) = wechatClient.jsCode2Session(
        wechatProperties.miniProgramAppId,
        wechatProperties.miniProgramAppSecret,
        "authorization_code",
        jsCode
    ).check()

    fun createQrCode(
        @Validated
        req: WechatQrCodeCreateReq
    ) = wechatClient.createQrCode(req, accessToken()).check()

    private fun accessToken() = wechatClient.accessToken(
        wechatProperties.subscriptionAppId,
        wechatProperties.appSecret,
        "client_credential"
    ).check().accessToken

    fun userAccessToken(code: String?) = wechatClient.userAccessToken(
        wechatProperties.subscriptionAppId,
        wechatProperties.appSecret,
        code,
        "authorization_code"
    ).check()

    fun createMenu(menu: WechatMenu) = wechatClient.createMenu(accessToken(), menu).check()

    fun deleteMenu() = wechatClient.deleteMenu(accessToken()).check()

    fun userInfo(openId: String?) = wechatClient.userInfo(accessToken(), openId).check()

    private fun getTicket() = wechatClient.getTicket(accessToken(), "jsapi").check().ticket

    private fun getJsApiSignature(nonceStr: String, timestamp: Long, url: String) =
        DigestUtils.sha1Hex(
            listOf(
                "jsapi_ticket" to getTicket(),
                "noncestr" to nonceStr,
                "timestamp" to timestamp,
                "url" to url
            ).joinToString("&") {
                "${it.first}=${it.second}"
            }
        )

    fun jsSdkConfig(url: String, debug: Boolean = false) =
        run {
            val timestamp = genTimeStamp()
            val nonceStr = genNonceStr()
            WechatJsSdkConfigResp(
                debug = debug,
                appId = wechatProperties.subscriptionAppId,
                timestamp = timestamp,
                nonceStr = nonceStr,
                signature = getJsApiSignature(nonceStr, timestamp, url)
            )
        }

    fun showQrCode(ticket: String) = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    private fun wechatRedirect(url: String): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=${wechatProperties.subscriptionAppId}" +
            "&redirect_uri=${url.urlEncode()}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=#wechat_redirect"

    private fun <T : WechatResp> T.check(): T {
        if (!success()) {
            throw ApiException("errcode: $errCode, errmsg: $errMsg")
        }
        return this
    }
}
