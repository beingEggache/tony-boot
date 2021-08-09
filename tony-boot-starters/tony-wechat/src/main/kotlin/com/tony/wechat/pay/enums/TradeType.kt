package com.tony.wechat.pay.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class TradeType(
    @Suppress("unused") @JsonValue
    private val type: String?
) {
    APP("APP"),
    JSAPI("JSAPI")
    ;
}
