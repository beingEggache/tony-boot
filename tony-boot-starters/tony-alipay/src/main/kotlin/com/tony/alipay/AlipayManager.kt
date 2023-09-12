package com.tony.alipay

import com.alipay.api.AlipayApiException
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.internal.util.AlipaySignature
import com.alipay.api.request.AlipayTradeAppPayRequest
import com.tony.alipay.exception.AlipayException
import com.tony.utils.urlEncode

/**
 * 支付宝 Manager
 * @author Tang Li
 * @date 2023/09/12 10:05
 * @since 1.0.0
 */
@Suppress("unused")
public class AlipayManager(
    private val appId: String,
    private val publicKey: String,
    private val privateKey: String,
    private val aliPayPublicKey: String,
) {

    private val alipayGateway = "https://openapi.alipay.com/gateway.do"

    private val alipayClient by lazy {
        DefaultAlipayClient(alipayGateway, appId, privateKey, "JSON", "utf-8", aliPayPublicKey, "RSA2")
    }

    /**
     * 通知 签名 检查
     * @param [params] params
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/12 10:05
     * @since 1.0.0
     */
    public fun notifySignCheck(params: Map<String, String?>): Boolean {
        val requestAppId = params["app_id"]
        val requestCharset = params["charset"]
        val requestSignType = params["sign_type"]
        if (requestAppId != appId) return false

        return try {
            AlipaySignature.rsaCheckV1(params, aliPayPublicKey, requestCharset, requestSignType)
        } catch (e: AlipayApiException) {
            throw AlipayException(e.message, e)
        }
    }

    /**
     * app下单 并生成支付参数
     * @param [totalAmount] 总额
     * @param [subject] 主题
     * @param [outTradeNo] 订单号
     * @param [notifyURL] 通知url
     * @param [passBackParams] 传回参数
     * @param [body] 请求体
     * @return [String]
     * @author Tang Li
     * @date 2023/09/12 10:05
     * @since 1.0.0
     */
    public fun appOrderAndPayGenParams(
        totalAmount: String,
        subject: String?,
        outTradeNo: String?,
        notifyURL: String,
        passBackParams: String? = null,
        body: String? = null,
    ): String =
        try {
            alipayClient.sdkExecute(
                AlipayTradeAppPayRequest().apply {
                    this.notifyUrl = notifyURL
                    bizModel = AlipayTradeAppPayModel().apply {
                        this.totalAmount = totalAmount
                        this.body = body
                        this.subject = subject
                        this.outTradeNo = outTradeNo
                        this.timeoutExpress = "3m"
                        this.passbackParams = passBackParams?.urlEncode()
                        this.productCode = "QUICK_MSECURITY_PAY"
                    }
                }
            )
        } catch (e: AlipayApiException) {
            throw AlipayException(e.message, e)
        }.body
}
