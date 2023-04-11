package com.tony.api.controller

import com.tony.api.permission.NoPermissionCheck
import com.tony.db.service.UserService
import com.tony.dto.req.UserLoginReq
import com.tony.jwt.JwtToken
import com.tony.utils.defaultZoneId
import com.tony.utils.toString
import com.tony.web.WebApp
import com.tony.web.interceptor.NoLoginCheck
import io.swagger.v3.oas.annotations.Operation
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.Locale

@RestController
@Validated
class IndexController(
    private val userService: UserService,
) {

    @Operation(summary = "首页")
    @GetMapping("/")
    @NoLoginCheck
    @NoPermissionCheck
    fun index(): String = WebApp.appId

    @Operation(summary = "区域")
    @GetMapping("/locale")
    @NoLoginCheck
    @NoPermissionCheck
    fun locale(): String = Locale.getDefault().toLanguageTag()

    @Operation(summary = "zoneId")
    @GetMapping("/zone-id")
    @NoLoginCheck
    @NoPermissionCheck
    fun zoneId(): String = defaultZoneId.toString()

    @Operation(summary = "时间戳")
    @GetMapping("/now")
    @NoLoginCheck
    @NoPermissionCheck
    fun now(): String = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")

    @Operation(summary = "登录")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/login")
    fun login(
        @Validated
        @RequestBody
        loginReq: UserLoginReq,
    ) = JwtToken.gen("userId" to userService.login(loginReq).userId)

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
    fun exception() {
        throw Exception("exception")
    }
}
