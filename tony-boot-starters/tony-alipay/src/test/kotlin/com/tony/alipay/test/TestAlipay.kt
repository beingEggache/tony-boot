package com.tony.alipay.test

import com.tony.alipay.AlipayNotifyRequest
import com.tony.utils.jsonToObj

fun main() {
    val json = """{ "app_id":"123",
                    "auth_app_id":"",
                    "buyer_id":"",
                    "buyer_logon_id":"",
                    "buyer_pay_amount":"",
                    "charset":"",
                    "fund_bill_list":"",
                    "gmt_create":"",
                    "gmt_payment":"",
                    "invoice_amount":"",
                    "notify_id":"",
                    "notify_time":"",
                    "notify_type":"",
                    "out_trade_no":"",
                    "passback_params":"",
                    "point_amount":"",
                    "receipt_amount":"",
                    "seller_email":"",
                    "seller_id":"",
                    "sign":"",
                    "sign_type":"",
                    "subject":"",
                    "total_amount":"",
                    "trade_no":"",
                    "trade_status":"",
                    "version":"",
                    "voucher_detail_list":""}""".trimIndent()

    val jsonToObj = json.jsonToObj<AlipayNotifyRequest>()
    println(jsonToObj)

}
