package com.tony.web.test.controller

import com.tony.web.test.req.TestDefaultInjectReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * TestInjectController is
 * @author Tang Li
 * @date 2023/07/06 15:31
 */
@Tag(name = "测试注入")
@Validated
@RestController
class TestInjectController {

    @Operation(summary = "request body 注入 默认值", description = "request body 注入 默认值")
    @PostMapping("/test/request-body-default-inject")
    fun testInject(
        @Validated
        @RequestBody
        req: TestDefaultInjectReq
    ) = req
}
