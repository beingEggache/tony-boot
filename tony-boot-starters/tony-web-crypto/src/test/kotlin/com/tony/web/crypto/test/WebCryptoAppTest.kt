package com.tony.web.crypto.test

import com.tony.crypto.symmetric.decryptToString
import com.tony.crypto.symmetric.enums.CryptoEncoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.getLogger
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestWebCryptoApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
    @Value("\${web.crypto.encoding:base64}")
    lateinit var encoding: CryptoEncoding

    private final val logger: Logger = getLogger()

    @Test
    fun decrypt() {
        println(123)
        val decryptToString =
            "olWw4bzd5vsststymBOalQ=="
                .decryptToString(
                    algorithm,
                    secret,
                    encoding
                )
        logger.info(decryptToString)
    }
}
