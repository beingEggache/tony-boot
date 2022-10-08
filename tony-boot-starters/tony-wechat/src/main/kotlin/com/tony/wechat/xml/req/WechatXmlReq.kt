package com.tony.wechat.xml.req

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.ScanCodeInfo
import com.tony.wechat.xml.WechatObj
import com.tony.wechat.xml.XStreamCDataConverter

@Suppress("unused")
@XStreamAlias("xml")
public data class WechatXmlReq(
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ToUserName")
    var toUserName: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("FromUserName")
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
    @XStreamAlias("EventKey")
    var eventKey: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Ticket")
    var ticket: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MediaId")
    var mediaId: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("PicUrl")
    var picUrl: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Format")
    var format: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Recognition")
    var recognition: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ThumbMediaId")
    var thumbMediaId: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Location_X")
    var locationX: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Location_Y")
    var locationY: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Scale")
    var scale: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Label")
    var label: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Title")
    var title: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Description")
    var description: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Url")
    var url: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Latitude")
    var latitude: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Longitude")
    var longitude: String? = null,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Precision")
    var precision: String? = null,

    @XStreamAlias("MenuId")
    var menuId: String? = null,

    @XStreamAlias("ScanCodeInfo")
    var scanCodeInfo: ScanCodeInfo? = null
) : WechatObj
