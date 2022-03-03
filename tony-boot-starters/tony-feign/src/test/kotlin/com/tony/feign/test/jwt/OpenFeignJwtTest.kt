package com.tony.feign.test.jwt

import com.tony.feign.test.jwt.client.OpenFeignTestJwtClient
import com.tony.feign.test.jwt.dto.LoginReq
import com.tony.utils.getLogger
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@SpringBootTest(
    classes = [OpenFeignTestJwtApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OpenFeignJwtTest {

    @Resource
    lateinit var openFeignTestJwtClient: OpenFeignTestJwtClient

    private val logger = getLogger()

    @Test
    fun testJwt() {
        val login = openFeignTestJwtClient.testLogin(LoginReq("123", "pwd"))
        logger.info(login)
    }
}
