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

@file:Suppress("unused")

package tony.alipay

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tony.core.exception.BaseException
import tony.core.utils.getLogger
import tony.core.utils.toQueryString

private const val TRADE_SUCCESS = "TRADE_SUCCESS"

private const val TRADE_CLOSED = "TRADE_CLOSED"

/**
 * 支付宝通知请求
 * @author tangli
 * @date 2023/09/12 19:07
 */
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

    public fun process(
        signValid: Boolean,
        doOnTradeSuccess: Runnable,
    ): String {
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
