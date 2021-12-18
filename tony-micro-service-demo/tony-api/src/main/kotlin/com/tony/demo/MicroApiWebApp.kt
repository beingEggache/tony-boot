package com.tony.demo

import com.tony.Env
import com.tony.annotation.EnableTonyBoot
import com.tony.web.WebApp
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@Suppress("HttpUrlsUsage")
fun main(args: Array<String>) {
    runApplication<MicroApiWebApp>(*args) {
        setHeadless(true)
    }
    val info =
        "${WebApp.appId} " +
            "http://${WebApp.ip}:${WebApp.port}${WebApp.contextPath.padStart(1, '/')} " +
            "started success. " +
            "profiles: ${Env.activeProfiles.joinToString()}. " +
            "He who has a why to live can bear almost any how."
    LoggerFactory.getLogger(MicroApiWebApp::class.java).info(info)
}

@EnableDiscoveryClient
@EnableTonyBoot
@SpringBootApplication
class MicroApiWebApp
