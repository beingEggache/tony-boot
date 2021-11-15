package com.tony.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {

    @GetMapping("/hello")
    fun test(): Map<String, String> {
        return mapOf("hello" to "你好吖")
    }

    @GetMapping("/ignore")
    fun ignore() = "aloha ignore"

    @GetMapping("/loginNoPermission")
    fun loginNoPermission() = "loginNoPermission"

    @GetMapping("/loginHasPermission")
    fun loginHasPermission() = "loginHasPermission"

    @GetMapping("/loginHasNoPermission")
    fun loginHasNoPermission() = "loginHasNoPermission"
}
