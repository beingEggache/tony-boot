package com.tony.test

import com.quzhu.core.utils.println
import com.quzhu.jwt.config.JwtToken
import com.tony.admin.ApiWebApp
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [ApiWebApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
)
class TestGateway {


    @Test
    fun genToken() {
        JwtToken.gen("userId" to "123").println()
    }
}
