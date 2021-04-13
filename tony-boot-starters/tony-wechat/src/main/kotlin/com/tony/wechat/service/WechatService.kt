package com.tony.wechat.service

import com.tony.core.exception.ApiException
import com.tony.core.utils.getLogger
import com.tony.core.utils.jsonToObj
import com.tony.core.utils.toJsonString
import com.tony.core.utils.urlEncode
import com.tony.http.httpGet
import com.tony.http.httpPost
import com.tony.wechat.WechatButton
import com.tony.wechat.WechatMenu
import com.tony.wechat.genNonceStr
import com.tony.wechat.genTimeStamp
import com.tony.wechat.response.WechatApiTokenResponse
import com.tony.wechat.response.WechatJs2CodeResponse
import com.tony.wechat.response.WechatJsApiTicketResponse
import com.tony.wechat.response.WechatJsSdkConfigResponse
import com.tony.wechat.response.WechatQrCodeResponse
import com.tony.wechat.response.WechatServerResponse
import com.tony.wechat.response.WechatSnsTokenResponse
import com.tony.wechat.response.WechatUserInfoResponse
import org.apache.commons.codec.digest.DigestUtils
import java.time.LocalTime
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused")
class WechatService(
    private val miniProgramAppId: String,
    private val subscriptionAppId: String,
    private val appSecret: String,
    private val miniProgramAppSecret: String
) {

    private val apiAccessTokenMap = ConcurrentHashMap<LocalTime, String>(1, 1F)

    private val apiPath = "https://api.weixin.qq.com/cgi-bin"

    private val jsAPITicketMap = HashMap<LocalTime, String>(1, 1F)

    private val jsCode2SessionPath = "https://api.weixin.qq.com/sns/jscode2session"

    private val logger = getLogger()

    private val snsApiPath = "https://api.weixin.qq.com/sns/oauth2"

    fun checkSignature(token: String, signature: String, nonce: String, timestamp: String) =
        DigestUtils
            .sha1Hex(
                listOf(token, timestamp, nonce)
                    .sorted()
                    .joinToString("")
            ) == signature

    fun code2Session(code: String) =
        jsCode2SessionPath.httpGet(
            mapOf(
                "appid" to miniProgramAppId,
                "secret" to miniProgramAppSecret,
                "js_code" to code,
                "grant_type" to "authorization_code"
            )
        ).json<WechatJs2CodeResponse>()

    fun createLimitQrCode(sceneStr: String) =
        "$apiPath/qrcode/create?access_token=${getApiAccessToken()}"
            .httpPost(
                """
              {"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "$sceneStr"}}}
                """.trimIndent()
            )
            .json<WechatQrCodeResponse>()

    fun createQrCode(sceneStr: String, expireSeconds: Int = 2592000) =
        "$apiPath/qrcode/create?access_token=${getApiAccessToken()}"
            .httpPost(
                """
                {
                "expire_seconds": $expireSeconds,
                "action_name": "QR_STR_SCENE",
                "action_info": {"scene": {"scene_str": "$sceneStr"}}
                }
                """.trimIndent()
            )
            .json<WechatQrCodeResponse>()

    fun deleteMenu(): Boolean {
        val responseObj =
            "$apiPath/menu/delete?access_token=${getApiAccessToken()}"
                .httpGet()
                .json<WechatServerResponse>()

        if (responseObj.errCode == 0) return true
        throw ApiException(responseObj.toJsonString())
    }

    private fun getApiAccessToken(appId: String = this.subscriptionAppId, secret: String = this.appSecret): String {

        val resultStr =
            "$apiPath/token"
                .httpGet(
                    mapOf(
                        "grant_type" to "client_credential",
                        "appid" to appId,
                        "secret" to secret
                    )
                )
                .string()

        val (accessToken, expiresIn) =
            resultStr
                .jsonToObj<WechatApiTokenResponse>()

        val now = LocalTime.now()
        if (apiAccessTokenMap.isEmpty()) {
            apiAccessTokenMap[now] = accessToken
        } else {
            val first = apiAccessTokenMap.keys.first()
            if (first.plusSeconds((expiresIn - 2).toLong()) < now) {
                apiAccessTokenMap.clear()
                apiAccessTokenMap[now] = accessToken
            }
        }

        if (expiresIn == -1) {
            val (_, errmsg) = resultStr.jsonToObj<WechatServerResponse>()
            logger.error("getAPIAccessTokenError: $errmsg")
            throw ApiException(errmsg)
        }
        return accessToken
    }

    private fun getJsApiSignature(nonceStr: String, timestamp: Long, url: String) =
        DigestUtils.sha1Hex(
            listOf(
                "jsapi_ticket" to getJsApiTicket(),
                "noncestr" to nonceStr,
                "timestamp" to timestamp,
                "url" to url
            ).joinToString("&") {
                "${it.first}=${it.second}"
            }
        )

    private fun getJsApiTicket() =
        synchronized(jsAPITicketMap) {
            val (_, _, ticket, expiresIn) =
                "$apiPath/ticket/getticket".httpGet(
                    mapOf(
                        "access_token" to getApiAccessToken(),
                        "type" to "jsapi"
                    )
                ).json<WechatJsApiTicketResponse>()

            val now = LocalTime.now()
            if (jsAPITicketMap.isEmpty()) {
                jsAPITicketMap[now] = ticket
            } else {
                val first = jsAPITicketMap.keys.first()
                if (first.plusSeconds((expiresIn - 2).toLong()) < now) {
                    jsAPITicketMap.clear()
                    jsAPITicketMap[now] = ticket
                }
            }
            ticket
        }

    fun getUserAccessTokenObj(code: String): WechatSnsTokenResponse {

        val resultStr = "$snsApiPath/access_token".httpGet(
            mapOf(
                "appid" to subscriptionAppId,
                "secret" to appSecret,
                "code" to code,
                "grant_type" to "authorization_code"
            )
        ).string()
        val response = resultStr.jsonToObj<WechatSnsTokenResponse>()

        if (response.expiresIn == -1) {
            val (_, errmsg) = resultStr.jsonToObj<WechatServerResponse>()
            throw ApiException(errmsg)
        }
        return response
    }

    fun getUserInfo(openId: String) =
        "$apiPath/user/info?access_token=${getApiAccessToken()}&openid=$openId&lang=zh_CN"
            .httpGet()
            .json<WechatUserInfoResponse>()

    fun initMenu(buttons: List<WechatButton>) =
        "$apiPath/menu/create?access_token=${getApiAccessToken(subscriptionAppId, appSecret)}".httpPost(
            WechatMenu(buttons).toJsonString()
        ).json<WechatServerResponse>().success()

    fun initMenu(
        menu: WechatMenu,
        appId: String = subscriptionAppId,
        secret: String = appSecret
    ) = "$apiPath/menu/create?access_token=${getApiAccessToken(appId, secret)}"
        .httpPost(menu.toJsonString())
        .json<WechatServerResponse>()
        .success()

    fun jsSdkConfig(url: String, debug: Boolean = false) =
        run {
            val timestamp = genTimeStamp()
            val nonceStr = genNonceStr()
            WechatJsSdkConfigResponse(
                debug = debug,
                appId = subscriptionAppId,
                timestamp = timestamp,
                nonceStr = nonceStr,
                signature = getJsApiSignature(nonceStr, timestamp, url)
            )
        }

    fun showQrCode(ticket: String) =
        "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=$ticket"

    private fun wechatRedirect(url: String): String =
        "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=$subscriptionAppId" +
            "&redirect_uri=${url.urlEncode()}" +
            "&response_type=code" +
            "&scope=snsapi_base" +
            "&state=#wechat_redirect"
}
