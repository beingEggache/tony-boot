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

package tony.test.web.controller

import tony.test.web.req.TestInjectReq
import tony.test.web.req.TestLoginReq
import tony.test.web.req.TestRemoveInjectTestReq
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "测试验证")
@Validated
@RestController
class TestRequestBodyInjectController {

    @Operation(summary = "request body 注入", description = "request body 注入")
    @PostMapping("/request-body-inject")
    fun testInject1(
        @Validated @RequestBody req:
        TestInjectReq<Int, String, List<TestLoginReq>, Map<String, TestLoginReq>>
    ) = let {
        val string = req.string
        println(string)
        val string1 = req.string1
        println(string1)
        val int = req.int
        println(int)
        val int2 = req.int2
        println(int2)
        val map = req.map
        println(map)
        val objList = req.objList
        println(objList)
        val objMap = req.objMap
        println(objMap)
        val list = req.list
        println(list)
        val testTypeParam2 = req.testTypeParam2
        println(testTypeParam2)
        val testTypeParam3 = req.testTypeParam3
        println(testTypeParam3)
        val testTypeParam4 = req.testTypeParam4
        println(testTypeParam4)
        "req"
    }

    @Operation(summary = "request body 注入 移除支持", description = "request body 注入")
    @PostMapping("/request-body-inject-remove-support")
    fun testRemoveInjectSupport(
        @Validated @RequestBody req:
        TestRemoveInjectTestReq
    ) = req


}
