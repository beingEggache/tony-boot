package com.tony.web.test

import com.tony.annotation.EnableTonyBoot
import com.tony.jackson.InjectableValueSupplier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.lang.reflect.AnnotatedElement

@EnableTonyBoot
@SpringBootApplication
class TestWebApp {

    @Bean
    fun stringInject(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string"

            override fun value(e: AnnotatedElement?, instance: Any?, originValue: Any?): String {
                println(e)
                println(instance)
                println(originValue)
                println(count++)
                return "go fuck yourself"
            }
        }
}

fun main() {
    runApplication<TestWebApp>()
}
