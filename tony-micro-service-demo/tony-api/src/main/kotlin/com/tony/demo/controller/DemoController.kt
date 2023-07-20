package com.tony.demo.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {

    @Operation(summary = "hello")
    @GetMapping("/hello")
    fun test(): Map<String, String> {
        return mapOf("hello" to "你好吖")
    }

    @Operation(summary = "ignore")
    @GetMapping("/ignore")
    fun ignore() = "aloha ignore"

    @Operation(summary = "loginNoPermission")
    @GetMapping("/loginNoPermission")
    fun loginNoPermission() = "loginNoPermission"

    @Operation(summary = "loginHasPermission")
    @GetMapping("/loginHasPermission")
    fun loginHasPermission() = "loginHasPermission"

    @Operation(summary = "loginHasNoPermission")
    @GetMapping("/loginHasNoPermission")
    fun loginHasNoPermission() = "loginHasNoPermission"
}
