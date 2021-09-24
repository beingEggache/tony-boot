package com.tony.api.controller

import com.tony.api.permission.NoPermissionCheck
import com.tony.auth.extensions.WebContextExtensions.userId
import com.tony.db.service.UserService
import com.tony.dto.req.UserCreateReq
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author tangli
 * @since 2020-11-03 14:40
 */
@Api(tags = ["用户"])
@RestController
class UserController(
    private val userService: UserService
) {

    @ApiOperation("创建用户", notes = "测试创建用户")
    @PostMapping("/user/create")
    fun create(req: UserCreateReq) =
        userService.add(req)

    @ApiOperation("登录用户信息", notes = "当前用户权限")
    @NoPermissionCheck
    @GetMapping("/user/info")
    fun info() =
        userService.info(WebContext.userId, WebApp.appId)
}
