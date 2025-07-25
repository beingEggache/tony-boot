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

package tony.test.feign.module.jwt.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.annotation.web.auth.NoLoginCheck
import tony.core.model.MonoResultLike.Companion.ofMonoResult
import tony.jwt.JwtToken
import tony.test.feign.dto.LoginReq
import tony.test.feign.dto.Person
import tony.test.feign.module.jwt.api.FeignJwtTestApi

@RestController
@Validated
class FeignTestJwtController : FeignJwtTestApi {

    @NoLoginCheck
    override fun login(@RequestBody req: LoginReq) =
        JwtToken.gen("userId" to "99efd6bbc03b491191ca3206bd20046f").ofMonoResult()

    override fun doAfterLogin(@RequestBody person: Person) = person
}
