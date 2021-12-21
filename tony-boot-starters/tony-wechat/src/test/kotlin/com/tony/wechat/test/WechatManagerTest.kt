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
import com.tony.wechat.WechatManager
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@EnableTonyBoot
@SpringBootApplication
@SpringBootTest
class WechatManagerTest {

    @Resource
    private lateinit var wechatManager: WechatManager

    @Test
    fun testAccessToken() {
        val accessTokenSuccess = wechatManager.accessToken("test")
        accessTokenSuccess.errCode.println()
        accessTokenSuccess.errMsg.println()
        accessTokenSuccess.toJsonString().println()
    }

    @Test
    fun test0() {
        val resp = wechatManager.createQrCode(
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
        val resp = wechatManager.userInfo(
            "o68xis3f2Slo6uhnKdu-VA__iInA",
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test2() {
        val accessToken = wechatManager.userAccessToken(
            "0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD",
            "test"
        )
        accessToken.toJsonString().println()
        val resp = wechatManager.userInfo(
            "o68xis3f2Slo6uhnKdu-VA__iInA",
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test3() {

        val resp = wechatManager.getTicket(
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test4() {

        val resp = wechatManager.createMenu(
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
        val resp = wechatManager.deleteMenu("test")
        resp.toJsonString().println()
    }

    @Test
    fun test6() {
        val resp = wechatManager.jsCode2Session("0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD", "test")
        resp.toJsonString().println()
    }
}

