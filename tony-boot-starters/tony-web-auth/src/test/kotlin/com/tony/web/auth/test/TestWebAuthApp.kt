package com.tony.web.auth.test

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableTonyBoot
@SpringBootApplication
class TestWebAuthApp

fun main() {
    runApplication<TestWebAuthApp>()
}
