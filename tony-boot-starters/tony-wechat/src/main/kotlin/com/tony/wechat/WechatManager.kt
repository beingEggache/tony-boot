@file:Suppress("MemberVisibilityCanBePrivate")

package com.tony.wechat

import com.tony.Beans
import com.tony.exception.ApiException
import com.tony.utils.getLogger
import com.tony.utils.urlEncode
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.resp.WechatApiTokenResp
import com.tony.wechat.client.resp.WechatJsSdkConfigResp
import com.tony.wechat.client.resp.WechatUserTokenResp
import com.tony.wechat.config.WechatProperties
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.validation.annotation.Validated

@Suppress("unused")
class WechatManager(
    private val wechatClient: WechatClient,
    private val wechatPropProvider: WechatPropProvider,
    private val apiAccessTokenProviderWrapper: WechatApiAccessTokenProviderWrapper
) {

    private val logger = getLogger()

    fun checkSignature(token: String, signature: String, nonce: String, timestamp: String) =
        DigestUtils
            .sha1Hex(
                listOf(token, timestamp, nonce)
                    .sorted()
                    .joinToString("")
            ) == signature

    fun jsCode2Session(jsCode: String, app: String = "") = wechatClient.jsCode2Session(
        wechatPropProvider.getAppId(app),
        wechatPropProvider.getAppSecret(app),
        "authorization_code",
        jsCode
    ).check()

    @JvmOverloads
    fun createQrCode(
        @Validated
        req: WechatQrCodeCreateReq,
        app: String = "",
        accessToken: String? = accessToken(app).accessToken
    ) = wechatClient.createQrCode(req, accessToken).check()

    fun accessToken(app: String = "") = apiAccessTokenProviderWrapper.accessToken(
        wechatPropProvider.getAppId(app),
        wechatPropProvider.getAppSecret(app)
    )

    fun userAccessToken(
        code: String?,
        app: String = "",
    ) = apiAccessTokenProviderWrapper.userAccessToken(
        wechatPropProvider.getAppId(app),
        wechatPropProvider.getAppSecret(app),
        code
    )

    @JvmOverloads
    fun createMenu(
        menu: WechatMenu,
        app: String = "",
        accessToken: String? = accessToken(app).accessToken
    ) = wechatClient.createMenu(accessToken, menu).check()

    @JvmOverloads
    fun deleteMenu(
        app: String = "",
        accessToken: String? = accessToken(app).accessToken
    ) = wechatClient.deleteMenu(accessToken).check()

    @JvmOverloads
    fun userInfo(
        openId: String?,
        app: String = "",
        accessToken: String? = accessToken(app).accessToken
    ) = wechatClient.userInfo(accessToken, openId).check()

    @JvmOverloads
    fun getTicket(
        app: String,
        accessToken: String? = accessToken(app).accessToken
    ) = wechatClient.getTicket(accessToken, "jsapi").check().ticket

    fun getJsApiSignature(nonceStr: String, timestamp: Long, url: String, app: String): String =
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
    fun jsSdkConfig(
        url: String,
        app: String,
        debug: Boolean = false
    ) = run {
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

    fun showQrCode(ticket: String) = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    private fun wechatRedirect(url: String, app: String = ""): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid= ${wechatPropProvider.getAppId(app)}" +
            "&redirect_uri=${url.urlEncode()}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=#wechat_redirect"
}

interface WechatPropProvider {

    fun getAppId(app: String? = ""): String?

    fun getAppSecret(app: String? = ""): String?

    fun getMchId(app: String? = ""): String?

    fun getMchSecretKey(app: String? = ""): String?
}

internal class DefaultWechatPropProvider(
    private val wechatProperties: WechatProperties
) : WechatPropProvider {

    override fun getAppId(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.appId
    } else {
        wechatProperties.app[app]?.appId
    } ?: throw ApiException("$app app-id not found")

    override fun getAppSecret(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.appSecret
    } else {
        wechatProperties.app[app]?.appSecret
    } ?: throw ApiException("$app app-secret not found")

    override fun getMchId(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.mchId
    } else {
        wechatProperties.app[app]?.mchId
    } ?: throw ApiException("$app mchId not found")

    override fun getMchSecretKey(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.mchSecretKey
    } else {
        wechatProperties.app[app]?.mchSecretKey
    } ?: throw ApiException("$app mch-secret-key not found")
}

interface WechatApiAccessTokenProviderWrapper {

    fun accessToken(
        appId: String?,
        appSecret: String?
    ): WechatApiTokenResp

    fun userAccessToken(
        appId: String?,
        secret: String?,
        code: String?
    ): WechatUserTokenResp

    var wechatApiAccessTokenProvider: WechatApiAccessTokenProvider
}

internal class DefaultWechatApiAccessTokenProviderWrapper(
    override var wechatApiAccessTokenProvider: WechatApiAccessTokenProvider
) : WechatApiAccessTokenProviderWrapper {

    override fun accessToken(
        appId: String?,
        appSecret: String?
    ) = wechatApiAccessTokenProvider.accessToken(
        appId,
        appSecret
    )

    override fun userAccessToken(
        appId: String?,
        secret: String?,
        code: String?
    ) = wechatApiAccessTokenProvider.userAccessToken(
        appId,
        secret,
        code
    )
}

class WechatApiAccessTokenProvider {

    private val wechatClient: WechatClient by Beans.getBeanByLazy()

    fun accessToken(
        appId: String?,
        appSecret: String?
    ) = wechatClient.accessToken(
        appId,
        appSecret,
        "client_credential"
    ).check()

    fun userAccessToken(
        appId: String?,
        secret: String?,
        code: String?,
    ) = wechatClient.userAccessToken(
        appId,
        secret,
        code,
        "authorization_code"
    ).check()
}
