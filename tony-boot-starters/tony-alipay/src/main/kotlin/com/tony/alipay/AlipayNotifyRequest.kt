@file:Suppress("PropertyName", "unused")

package com.tony.alipay

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.tony.exception.BaseException
import com.tony.utils.getLogger
import com.tony.utils.toQueryString

private const val TRADE_SUCCESS = "TRADE_SUCCESS"

private const val TRADE_CLOSED = "TRADE_CLOSED"

@Suppress("MemberVisibilityCanBePrivate")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
public class AlipayNotifyRequest(
    public val appId: String? = null,
    public val authAppId: String? = null,
    public val buyerId: String? = null,
    public val buyerLogonId: String? = null,
    public val buyerPayAmount: String? = null,
    public val charset: String? = null,
    public val fundBillList: String? = null,
    public val gmtCreate: String? = null,
    public val gmtPayment: String? = null,
    public val invoiceAmount: String? = null,
    public val notifyId: String? = null,
    public val notifyTime: String? = null,
    public val notifyType: String? = null,
    public val outTradeNo: String? = null,
    public val passbackParams: String? = null,
    public val pointAmount: String? = null,
    public val receiptAmount: String? = null,
    public val sellerEmail: String? = null,
    public val sellerId: String? = null,
    public val sign: String? = null,
    public val signType: String? = null,
    public val subject: String? = null,
    public val totalAmount: String? = null,
    public val tradeNo: String? = null,
    public val tradeStatus: String? = null,
    public val version: String? = null,
    public val voucherDetailList: String? = null,
) {

    private val logger = getLogger()

    @JvmSynthetic
    public fun process(signValid: Boolean, doOnTradeSuccess: () -> Unit): String {
        if (!signValid) {
            logger.error("Alipay order($outTradeNo) sign invalid, notify request:${toQueryString()}")
            return "success"
        }
        try {
            if (tradeStatus == TRADE_SUCCESS) {
                doOnTradeSuccess()
            } else {
                logger.info("Alipay order($outTradeNo) closed.")
            }
        } catch (e: BaseException) {
            logger.error(e.message)
        }
        return "success"
    }

    public fun process(signValid: Boolean, doOnTradeSuccess: Runnable): String {
        if (!signValid) {
            logger.error("Alipay order($outTradeNo) sign invalid, notify request:${toQueryString()}")
            return "success"
        }
        try {
            if (tradeStatus == TRADE_SUCCESS) {
                doOnTradeSuccess.run()
            } else {
                logger.info("Alipay order($outTradeNo) closed.")
            }
        } catch (e: BaseException) {
            logger.error(e.message)
        }
        return "success"
    }
}
