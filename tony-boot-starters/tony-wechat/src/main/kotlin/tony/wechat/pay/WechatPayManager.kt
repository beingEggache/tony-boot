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

package tony.wechat.pay

import java.time.LocalDateTime
import tony.core.SpringContexts
import tony.core.exception.ApiException
import tony.core.utils.getLogger
import tony.core.utils.ifNullOrBlank
import tony.core.utils.md5
import tony.core.utils.toString
import tony.wechat.WechatPropProvider
import tony.wechat.client.WechatPayClient
import tony.wechat.config.WechatProperties
import tony.wechat.genMd5UpperCaseSign
import tony.wechat.genNonceStr
import tony.wechat.genTimeStamp
import tony.wechat.pay.req.WechatAppPayReq
import tony.wechat.pay.req.WechatMiniProgramPayReq
import tony.wechat.pay.req.WechatPayOrderReq
import tony.wechat.pay.req.WechatPayReq
import tony.wechat.pay.xml.req.WechatPayNotifyReq
import tony.wechat.pay.xml.resp.WechatPayOrderResp
import tony.wechat.xml.toXmlString
import tony.wechat.xml.xmlToObj

public data object WechatPayManager {
    @JvmStatic
    private val wechatProperties: WechatProperties by SpringContexts.getBeanByLazy()

    @JvmStatic
    private val wechatPayClient: WechatPayClient by SpringContexts.getBeanByLazy()

    @JvmStatic
    private val wechatPropProvider: WechatPropProvider by SpringContexts.getBeanByLazy()

    //    fun enchashment(openId: String, amount: Long, ip: String): WechatTransferResponse {
//        val charArray = wechatProperties.mchId.toCharArray()
//        val keyStore = KeyStore.getInstance("PKCS12").apply {
//            ClassPathResource("classpath:apiclient_cert.p12").inputStream.use {
//                this.load(it, charArray)
//            }
//        }
//        val sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, charArray).build()
//        val factory = SSLConnectionSocketFactory(sslContext, arrayOf("TLSv1"), null, DefaultHostnameVerifier())
//
//        val builder = HttpClientBuilder.create().setSSLSocketFactory(factory)
//
//        val path = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers"
//
//        val request = WechatTransferRequest(
//            mchAppId = wechatProperties.subscriptionAppId,
//            mchId = wechatProperties.mchId,
//            nonceStr = genNonceStr(),
//            partnerTradeNo = "XLFX${Date().yyyyMMddHHmmssS()}",
//            openId = openId,
//            amount = amount,
//            desc = "提现",
//            spbillCreateIP = ip
//        ).apply {
//            sign = genMD5UpperCaseSign(this, mapOf("key" to mchKey))
//        }
//        val requestXml = request.toXmlString()
//        val httpPost = HttpPost(path).apply {
//            entity = StringEntity(String(requestXml.toByteArray(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
//        }
//
//        val response = builder.build().use {
//            val response = it.execute(httpPost)
//            EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
//
//        }.xmlToObj<WechatTransferResponse>()
//
//        if (response.returnCode != "SUCCESS") {
//            logger.error(response.returnMsg)
//            throw APIException("${response.returnCode}:${response.returnMsg}", 500)
//        }
//        if (response.resultCode != "SUCCESS") {
//            logger.error(response.errCodeDes)
//            throw APIException("${response.errCode}:${response.errCodeDes}", 500)
//        }
//
//        return response
//    }
    @JvmStatic
    private val logger = getLogger()

    @JvmStatic
    public fun checkSign(notifyRequest: WechatPayNotifyReq): Boolean {
        val app = wechatProperties.getAppByAppId(notifyRequest.appId)
        val deepLink =
            "appid=${notifyRequest.appId}&" +
                "bank_type=${notifyRequest.bankType}&" +
                "cash_fee=${notifyRequest.cashFee}&" +
                "fee_type=${notifyRequest.feeType}&" +
                "is_subscribe=${notifyRequest.isSubscribe}&" +
                "mch_id=${notifyRequest.mchId}&" +
                "nonce_str=${notifyRequest.nonceStr}&" +
                "openid=${notifyRequest.openId}&" +
                "out_trade_no=${notifyRequest.outTradeNo}&" +
                "result_code=${notifyRequest.resultCode}&" +
                "return_code=${notifyRequest.returnCode}&" +
                "time_end=${notifyRequest.timeEnd}&" +
                "total_fee=${notifyRequest.totalFee}&" +
                "trade_type=${notifyRequest.tradeType}&" +
                "transaction_id=${notifyRequest.transactionId}&" +
                "key=${wechatPropProvider.mchSecretKey(app.ifNullOrBlank())}"

        return deepLink.md5().uppercase() == notifyRequest.sign
    }

