package com.tony.web.crypto.test

import com.tony.annotation.EnableTonyBoot
import com.tony.web.crpto.config.WebCryptoConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@EnableTonyBoot
@Import(WebCryptoConfig::class)
@SpringBootApplication
class TestWebCryptoApp

fun main() {
    runApplication<TestWebCryptoApp>()
}
