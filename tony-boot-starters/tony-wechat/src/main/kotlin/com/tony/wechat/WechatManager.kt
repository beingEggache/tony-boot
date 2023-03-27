package com.tony.wechat

import com.tony.Beans
import com.tony.exception.ApiException
import com.tony.utils.getLogger
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
import com.tony.wechat.config.WechatProperties
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.validation.annotation.Validated
import java.util.Base64

@Suppress("unused")
public object WechatManager {

    private val logger = getLogger()
    private val wechatClient: WechatClient by Beans.getBeanByLazy()
    private val wechatPropProvider: WechatPropProvider by Beans.getBeanByLazy()
    private val apiAccessTokenProvider: WechatApiAccessTokenProvider by Beans.getBeanByLazy()

    @JvmOverloads
    @JvmStatic
    public fun checkSignature(signature: String, nonce: String, timestamp: String, app: String = ""): Boolean =
        DigestUtils
            .sha1Hex(
                listOf(wechatPropProvider.getToken(app), timestamp, nonce)
                    .sorted()
                    .joinToString(""),
            ) == signature

    @JvmStatic
    public fun checkSignatureDirect(signature: String, nonce: String, timestamp: String, token: String): Boolean =
        DigestUtils
            .sha1Hex(
                listOf(token, timestamp, nonce)
                    .sorted()
                    .joinToString(""),
            ) == signature

    @JvmOverloads
    @JvmStatic
    public fun jsCode2Session(jsCode: String, app: String = ""): WechatJsCode2SessionResp = wechatClient.jsCode2Session(
        wechatPropProvider.getAppId(app),
        wechatPropProvider.getAppSecret(app),
        "authorization_code",
        jsCode,
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
    public fun createMenu(menu: WechatMenu, app: String = "", accessToken: String? = accessTokenStr(app)): WechatResp =
        wechatClient.createMenu(accessToken, menu).check()

    @JvmOverloads
    @JvmStatic
    public fun deleteMenu(app: String = "", accessToken: String? = accessTokenStr(app)): WechatResp =
        wechatClient.deleteMenu(accessToken).check()

    @JvmOverloads
    @JvmStatic
    public fun userInfo(
        openId: String?,
        app: String = "",
        accessToken: String? = accessTokenStr(app),
    ): WechatUserInfoResp = wechatClient.userInfo(accessToken, openId).check()

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
                "url" to url,
            ).joinToString("&") {
                "${it.first}=${it.second}"
            },
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
            signature = getJsApiSignature(nonceStr, timestamp, url, app),
        )
    }

    @JvmStatic
    public fun showQrCode(ticket: String): String = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    @JvmStatic
    public fun userAccessToken(code: String?, app: String = ""): WechatUserTokenResp =
        apiAccessTokenProvider.userAccessToken(
            wechatPropProvider.getAppId(app),
            wechatPropProvider.getAppSecret(app),
            code,
        )

    private fun accessTokenStr(app: String = "") = apiAccessTokenProvider.accessTokenStr(
        wechatPropProvider.getAppId(app),
        wechatPropProvider.getAppSecret(app),
    )

    private fun wechatRedirect(url: String, app: String = ""): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid= ${wechatPropProvider.getAppId(app)}" +
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
            Base64.getEncoder().encode(it.readAllBytes()).toString(Charsets.UTF_8)
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

public interface WechatPropProvider {

    public fun getToken(app: String? = ""): String

    public fun getAppId(app: String? = ""): String?

    public fun getAppSecret(app: String? = ""): String?

    public fun getMchId(app: String? = ""): String?

    public fun getMchSecretKey(app: String? = ""): String?
}

internal class DefaultWechatPropProvider(
    private val wechatProperties: WechatProperties,
) : WechatPropProvider {

    override fun getToken(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.token
    } else {
        wechatProperties.app?.get(app)?.token
    } ?: throw ApiException("$app app-id not found")

    override fun getAppId(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.appId
    } else {
        wechatProperties.app?.get(app)?.appId
    } ?: throw ApiException("$app app-id not found")

    override fun getAppSecret(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.appSecret
    } else {
        wechatProperties.app?.get(app)?.appSecret
    } ?: throw ApiException("$app app-secret not found")

    override fun getMchId(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.mchId
    } else {
        wechatProperties.app?.get(app)?.mchId
    } ?: throw ApiException("$app mchId not found")

    override fun getMchSecretKey(app: String?) = if (app.isNullOrBlank()) {
        wechatProperties.mchSecretKey
    } else {
        wechatProperties.app?.get(app)?.mchSecretKey
    } ?: throw ApiException("$app mch-secret-key not found")
}

public interface WechatApiAccessTokenProvider {

    public fun accessTokenStr(appId: String?, appSecret: String?): String? = wechatClient().accessToken(
        appId,
        appSecret,
    ).check().accessToken

    public fun userAccessToken(appId: String?, secret: String?, code: String?): WechatUserTokenResp =
        wechatClient().userAccessToken(
            appId,
            secret,
            code,
        ).check()

    public fun wechatClient(): WechatClient = Beans.getBean(WechatClient::class.java)
}

internal class DefaultWechatApiAccessTokenProvider : WechatApiAccessTokenProvider {

    private val wechatClient: WechatClient by Beans.getBeanByLazy()

    override fun wechatClient(): WechatClient {
        return wechatClient
    }
}
