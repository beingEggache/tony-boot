package com.tony.test.web.controller

import com.tony.test.web.req.TestInjectReq
import com.tony.test.web.req.TestLoginReq
import com.tony.test.web.req.TestRemoveInjectTestReq
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
