package com.tony.feign.test.module.targeter_signature

import com.tony.feign.test.dto.Person
import com.tony.feign.test.module.targeter_signature.client.OpenFeignTestTargeterSignatureClient
import com.tony.utils.getLogger
import com.tony.utils.println
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [OpenFeignTestTargeterSignatureApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OpenFeignTargeterTest {

    @Resource
    lateinit var openFeignTestTargeterSignatureClient: OpenFeignTestTargeterSignatureClient


    private val logger = getLogger()

    @Test
    fun testWithGlobalInterceptor() {
        val client = openFeignTestTargeterSignatureClient
        client.person(Person(listOf(1, 2, 3).toIntArray(), 123, "432", mapOf("age" to 456))).toJsonString().println()
    }
}
