package com.tony.wechat.pay.service

import com.tony.core.exception.ApiException
import com.tony.core.utils.getLogger
import com.tony.core.utils.toString
import com.tony.http.httpPost
import com.tony.wechat.enums.WechatSource
import com.tony.wechat.genMd5UpperCaseSign
import com.tony.wechat.genNonceStr
import com.tony.wechat.genTimeStamp
import com.tony.wechat.pay.enums.TradeType
import com.tony.wechat.pay.request.WechatAppPayRequest
import com.tony.wechat.pay.request.WechatMiniProgramPayRequest
import com.tony.wechat.pay.request.WechatPayNotifyRequest
import com.tony.wechat.pay.request.WechatPayOrderRequest
import com.tony.wechat.pay.request.WechatPayRequest
import com.tony.wechat.pay.response.WechatPayOrderResponse
import com.tony.wechat.xml.toXmlString
import com.tony.wechat.xml.xmlToObj
import org.apache.commons.codec.digest.DigestUtils
import java.time.LocalDateTime

@Suppress("unused")
class WechatPayService(
    private val appId: String,
    private val miniProgramAppId: String,
    private val subscriptionAppId: String,
    private val mchId: String,
    private val mchKey: String
) {

//    fun enchashment(openId: String, amount: Long, ip: String): WechatTransferResponse {
//        val charArray = mchId.toCharArray()
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
//            mchAppId = subscriptionAppId,
//            mchId = mchId,
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

    private val logger = getLogger()

    private val unifiedOrderPath = "https://api.mch.weixin.qq.com/pay/unifiedorder"

    fun checkSign(notifyRequest: WechatPayNotifyRequest): Boolean {

        val sourceAppId = when (notifyRequest.tradeType) {
            TradeType.APP.name -> appId
            TradeType.JSAPI.name -> miniProgramAppId
            else -> ""
        }

        if (notifyRequest.tradeType == TradeType.APP.name && sourceAppId != appId)
            return false
        else if (notifyRequest.tradeType == TradeType.JSAPI.name && sourceAppId != miniProgramAppId)
            return false
        if (notifyRequest.mchId != mchId) return false

        val deepLink = "appid=$sourceAppId&" +
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
            "key=$mchKey"

        return DigestUtils.md5Hex(deepLink).uppercase() == notifyRequest.sign
    }

    fun genPayParams(
        appId: String,
        partnerId: String,
        prepayId: String,
        `package`: String,
        nonceStr: String,
        timestamp: String,
        sign: String
    ) =
        WechatAppPayRequest(
            appId = appId,
            partnerId = partnerId,
            prepayId = prepayId,
            `package` = `package`,
            nonceStr = nonceStr,
            timestamp = timestamp,
            sign = sign
        )

    private fun getAppId(wechatSource: WechatSource) =
        when (wechatSource) {
            WechatSource.APP -> appId
            WechatSource.MINI_PROGRAM -> miniProgramAppId
        }

    /**
     * 返回统一下单响应对象
     */
    private fun getPayOrderResponse(
        orderRequest: WechatPayOrderRequest
    ): WechatPayOrderResponse {
        val resultStr =
            unifiedOrderPath.httpPost(orderRequest.toXmlString()).string()

        val orderResponse = resultStr.xmlToObj<WechatPayOrderResponse>()

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

    private fun getTradeType(wechatSource: WechatSource) =
        when (wechatSource) {
            WechatSource.APP -> TradeType.APP
            WechatSource.MINI_PROGRAM -> TradeType.JSAPI
        }

    fun unifiedOrder(
        outTradeNo: String?,
        body: String?,
        totalAmount: Long?,
        ip: String,
        notifyUrl: String,
        wechatSource: WechatSource = WechatSource.APP,
        detail: String? = null,
        attach: String? = null,
        signType: String = "MD5",
        openId: String? = null,
        timeStart: LocalDateTime? = LocalDateTime.now(),
        timeExpire: LocalDateTime? = timeStart?.plusMinutes(2)
    ): WechatPayRequest {
        val sourceAppId = getAppId(wechatSource)

        val tradeType = getTradeType(wechatSource)

        val orderResponse = getPayOrderResponse(
            WechatPayOrderRequest(
                appId = sourceAppId,
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
                tradeType = tradeType.name,
                openId = openId
            ).apply {
                sign = genMd5UpperCaseSign(this, "key" to mchKey)
            }
        )

        val prePayId = orderResponse.prePayId
            ?: throw ApiException("${orderResponse.errCode}:${orderResponse.errCodeDes}")

        return when (tradeType) {
            TradeType.APP -> WechatAppPayRequest(
                appId = sourceAppId,
                partnerId = mchId,
                prepayId = prePayId,
                nonceStr = genNonceStr(),
                timestamp = genTimeStamp().toString()
            ).apply {
                sign = genMd5UpperCaseSign(this, "key" to mchKey)
            }
            TradeType.JSAPI -> WechatMiniProgramPayRequest(
                appId = sourceAppId,
                timeStamp = genTimeStamp().toString(),
                nonceStr = genNonceStr(),
                `package` = "prepay_id=$prePayId",
                signType = signType
            ).apply {
                paySign = genMd5UpperCaseSign(this, "key" to mchKey)
            }
        }
    }
}
