package com.tony.api

import com.tony.core.PageResult
import com.tony.webcore.auth.annotation.NoLoginCheck
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

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

    @NoLoginCheck
    @GetMapping("/enum")
    fun enum() = Gender.MALE

    @NoLoginCheck
    @GetMapping("/array")
    fun array() = arrayOf(1)

    @NoLoginCheck
    @GetMapping("/intArray")
    fun intArray() = IntArray(1) { 1 }

    @NoLoginCheck
    @GetMapping("/list")
    fun list() = listOf(1)

    @NoLoginCheck
    @GetMapping("/obj")
    fun obj() = Person()

    @NoLoginCheck
    @GetMapping("/date")
    fun date() = Date()

    @NoLoginCheck
    @GetMapping("/local-date")
    fun localDate() = LocalDate.now()

    @NoLoginCheck
    @GetMapping("/local-date-time")
    fun localDateTime() = LocalDateTime.now()

    @NoLoginCheck
    @GetMapping("/page-result")
    fun pageResult() = PageResult<Int>(IntArray(1) { 1 }, 1, 1, 1, 1, false)
}

enum class Gender {
    MALE
}

class Person {
    var name = "Tony"
}
