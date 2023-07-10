package com.tony.wechat

import com.tony.SpringContexts
import com.tony.exception.ApiException
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.resp.WechatUserTokenResp
import com.tony.wechat.config.WechatProperties

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

    override fun getToken(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.token
        } else {
            wechatProperties.app?.get(app)?.token
        } ?: throw ApiException("$app app-id not found")

    override fun getAppId(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.appId
        } else {
            wechatProperties.app?.get(app)?.appId
        } ?: throw ApiException("$app app-id not found")

    override fun getAppSecret(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.appSecret
        } else {
            wechatProperties.app?.get(app)?.appSecret
        } ?: throw ApiException("$app app-secret not found")

    override fun getMchId(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.mchId
        } else {
            wechatProperties.app?.get(app)?.mchId
        } ?: throw ApiException("$app mchId not found")

    override fun getMchSecretKey(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.mchSecretKey
        } else {
            wechatProperties.app?.get(app)?.mchSecretKey
        } ?: throw ApiException("$app mch-secret-key not found")
}

public interface WechatApiAccessTokenProvider {

    public fun accessTokenStr(appId: String?, appSecret: String?): String? = wechatClient.accessToken(
        appId,
        appSecret
    ).check().accessToken

    public fun userAccessToken(appId: String?, secret: String?, code: String?): WechatUserTokenResp =
        wechatClient.userAccessToken(
            appId,
            secret,
            code
        ).check()

    public val wechatClient: WechatClient
        get() = SpringContexts.getBean(WechatClient::class.java)
}

internal class DefaultWechatApiAccessTokenProvider : WechatApiAccessTokenProvider {
    override val wechatClient: WechatClient by SpringContexts.getBeanByLazy()
}
