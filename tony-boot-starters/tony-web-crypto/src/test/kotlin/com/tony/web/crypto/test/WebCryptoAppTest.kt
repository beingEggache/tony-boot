package com.tony.web.crypto.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [TestWebCryptoApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebCryptoAppTest {

    @Test
    fun test() {
        println(123)
    }
}
