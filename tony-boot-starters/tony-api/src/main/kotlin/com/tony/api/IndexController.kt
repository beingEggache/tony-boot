package com.tony.api

import com.tony.core.PageResult
import com.tony.webcore.auth.annotation.NoLoginCheck
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
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
    @GetMapping("/char")
    fun char() = 'a'

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
    @GetMapping("/empty-list")
    fun emptyList() = listOf<Any>()

    @NoLoginCheck
    @GetMapping("/empty-array")
    fun emptyArray() = arrayOf<Any>()

    @NoLoginCheck
    @GetMapping("/obj")
    fun obj(): Person? = null

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

    @NoLoginCheck
    @PostMapping("/test-post-param")
    fun testPostParam(@RequestBody person: Person) =
        0

    @NoLoginCheck
    @GetMapping("/test-param")
    fun testGetParam(@RequestParam int: Int) = 0
}

enum class Gender {
    MALE
}

data class Person(val gender: Gender)
