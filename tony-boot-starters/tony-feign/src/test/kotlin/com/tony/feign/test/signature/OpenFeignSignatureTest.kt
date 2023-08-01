package com.tony.feign.test.signature

import com.tony.feign.test.signature.client.OpenFeignTestSignatureClient
import com.tony.feign.test.signature.dto.Person
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [OpenFeignTestSignatureApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OpenFeignSignatureTest {

    @Resource
    lateinit var openFeignTestSignatureClient: OpenFeignTestSignatureClient


    private val logger = getLogger()

    @Test
    fun testSignature() {
        val person = Person(listOf(1, 2, 3).toIntArray(), 123, "432", mapOf("qwe" to 123))
        val result = openFeignTestSignatureClient.testSignature(person)
        logger.info(result.toJsonString())
    }
}
