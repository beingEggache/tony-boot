package com.tony.feign.test.module.jwt

import com.tony.feign.test.dto.LoginReq
import com.tony.feign.test.jwt.client.OpenFeignTestJwtClient
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "jwt.secret: $secret",
    ],
    classes = [OpenFeignTestJwtApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OpenFeignJwtTest {

    @Resource
    lateinit var openFeignTestJwtClient: OpenFeignTestJwtClient

    private val logger = getLogger()

    @Test
    fun testJwt() {
        val login = openFeignTestJwtClient.login(LoginReq("123", "pwd"))
        logger.info(login.toJsonString())
    }
}
