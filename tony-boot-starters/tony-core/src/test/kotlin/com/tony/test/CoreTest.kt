package com.tony.test

import com.tony.SpringContexts
import com.tony.annotation.EnableTonyBoot
import com.tony.utils.println
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.StandardEnvironment

@SpringBootTest(
    classes = [CoreTestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class CoreTest {

    @Test
    fun test() {
        val env = SpringContexts.environment as StandardEnvironment
        env.propertySources.forEach { it.source.javaClass.println() }
        println("--------")
        env.systemProperties.forEach(::println)
    }
}

@EnableTonyBoot
@SpringBootApplication
class CoreTestApp
