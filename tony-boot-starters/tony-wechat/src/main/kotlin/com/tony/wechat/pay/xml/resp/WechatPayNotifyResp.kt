package com.tony.wechat.pay.xml.resp

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.XStreamCDataConverter

/**
 * ## 支付回调请求对象
 * >  收到微信支付结果通知后，返回该对象给微信
 */
@XStreamAlias("xml")
public data class WechatPayNotifyResp(

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
    var returnMsg: String = "OK",

)
