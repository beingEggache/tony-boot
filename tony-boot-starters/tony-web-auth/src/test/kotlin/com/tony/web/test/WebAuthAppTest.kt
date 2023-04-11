package com.tony.web.test

import org.junit.jupiter.api.Test
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [TestWebAuthApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebAuthAppTest {

    @Test
    fun test() {
        println(123)
    }
}

fun main() {
    runApplication<TestWebAuthApp>()
}
