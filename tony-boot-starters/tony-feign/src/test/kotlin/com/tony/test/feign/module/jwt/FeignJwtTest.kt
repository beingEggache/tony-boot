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

package com.tony.test.feign.module.jwt

import com.tony.test.feign.config.mockWebAttributes
import com.tony.test.feign.dto.LoginReq
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.jwt.client.FeignJwtTestClient
import com.tony.utils.getLogger
import com.tony.utils.ifNullOrBlank
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.boot.test.context.SpringBootTest

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
    properties = [
        "server.port=10003"
    ],
    classes = [FeignJwtTestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class FeignJwtTest {

    @Resource
    lateinit var feignJwtTestClient: FeignJwtTestClient

    private val logger = getLogger()

    @Test
    fun testJwt() {
        val login = feignJwtTestClient.login(LoginReq("123", "pwd"))
        logger.info(login.toJsonString())
        mockWebAttributes["token"] = login.value.ifNullOrBlank()
        val person = feignJwtTestClient.doAfterLogin(Person(null, null, null, null))
        logger.info(person.toJsonString())
    }
}
