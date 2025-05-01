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

package tony.test.feign.module.signature.client

import tony.annotation.feign.FeignUnwrapResponse
import tony.annotation.feign.FeignUseGlobalInterceptor
import tony.annotation.feign.RequestProcessors
import tony.annotation.web.auth.NoLoginCheck
import tony.test.feign.config.SignatureRequestProcessor
import tony.test.feign.dto.Person
import tony.test.feign.module.signature.api.FeignSignatureTestApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignUnwrapResponse
@FeignUseGlobalInterceptor
@FeignClient(name = "feignSignatureTestClient", url = "http://localhost:10004")
interface FeignSignatureTestClient : FeignSignatureTestApi {

    @NoLoginCheck
    @PostMapping("/test/boolean")
    override fun boolean(): Boolean

    @RequestProcessors(RequestProcessors.Value(SignatureRequestProcessor::class))
    @PostMapping("/test/test-json-unwrap2")
    override fun person(@RequestBody person: Person): Person

}

