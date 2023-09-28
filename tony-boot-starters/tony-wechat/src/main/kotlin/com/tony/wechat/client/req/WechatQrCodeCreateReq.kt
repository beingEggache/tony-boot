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

/**
 * tony-boot-starters
 * WechatQrCodeCreateReq
 *
 * @author Tang Li
 * @date 2021/9/26 13:22
 */
package com.tony.wechat.client.req

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.enums.EnumCreator
import com.tony.enums.StringEnumValue
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Positive

public data class WechatQrCodeCreateReq(

    @Max(2592000)
    @Positive
    @JsonProperty("expire_seconds")
    val expireSeconds: Int?,

    @JsonProperty("action_name")
    val actionName: WechatQrCodeType?,

    @JsonProperty("action_info")
    val actionInfo: WechatQrCodeActionInfo?,
)

public data class WechatQrCodeActionInfo(
    val scene: Scene,
) {
    public constructor(sceneId: Int?, sceneStr: String?) : this(Scene(sceneId, sceneStr))
    public constructor(sceneStr: String?) : this(Scene(null, sceneStr))
    public constructor(sceneId: Int?) : this(Scene(sceneId, null))

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public data class Scene(
        @JsonProperty("scene_id", namespace = "scene")
        val sceneId: Int?,

        @JsonProperty("scene_str", namespace = "scene")
        val sceneStr: String?,
    )
}

public enum class WechatQrCodeType(
    override val value: String,
) : StringEnumValue {

    @JsonEnumDefaultValue
    QR_SCENE("QR_SCENE"),
    QR_STR_SCENE("QR_STR_SCENE"),
    QR_LIMIT_SCENE("QR_LIMIT_SCENE"),
    QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE"),
    ;

    public companion object : EnumCreator<WechatQrCodeType, String>(WechatQrCodeType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): WechatQrCodeType? = super.create(value)
    }
}
