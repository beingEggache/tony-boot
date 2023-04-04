package com.tony.knife4j.test.controller

import com.alibaba.excel.EasyExcel
import com.tony.knife4j.test.req.TestReq
import com.tony.knife4j.test.resp.TestExcelResp
import com.tony.knife4j.test.resp.TestResp
import com.tony.web.utils.responseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
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
            EasyExcel
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
