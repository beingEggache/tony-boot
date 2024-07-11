package com.tony.api.controller

import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.api.permission.NoPermissionCheck
import com.tony.utils.toString
import io.swagger.v3.oas.annotations.Operation
import java.time.LocalDateTime
import java.util.Locale
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class TestController {

    @Operation(summary = "区域")
    @GetMapping("/locale")
    @NoLoginCheck
    @NoPermissionCheck
    fun locale(): String =
        Locale.getDefault().toLanguageTag()

    @Operation(summary = "时间戳")
    @GetMapping("/now")
    @NoLoginCheck
    @NoPermissionCheck
    fun now(): String =
        LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")

    @Operation(summary = "空")
    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/void")
    fun void() {
    }

    @Operation(summary = "异常")
    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/exception")
    fun exception(): Unit =
        throw Exception("exception")
}
