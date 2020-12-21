package com.tony.wechat.pay.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.XStreamCDataConverter

/**
 * 统一下单请求对象
 */
@XStreamAlias("xml")
data class WechatPayOrderRequest(

    /**
     * ### 公众账号ID
     *     H5支付与公众号支付
     *     微信支付分配的公众账号ID
     *     （企业号corpid即为此appId）
     *
     * ### 应用ID
     *     App支付
     *     微信开放平台审核通过的应用APPID
     *     （请登录open.weixin.qq.com查看，
     *     注意与公众号的APPID不同）
     *
     * ### 小程序ID
     *     微信分配的小程序ID
     */
    @XStreamAlias("appid")
    @JsonProperty("appid")
    var appId: String,

    /**
     * 微信支付分配的商户号
     */
    @XStreamAlias("mch_id")
    @JsonProperty("mch_id")
    var mchId: String,

    /**
     * ## 终端设备号(门店号或收银设备ID)
     * > 默认请传"WEB"
     */
    @XStreamAlias("device_info")
    @JsonProperty("device_info")
    var deviceInfo: String? = null,

    /**
     * ## 随机字符串
     * > 随机字符串，不长于32位。
     *
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3'>随机数生成算法</a>
     */
    @XStreamAlias("nonce_str")
    @JsonProperty("nonce_str")
    var nonceStr: String,

    /**
     * ## 签名
     * > 通过签名算法计算得出的签名值
     *
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3'>签名生成算法</a>
     */
    @XStreamAlias("sign")
    @JsonProperty("sign")
    var sign: String? = null,

    /**
     * ## 签名类型
     * > 目前支持HMAC-SHA256和MD5，默认为MD5
     */
    @XStreamAlias("sign_type")
    @JsonProperty("sign_type")
    var signType: String = "MD5",

    /**
     *
     * ## 商品简单描述
     *
     * > 商品描述交易字段格式根据不同的应用场景按照以下格式： APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     * > String(128)
     *
     *
     *
     * 该字段请按照规范传递，具体请见
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2'>参数规定</a>
     */
    var body: String?,

    /**
     * ## 商品详细描述
     *
     * >对于使用单品优惠的商户，改字段必须按照规范上传，详见
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/danpin.php?chapter=9_102&index=2'>参数规定</a>
     */
    @XStreamConverter(value = XStreamCDataConverter::class)
    var detail: String? = null,

    /**
     * > 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     * > String(127)
     */
    var attach: String? = null,

    /**
     * ## 商户系统内部订单号
     *
     * > 要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2'>商户订单号</a>
     */
    @XStreamAlias("out_trade_no")
    @JsonProperty("out_trade_no")
    var outTradeNo: String?,

    /**
     * ## 货币类型
     * > 符合ISO 4217标准的三位字母代码，默认人民币：CNY
     *
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2'>货币类型</a>
     */
    @XStreamAlias("fee_type")
    @JsonProperty("fee_type")
    var feeType: String = "CNY",

    /**
     * ## 总金额
     * > 订单总金额，单位为分
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2'>支付金额</a>
     */
    @XStreamAlias("total_fee")
    @JsonProperty("total_fee")
    var totalFee: Long?,

    /**
     * ## 终端IP
     * > 用户端实际ip
     */
    @XStreamAlias("spbill_create_ip")
    @JsonProperty("spbill_create_ip")
    var spbillCreateIP: String,

    /**
     * ## 交易起始时间
     *    订单生成时间,
     *    格式为yyyyMMddHHmmss,
     *    如2009年12月25日9点10分10秒表示为
     *    20091225091010。
     *
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2'>时间规则</a>
     */
    @XStreamAlias("time_start")
    @JsonProperty("time_start")
    var timeStart: String? = null,

    /**
     * ## 交易结束时间
     *    订单失效时间,
     *    格式为yyyyMMddHHmmss,
     *    如2009年12月27日9点10分10秒表示为
     *    20091227091010。
     *    订单失效时间是针对订单号而言的，
     *    由于在请求支付的时候有一个必传参数
     *    prepay_id只有两小时的有效期，
     *    所以在重入时间超过2小时的时候需要
     *    重新请求下单接口获取新的prepay_id。
     *
     *    建议：最短失效时间间隔大于1分钟
     *
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2'>时间规则</a>
     */
    @XStreamAlias("time_expire")
    @JsonProperty("time_expire")
    var timeExpire: String? = null,

    /**
     * ## 订单优惠标记
     *    订单优惠标记，代金券或立减优惠功能的参数
     *
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/tools/sp_coupon.php?chapter=12_1'>代金券或立减优惠</a>
     */
    @XStreamAlias("goods_tag")
    @JsonProperty("goods_tag")
    var goodsTag: String? = null,

    /**
     * ## 通知地址
     *    接收微信支付异步通知回调地址，
     *    通知url必须为直接可访问的url，不能携带参数。
     */
    @XStreamAlias("notify_url")
    @JsonProperty("notify_url")
    var notifyUrl: String,

    /**
     * ## 交易类型
     *    JSAPI 公众号支付
     *    NATIVE 扫码支付
     *    APP APP支付
     */
    @XStreamAlias("trade_type")
    @JsonProperty("trade_type")
    var tradeType: String,

    /**
     * ## 商品ID
     *    trade_type=NATIVE时（即扫码支付）,
     *    此参数必传。
     *    此参数为二维码中包含的商品ID，商户自行定义。
     */
    @XStreamAlias("product_id")
    @JsonProperty("product_id")
    var productId: String? = null,

    /**
     * ## 指定支付方式
     *    no_credit--指定不能使用信用卡支付
     */
    @XStreamAlias("limit_pay")
    @JsonProperty("limit_pay")
    var limitPay: String? = null,

    /**
     * ## 场景信息
     *    该字段用于统一下单时上报场景信息，
     *    目前支持上报实际门店信息。
     *    {
     *      "store_id": "", //门店唯一标识，选填，String(32)
     *      "store_name":"" //门店名称，选填，String(64)
     *    }
     */
    @XStreamAlias("scene_info")
    @JsonProperty("scene_info")
    var sceneInfo: String? = null,

    /**
     * ## 用户标识
     *    trade_type=JSAPI，
     *    此参数必传，
     *    用户在商户appid下的唯一标识。
     * @see <a href='https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=4_4'>获取openid</a>
     * @see <span>企业号请使用</span><a href='http://qydev.weixin.qq.com/wiki/index.php?title=OAuth%E9%AA%8C%E8%AF%81%E6%8E%A5%E5%8F%A3'>企业号OAuth2.0接口</a>
     * @see <span>获取企业号内成员userid，再调用</span>
     * <a href='http://qydev.weixin.qq.com/wiki/index.php?title=Userid%E4%B8%8Eopenid%E4%BA%92%E6%8D%A2%E6%8E%A5%E5%8F%A3'>企业号userid转openid接口</a>
     */
    @XStreamAlias("openid")
    @JsonProperty("openid")
    var openId: String? = null
)
