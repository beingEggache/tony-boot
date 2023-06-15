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
    fun stringInject(): RequestBodyFieldInjector<String> {
        return object : RequestBodyFieldInjector<String>() {
            override fun value(): String {
                return "aloha"
            }

            override val name: String = "go fuck Yourself"
        }
    }

    @Bean
    fun listInject(): RequestBodyFieldInjector<List<String>> {
        return object : RequestBodyFieldInjector<List<String>>() {
            override fun value(): List<String> {
                return listOf("go fuck your self")
            }

            override val name: String = "list"
        }
    }
}

fun main() {
    runApplication<TestWebApp>()
}
