package com.tony.wechat.test

import com.tony.annotation.EnableTonyBoot
import com.tony.utils.println
import com.tony.utils.toJsonString
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import javax.annotation.Resource

@EnableTonyBoot
@EnableFeignClients("com.tony.wechat.test")
@SpringBootTest
class WechatTest2 {

    @Resource
    private lateinit var testWechatClient1: TestWechatClient1

    @Resource
    private lateinit var testWechatClient2: TestWechatClient2

    @Test
    fun testAccessToken() {
        val accessTokenError = testWechatClient1.accessToken(
            "qwe",
            "ewq",
            "client_credential"
        )
        val accessTokenSuccess = testWechatClient1.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        accessTokenError.errCode.println()
        accessTokenError.errMsg.println()
        accessTokenError.toJsonString().println()
        accessTokenSuccess.errCode.println()
        accessTokenSuccess.errMsg.println()
        accessTokenSuccess.toJsonString().println()

        val accessTokenError2 = testWechatClient2.accessToken(
            "qwe",
            "ewq",
            "client_credential"
        )
        val accessTokenSuccess2 = testWechatClient2.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        accessTokenError2.errCode.println()
        accessTokenError2.errMsg.println()
        accessTokenError2.toJsonString().println()
        accessTokenSuccess2.errCode.println()
        accessTokenSuccess2.errMsg.println()
        accessTokenSuccess2.toJsonString().println()
    }

}
