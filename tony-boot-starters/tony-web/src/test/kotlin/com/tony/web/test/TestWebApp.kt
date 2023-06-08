package com.tony.web.test

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableTonyBoot
@SpringBootApplication
class TestWebApp

fun main() {
    runApplication<TestWebApp>()
}
