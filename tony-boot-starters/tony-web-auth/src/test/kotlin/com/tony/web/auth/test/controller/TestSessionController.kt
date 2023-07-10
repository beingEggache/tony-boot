package com.tony.web.auth.test.controller

import com.tony.ApiResult
import com.tony.jwt.JwtToken
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.apiSession
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.web.auth.test.req.TestLoginReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "测试session")
@Validated
@RestController
class TestSessionController {

    @Operation(summary = "测试session", description = "测试session description")
    @GetMapping("/test-token-user-id")
    fun testUserId(): String {
        return WebContext.apiSession.userId
    }

    @NoLoginCheck
    @Operation(summary = "测试登录", description = "测试登录 description")
    @PostMapping("/test-login")
    fun login(
        @Validated
        @RequestBody
        req: TestLoginReq
    ) = ApiResult.of(JwtToken.gen("userId" to req.name))
}
