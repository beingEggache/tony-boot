package com.tony.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

fun main(args: Array<String>) {
    runApplication<ApiWebApp>(*args)
}

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = ["com.tony.**"])
class ApiWebApp
