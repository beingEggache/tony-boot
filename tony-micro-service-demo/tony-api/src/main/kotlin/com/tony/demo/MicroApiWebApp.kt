package com.tony.demo

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

fun main(args: Array<String>) {
    runApplication<MicroApiWebApp>(*args) {
        setHeadless(true)
    }
}

@EnableDiscoveryClient
@EnableTonyBoot
@SpringBootApplication
class MicroApiWebApp
