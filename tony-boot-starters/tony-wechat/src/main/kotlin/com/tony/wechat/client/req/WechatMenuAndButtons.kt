/**
 * tony-boot-starters
 * WechatMenuAndButtons
 *
 * @author tangli
 * @since 2021/9/26 12:49
 */
@file:Suppress("unused")

package com.tony.wechat.client.req

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.enums.EnumCreator
import com.tony.enums.EnumStringValue

public data class WechatMenu(val button: List<WechatButton>)

public sealed class WechatButton(
    public open val name: String
)

public sealed class WechatTypedButton(
    override val name: String
) : WechatButton(
    name
) {
    @get:JsonProperty("type")
    protected abstract val type: WechatButtonType
}

public data class WechatMenuButton(
    override val name: String,
    @JsonProperty("sub_button")
    val subButton: List<WechatTypedButton>
) : WechatButton(name)

public class WechatViewButton(
    override val name: String,
    public val url: String
) : WechatTypedButton(
    name
) {
    override val type: WechatButtonType = WechatButtonType.VIEW
}

public class WechatClickButton(
    override val name: String,
    public val key: String
) : WechatTypedButton(name) {
    override val type: WechatButtonType = WechatButtonType.CLICK
}

public class WechatScanCodeButton(
    override val name: String,
    public val key: String
) : WechatTypedButton(
    name
) {
    override val type: WechatButtonType = WechatButtonType.SCANCODE_PUSH
}

public enum class WechatButtonType(
    override val value: String
) : EnumStringValue {

    CLICK("click"),
    VIEW("view"),
    SCANCODE_PUSH("scancode_push"),
    SCANCODE_WAITMSG("scancode_waitmsg"),
    PIC_SYSPHOTO("pic_sysphoto"),
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
    PIC_WEIXIN("pic_weixin"),
    LOCATION_SELECT("location_select"),
    MEDIA_ID("media_id"),
    VIEW_LIMITED("view_limited");

    public companion object : EnumCreator<WechatButtonType, String>(WechatButtonType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): WechatButtonType? =
            super.create(value)
    }
}
