package com.tony.test.feign.module.jwt

import com.tony.test.feign.dto.LoginReq
import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.jwt.client.FeignJwtTestClient
import com.tony.utils.getLogger
import com.tony.utils.ifNullOrBlank
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "jwt.secret=saoidadsio3jsdfn12323",
        "server.port=9092"
    ],
    classes = [FeignJwtTestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class FeignJwtTest {

    @Resource
    lateinit var feignJwtTestClient: FeignJwtTestClient

    private val logger = getLogger()

    @Test
    fun testJwt() {
        val login = feignJwtTestClient.login(LoginReq("123", "pwd"))
        logger.info(login.toJsonString())
        mockWebAttributes["token"] = login.value.ifNullOrBlank()
        val person = feignJwtTestClient.doAfterLogin(Person(null, null, null, null))
        logger.info(person.toJsonString())
    }
}
