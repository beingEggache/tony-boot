package com.tony.api.controller

import com.tony.api.permission.NoPermissionCheck
import com.tony.core.utils.defaultZoneId
import com.tony.core.utils.toString
import com.tony.db.service.UserService
import com.tony.dto.req.UserLoginReq
import com.tony.webcore.WebApp
import com.tony.webcore.auth.JwtToken
import com.tony.webcore.auth.annotation.NoLoginCheck
import com.tony.webcore.toOneResult
import io.swagger.annotations.ApiOperation
import java.time.LocalDateTime
import java.util.Locale
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
        loginReq: UserLoginReq) =
        JwtToken.gen("userId" to userService.login(loginReq).userId).toOneResult()

}

