package com.tony.wechat.pay.response

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.XStreamCDataConverter

/**
 * ## 支付回调请求对象
 * >  收到微信支付结果通知后，返回该对象给微信
 */
@XStreamAlias("xml")
data class WechatPayNotifyResponse(

    /**
     * ## 返回状态码
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_code")
    var returnCode: String = "SUCCESS",

    /**
     * ## 返回信息
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("return_msg")
    var returnMsg: String = "OK"

)
