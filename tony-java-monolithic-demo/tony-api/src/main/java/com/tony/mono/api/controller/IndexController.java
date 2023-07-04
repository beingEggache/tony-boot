package com.tony.mono.api.controller;

import com.tony.ApiResult;
import com.tony.OneResult;
import com.tony.PageResult;
import com.tony.jwt.JwtToken;
import com.tony.mono.db.po.User;
import com.tony.mono.db.service.UserService;
import com.tony.mono.dto.req.UserListQueryReq;
import com.tony.mono.dto.req.UserLoginReq;
import com.tony.web.WebApp;
import com.tony.web.interceptor.NoLoginCheck;
import com.tony.web.utils.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
public class IndexController {

    private final UserService userService;

    @Operation(summary = "首页")
    @NoLoginCheck
    @GetMapping("/")
    public String index() {
        return WebApp.getAppId();
    }

    @Operation(summary = "登录")
    @NoLoginCheck
    @PostMapping("/login")
    public ApiResult<OneResult<String>> login(
        @Validated
        @RequestBody final UserLoginReq req) {
        //noinspection unchecked
        return ResultUtils.ofResult(JwtToken.gen(new Pair<>("userId", userService.login(req))));
    }

    @Operation(summary = "用户信息")
    @NoLoginCheck
    @PostMapping("/info")
    public void info() {
    }

    @Operation(summary = "用户列表")
    @NoLoginCheck
    @PostMapping("/user/list")
    public PageResult<User> list(
        @Validated
        @RequestBody final UserListQueryReq req
    ) {
        return userService.list(req);
    }
}
