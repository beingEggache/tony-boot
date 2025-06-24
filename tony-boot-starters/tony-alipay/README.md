## 概述

`tony-alipay` 是 `tony-boot-starters` 体系下的支付宝支付集成模块，专注于为 Spring Boot 应用提供安全、便捷的支付宝支付能力。支持 APP 支付、异步通知处理、签名验证等核心功能，极大简化了支付宝支付的集成开发。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [APP 支付下单](#1-app-支付下单)
  - [异步通知处理](#2-异步通知处理)
  - [签名验证](#3-签名验证)
  - [异常处理](#4-异常处理)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **支付宝 SDK**: 4.40.251.ALL

### 添加依赖

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-alipay:0.1-SNAPSHOT")
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

### 1. APP 支付下单

- **核心特性描述**：提供 APP 支付下单接口，自动生成支付参数，支持自定义订单信息
- **技术实现**：基于支付宝官方 SDK，封装 `AlipayTradeAppPayRequest` 接口
- **使用场景**：移动端 APP 支付场景，生成支付参数供客户端调用

### 2. 异步通知处理

- **功能描述**：处理支付宝异步通知，自动验证签名，支持交易状态处理
- **配置选项**：支持自定义通知处理逻辑，灵活配置成功/失败回调
- **代码示例**：提供标准化的通知处理流程

### 3. 签名验证

- **高级特性**：自动验证支付宝通知签名，确保数据安全性和完整性
- **扩展能力**：支持 RSA2 签名算法，兼容支付宝官方签名规范
- **最佳实践**：推荐在通知处理前进行签名验证

### 4. 异常处理

- **统一异常体系**：提供 `AlipayException` 异常类，继承自 `ApiException`
- **错误码管理**：支持自定义错误码和错误信息
- **异常链传递**：保留原始异常信息，便于问题排查

## 配置说明

### 基础配置

```yaml
alipay:
  app-id: "your-app-id"
  public-key-path: "classpath:alipay/public-key.pem"
  private-key-path: "classpath:alipay/private-key.pem"
  ali-public-key-path: "classpath:alipay/ali-public-key.pem"
```

## 使用示例

### 基础用法

```kotlin
@Service
class PaymentService(
    private val alipayManager: AlipayManager
) {

    // APP 支付下单
    fun createAppOrder(order: Order): String {
        return alipayManager.appOrderAndPayGenParams(
            totalAmount = order.amount.toString(),
            subject = order.title,
            outTradeNo = order.orderNo,
            notifyURL = "https://your-domain.com/api/pay/notify",
            passBackParams = order.orderNo,
            body = order.description
        )
    }

    // 处理异步通知
    fun handleNotify(params: Map<String, String?>): String {
        // 验证签名
        val signValid = alipayManager.notifySignCheck(params)

        // 转换为通知对象
        val notifyRequest = AlipayNotifyRequest(
            appId = params["app_id"],
            outTradeNo = params["out_trade_no"],
            tradeStatus = params["trade_status"],
            totalAmount = params["total_amount"],
            tradeNo = params["trade_no"]
        )

        // 处理通知
        return notifyRequest.process(signValid) {
            // 支付成功处理逻辑
            orderService.updateOrderStatus(notifyRequest.outTradeNo!!, "PAID")
        }
    }
}
```


## 适用场景

- **移动端 APP 支付**：需要集成支付宝 APP 支付的应用
- **异步通知处理**：需要处理支付宝支付结果通知的场景
- **支付安全验证**：对支付数据安全性有较高要求的业务
- **统一支付接口**：需要统一管理多种支付方式的系统

## 注意事项

- **重要提醒1**：请妥善保管支付宝私钥，不要泄露或提交到代码仓库
- **重要提醒2**：生产环境必须使用 HTTPS 协议，确保数据传输安全
- **重要提醒3**：异步通知处理必须返回 "success" 字符串，否则支付宝会重复发送通知
