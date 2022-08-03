@file:Suppress("unused")

package com.tony.wechat.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter

@XStreamAlias("item")
data class WechatArticleItem(
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Title")
    var title: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Description")
    var description: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("PicUrl")
    var picUrl: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Url")
    var url: String
)

data class ScanCodeInfo(
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ScanType")
    var scanType: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ScanResult")
    var scanResult: String
)

interface WechatObj {
    var event: String?
    var msgType: String
}

enum class Event {

    CLICK, LOCATION, SCAN, SUBSCRIBE, UNSUBSCRIBE, VIEW;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}

enum class MsgType {

    EVENT, IMAGE, LINK, LOCATION, MUSIC, NEWS, SHORTVIDEO, TEXT, VIDEO, VOICE;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}
