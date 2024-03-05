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

package com.tony.wechat.client.resp

/**
 * tony-boot-starters
 * WechatResp
 *
 * @author tangli
 * @date 2021/09/26 19:23
 */
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.utils.toLocalDateTime
import java.time.LocalDateTime
import java.util.Date

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public open class WechatResp {
    @JsonProperty("errcode")
    public var errCode: String? = null

    @JsonProperty("errmsg")
    public var errMsg: String? = null

    public val success: Boolean
        get() = errCode == null || errCode == "0"
}

public data class WechatApiTokenResp(
    @JsonProperty("access_token") val accessToken: String?,
    @JsonProperty("expires_in") val expiresIn: Int?,
) : WechatResp() {
    val expiresAt: LocalDateTime?
        get() = expiresIn?.let { LocalDateTime.now().plusSeconds(expiresIn.toLong()) }
}

public data class WechatUserTokenResp(
    @JsonProperty("access_token") val accessToken: String?,
    @JsonProperty("expires_in") val expiresIn: Int?,
    @JsonProperty("refresh_token") val refreshToken: String?,
    @JsonProperty("openid") val openId: String?,
    @JsonProperty("scope") val scope: String?,
) : WechatResp() {
    val expiresAt: LocalDateTime?
        get() = expiresIn?.let { LocalDateTime.now().plusSeconds(expiresIn.toLong()) }
}

public data class WechatJsApiTicketResp(
    @JsonProperty("ticket") val ticket: String?,
    @JsonProperty("expires_in") val expiresIn: Int?,
) : WechatResp() {
    val expiresAt: LocalDateTime?
        get() = expiresIn?.let { LocalDateTime.now().plusSeconds(expiresIn.toLong()) }
}

public data class WechatQrCodeResp(
    val ticket: String?,
    @JsonProperty("expire_seconds") val expireSeconds: Int?,
    val url: String?,
) : WechatResp()

public data class WechatUserInfoResp(
    @JsonProperty("subscribe") val subscribe: Int?,
    @JsonProperty("openid") val openId: String?,
    @JsonProperty("nickname") val nickname: String?,
    @JsonProperty("sex") val sex: Int?,
    @JsonProperty("language") val language: String?,
    @JsonProperty("city") val city: String?,
    @JsonProperty("province") val province: String?,
    @JsonProperty("country") val country: String?,
    @JsonProperty("headimgurl") val headImgUrl: String?,
    @JsonProperty("subscribe_time") private val subscribeTimeValue: Long?,
    @JsonProperty("unionid") val unionId: String?,
    @JsonProperty("remark") val remark: String?,
    @JsonProperty("groupid") val groupId: Int?,
    @JsonProperty("tagid_list") val tagIdList: List<Int>?,
    @JsonProperty("subscribe_scene") val subscribeScene: String?,
    @JsonProperty("qr_scene") val qrScene: Int?,
    @JsonProperty("qr_scene_str") val qrSceneStr: String?,
) : WechatResp() {
    @get:JsonProperty("subscribe_time")
    val subscribeTime: LocalDateTime = Date((subscribeTimeValue ?: 0) * 1000).toLocalDateTime()
}

public data class WechatJsCode2SessionResp(
    @JsonProperty("openid") val openId: String?,
    @JsonProperty("session_key") val sessionKey: String?,
    @JsonProperty("unionid") val unionId: String?,
) : WechatResp()

public data class WechatJsSdkConfigResp(
    val debug: Boolean,
    val appId: String?,
    val timestamp: Long,
    val nonceStr: String,
    val signature: String,
)

public data class WechatMiniProgramUserPhoneResp(
    @JsonProperty("phone_info") val phoneInfo: WechatMiniProgramUserPhoneInfoResp?,
) : WechatResp()

public data class WechatMiniProgramUserPhoneInfoResp(
    val phoneNumber: String,
    val purePhoneNumber: String,
    val countryCode: String,
)
