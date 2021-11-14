package com.tony.api.controller

import com.tony.ApiResult.Companion.toOneResult
import com.tony.api.permission.NoPermissionCheck
import com.tony.db.service.UserService
import com.tony.dto.req.UserLoginReq
import com.tony.jwt.config.JwtToken
import com.tony.utils.defaultZoneId
import com.tony.utils.toString
import com.tony.web.WebApp
import com.tony.web.interceptor.NoLoginCheck
import io.swagger.annotations.ApiOperation

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
    private val userService: UserService
) {

    @ApiOperation("首页")
    @GetMapping("/")
    @NoLoginCheck
    @NoPermissionCheck
    fun index(): String = WebApp.appId

    @ApiOperation("区域")
    @GetMapping("/locale")
    @NoLoginCheck
    @NoPermissionCheck
    fun locale(): String = Locale.getDefault().toLanguageTag()

    @ApiOperation("zoneId")
    @GetMapping("/zone-id")
    @NoLoginCheck
    @NoPermissionCheck
    fun zoneId(): String = defaultZoneId.toString()

    @ApiOperation("时间戳")
    @GetMapping("/now")
    @NoLoginCheck
    @NoPermissionCheck
    fun now(): String = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")

    @ApiOperation("登录")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/login")
    fun login(
        @Validated
        @RequestBody
        loginReq: UserLoginReq
    ) = JwtToken.gen("userId" to userService.login(loginReq).userId).toOneResult()
}
