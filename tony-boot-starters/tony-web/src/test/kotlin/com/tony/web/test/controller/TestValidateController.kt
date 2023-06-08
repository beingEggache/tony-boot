package com.tony.web.test.controller

import com.tony.web.test.req.TestPatternReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "测试验证")
@Validated
@RestController
class TestValidateController {

    @Operation(summary = "pattern验证", description = "pattern验证")
    @PostMapping("/pattern-validate")
    fun testLoginCheck(@Validated @RequestBody req: TestPatternReq) = req.mobile

}
