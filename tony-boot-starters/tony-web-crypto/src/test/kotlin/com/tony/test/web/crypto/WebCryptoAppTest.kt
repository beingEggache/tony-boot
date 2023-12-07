package com.tony.test.web.crypto

import com.tony.test.web.crypto.client.FeignCryptoTestClient
import com.tony.utils.getLogger
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootTest(classes = [TestWebCryptoApp::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebCryptoAppTest {

    @Resource
    lateinit var feignCryptoTestClient: FeignCryptoTestClient

    private final val logger: Logger = getLogger()

    @Test
    fun decrypt() {
        val monoResult = feignCryptoTestClient.mono()
        logger.info(monoResult.toString())
    }
}
