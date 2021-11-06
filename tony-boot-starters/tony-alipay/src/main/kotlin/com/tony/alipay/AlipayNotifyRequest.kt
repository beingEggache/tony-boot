@file:Suppress("PropertyName", "unused")

package com.tony.alipay

import com.tony.exception.BaseException
import com.tony.utils.getLogger
import com.tony.utils.toDeepLink

private const val TRADE_SUCCESS = "TRADE_SUCCESS"

private const val TRADE_CLOSED = "TRADE_CLOSED"
@Suppress("VariableNaming", "MemberVisibilityCanBePrivate")
class AlipayNotifyRequest {

    var app_id: String? = null

    var auth_app_id: String? = null

    var buyer_id: String? = null

    var buyer_logon_id: String? = null

    var buyer_pay_amount: String? = null

    var charset: String? = null

    var fund_bill_list: String? = null

    var gmt_create: String? = null

    var gmt_payment: String? = null

    var invoice_amount: String? = null

    var notify_id: String? = null

    var notify_time: String? = null

    var notify_type: String? = null

    var out_trade_no: String? = null

    var passback_params: String? = null

    var point_amount: String? = null

    var receipt_amount: String? = null

    var seller_email: String? = null

    var seller_id: String? = null

    var sign: String? = null

    var sign_type: String? = null

    var subject: String? = null

    var total_amount: String? = null

    var trade_no: String? = null

    var trade_status: String? = null

    var version: String? = null

    var voucher_detail_list: String? = null

    private val logger = getLogger()

    fun process(signValid: Boolean, doOnTradeSuccess: () -> Unit): String {

        if (!signValid) {
            logger.error("alipay order($out_trade_no) sign invalid, notify request:${toDeepLink()}")
            return "success"
        }
        try {
            if (trade_status == TRADE_SUCCESS) {
                doOnTradeSuccess()
            } else {
                logger.info("alipay order($out_trade_no) closed")
            }
        } catch (e: BaseException) {
            logger.error(e.message)
        }
        return "success"
    }
}
