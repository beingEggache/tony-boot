package com.tony.web.crypto.test.controller

import com.tony.web.crypto.test.req.TestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * TestController is
 * @author tangli
 * @since 2023/05/26 17:14
 */
@Tag(name = "测试")
@Validated
@RestController
class TestController {

    @Operation(description = "test-crypto")
    @PostMapping("/test-crypto")
    fun body(@Validated @RequestBody req: TestReq) = TestReq("test", 99)
}
