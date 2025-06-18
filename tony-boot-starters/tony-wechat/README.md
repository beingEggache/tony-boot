## 概述
`tony-wechat` 是 `tony-boot-starters` 项目中的一个模块，主要提供微信相关功能，如微信支付、微信公众号开发、微信小程序开发等。它通过集成微信的官方 API，为开发者提供了便捷的微信功能调用方式。

## 如何使用
- Java 21 或更高版本


在 `build.gradle.kts` 中添加：
```kotlin
dependencies {
    implementation("tony:tony-wechat:0.1-SNAPSHOT")
}
```
### 启用 `tony-boot-starters`
在 Spring Boot 应用主类上添加 `@EnableTonyBoot` 注解，以启用 `tony-boot-starters` 的功能：
```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

### 配置微信属性
在 `application.properties` 或 `application.yml` 中配置微信相关属性：
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

### 使用示例

#### 微信签名验证
```kotlin
val signature = "your_signature"
val nonce = "your_nonce"
val timestamp = "your_timestamp"
val app = "your_app"
val isValid = WechatManager.checkSignature(signature, nonce, timestamp, app)
```

#### 获取微信用户信息
```kotlin
val openId = "your_open_id"
val app = "your_app"
val userInfo = WechatManager.userInfo(openId, app)
```

#### 创建微信二维码
```kotlin
val req = WechatQrCodeCreateReq(...)
val app = "your_app"
val qrCode = WechatManager.createQrCode(req, app)
```

## 注意事项
- 请确保你的项目中已经正确配置了微信的相关属性，包括 `appId`、`appSecret` 等。
- 在使用微信 API 时，需要注意 API 的调用频率限制和权限要求。
- 对于微信 XML 数据的处理，建议使用 `tony.wechat.xml` 包下的工具类进行转换。
