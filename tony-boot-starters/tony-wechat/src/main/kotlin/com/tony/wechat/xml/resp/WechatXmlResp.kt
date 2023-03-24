@file:Suppress("ArrayInDataClass", "unused")

package com.tony.wechat.xml.resp

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.WechatArticleItem
import com.tony.wechat.xml.WechatObj
import com.tony.wechat.xml.XStreamCDataConverter

@XStreamAlias("xml")
public data class WechatXmlResp(
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
    var articles: List<WechatArticleItem>? = null,

) : WechatObj
