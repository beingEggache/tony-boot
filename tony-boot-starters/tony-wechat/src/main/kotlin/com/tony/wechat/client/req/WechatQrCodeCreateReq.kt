/**
 * tony-boot-starters
 * WechatQrCodeCreateReq
 *
 * @author tangli
 * @since 2021/9/26 13:22
 */
@file:Suppress("unused")

package com.tony.wechat.client.req

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.enums.EnumCreator
import com.tony.enums.EnumStringValue
import javax.validation.constraints.Max
import javax.validation.constraints.Positive

public data class WechatQrCodeCreateReq(

    @Max(2592000)
    @Positive
    @JsonProperty("expire_seconds")
    val expireSeconds: Int?,

    @JsonProperty("action_name")
    val actionName: WechatQrCodeType?,

    @JsonProperty("action_info")
    val actionInfo: WechatQrCodeActionInfo?
)

public data class WechatQrCodeActionInfo(
    val scene: Scene
) {
    public constructor(sceneId: Int?, sceneStr: String?) : this(Scene(sceneId, sceneStr))
    public constructor(sceneStr: String?) : this(Scene(null, sceneStr))
    public constructor(sceneId: Int?) : this(Scene(sceneId, null))

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public data class Scene(
        @JsonProperty("scene_id", namespace = "scene")
        val sceneId: Int?,

        @JsonProperty("scene_str", namespace = "scene")
        val sceneStr: String?
    )
}

public enum class WechatQrCodeType(
    override val value: String
) : EnumStringValue {

    @JsonEnumDefaultValue
    QR_SCENE("QR_SCENE"),
    QR_STR_SCENE("QR_STR_SCENE"),
    QR_LIMIT_SCENE("QR_LIMIT_SCENE"),
    QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE");

    public companion object : EnumCreator<WechatQrCodeType, String>(WechatQrCodeType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): WechatQrCodeType? =
            super.create(value)
    }
}
