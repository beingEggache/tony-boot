package com.tony.test

import com.tony.gateway.GatewayWebApp
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [GatewayWebApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
)
class TestGateway {


    @Test
    fun genToken() {
    }
}
