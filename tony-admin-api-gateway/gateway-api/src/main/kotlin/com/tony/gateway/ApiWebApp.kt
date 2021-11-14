/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.gateway

import com.tony.annotation.EnableTonyBoot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import org.springframework.cloud.client.discovery.EnableDiscoveryClient

fun main(args: Array<String>) {
    runApplication<ApiWebApp>(*args)
}

@EnableDiscoveryClient
@EnableTonyBoot
@SpringBootApplication
class ApiWebApp
