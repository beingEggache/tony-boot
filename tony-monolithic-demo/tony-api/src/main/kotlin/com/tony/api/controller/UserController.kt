package com.tony.api.controller

import com.tony.MonoResult.Companion.ofMonoResult
import com.tony.PageQuery
import com.tony.api.permission.NoPermissionCheck
import com.tony.db.service.UserService
import com.tony.dto.req.UserAddReq
import com.tony.dto.req.UserUpdateReq
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.appId
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
 * @date 2020-11-03 14:40
 */
@Tag(name = "用户")
@RestController
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "创建用户", description = "测试创建用户")
    @PostMapping("/user/add")
    fun add(
        @Validated
        @RequestBody
        req: UserAddReq,
    ) = userService.add(req).ofMonoResult()

    @Operation(summary = "更新用户", description = "测试更新用户")
    @PostMapping("/user/update")
    fun update(
        @Validated
        @RequestBody
        req: UserUpdateReq,
    ) = userService.update(req).ofMonoResult()

    @Operation(summary = "登录用户信息", description = "当前用户权限")
    @NoPermissionCheck
    @GetMapping("/user/info")
    fun info() =
        userService.info(WebContext.userId, WebContext.appId)

    @Operation(summary = "用户列表", description = "用户列表")
    @NoPermissionCheck
    @PostMapping("/user/list")
    fun list(
        @Validated
        @RequestBody req: PageQuery<String>,
    ) = userService.list(req)
}
