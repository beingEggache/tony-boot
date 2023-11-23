package com.tony.test.feign.module.jwt

import com.tony.test.feign.dto.LoginReq
import com.tony.test.feign.module.jwt.client.FeignTestJwtClient
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "jwt.secret: $secret",
    ],
    classes = [FeignTestJwtApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class FeignJwtTest {

    @Resource
    lateinit var feignTestJwtClient: FeignTestJwtClient

    private val logger = getLogger()

    @Test
    fun testJwt() {
        val login = feignTestJwtClient.login(LoginReq("123", "pwd"))
        logger.info(login.toJsonString())
    }
}
