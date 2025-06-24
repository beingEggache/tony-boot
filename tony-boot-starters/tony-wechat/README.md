## 概述

`tony-wechat` 是 `tony-boot-starters` 体系下的微信服务集成模块，提供微信公众号、小程序、微信支付等全场景服务端开发能力。该模块封装了微信官方 API，支持多应用配置、自动 token 管理、支付签名、二维码生成、菜单管理、用户信息获取、消息/支付 XML 处理等功能，极大简化了企业级微信开发。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [多应用配置管理](#1-多应用配置管理)
  - [微信公众号/小程序能力](#2-微信公众号小程序能力)
  - [微信支付能力](#3-微信支付能力)
  - [XML消息处理](#4-xml消息处理)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [进阶用法](#进阶用法)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **其他依赖**: Spring Cloud OpenFeign, XStream

### 添加依赖

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-wechat:0.1-SNAPSHOT")
}
```

### 启用模块

在 Spring Boot 应用主类上添加 `@EnableTonyBoot` 注解：

```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

## 主要功能

### 1. 多应用配置管理

- **核心特性描述**：支持多公众号/小程序/商户号配置，灵活适配多业务场景
- **技术实现**：基于 Spring Boot 自动配置，支持全局配置和子应用配置
- **使用场景**：企业级应用需要管理多个微信应用账号的场景

### 2. 微信公众号/小程序能力

- **签名校验**：一行代码完成微信服务器签名校验
- **用户信息获取**：支持 OAuth2 用户授权、用户基本信息获取
- **二维码生成**：支持公众号、小程序二维码生成
- **菜单管理**：支持自定义菜单创建、删除
- **JS-SDK 配置**：自动生成 JS-SDK 所需签名与参数
- **小程序登录**：支持小程序 code2Session 接口

### 3. 微信支付能力

- **统一下单**：支持 App、H5、小程序等多端微信支付统一下单
- **支付回调验签**：内置支付回调签名校验
- **支付参数生成**：一行代码生成前端所需支付参数
- **退款、转账**：支持退款、企业付款等扩展能力

### 4. XML消息处理

- **消息解析**：内置 XML <-> 对象转换工具
- **消息生成**：支持微信消息、支付通知等 XML 格式数据的生成
- **类型支持**：支持文本、图片、语音、视频、地理位置等多种消息类型

## 配置说明

### 基础配置

```yaml
wechat:
  # 全局配置
  token: your_token
  appId: your_app_id
  appSecret: your_app_secret
  mchId: your_mch_id
  mchSecretKey: your_mch_secret_key

  # 多应用配置
  app:
    app1:
      token: app1_token
      appId: app1_app_id
      appSecret: app1_app_secret
      mchId: app1_mch_id
      mchSecretKey: app1_mch_secret_key
    app2:
      token: app2_token
      appId: app2_app_id
      appSecret: app2_app_secret
      mchId: app2_mch_id
      mchSecretKey: app2_mch_secret_key
```

## 使用示例

### 基础用法

#### 微信签名验证

```kotlin
@RestController
@RequestMapping("/api/wechat")
class WechatController {

    @GetMapping("/verify")
    fun verifySignature(
        @RequestParam signature: String,
        @RequestParam nonce: String,
        @RequestParam timestamp: String,
        @RequestParam(required = false) app: String = ""
    ): ApiResult<Boolean> {
        val isValid = WechatManager.checkSignature(signature, nonce, timestamp, app)
        return ApiResult.success(isValid)
    }
}
```

#### 获取用户信息

```kotlin
@GetMapping("/user/{openId}")
fun getUserInfo(
    @PathVariable openId: String,
    @RequestParam(required = false) app: String = ""
): ApiResult<WechatUserInfoResp> {
    val userInfo = WechatManager.userInfo(openId, app)
    return ApiResult.success(userInfo)
}
```

#### 创建二维码

```kotlin
@PostMapping("/qrcode")
fun createQrCode(
    @RequestBody req: WechatQrCodeCreateReq,
    @RequestParam(required = false) app: String = ""
): ApiResult<WechatQrCodeResp> {
    val qrCode = WechatManager.createQrCode(req, app)
    return ApiResult.success(qrCode)
}
```

#### 小程序登录

```kotlin
@PostMapping("/miniprogram/login")
fun miniProgramLogin(
    @RequestParam code: String,
    @RequestParam(required = false) app: String = ""
): ApiResult<WechatJsCode2SessionResp> {
    val session = WechatManager.jsCode2Session(code, app)
    return ApiResult.success(session)
}
```

### 进阶用法

#### 微信支付统一下单

```kotlin
@PostMapping("/pay/unified-order")
fun unifiedOrder(
    @RequestBody orderRequest: CreateOrderRequest,
    @RequestParam(required = false) app: String = ""
): ApiResult<WechatPayOrderResp> {
    val payReq = WechatPayManager.unifiedOrderInApp(
        outTradeNo = orderRequest.orderNo,
        body = orderRequest.description,
        totalAmount = orderRequest.amount,
        ip = WebContext.request.remoteAddr,
        notifyUrl = "https://yourdomain.com/pay/notify",
        app = app
    )
    return ApiResult.success(payReq)
}
```

#### 支付回调验签

```kotlin
@PostMapping("/pay/notify")
fun payNotify(@RequestBody notifyXml: String): String {
    val notifyReq = notifyXml.xmlToObj<WechatPayNotifyReq>()

    // 验证签名
    val isValid = WechatPayManager.checkSign(notifyReq)
    if (!isValid) {
        return WechatPayNotifyResp.fail("签名验证失败").toXmlString()
    }

    // 处理支付结果
    if (notifyReq.resultCode == "SUCCESS") {
        // 支付成功，更新订单状态
        orderService.updateOrderStatus(notifyReq.outTradeNo, "PAID")
    }

    return WechatPayNotifyResp.success().toXmlString()
}
```

#### 自定义菜单管理

```kotlin
@PostMapping("/menu/create")
fun createMenu(
    @RequestBody menu: WechatMenu,
    @RequestParam(required = false) app: String = ""
): ApiResult<WechatResp> {
    val result = WechatManager.createMenu(menu, app)
    return ApiResult.success(result)
}

@DeleteMapping("/menu")
fun deleteMenu(@RequestParam(required = false) app: String = ""): ApiResult<WechatResp> {
    val result = WechatManager.deleteMenu(app)
    return ApiResult.success(result)
}
```

## 进阶用法

### 自定义扩展

#### 自定义AccessToken提供器

```kotlin
@Component
class CustomWechatApiAccessTokenProvider(
    override val wechatClient: WechatClient
) : WechatApiAccessTokenProvider {

    @RedisCacheable(cacheKey = "wechat-accessToken-%s", expressions = ["appId"], expire = 7100)
    override fun accessTokenStr(appId: String?, appSecret: String?, forceRefresh: Boolean): String? {
        return super.accessTokenStr(appId, appSecret, forceRefresh)
    }
}
```

#### 自定义微信消息处理

```kotlin
@Service
class WechatMessageService {

    fun handleTextMessage(xmlReq: WechatXmlReq): WechatXmlResp {
        return WechatXmlResp(
            toUserName = xmlReq.fromUserName,
            fromUserName = xmlReq.toUserName,
            createTime = System.currentTimeMillis() / 1000,
            msgType = "text",
            content = "收到您的消息：${xmlReq.content}"
        )
    }

    fun handleEventMessage(xmlReq: WechatXmlReq): WechatXmlResp? {
        return when (xmlReq.event) {
            "subscribe" -> {
                WechatXmlResp(
                    toUserName = xmlReq.fromUserName,
                    fromUserName = xmlReq.toUserName,
                    createTime = System.currentTimeMillis() / 1000,
                    msgType = "text",
                    content = "感谢关注！"
                )
            }
            else -> null
        }
    }
}
```

## 适用场景

- **企业级微信公众号开发**：提供完整的公众号开发能力
- **微信小程序服务端**：支持小程序登录、支付等功能
- **微信支付集成**：完整的支付流程和回调处理
- **多应用管理**：支持多个微信应用统一管理
- **消息处理**：微信消息的接收和回复处理

## 注意事项

1. **配置安全**：生产环境请使用环境变量配置敏感信息（appSecret、mchSecretKey等）
2. **IP白名单**：微信支付需要在微信商户平台配置服务器IP白名单
3. **接口限制**：微信接口有调用频率限制，建议做好异常处理和重试机制
4. **HTTPS要求**：生产环境必须使用HTTPS协议
5. **XML处理**：建议使用模块提供的XML工具类进行消息处理
6. **Token管理**：AccessToken会自动缓存，无需手动管理
7. **签名验证**：所有微信回调都必须进行签名验证
8. **错误处理**：微信接口返回的错误码需要妥善处理

