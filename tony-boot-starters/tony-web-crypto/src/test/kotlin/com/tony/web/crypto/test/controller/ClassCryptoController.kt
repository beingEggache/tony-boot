package com.tony.web.crypto.test.controller

import com.tony.web.crpto.ApiDecrypt
import com.tony.web.crpto.ApiEncrypt
import com.tony.web.crypto.test.req.TestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 类加密解密测试
 * @author tangli
 * @since 2023/05/26 17:14
 */
@ApiDecrypt
@ApiEncrypt
@Tag(name = "类加密解密测试")
@Validated
@RestController
class ClassCryptoController {

    @Operation(description = "class-test-crypto")
    @PostMapping("/class/test-crypto")
    fun body(@Validated @RequestBody req: TestReq) = TestReq("test", 99)
}
