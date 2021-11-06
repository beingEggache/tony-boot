package com.tony.wechat.test

import com.tony.annotation.EnableTonyBoot
import com.tony.utils.println
import com.tony.utils.toJsonString
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatMenuButton
import com.tony.wechat.client.req.WechatQrCodeActionInfo
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.req.WechatQrCodeType
import com.tony.wechat.client.req.WechatScanCodeButton
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@EnableTonyBoot
@SpringBootApplication
@SpringBootTest
class WechatTest {

    @Resource
    private lateinit var wechatClient: WechatClient

    @Test
    fun testAccessToken() {
        val accessTokenError = wechatClient.accessToken(
            "qwe",
            "ewq",
            "client_credential"
        )
        val accessTokenSuccess = wechatClient.accessToken(
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
    }

    @Test
    fun test0() {
        val accessToken = wechatClient.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        val resp = wechatClient.createQrCode(
            WechatQrCodeCreateReq(
                60,
                WechatQrCodeType.QR_LIMIT_STR_SCENE,
                WechatQrCodeActionInfo("test")
            ), accessToken.accessToken
        )
        resp.toJsonString().println()
    }

    @Test
    fun test1() {
        val accessToken = wechatClient.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        val resp = wechatClient.userInfo(
            accessToken.accessToken,
            "o68xis3f2Slo6uhnKdu-VA__iInA"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test2() {
        val accessToken = wechatClient.userAccessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD",
            "authorization_code"
        )
        accessToken.toJsonString().println()
        val resp = wechatClient.userInfo(
            accessToken.accessToken,
            "o68xis3f2Slo6uhnKdu-VA__iInA"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test3() {
        val accessToken = wechatClient.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        accessToken.toJsonString().println()
        val resp = wechatClient.getTicket(
            accessToken.accessToken,
            "jsapi"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test4() {
        val accessToken = wechatClient.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        accessToken.toJsonString().println()
        val resp = wechatClient.createMenu(
            accessToken.accessToken,
            WechatMenu(
                listOf(
                    WechatMenuButton(
                        "test", listOf(
                            WechatScanCodeButton("testScan", "scan")
                        )
                    )
                )
            )
        )
        resp.toJsonString().println()
    }

    @Test
    fun test5() {
        val accessToken = wechatClient.accessToken(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "client_credential"
        )
        accessToken.toJsonString().println()
        val resp = wechatClient.deleteMenu(
            accessToken.accessToken
        )
        resp.toJsonString().println()
    }

    @Test
    fun test6() {
        val resp = wechatClient.jsCode2Session(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68",
            "authorization_code",
            "0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD"
        )
        resp.toJsonString().println()
    }
}
