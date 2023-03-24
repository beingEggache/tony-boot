@file:Suppress("unused")

package com.tony.wechat.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter

@XStreamAlias("item")
public data class WechatArticleItem(
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
    var url: String,
)

public data class ScanCodeInfo(
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ScanType")
    var scanType: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ScanResult")
    var scanResult: String,
)

public interface WechatObj {
    public var event: String?
    public var msgType: String
}

public enum class Event {

    CLICK, LOCATION, SCAN, SUBSCRIBE, UNSUBSCRIBE, VIEW;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}

public enum class MsgType {

    EVENT, IMAGE, LINK, LOCATION, MUSIC, NEWS, SHORTVIDEO, TEXT, VIDEO, VOICE;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}
