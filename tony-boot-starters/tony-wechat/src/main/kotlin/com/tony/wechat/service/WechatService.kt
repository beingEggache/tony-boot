@file:Suppress("MemberVisibilityCanBePrivate")

package com.tony.wechat.service

import com.tony.exception.ApiException
import com.tony.utils.getLogger
import com.tony.utils.urlEncode
import com.tony.wechat.check
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.resp.WechatApiTokenResp
import com.tony.wechat.client.resp.WechatJsSdkConfigResp
import com.tony.wechat.client.resp.WechatUserTokenResp
import com.tony.wechat.config.WechatProperties
import com.tony.wechat.genNonceStr
import com.tony.wechat.genTimeStamp
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.validation.annotation.Validated

@Suppress("unused")
class WechatService(
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
        wechatPropProvider.appId(app),
        wechatPropProvider.appSecret(app),
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
        wechatPropProvider.appId(app),
        wechatPropProvider.appSecret(app)
    )

    fun userAccessToken(
        code: String?,
        app: String = "",
    ) = apiAccessTokenProviderWrapper.userAccessToken(
        wechatPropProvider.appId(app),
        wechatPropProvider.appSecret(app),
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
            "appid=$app" +
            "&redirect_uri=${url.urlEncode()}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=#wechat_redirect"
}

interface WechatPropProvider {

    fun appId(app: String): String?

    fun appSecret(app: String): String?

    fun mchId(app: String): String?

    fun mchSecretKey(app: String): String?
}

internal class DefaultWechatPropProvider(
    private val wechatProperties: WechatProperties
) : WechatPropProvider {

    private val logger = getLogger()

    override fun appId(app: String): String {
        val properties = wechatProperties.app[app]
        return if (properties == null) {
            logger.warn("wechat.app.$app prop not found.Use root prop.")
            wechatProperties.appId
        } else {
            properties.appId
        } ?: throw ApiException("$app app-id not found")
    }


    override fun appSecret(app: String): String {
        val properties = wechatProperties.app[app]
        return if (properties == null) {
            logger.warn("wechat.app.$app prop not found.Use root prop.")
            wechatProperties.appSecret
        } else {
            properties.appSecret
        } ?: throw ApiException("$app app-secret not found")
    }

    override fun mchId(app: String): String {
        val properties = wechatProperties.app[app]
        return if (properties == null) {
            logger.warn("wechat.app.$app prop not found.Use root prop.")
            wechatProperties.mchId
        } else {
            properties.mchId
        } ?: throw ApiException("$app mchId not found")
    }

    override fun mchSecretKey(app: String): String {
        val properties = wechatProperties.app[app]
        return if (properties == null) {
            logger.warn("wechat.app.$app prop not found.Use root prop.")
            wechatProperties.mchSecretKey
        } else {
            properties.mchSecretKey
        } ?: throw ApiException("$app mch-secret-key not found")
    }
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
}

internal class DefaultWechatApiAccessTokenProviderWrapper(
    private val wechatApiAccessTokenProvider: WechatApiAccessTokenProvider
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

class WechatApiAccessTokenProvider(
    private val wechatClient: WechatClient
) {

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
