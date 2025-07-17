/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.test.web.crypto.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.annotation.web.crypto.DecryptRequestBody
import tony.annotation.web.crypto.EncryptResponseBody
import tony.core.exception.BizException
import tony.core.model.MonoResultLike.Companion.ofMonoResult
import tony.test.web.crypto.api.FeignCryptoTestApi
import tony.test.web.crypto.req.TestReq

/**
 * 方法注解加密解密
 * @author tangli
 * @date 2023/05/26 19:14
 */
@Tag(name = "方法加密解密测试")
@Validated
@RestController
class FeignCryptoTestController : FeignCryptoTestApi {

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
    @Operation(description = "mono")
    override fun mono() = "hello world".ofMonoResult()

    @DecryptRequestBody
    @EncryptResponseBody
    @Operation(description = "string")
    override fun string() = "hello world"

}
