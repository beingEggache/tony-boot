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

package com.tony.test.wechat

import com.tony.utils.println
import com.tony.utils.toJsonString
import com.tony.wechat.client.WechatClient
import com.tony.wechat.client.req.WechatMenu
import com.tony.wechat.client.req.WechatMenuButton
import com.tony.wechat.client.req.WechatQrCodeActionInfo
import com.tony.wechat.client.req.WechatQrCodeCreateReq
import com.tony.wechat.client.req.WechatQrCodeType
import com.tony.wechat.client.req.WechatScanCodeButton
import com.tony.wechat.client.req.WechatStableAccessTokenReq
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WechatClientTest {

    @Resource
    private lateinit var wechatClient: WechatClient

    @Test
    fun testAccessToken() {
        val accessTokenError = wechatClient.accessToken(
            "qwe",
            "ewq",
            "client_credential"
        )
        val req = WechatStableAccessTokenReq(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68"
        )
        val accessTokenSuccess = wechatClient.stableAccessToken(req)
        accessTokenError.errCode.println()
        accessTokenError.errMsg.println()
        accessTokenError.toJsonString().println()
        accessTokenSuccess.errCode.println()
        accessTokenSuccess.errMsg.println()
        accessTokenSuccess.toJsonString().println()
    }

    @Test
    fun test0() {
        val req = WechatStableAccessTokenReq(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68"
        )
        val accessToken = wechatClient.stableAccessToken(req)
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
        val req = WechatStableAccessTokenReq(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68"
        )
        val accessToken = wechatClient.stableAccessToken(req)
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
            "0114vMFa1Y3BOB0EeAFa1KKY1Q24vMFD"
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
        val req = WechatStableAccessTokenReq(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68"
        )
        val accessToken = wechatClient.stableAccessToken(req)
        val resp = wechatClient.getTicket(
            accessToken.accessToken,
            "jsapi"
        )
        resp.toJsonString().println()
    }

    @Test
    fun test4() {
        val req = WechatStableAccessTokenReq(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68"
        )
        val accessToken = wechatClient.stableAccessToken(req)
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
        val req = WechatStableAccessTokenReq(
            "wx35db161a25e35a89",
            "9715603578fb8ad6cd510b1566642b68"
        )
        val accessToken = wechatClient.stableAccessToken(req)
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
