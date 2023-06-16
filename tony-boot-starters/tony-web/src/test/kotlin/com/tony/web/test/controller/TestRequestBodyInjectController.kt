package com.tony.web.test.controller

import com.tony.web.test.req.TestInjectReq
import com.tony.web.test.req.TestLoginReq
import com.tony.web.test.req.TestRemoveInjectTestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "测试验证")
@Validated
@RestController
class TestRequestBodyInjectController {

    @Operation(summary = "request body 注入", description = "request body 注入")
    @PostMapping("/request-body-inject")
    fun testInject1(
        @Validated @RequestBody req:
        TestInjectReq<Int, String, List<TestLoginReq>, Map<String, TestLoginReq>>
    ) = req

    @Operation(summary = "request body 注入 移除支持", description = "request body 注入")
    @PostMapping("/request-body-inject-remove-support")
    fun testRemoveInjectSupport(
        @Validated @RequestBody req:
        TestRemoveInjectTestReq
    ) = req


}
