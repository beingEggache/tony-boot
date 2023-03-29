package com.tony.api.controller

import com.tony.api.permission.NoPermissionCheck
import com.tony.db.service.UserService
import com.tony.dto.req.UserCreateReq
import com.tony.web.WebApp
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.userId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@Tag(name = "用户")
@RestController
class UserController(
    private val userService: UserService,
) {

    @Operation(summary = "创建用户", description = "测试创建用户")
    @PostMapping("/user/create")
    fun create(
        @Validated
        @RequestBody
        req: UserCreateReq,
    ) = userService.add(req)

    @Operation(summary = "登录用户信息", description = "当前用户权限")
    @NoPermissionCheck
    @GetMapping("/user/info")
    fun info() = userService.info(WebContext.userId, WebApp.appId)
}
