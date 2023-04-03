package com.tony.knife4j.test.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "knife4j测试")
@RestController
class TestController {

    @Operation(summary = "test1")
    @GetMapping("/test1")
    fun test1() {
    }

    @GetMapping("/test2")
    fun test2() {
    }
}
