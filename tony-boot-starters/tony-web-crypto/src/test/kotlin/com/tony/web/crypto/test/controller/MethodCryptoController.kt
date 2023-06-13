package com.tony.web.crypto.test.controller

import com.tony.web.crpto.DecryptRequestBody
import com.tony.web.crpto.EncryptResponseBody
import com.tony.web.crypto.test.req.TestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 方法注解加密解密
 * @author tangli
 * @since 2023/05/26 17:14
 */
@Tag(name = "方法加密解密测试")
@Validated
@RestController
class MethodCryptoController {

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "method-test-crypto")
    @PostMapping("/method/test-crypto")
    fun body(@Validated @RequestBody req: TestReq) =
        TestReq(req.name + " checked", req.age, req.mode)
}
