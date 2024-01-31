package com.tony.mono.api.controller;

import com.tony.MonoResult;
import com.tony.PageQuery;
import com.tony.PageResult;
import com.tony.annotation.web.auth.NoLoginCheck;
import com.tony.jwt.JwtToken;
import com.tony.mono.db.po.User;
import com.tony.mono.db.service.UserService;
import com.tony.mono.dto.req.UserLoginReq;
import com.tony.web.WebApp;
import io.swagger.v3.oas.annotations.Operation;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 *
 * @author Tang Li
 * @date 2024/01/31 15:27
 */
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
    public MonoResult<String> login(
        @Validated
        @RequestBody final UserLoginReq req) {
        return MonoResult.ofMonoResult(JwtToken.gen(new Pair<>("userId", userService.login(req))));
    }

    @Operation(summary = "用户信息")
    @NoLoginCheck
    @PostMapping("/info")
    public void info() {
        // Yeah
    }

    @Operation(summary = "用户列表")
    @NoLoginCheck
    @PostMapping("/user/list")
    public PageResult<User> list(
        @Validated
        @RequestBody final PageQuery<String> req
    ) {
        return userService.list(req);
    }
}
