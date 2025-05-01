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

package tony.wechat.client.resp

/**
 * tony-boot-starters
 * WechatResp
 *
 * @author tangli
 * @date 2021/09/26 19:23
 */
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.Date
import tony.utils.toLocalDateTime

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
    @param:JsonProperty("access_token") val accessToken: String?,
    @param:JsonProperty("expires_in") val expiresIn: Int?,
) : WechatResp() {
    val expiresAt: LocalDateTime?
        get() = expiresIn?.let { LocalDateTime.now().plusSeconds(expiresIn.toLong()) }
}

public data class WechatUserTokenResp(
    @param:JsonProperty("access_token") val accessToken: String?,
    @param:JsonProperty("expires_in") val expiresIn: Int?,
    @param:JsonProperty("refresh_token") val refreshToken: String?,
    @param:JsonProperty("openid") val openId: String?,
    @param:JsonProperty("scope") val scope: String?,
) : WechatResp() {
    val expiresAt: LocalDateTime?
        get() = expiresIn?.let { LocalDateTime.now().plusSeconds(expiresIn.toLong()) }
}

public data class WechatJsApiTicketResp(
    @param:JsonProperty("ticket") val ticket: String?,
    @param:JsonProperty("expires_in") val expiresIn: Int?,
) : WechatResp() {
    val expiresAt: LocalDateTime?
        get() = expiresIn?.let { LocalDateTime.now().plusSeconds(expiresIn.toLong()) }
}

public data class WechatQrCodeResp(
    val ticket: String?,
    @param:JsonProperty("expire_seconds") val expireSeconds: Int?,
    val url: String?,
) : WechatResp()

public data class WechatUserInfoResp(
    @param:JsonProperty("subscribe") val subscribe: Int?,
    @param:JsonProperty("openid") val openId: String?,
    @param:JsonProperty("nickname") val nickname: String?,
    @param:JsonProperty("sex") val sex: Int?,
    @param:JsonProperty("language") val language: String?,
    @param:JsonProperty("city") val city: String?,
    @param:JsonProperty("province") val province: String?,
    @param:JsonProperty("country") val country: String?,
    @param:JsonProperty("headimgurl") val headImgUrl: String?,
    @param:JsonProperty("subscribe_time") private val subscribeTimeValue: Long?,
    @param:JsonProperty("unionid") val unionId: String?,
    @param:JsonProperty("remark") val remark: String?,
    @param:JsonProperty("groupid") val groupId: Int?,
    @param:JsonProperty("tagid_list") val tagIdList: List<Int>?,
    @param:JsonProperty("subscribe_scene") val subscribeScene: String?,
    @param:JsonProperty("qr_scene") val qrScene: Int?,
    @param:JsonProperty("qr_scene_str") val qrSceneStr: String?,
) : WechatResp() {
    @get:JsonProperty("subscribe_time")
    val subscribeTime: LocalDateTime = Date((subscribeTimeValue ?: 0) * 1000).toLocalDateTime()
}

public data class WechatJsCode2SessionResp(
    @param:JsonProperty("openid") val openId: String?,
    @param:JsonProperty("session_key") val sessionKey: String?,
    @param:JsonProperty("unionid") val unionId: String?,
) : WechatResp()

public data class WechatJsSdkConfigResp(
    val debug: Boolean,
    val appId: String?,
    val timestamp: Long,
    val nonceStr: String,
    val signature: String,
)

public data class WechatMiniProgramUserPhoneResp(
    @param:JsonProperty("phone_info") val phoneInfo: WechatMiniProgramUserPhoneInfoResp?,
) : WechatResp()

public data class WechatMiniProgramUserPhoneInfoResp(
    val phoneNumber: String,
    val purePhoneNumber: String,
    val countryCode: String,
)
