package com.tony.test.web.crypto

import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.test.web.crypto.client.FeignCryptoTestClient
import com.tony.utils.getLogger
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootTest(classes = [TestWebCryptoApp::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebCryptoAppTest {


    @Value("\${web.crypto.algorithm:des}")
    lateinit var algorithm: SymmetricCryptoAlgorithm

    /**
     * 秘钥
     */
    @Value("\${web.crypto.secret:}")
    lateinit var secret: String

    /**
     * 二进制编码
     */
    @Value("\${web.crypto.codec:base64}")
    lateinit var encoding: Encoding

    @Resource
    lateinit var feignCryptoTestClient: FeignCryptoTestClient

    private final val logger: Logger = getLogger()

    @Test
    fun decrypt() {
        val monoResult = feignCryptoTestClient.mono()
        logger.info(monoResult.toString())
    }
}
