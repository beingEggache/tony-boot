@file:Suppress("ArrayInDataClass", "unused")

package com.tony.wechat

import com.fasterxml.jackson.annotation.JsonValue
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.XStreamCDataConverter

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

data class WechatMenu(var button: List<WechatButton>)

open class WechatButton(
    open var name: String
)

open class WechatTypedButton(
    open var type: WechatButtonType? = null,
    override var name: String
) : WechatButton(
    name
)

class WechatMenuButton(
    override var name: String,
    var sub_button: List<WechatTypedButton>
) : WechatButton(name)

class WechatViewButton(
    override var name: String,
    var url: String,
    override var type: WechatButtonType? = WechatButtonType.VIEW
) : WechatTypedButton(
    type,
    name
)

class WechatClickButton(
    override var name: String,
    var key: String,
    override var type: WechatButtonType? = WechatButtonType.CLICK
) : WechatTypedButton(type, name)

class WechatScanCodeButton(
    override var name: String,
    var key: String,
    override var type: WechatButtonType? = WechatButtonType.SCANCODE_PUSH
) : WechatTypedButton(
    type,
    name
)

enum class WechatButtonType {

    CLICK,
    LOCATION_SELECT,
    MEDIA_ID,
    PIC_PHOTO_OR_ALBUM,
    PIC_SYSPHOTO,
    PIC_WEIXIN,
    SCANCODE_PUSH,
    SCANCODE_WAITMSG,
    VIEW,
    VIEW_LIMITED;

    @JsonValue
    override fun toString() = super.toString().lowercase()
}
