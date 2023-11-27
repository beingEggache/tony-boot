package com.tony.test.web.auth

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableTonyBoot
@SpringBootApplication
class TestWebAuthApp

fun main() {
    runApplication<TestWebAuthApp>()
}
