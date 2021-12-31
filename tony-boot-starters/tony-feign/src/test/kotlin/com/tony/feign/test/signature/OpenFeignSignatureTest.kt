package com.tony.feign.test.signature

import com.tony.ApiProperty
import com.tony.feign.test.signature.client.OpenFeignTestSignatureClient
import com.tony.feign.test.signature.dto.Person
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

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
        if (result.code != ApiProperty.successCode) {
            logger.error(result.toJsonString())
            throw RuntimeException("error")
        }
        logger.info(result.toJsonString())
    }
}
