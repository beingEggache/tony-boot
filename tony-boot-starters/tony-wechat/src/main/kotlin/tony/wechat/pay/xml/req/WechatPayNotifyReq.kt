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

package tony.wechat.pay.xml.req

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import java.util.function.Consumer
import tony.core.exception.BaseException
import tony.core.utils.getLogger
import tony.wechat.pay.xml.resp.WechatPayNotifyResp
import tony.wechat.xml.XStreamCDataConverter
import tony.wechat.xml.toXmlString

/**
 * ## 支付回调请求对象
 * >  客户端支付成功后，微信会请求我方设置的notifyURL，请求体就是该对象
 */
@XStreamAlias("xml")
public data class WechatPayNotifyReq(
    /**
     * ## 返回状态码
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_code")
    var returnCode: String,
    /**
     * ## 返回信息
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_msg")
    var returnMsg: String,
    /**
     * ## 应用ID
     *    微信开放平台审核通过的应用APPID
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("appid")
    var appId: String,
    /**
     * ## 商户号
     *    微信支付分配的商户号
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("mch_id")
    var mchId: String,
    /**
     * ## 设备号
     *    微信支付分配的终端设备号
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("device_info")
    var deviceInfo: String,
    /**
     * ## 随机字符串
     *    随机字符串，不长于32位
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("nonce_str")
    var nonceStr: String,
    /**
     * ## 签名
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    var sign: String,
    /**
     * ## 业务结果
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("result_code")
    var resultCode: String,
    /**
     * ## 错误代码
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("err_code")
    var errCode: String,
    /**
     * ## 错误代码描述
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("err_code_des")
    var errCodeDes: String,
    /**
     * ## 用户标识
     * > 用户在商户appid下的唯一标识
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("openid")
    var openId: String,
    /**
     * ## 是否关注公众账号
     *    用户是否关注公众账号，仅在公众账号类型支付有效
     *    Y-关注，N-未关注
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("is_subscribe")
    var isSubscribe: String?,
    /**
     * ## 交易类型
     *    JSAPI 公众号支付
     *    NATIVE 扫码支付
     *    APP APP支付
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("trade_type")
    var tradeType: String,
    /**
     * ## 付款银行
     *    银行类型，采用字符串类型的银行标识
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2">银行列表</a>
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("bank_type")
    var bankType: String,
    /**
     * ## 总金额
     *    订单总金额，单位为分
     */
    @XStreamAlias("total_fee")
    var totalFee: Long,
    /**
     * ## 货币种类
     *    货币类型，符合ISO4217标准的三位字母代码。
     *    默认人民币：CNY
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2">货币类型</a>
     */
    @XStreamAlias("fee_type")
    var feeType: String?,
    /**
     * ## 现金支付金额
     *    现金支付金额订单现金支付金额
     */
    @XStreamAlias("cash_fee")
    var cashFee: Long,
    /**
     * ## 现金支付货币类型
     *    货币类型，符合ISO4217标准的三位字母代码。
     *    默认人民币：CNY
     */
    @XStreamAlias("cash_fee_type")
    var cashFeeType: String?,
    /**
     * ## 微信支付订单号
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("transaction_id")
    var transactionId: String,
    /**
     * ## 商户订单号
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("out_trade_no")
    var outTradeNo: String,
    /**
     * ## 商家数据包
     *    商家数据包，原样返回
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    var attach: String,
    /**
     * ## 支付完成时间
     *    支付完成时间，
     *    格式为yyyyMMddHHmmss，
     *    如2009年12月25日9点10分10秒表示为20091225091010。
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("time_end")
    var timeEnd: String,
) {
    public companion object {
        private val logger = getLogger()

        @JvmStatic
        public fun process(
            notifyRequest: WechatPayNotifyReq,
            signValid: Boolean,
            doOnSuccess: Runnable,
            doOnFailed: Consumer<String>,
        ): String {
            if (!signValid) {
                logger.error(
                    "wechat pay order ${notifyRequest.outTradeNo} " +
                        "sign invalid,notify request:${notifyRequest.toXmlString()}"
                )
                return WechatPayNotifyResp().toXmlString()
            }
            try {
                if (notifyRequest.returnCode == "SUCCESS" && notifyRequest.resultCode == "SUCCESS") {
                    doOnSuccess.run()
                } else {
                    val errorDetail = "${notifyRequest.errCode}:${notifyRequest.errCodeDes}"
                    doOnFailed.accept(errorDetail)
                    logger.error("Wechat pay order(${notifyRequest.outTradeNo}) error:$errorDetail")
                }
            } catch (e: BaseException) {
                logger.error(e.message)
            }
            return WechatPayNotifyResp().toXmlString()
        }
    }
}
