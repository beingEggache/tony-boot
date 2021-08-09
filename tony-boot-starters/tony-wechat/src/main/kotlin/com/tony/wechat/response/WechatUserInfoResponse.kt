package com.tony.wechat.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class WechatUserInfoResponse(
    @JsonProperty("subscribe") var subscribe: Int,

    @JsonProperty("openid") var openId: String,

    @JsonProperty("nickname") var nickname: String?,

    @JsonProperty("sex") var sex: Int,

    @JsonProperty("language") var language: String?,

    @JsonProperty("city") var city: String?,

    @JsonProperty("province") var province: String?,

    @JsonProperty("country") var country: String?,

    @JsonProperty("headimgurl") var headImgUrl: String,

    @JsonProperty("subscribe_time") var subscribeTime: Date,

    @JsonProperty("unionid") var unionId: String?,

    @JsonProperty("remark") var remark: String,

    @JsonProperty("groupid") var groupId: Int,

    @JsonProperty("tagid_list") var tagIdList: List<Int>,

    @JsonProperty("subscribe_scene") var subscribeScene: String?,

    @JsonProperty("qr_scene") var qrScene: Int,

    @JsonProperty("qr_scene_str") var qrSceneStr: String?
)
