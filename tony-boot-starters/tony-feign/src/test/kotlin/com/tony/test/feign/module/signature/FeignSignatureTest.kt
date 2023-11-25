package com.tony.test.feign.module.signature

import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.signature.client.FeignSignatureTestClient
import com.tony.utils.getLogger
import com.tony.utils.println
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = ["server.port=9093"],
    classes = [FeignSignatureTestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class FeignSignatureTest {

    @Resource
    lateinit var openFeignSignatureTestClient: FeignSignatureTestClient


    private val logger = getLogger()

    @Test
    fun test() {
        val client = openFeignSignatureTestClient
        client.person(Person(listOf(1, 2, 3).toIntArray(), 123, "432", mapOf("age" to 456))).toJsonString().println()
    }
}
