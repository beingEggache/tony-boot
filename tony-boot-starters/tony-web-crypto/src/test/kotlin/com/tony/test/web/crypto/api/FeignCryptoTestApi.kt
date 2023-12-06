package com.tony.test.web.crypto.api

import com.tony.MonoResult
import com.tony.test.web.crypto.req.TestReq
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * MethodCrypto is
 * @author tangli
 * @date 2023/12/06 16:16
 * @since 1.0.0
 */
interface FeignCryptoTestApi {

    @PostMapping("/method/test/crypto-body")
    fun body(@Validated @RequestBody req: TestReq): TestReq

    @PostMapping("/method/test/crypto-exception")
    fun exception()

    @PostMapping("/method/test/crypto-mono")
    fun mono(): MonoResult<String>

    @PostMapping("/method/test/crypto-string")
    fun string(): String
}
