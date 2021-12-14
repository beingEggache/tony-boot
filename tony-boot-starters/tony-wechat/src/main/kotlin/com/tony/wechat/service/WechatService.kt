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
import com.tony.wechat.sourceMiniProgram
import com.tony.wechat.sourceSubscription
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

    fun jsCode2Session(jsCode: String, source: String = sourceMiniProgram) = wechatClient.jsCode2Session(
        wechatPropProvider.appId(source),
        wechatPropProvider.appSecret(source),
        "authorization_code",
        jsCode
    ).check()

    @JvmOverloads
    fun createQrCode(
        @Validated
        req: WechatQrCodeCreateReq,
        source: String = sourceSubscription,
        accessToken: String? = accessToken(source).accessToken
    ) = wechatClient.createQrCode(req, accessToken).check()

    fun accessToken(source: String = sourceSubscription) = apiAccessTokenProviderWrapper.accessToken(
        wechatPropProvider.appId(source),
        wechatPropProvider.appSecret(source)
    )

    fun userAccessToken(
        code: String?,
        source: String = sourceSubscription,
    ) = apiAccessTokenProviderWrapper.userAccessToken(
        wechatPropProvider.appId(source),
        wechatPropProvider.appSecret(source),
        code
    )

    @JvmOverloads
    fun createMenu(
        menu: WechatMenu,
        source: String = sourceSubscription,
        accessToken: String? = accessToken(source).accessToken
    ) = wechatClient.createMenu(accessToken, menu).check()

    @JvmOverloads
    fun deleteMenu(
        source: String = sourceSubscription,
        accessToken: String? = accessToken(source).accessToken
    ) = wechatClient.deleteMenu(accessToken).check()

    @JvmOverloads
    fun userInfo(
        openId: String?,
        source: String = sourceSubscription,
        accessToken: String? = accessToken(source).accessToken
    ) = wechatClient.userInfo(accessToken, openId).check()

    @JvmOverloads
    fun getTicket(
        source: String = sourceMiniProgram,
        accessToken: String? = accessToken(source).accessToken
    ) = wechatClient.getTicket(accessToken, "jsapi").check().ticket

    fun getJsApiSignature(nonceStr: String, timestamp: Long, url: String): String =
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

    @JvmOverloads
    fun jsSdkConfig(
        url: String,
        source: String = sourceSubscription,
        debug: Boolean = false
    ) = run {
        val timestamp = genTimeStamp()
        val nonceStr = genNonceStr()
        WechatJsSdkConfigResp(
            debug = debug,
            appId = source,
            timestamp = timestamp,
            nonceStr = nonceStr,
            signature = getJsApiSignature(nonceStr, timestamp, url)
        )
    }

    fun showQrCode(ticket: String) = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    private fun wechatRedirect(url: String, source: String = sourceSubscription): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=$source" +
            "&redirect_uri=${url.urlEncode()}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=#wechat_redirect"
}

interface WechatPropProvider {

    fun appId(source: String): String?

    fun appSecret(source: String): String?

    fun mchId(source: String): String?

    fun mchSecretKey(source: String): String?
}

internal class DefaultWechatPropProvider(
    private val wechatProperties: WechatProperties
) : WechatPropProvider {
    override fun appId(source: String) =
        wechatProperties.app[source]?.appId ?: wechatProperties.appId
            ?: throw ApiException("$source app-id not found")

    override fun appSecret(source: String) =
        wechatProperties.app[source]?.appSecret ?: wechatProperties.appSecret
            ?: throw ApiException("$source app-secret not found")

    override fun mchId(source: String) =
        wechatProperties.app[source]?.mchId ?: wechatProperties.mchId
            ?: throw ApiException("$source mch-id not found")

    override fun mchSecretKey(source: String) =
        wechatProperties.app[source]?.mchSecretKey ?: wechatProperties.mchSecretKey
            ?: throw ApiException("$source mch-secret-key not found")
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
