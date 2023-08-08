package com.tony.web.crypto.test.controller

import com.tony.MonoResult.Companion.ofMonoResult
import com.tony.annotation.web.crypto.DecryptRequestBody
import com.tony.annotation.web.crypto.EncryptResponseBody
import com.tony.exception.BizException
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
    @Operation(description = "crypto-body")
    @PostMapping("/method/test/crypto-body")
    fun body(@Validated @RequestBody req: TestReq) =
        TestReq(req.name + " checked", req.age, req.mode)

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "crypto-exception")
    @PostMapping("/method/test/crypto-exception")
    fun exception() {
        throw BizException("")
    }

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "crypto-exception")
    @PostMapping("/method/test/crypto-mono")
    fun mono() = "hello world".ofMonoResult()

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "crypto-exception")
    @PostMapping("/method/test/crypto-string")
    fun string() = "hello world"

}
