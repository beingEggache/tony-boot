package com.tony.wechat.test

import com.tony.annotation.EnableTonyBoot
import com.tony.utils.println
import com.tony.utils.toJsonString
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatMenuButton
import com.tony.wechat.client.req.WechatQrCodeActionInfo
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.req.WechatQrCodeType
import com.tony.wechat.client.req.WechatScanCodeButton
import com.tony.wechat.service.WechatService
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@EnableTonyBoot
@SpringBootApplication
@SpringBootTest
class WechatServiceTest {

    @Resource
    private lateinit var wechatService: WechatService

    @Test
    fun testAccessToken() {
        val accessTokenSuccess = wechatService.accessToken("test")
        accessTokenSuccess.errCode.println()
        accessTokenSuccess.errMsg.println()
        accessTokenSuccess.toJsonString().println()
    }

    @Test
    fun test0() {
        val resp = wechatService.createQrCode(
            WechatQrCodeCreateReq(
                60,
                WechatQrCodeType.QR_LIMIT_STR_SCENE,
                WechatQrCodeActionInfo("test")
            ),
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test1() {
        val resp = wechatService.userInfo(
            "o68xis3f2Slo6uhnKdu-VA__iInA",
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test2() {
        val accessToken = wechatService.userAccessToken(
            "0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD",
            "test"
        )
        accessToken.toJsonString().println()
        val resp = wechatService.userInfo(
            "o68xis3f2Slo6uhnKdu-VA__iInA",
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test3() {

        val resp = wechatService.getTicket(
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test4() {

        val resp = wechatService.createMenu(
            WechatMenu(
                listOf(
                    WechatMenuButton(
                        "test", listOf(
                            WechatScanCodeButton("testScan", "scan")
                        )
                    )
                )
            ),
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test5() {
        val resp = wechatService.deleteMenu("test")
        resp.toJsonString().println()
    }

    @Test
    fun test6() {
        val resp = wechatService.jsCode2Session("0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD", "test")
        resp.toJsonString().println()
    }
}

