package com.tony.wechat.test

import com.tony.annotation.EnableTonyBoot
import com.tony.annotation.redis.RedisCacheable
import com.tony.utils.println
import com.tony.utils.toJsonString
import com.tony.wechat.WechatApiAccessTokenProvider
import com.tony.wechat.WechatManager
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
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.stereotype.Component

@SpringBootTest(classes = [WechatTestApp::class])
class WechatManagerTest {

    @Test
    fun testCreateQrCode() {
        val resp = WechatManager.createQrCode(
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
    fun testUserInfo() {
        val resp = WechatManager.userInfo(
            "o68xis3f2Slo6uhnKdu-VA__iInA",
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun testGetTicket() {

        val resp = WechatManager.getTicket(
            "test"
        )
        resp.toJsonString().println()
    }

    @Test
    fun testCreateMenu() {

        val resp = WechatManager.createMenu(
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
    fun testDeleteMenu() {
        val resp = WechatManager.deleteMenu("test")
        resp.toJsonString().println()
    }

    @Test
    fun testJsCode2Session() {
        val resp = WechatManager.jsCode2Session("0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD", "test")
        resp.toJsonString().println()
    }
}


@EnableFeignClients
@SpringBootApplication
@EnableTonyBoot
class WechatTestApp

@Component
class TestWechatApiAccessTokenProvider(
    override val wechatClient: WechatClient
) : WechatApiAccessTokenProvider {

    @RedisCacheable(cacheKey = wechatTestCacheKey, expressions = ["appId"], expire = 7100)
    override fun accessTokenStr(appId: String?, appSecret: String?, forceRefresh: Boolean) =
        super.accessTokenStr(appId, appSecret, false)

}

const val wechatTestCacheKey = "wechat-accessToken-%s"
