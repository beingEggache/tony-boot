package com.tony.web.test.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @PostMapping("/exception")
    fun exception(){
        throw Exception()
    }
}
