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
