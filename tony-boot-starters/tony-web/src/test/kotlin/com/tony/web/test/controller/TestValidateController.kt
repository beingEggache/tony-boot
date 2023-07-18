package com.tony.web.test.controller

import com.tony.JPageQuery
import com.tony.web.test.req.TestPatternReq
import com.tony.web.test.req.TestQuery
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

    @Operation(summary = "pageQuery 验证", description = "pageQuery 验证")
    @PostMapping("/page-query-validate")
    fun testPageQueryValidation(
        @Validated @RequestBody req: JPageQuery<TestQuery>
    ) = "req.query"

}
