package com.tony.mono.api.controller;

import com.tony.MonoResult;
import com.tony.PageQuery;
import com.tony.PageResult;
import com.tony.annotation.web.auth.NoLoginCheck;
import com.tony.jwt.JwtToken;
import com.tony.mono.db.po.User;
import com.tony.mono.db.service.UserService;
import com.tony.mono.dto.req.UserLoginReq;
import com.tony.web.utils.Servlets;
import io.swagger.v3.oas.annotations.Operation;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.Map;

/**
 * IndexController
 *
 * @author tangli
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
        return "index";
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

    @Operation(summary = "requestHeaders")
    @NoLoginCheck
    @PostMapping("/request-headers")
    public Map<String,?> requestHeaders(){
        return Servlets.requestHeaders();
    }

    @Operation(summary = "responseHeaders")
    @NoLoginCheck
    @PostMapping("/response-headers")
    public Map<String,?> responseHeaders(){
        return Servlets.responseHeaders();
    }

    @Operation(summary = "origin")
    @NoLoginCheck
    @PostMapping("/origin")
    public MonoResult<String> origin(){
        return MonoResult.ofMonoResult(Servlets.origin());
    }

    @Operation(summary = "remoteIp")
    @NoLoginCheck
    @PostMapping("/remote-ip")
    public MonoResult<String> remoteIp(){
        return MonoResult.ofMonoResult(Servlets.remoteIp());
    }

    @Operation(summary = "url")
    @NoLoginCheck
    @PostMapping("/url")
    public URL url(){
        return Servlets.url();
    }

    @Operation(summary = "requestParsedMedia")
    @NoLoginCheck
    @PostMapping("/request-parsed-media")
    public MediaType requestParsedMedia(){
        return Servlets.requestParsedMedia();
    }

    @Operation(summary = "responseParsedMedia")
    @NoLoginCheck
    @PostMapping("/response-parsed-media")
    public MediaType responseParsedMedia(){
        return Servlets.responseParsedMedia();
    }
}
