/**
 * tony-boot-starters
 * WechatResp
 *
 * TODO
 *
 * @author tangli
 * @since 2021/9/26 13:23
 */
package com.tony.wechat.client.resp

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.utils.toLocalDateTime
import java.time.LocalDateTime
import java.util.Date

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
open class WechatResp {
    @JsonProperty("errcode")
    var errCode: String? = null

    @JsonProperty("errmsg")
    var errMsg: String? = null

    fun success() = errCode == null || errCode == "0"
}

data class WechatApiTokenResp(
    @JsonProperty("access_token") val accessToken: String?,
    @JsonProperty("expires_in") val expiresIn: Int?
) : WechatResp()

data class WechatUserTokenResp(
    @JsonProperty("access_token") val accessToken: String?,
    @JsonProperty("expires_in") val expiresIn: Int?,
    @JsonProperty("refresh_token") val refreshToken: String?,
    @JsonProperty("openid") val openId: String?,
    @JsonProperty("scope") val scope: String?
) : WechatResp()

data class WechatJsApiTicketResp(
    @JsonProperty("ticket") val ticket: String?,
    @JsonProperty("expires_in") val expiresIn: Int?
) : WechatResp()

data class WechatQrCodeResp(
    val ticket: String?,
    @JsonProperty("expire_seconds") val expireSeconds: Int?,
    val url: String?
) : WechatResp()

data class WechatUserInfoResp(
    @JsonProperty("subscribe") var subscribe: Int?,
    @JsonProperty("openid") var openId: String?,
    @JsonProperty("nickname") var nickname: String?,
    @JsonProperty("sex") var sex: Int?,
    @JsonProperty("language") var language: String?,
    @JsonProperty("city") var city: String?,
    @JsonProperty("province") var province: String?,
    @JsonProperty("country") var country: String?,
    @JsonProperty("headimgurl") var headImgUrl: String?,
    @JsonProperty("subscribe_time") private var subscribe_time: Long?,
    @JsonProperty("unionid") var unionId: String?,
    @JsonProperty("remark") var remark: String?,
    @JsonProperty("groupid") var groupId: Int?,
    @JsonProperty("tagid_list") var tagIdList: List<Int>?,
    @JsonProperty("subscribe_scene") var subscribeScene: String?,
    @JsonProperty("qr_scene") var qrScene: Int?,
    @JsonProperty("qr_scene_str") var qrSceneStr: String?
) : WechatResp() {

    @Suppress("unused")
    @get:JsonProperty("subscribe_time")
    val subscribeTime: LocalDateTime = Date((subscribe_time ?: 0) * 1000).toLocalDateTime()
}

data class WechatJsCode2SessionResp(
    @JsonProperty("openid") val openId: String?,
    @JsonProperty("session_key") val sessionKey: String?,
    @JsonProperty("unionid") val unionId: String?
) : WechatResp()

data class WechatJsSdkConfigResp(
    val debug: Boolean,
    val appId: String?,
    val timestamp: Long,
    val nonceStr: String,
    val signature: String
)
