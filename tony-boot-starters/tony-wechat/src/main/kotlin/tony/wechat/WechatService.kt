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

import tony.SpringContexts
import tony.exception.ApiException
import tony.wechat.client.WechatClient
import tony.wechat.client.req.WechatStableAccessTokenReq
import tony.wechat.client.resp.WechatUserTokenResp
import tony.wechat.config.WechatProperties

public interface WechatPropProvider {
    public fun token(app: String? = ""): String

    public fun appId(app: String? = ""): String?

    public fun appSecret(app: String? = ""): String?

    public fun mchId(app: String? = ""): String?

    public fun mchSecretKey(app: String? = ""): String?
}

internal class DefaultWechatPropProvider(
    private val wechatProperties: WechatProperties,
) : WechatPropProvider {
    override fun token(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.token
        } else {
            wechatProperties
                .app
                ?.get(app)
                ?.token
        } ?: throw ApiException("$app app-id not found")

    override fun appId(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.appId
        } else {
            wechatProperties
                .app
                ?.get(app)
                ?.appId
        } ?: throw ApiException("$app app-id not found")

    override fun appSecret(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.appSecret
        } else {
            wechatProperties
                .app
                ?.get(app)
                ?.appSecret
        } ?: throw ApiException("$app app-secret not found")

    override fun mchId(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.mchId
        } else {
            wechatProperties
                .app
                ?.get(app)
                ?.mchId
        } ?: throw ApiException("$app mchId not found")

    override fun mchSecretKey(app: String?) =
        if (app.isNullOrBlank()) {
            wechatProperties.mchSecretKey
        } else {
            wechatProperties
                .app
                ?.get(app)
                ?.mchSecretKey
        } ?: throw ApiException("$app mch-secret-key not found")
}

public interface WechatApiAccessTokenProvider {
    public fun accessTokenStr(
        appId: String?,
        appSecret: String?,
        forceRefresh: Boolean,
    ): String? =
        wechatClient
            .stableAccessToken(
                WechatStableAccessTokenReq(
                    appId,
                    appSecret,
                    forceRefresh
                )
            ).check()
            .accessToken

    public fun userAccessToken(
        appId: String?,
        secret: String?,
        code: String?,
    ): WechatUserTokenResp =
        wechatClient
            .userAccessToken(
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
