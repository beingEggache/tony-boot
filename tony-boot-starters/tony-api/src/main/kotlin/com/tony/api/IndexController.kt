package com.tony.api

import com.tony.webcore.auth.annotation.NoLoginCheck
import io.swagger.annotations.ApiModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author tangli
 * @since 2020-11-05 9:08
 */
@RestController
class IndexController {

    @NoLoginCheck
    @GetMapping("/string")
    fun string() = ""

    @NoLoginCheck
    @GetMapping("/int")
    fun int() = 1

    @NoLoginCheck
    @GetMapping("/boolean")
    fun boolean() = false
}

@ApiModel("测试用")
data class TestResp(val name: String, val age: Int)
