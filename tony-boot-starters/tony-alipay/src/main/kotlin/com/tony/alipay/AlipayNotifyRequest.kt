@file:Suppress("PropertyName", "unused")

package com.tony.alipay

import com.tony.exception.BaseException
import com.tony.utils.getLogger
import com.tony.utils.toDeepLink

private const val TRADE_SUCCESS = "TRADE_SUCCESS"

private const val TRADE_CLOSED = "TRADE_CLOSED"

@Suppress("VariableNaming", "SameReturnValue", "PropertyName", "MemberVisibilityCanBePrivate")
public class AlipayNotifyRequest {

    public var app_id: String? = null

    public var auth_app_id: String? = null

    public var buyer_id: String? = null

    public var buyer_logon_id: String? = null

    public var buyer_pay_amount: String? = null

    public var charset: String? = null

    public var fund_bill_list: String? = null

    public var gmt_create: String? = null

    public var gmt_payment: String? = null

    public var invoice_amount: String? = null

    public var notify_id: String? = null

    public var notify_time: String? = null

    public var notify_type: String? = null

    public var out_trade_no: String? = null

    public var passback_params: String? = null

    public var point_amount: String? = null

    public var receipt_amount: String? = null

    public var seller_email: String? = null

    public var seller_id: String? = null

    public var sign: String? = null

    public var sign_type: String? = null

    public var subject: String? = null

    public var total_amount: String? = null

    public var trade_no: String? = null

    public var trade_status: String? = null

    public var version: String? = null

    public var voucher_detail_list: String? = null

    private val logger = getLogger()

    public fun process(signValid: Boolean, doOnTradeSuccess: () -> Unit): String {
        if (!signValid) {
            logger.error("Alipay order($out_trade_no) sign invalid, notify request:${toDeepLink()}")
            return "success"
        }
        try {
            if (trade_status == TRADE_SUCCESS) {
                doOnTradeSuccess()
            } else {
                logger.info("Alipay order($out_trade_no) closed.")
            }
        } catch (e: BaseException) {
            logger.error(e.message)
        }
        return "success"
    }
}
