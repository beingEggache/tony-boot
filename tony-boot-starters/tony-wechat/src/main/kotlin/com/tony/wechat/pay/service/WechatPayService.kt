package com.tony.wechat.pay.service

import com.tony.exception.ApiException
import com.tony.utils.getLogger
import com.tony.utils.toString
import com.tony.wechat.client.WechatPayClient
import com.tony.wechat.enums.WechatSource
import com.tony.wechat.genMd5UpperCaseSign
import com.tony.wechat.genNonceStr
import com.tony.wechat.genTimeStamp
import com.tony.wechat.pay.config.WechatPayProperties
import com.tony.wechat.pay.enums.TradeType
import com.tony.wechat.pay.req.WechatAppPayReq
import com.tony.wechat.pay.req.WechatMiniProgramPayReq
import com.tony.wechat.pay.req.WechatPayOrderReq
import com.tony.wechat.pay.req.WechatPayReq
import com.tony.wechat.pay.xml.req.WechatPayNotifyReq
import com.tony.wechat.pay.xml.resp.WechatPayOrderResp
import com.tony.wechat.xml.toXmlString
import com.tony.wechat.xml.xmlToObj
import org.apache.commons.codec.digest.DigestUtils
import java.time.LocalDateTime

@Suppress("unused")
class WechatPayService(
    private val wechatProperties: WechatPayProperties,
    private val wechatPayClient: WechatPayClient
) {

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

    private val logger = getLogger()

    fun checkSign(notifyRequest: WechatPayNotifyReq): Boolean {

        val sourceAppId = when (notifyRequest.tradeType) {
            TradeType.APP.name -> wechatProperties.appId
            TradeType.JSAPI.name -> wechatProperties.miniProgramAppId
            else -> ""
        }

        if (notifyRequest.tradeType == TradeType.APP.name && sourceAppId != wechatProperties.appId)
            return false
        else if (notifyRequest.tradeType == TradeType.JSAPI.name && sourceAppId != wechatProperties.miniProgramAppId)
            return false
        if (notifyRequest.mchId != wechatProperties.mchId) return false

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
            "key=${wechatProperties.mchSecretKey}"

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
        WechatAppPayReq(
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
            WechatSource.APP -> wechatProperties.appId
            WechatSource.MINI_PROGRAM -> wechatProperties.miniProgramAppId
        }

    /**
     * 返回统一下单响应对象
     */
    private fun getPayOrderResponse(
        orderRequest: WechatPayOrderReq
    ): WechatPayOrderResp {

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
    ): WechatPayReq {
        val sourceAppId = getAppId(wechatSource)

        val tradeType = getTradeType(wechatSource)

        val orderResponse = getPayOrderResponse(
            WechatPayOrderReq(
                appId = sourceAppId,
                mchId = wechatProperties.mchId,
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
                sign = genMd5UpperCaseSign(this, "key" to wechatProperties.mchSecretKey)
            }
        )

        val prePayId = orderResponse.prePayId
            ?: throw ApiException("${orderResponse.errCode}:${orderResponse.errCodeDes}")

        return when (tradeType) {
            TradeType.APP -> WechatAppPayReq(
                appId = sourceAppId,
                partnerId = wechatProperties.mchId,
                prepayId = prePayId,
                nonceStr = genNonceStr(),
                timestamp = genTimeStamp().toString()
            ).apply {
                sign = genMd5UpperCaseSign(this, "key" to wechatProperties.mchSecretKey)
            }
            TradeType.JSAPI -> WechatMiniProgramPayReq(
                appId = sourceAppId,
                timeStamp = genTimeStamp().toString(),
                nonceStr = genNonceStr(),
                `package` = "prepay_id=$prePayId",
                signType = signType
            ).apply {
                paySign = genMd5UpperCaseSign(this, "key" to wechatProperties.mchSecretKey)
            }
        }
    }
}
