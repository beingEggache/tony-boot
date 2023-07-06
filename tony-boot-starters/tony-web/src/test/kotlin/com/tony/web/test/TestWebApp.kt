package com.tony.web.test

import com.tony.annotation.EnableTonyBoot
import com.tony.web.support.RequestBodyFieldInjector
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableTonyBoot
@SpringBootApplication
class TestWebApp {

    @Bean
    fun stringInject(): RequestBodyFieldInjector =
        object : RequestBodyFieldInjector("string") {
            override fun value(fieldType: Class<*>) = "aloha"
        }

    @Bean
    fun intInject(): RequestBodyFieldInjector =
        object : RequestBodyFieldInjector("int") {
            override fun value(fieldType: Class<*>) = 123

        }

    @Bean
    fun listInject(): RequestBodyFieldInjector =
        object : RequestBodyFieldInjector("list") {
            override fun value(fieldType: Class<*>) = listOf("item 1")
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
