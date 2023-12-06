package com.tony.test.web.crypto.controller

import com.tony.MonoResult.Companion.ofMonoResult
import com.tony.annotation.web.crypto.DecryptRequestBody
import com.tony.annotation.web.crypto.EncryptResponseBody
import com.tony.exception.BizException
import com.tony.test.web.crypto.api.FeignCryptoTestApi
import com.tony.test.web.crypto.req.TestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 方法注解加密解密
 * @author Tang Li
 * @date 2023/05/26 17:14
 */
@Tag(name = "方法加密解密测试")
@Validated
@RestController
class FeignCryptoTestController : FeignCryptoTestApi {

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "crypto-body")
    override fun body(@Validated @RequestBody req: TestReq) =
        TestReq(req.name + " checked", req.age, req.mode)

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "crypto-exception")
    override fun exception() {
        throw BizException("")
    }

    @EncryptResponseBody
    @Operation(description = "crypto-exception")
    override fun mono() = "hello world".ofMonoResult()

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "crypto-exception")
    override fun string() = "hello world"

}
