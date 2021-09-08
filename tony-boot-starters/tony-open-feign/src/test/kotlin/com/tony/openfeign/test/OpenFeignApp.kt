package com.tony.openfeign.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

fun main() {
    runApplication<OpenFeignApp>()
}

@EnableFeignClients
@SpringBootApplication
class OpenFeignApp
