package com.tony.test.web.crypto

import com.tony.annotation.EnableTonyBoot
import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.web.crpto.CryptoProvider
import com.tony.web.crpto.config.WebCryptoConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@EnableTonyBoot
@Import(WebCryptoConfig::class)
@SpringBootApplication
class TestWebCryptoApp {
    @Bean
    fun cryptoProvider(): CryptoProvider =
        object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.DES
            override val secret: String = "xvwe23dvxs"
            override val encoding: Encoding = Encoding.BASE64
        }
}

fun main() {
    runApplication<TestWebCryptoApp>()
}
