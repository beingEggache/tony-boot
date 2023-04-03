package com.tony.knife4j.test

import org.junit.jupiter.api.Test
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [TestKnife4jWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class Knife4jWebAppTest {

    @Test
    fun test() {
        println(123)
    }
}

fun main() {
    runApplication<TestKnife4jWebApp>()
}
