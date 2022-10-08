package com.tony.wechat.pay.xml.resp

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.XStreamCDataConverter

/**
 * 统一下单响应对象
 */
@XStreamAlias("xml")
public data class WechatPayOrderResp(

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_code")
    var returnCode: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_msg")
    var returnMsg: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("appid")
    var appId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("mch_id")
    var mchId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("nonce_str")
    var nonceStr: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("openid")
    var openId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("sign")
    var sign: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("result_code")
    var resultCode: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("err_code")
    var errCode: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("err_code_des")
    var errCodeDes: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("trade_type")
    var tradeType: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("prepay_id")
    var prePayId: String?,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("mweb_url")
    var webURL: String?
)
