package com.tony.web.test.controller

import com.tony.web.interceptor.NoLoginCheck
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "测试")
@RestController
class TestController {

    @Operation(summary = "需要登录", description = "需要登录")
    @PostMapping("/need-login-check")
    fun testLoginCheck()="need login check"

    @NoLoginCheck
    @Operation(summary = "不需要登录", description = "不需要登录")
    @PostMapping("/no-login-check")
    fun testNoLoginCheck()="no login check"
}
