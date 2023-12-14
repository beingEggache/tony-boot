/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.test.web.auth.controller

import com.tony.ApiResult
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.jwt.JwtToken
import com.tony.test.web.auth.req.TestLoginReq
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.webSession
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
        return WebContext.webSession.userId
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
