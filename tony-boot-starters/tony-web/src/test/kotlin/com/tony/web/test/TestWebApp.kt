package com.tony.web.test

import com.tony.annotation.EnableTonyBoot
import com.tony.web.advice.RequestBodyFieldInjector
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableTonyBoot
@SpringBootApplication
class TestWebApp {

    @Bean
    fun stringInject(): RequestBodyFieldInjector =
        RequestBodyFieldInjector("string") {
            "aloha"
        }

    @Bean
    fun intInject(): RequestBodyFieldInjector =
        RequestBodyFieldInjector("int") {
            123
        }

    @Bean
    fun listInject(): RequestBodyFieldInjector =
        RequestBodyFieldInjector("list") {
            listOf("item 1")
        }
}

fun main() {
    runApplication<TestWebApp>()
}

class FuncArgClass(val name: String, val func: () -> Any) {
    init {
        func()
    }
}
