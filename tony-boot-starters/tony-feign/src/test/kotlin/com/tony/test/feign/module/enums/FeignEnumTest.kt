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

/**
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author tangli
 * @date 2021/12/7 14:40
 */
package com.tony.test.feign.module.enums

import com.tony.test.feign.module.enums.client.FeignTestEnumClient
import com.tony.test.feign.module.enums.controller.EnumTest
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
    properties = [
        "server.port=10001"
    ],
    classes = [FeignTestEnumApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class FeignEnumTest {

    @Lazy
    @Resource
    lateinit var openFeignTestEnumClient: FeignTestEnumClient

    @Test
    fun testEnum() {
        openFeignTestEnumClient.testIntEnum(1)
        openFeignTestEnumClient.testStringEnum("yes")
        openFeignTestEnumClient.testPostEnum(EnumTest().apply {
            myIntEnum = 1
            myStringEnum = "yes"
        })
    }

}
