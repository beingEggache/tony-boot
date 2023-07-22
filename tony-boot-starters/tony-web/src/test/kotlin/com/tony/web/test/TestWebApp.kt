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
                get() = "string0"

            override fun value(e: AnnotatedElement?, instance: Any?, originValue: Any?): String {
                println(e)
                println(instance)
                println(originValue)
                println(count++)
                return "string0"
            }
        }

    @Bean
    fun stringInject1(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string1"

            override fun value(e: AnnotatedElement?, instance: Any?, originValue: Any?): String {
                println(e)
                println(instance)
                println(originValue)
                println(count++)
                return "string1"
            }
        }

    @Bean
    fun stringInject2(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string2"

            override fun value(e: AnnotatedElement?, instance: Any?, originValue: Any?): String {
                println(e)
                println(instance)
                println(originValue)
                println(count++)
                return "string2"
            }
        }

    @Bean
    fun stringInject3(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string3"

            override fun value(e: AnnotatedElement?, instance: Any?, originValue: Any?): String {
                println(e)
                println(instance)
                println(originValue)
                println(count++)
                return "string3"
            }
        }
}

fun main() {
    runApplication<TestWebApp>()
}
