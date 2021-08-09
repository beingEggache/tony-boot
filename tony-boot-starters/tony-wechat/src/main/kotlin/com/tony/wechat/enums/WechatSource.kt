package com.tony.wechat.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class WechatSource(
    @Suppress("unused")
    @JsonValue
    private val label: String
) {
    MINI_PROGRAM("MINI_PROGRAM"),
    APP("APP");
}
