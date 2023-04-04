package com.tony.web.test

import org.junit.jupiter.api.Test
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [TestWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebAppTest {

    @Test
    fun test() {
        println(123)
    }
}

fun main() {
    runApplication<TestWebApp>()
}
