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
        return object : RequestBodyFieldInjector<String>("go fuck Yourself") {
            override fun value(): String {
                return "aloha"
            }
        }
    }

    @Bean
    fun listInject(): RequestBodyFieldInjector<List<String>> {
        return object : RequestBodyFieldInjector<List<String>>("list") {
            override fun value(): List<String> {
                return listOf("go fuck your self")
            }
        }
    }
}

fun main() {
    runApplication<TestWebApp>()
}
