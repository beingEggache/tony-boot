## 概述

`tony-wechat` 是 `tony-boot-starters` 体系下的微信集成模块，支持微信公众号、小程序、微信支付等全场景服务端开发。模块封装了微信官方 API，支持多应用配置、自动 token 管理、支付签名、二维码、菜单、用户信息、消息/支付 XML 处理等，极大简化了企业级微信开发。

---

## 主要功能

### 1. 多应用配置与自动装配

- 支持多公众号/小程序/商户号配置，灵活适配多业务场景。
- 自动装配微信 API 客户端、属性提供器、token 管理器。

### 2. 微信公众号/小程序能力

- **签名校验**：一行代码完成微信服务器签名校验。
- **用户信息获取**：支持 OAuth2 用户授权、用户基本信息获取。
- **二维码生成**：支持公众号、小程序二维码生成。
- **菜单管理**：支持自定义菜单创建、删除。
- **JS-SDK 配置**：自动生成 JS-SDK 所需签名与参数。

### 3. 微信支付能力

- **统一下单**：支持 App、H5、小程序等多端微信支付统一下单。
- **支付回调验签**：内置支付回调签名校验。
- **支付参数生成**：一行代码生成前端所需支付参数。
- **退款、转账**：支持退款、企业付款等扩展能力（如需可自定义扩展）。

### 4. XML 消息/支付处理

- 内置 XML <-> 对象转换工具，支持微信消息、支付通知等 XML 格式数据的解析与生成。

### 5. 典型用法

#### 微信签名验证

```kotlin
val isValid = WechatManager.checkSignature(signature, nonce, timestamp, app)
```

#### 获取用户信息

```kotlin
val userInfo = WechatManager.userInfo(openId, app)
```

#### 创建二维码

```kotlin
val qrCode = WechatManager.createQrCode(req, app)
```

#### 微信支付统一下单

```kotlin
val payReq = WechatPayManager.unifiedOrderInApp(
    outTradeNo = "order123",
    body = "商品描述",
    totalAmount = 100,
    ip = "127.0.0.1",
    notifyUrl = "https://yourdomain.com/pay/notify",
    app = "your_app"
)
```

#### 支付回调验签

```kotlin
val isValid = WechatPayManager.checkSign(notifyRequest)
```

---

## 配置说明

在 `application.yml` 配置微信多应用参数：

```yaml
wechat:
  token: your_token
  appId: your_app_id
  appSecret: your_app_secret
  mchId: your_mch_id
  mchSecretKey: your_mch_secret_key
  app:
    app1:
      token: app1_token
      appId: app1_app_id
      appSecret: app1_app_secret
      mchId: app1_mch_id
      mchSecretKey: app1_mch_secret_key
```

---

## 注意事项

- 请确保已正确配置微信相关参数（如 appId、appSecret、mchId、mchSecretKey）。
- 微信支付需在微信商户平台配置 IP 白名单、API 密钥等。
- 微信接口有调用频率和权限限制，建议做好异常处理与重试机制。
- XML 数据建议使用 `tony.wechat.xml` 包下工具类进行转换。

---

## 适用场景

- 企业级微信公众号、小程序、微信支付服务端开发
- 多应用、多商户号统一接入
- 需要高效、可扩展的微信 API 封装与支付能力的项目

