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

package com.tony.wechat.client.req

/**
 * tony-boot-starters
 * WechatMenuAndButtons
 *
 * @author Tang Li
 * @date 2021/9/26 12:49
 */
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import com.tony.utils.asTo

public data class WechatMenu(
    val button: List<WechatButton>,
)

public sealed class WechatButton(
    public open val name: String,
)

public sealed class WechatTypedButton(
    override val name: String,
) : WechatButton(
        name
    ) {
    @get:JsonProperty("type")
    protected abstract val type: WechatButtonType
}

public data class WechatMenuButton(
    override val name: String,
    @JsonProperty("sub_button")
    val subButton: List<WechatTypedButton>,
) : WechatButton(name)

public class WechatViewButton(
    override val name: String,
    public val url: String,
) : WechatTypedButton(
        name
    ) {
    override val type: WechatButtonType = WechatButtonType.VIEW
}

public class WechatClickButton(
    override val name: String,
    public val key: String,
) : WechatTypedButton(name) {
    override val type: WechatButtonType = WechatButtonType.CLICK
}

public class WechatScanCodeButton(
    override val name: String,
    public val key: String,
) : WechatTypedButton(
        name
    ) {
    override val type: WechatButtonType = WechatButtonType.SCANCODE_PUSH
}

public enum class WechatButtonType(
    override val value: String,
) : StringEnumValue {
    CLICK("click"),
    VIEW("view"),
    SCANCODE_PUSH("scancode_push"),
    SCANCODE_WAITMSG("scancode_waitmsg"),
    PIC_SYSPHOTO("pic_sysphoto"),
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
    PIC_WEIXIN("pic_weixin"),
    LOCATION_SELECT("location_select"),
    MEDIA_ID("media_id"),
    VIEW_LIMITED("view_limited"),
    ;

    public companion object : StringEnumCreator(WechatButtonType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): WechatButtonType? =
            super.create(value).asTo()
    }
}
