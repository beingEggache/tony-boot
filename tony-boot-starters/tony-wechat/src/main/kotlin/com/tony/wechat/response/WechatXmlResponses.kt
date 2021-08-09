@file:Suppress("ArrayInDataClass", "unused")

package com.tony.wechat.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.core.exception.ApiException
import com.tony.wechat.WechatArticleItem
import com.tony.wechat.WechatObj
import com.tony.wechat.xml.XStreamCDataConverter

@XStreamAlias("xml")
data class WechatXmlResponse(
    @XStreamAlias("ToUserName")
    @XStreamConverter(value = XStreamCDataConverter::class)
    var toUserName: String,

    @XStreamAlias("FromUserName")
    @XStreamConverter(value = XStreamCDataConverter::class)
    var fromUserName: String,

    @XStreamAlias("CreateTime")
    var createTime: Long,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MsgType")
    override var msgType: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Event")
    override var event: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Content")
    var content: String? = null,

    @XStreamAlias("MsgId")
    var msgId: Long? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MediaId")
    var mediaId: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("EventKey")
    var eventKey: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Ticket")
    var ticket: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Title")
    var title: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MusicURL")
    var musicURL: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("HQMusicUrl")
    private var hQMusicUrl: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ThumbMediaId")
    var thumbMediaId: String? = null,

    @XStreamAlias("ArticleCount")
    var articleCount: Int? = null,

    @XStreamAlias("Articles")
    var articles: Array<WechatArticleItem>? = null

) : WechatObj

data class WechatAPIResponse(
    var errcode: Int,
    var errmsg: String
)

data class WechatApiTokenResponse(
    @JsonProperty("access_token")
    var accessToken: String = "",
    @JsonProperty("expires_in")
    var expiresIn: Int = -1
)

data class WechatJsApiTicketResponse(
    @JsonProperty("errcode")
    var errCode: Int,
    @JsonProperty("errmsg")
    var errMsg: String,
    @JsonProperty("ticket")
    var ticket: String,
    @JsonProperty("expires_in")
    var expiresIn: Int
)

data class WechatSnsTokenResponse(
    @JsonProperty("access_token") var accessToken: String?,
    @JsonProperty("expires_in") var expiresIn: Int = -1,
    @JsonProperty("refresh_token") var refreshToken: String?,
    @JsonProperty("openid") var openId: String?,
    @JsonProperty("scope") var scope: String?
)

data class WechatServerResponse(
    @JsonProperty("errcode")
    var errCode: Int,
    @JsonProperty("errmsg")
    var errMsg: String
) {
    fun success(callback: (WechatServerResponse.() -> Unit)? = null): Boolean {
        if (errCode != 0) throw ApiException(errMsg)
        callback?.invoke(this)
        return true
    }
}

data class WechatQrCodeResponse(
    var ticket: String,
    @JsonProperty("expire_seconds")
    var expireSeconds: Int,
    var url: String
)
