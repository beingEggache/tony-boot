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

package tony.test.knife4j.controller

import cn.idev.excel.FastExcel
import tony.test.knife4j.req.TestReq
import tony.test.knife4j.resp.TestExcelResp
import tony.test.knife4j.resp.TestResp
import tony.web.utils.responseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.time.LocalDate

@Tag(name = "knife4j测试")
@RestController
class TestController {

    @Operation(summary = "test1")
    @GetMapping("/test1")
    fun test1() {
    }

    @Operation(summary = "test2")
    @PostMapping("/test2")
    fun test2(@RequestBody req: TestReq) = TestResp()

    @Operation(summary = "test-form")
    @PostMapping("/test-form")
    fun testForm(@ParameterObject req: TestReq) = TestResp()

    @Operation(summary = "测试excel")
    @PostMapping("/test3")
    fun testExcel(): ResponseEntity<ByteArray> =
        ByteArrayOutputStream().use {
            FastExcel
                .write(it, TestExcelResp::class.java)
                .sheet(0)
                .doWrite {
                    (1..10).map {
                        TestExcelResp().apply {
                            name = "张$it"
                            age = it
                            sex = if (it % 2 == 1) "男" else "女"
                            birthDate = LocalDate.now()
                        }
                    }
                }
            it.toByteArray()
        }.responseEntity("test.xlsx")
}
