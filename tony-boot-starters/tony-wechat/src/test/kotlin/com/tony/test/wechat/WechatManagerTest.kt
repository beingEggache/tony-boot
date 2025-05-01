/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.test.wechat

import tony.annotation.EnableTonyBoot
import tony.annotation.redis.RedisCacheable
import tony.utils.println
import tony.utils.toJsonString
import tony.wechat.WechatApiAccessTokenProvider
import tony.wechat.WechatManager
import tony.wechat.client.WechatClient
import tony.wechat.client.req.WechatMenu
import tony.wechat.client.req.WechatMenuButton
import tony.wechat.client.req.WechatQrCodeActionInfo
import tony.wechat.client.req.WechatQrCodeCreateReq
import tony.wechat.client.req.WechatQrCodeType
import tony.wechat.client.req.WechatScanCodeButton
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