    @JvmStatic
    public fun genPayParams(
        appId: String,
        partnerId: String,
        prepayId: String,
        `package`: String,
        nonceStr: String,
        timestamp: String,
        sign: String,
    ): WechatAppPayReq =
        WechatAppPayReq(
            appId = appId,
            partnerId = partnerId,
            prepayId = prepayId,
            `package` = `package`,
            nonceStr = nonceStr,
            timestamp = timestamp,
            sign = sign
        )

    /**
     * 返回统一下单响应对象
     */
    @JvmStatic
    private fun getPayOrderResponse(orderRequest: WechatPayOrderReq): WechatPayOrderResp {
        val resultStr = wechatPayClient.unifiedOrder(orderRequest.toXmlString())

        val orderResponse = resultStr.xmlToObj<WechatPayOrderResp>()

        if (orderResponse.returnCode != "SUCCESS") {
            logger.error(orderResponse.returnMsg)
            throw ApiException("${orderResponse.returnCode}:${orderResponse.returnMsg}")
        }
        if (orderResponse.resultCode != "SUCCESS") {
            logger.error(orderResponse.errCodeDes)
            throw ApiException("${orderResponse.errCode}:${orderResponse.errCodeDes}")
        }

        return orderResponse
    }

    @JvmStatic
    public fun unifiedOrderInApp(
        outTradeNo: String?,
        body: String?,
        totalAmount: Long?,
        ip: String,
        notifyUrl: String,
        app: String = "",
        detail: String? = null,
        attach: String? = null,
        openId: String? = null,
        signType: String = "MD5",
        timeStart: LocalDateTime? = LocalDateTime.now(),
        timeExpire: LocalDateTime? = timeStart?.plusMinutes(2),
    ): WechatPayReq {
        val appId = wechatPropProvider.appId(app)
        val mchId = wechatPropProvider.mchId(app)
        val orderResponse =
            getPayOrderResponse(
                WechatPayOrderReq(
                    appId = appId,
                    mchId = mchId,
                    nonceStr = genNonceStr(),
                    body = body,
                    detail = detail,
                    attach = attach,
                    signType = signType,
                    outTradeNo = outTradeNo,
                    totalFee = totalAmount,
                    spbillCreateIP = ip,
                    timeStart = timeStart?.toString("yyyyMMddHHmmss"),
                    timeExpire = timeExpire?.toString("yyyyMMddHHmmss"),
                    notifyUrl = notifyUrl,
                    tradeType = "APP",
                    openId = openId
                ).apply {
                    sign = genMd5UpperCaseSign(this, "key" to wechatPropProvider.mchSecretKey(app))
                }
            )

        return WechatAppPayReq(
            appId = appId,
            partnerId = mchId,
            prepayId =
                orderResponse.prePayId
                    ?: throw ApiException("${orderResponse.errCode}:${orderResponse.errCodeDes}"),
            nonceStr = genNonceStr(),
            timestamp = genTimeStamp().toString()
        ).apply {
            sign = genMd5UpperCaseSign(this, "key" to wechatPropProvider.mchSecretKey(app))
        }
    }

    @JvmStatic
    public fun unifiedOrderInJs(
        outTradeNo: String?,
        body: String?,
        totalAmount: Long?,
        ip: String,
        notifyUrl: String,
        app: String = "",
        detail: String? = null,
        attach: String? = null,
        signType: String = "MD5",
        openId: String? = null,
        timeStart: LocalDateTime? = LocalDateTime.now(),
        timeExpire: LocalDateTime? = timeStart?.plusMinutes(2),
    ): WechatPayReq {
        val appId = wechatPropProvider.appId(app)
        val mchId = wechatPropProvider.mchId(app)
        val orderResponse =
            getPayOrderResponse(
                WechatPayOrderReq(
                    appId = appId,
                    mchId = mchId,
                    nonceStr = genNonceStr(),
                    body = body,
                    detail = detail,
                    attach = attach,
                    signType = signType,
                    outTradeNo = outTradeNo,
                    totalFee = totalAmount,
                    spbillCreateIP = ip,
                    timeStart = timeStart?.toString("yyyyMMddHHmmss"),
                    timeExpire = timeExpire?.toString("yyyyMMddHHmmss"),
                    notifyUrl = notifyUrl,
                    tradeType = "JSAPI",
                    openId = openId
                ).apply {
                    sign = genMd5UpperCaseSign(this, "key" to wechatPropProvider.mchSecretKey(app))
                }
            )

        val prePayId =
            orderResponse.prePayId
                ?: throw ApiException("${orderResponse.errCode}:${orderResponse.errCodeDes}")

        return WechatMiniProgramPayReq(
            appId = appId,
            timeStamp = genTimeStamp().toString(),
            nonceStr = genNonceStr(),
            `package` = "prepay_id=$prePayId",
            signType = signType
        ).apply {
            paySign = genMd5UpperCaseSign(this, "key" to wechatPropProvider.mchSecretKey(app))
        }
    }
}
