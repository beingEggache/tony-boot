@file:JvmName("App")
@file:Suppress("unused")

package com.tony.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.multipart.commons.CommonsMultipartResolver

fun main(args: Array<String>) {
    runApplication<ApiWebApp>(*args)
}

@SpringBootApplication(scanBasePackages = ["com.tony.**"])
class ApiWebApp {

    @Bean
    fun commonsMultipartResolver() = CommonsMultipartResolver()
}
