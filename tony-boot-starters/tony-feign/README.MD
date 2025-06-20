## 概述

`tony-feign` 基于 Spring Cloud OpenFeign 进行扩展，提供统一响应结构、灵活的请求/响应拦截机制、详细的请求日志、全局异常处理、二进制/表单上传支持等能力，助力微服务间高效、规范、安全地进行 HTTP 通信。

## 目录
- [如何使用](#如何使用)
- [功能特性](#功能特性)
    - [响应拦截器](#1-响应拦截器)
    - [请求拦截器](#2-请求拦截器)
    - [日志记录](#3-日志记录)
    - [全局异常处理](#4-全局异常处理)
    - [文件上传/表单支持](#5-文件上传表单支持)
    - [OkHttp 支持与高级配置](#6-okhttp-支持与高级配置)
    - [其他亮点](#7-其他亮点)
- [主要配置示例](#主要配置示例)
- [进阶用法](#进阶用法)

## 如何使用

- 依赖 Java 21 或更高版本

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-feign:0.1-SNAPSHOT")
}
```

### 启用 `tony-boot-starters`

在 Spring Boot 应用主类上添加 `@EnableTonyBoot` 注解：

```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

## 功能特性

### 1. 响应拦截器

- **解包拦截器（UnwrapResponseInterceptor）**
  自动解包 `tony-core` 的统一响应结构 `ApiResult`，只返回 `data` 字段内容，简化调用端处理。支持加密响应的自动解包。
- **全局响应拦截器**
  支持自定义全局响应拦截器，灵活扩展响应处理逻辑。

### 2. 请求拦截器

- **请求处理器拦截器（UseRequestProcessorsRequestInterceptor）**
  支持通过 `@RequestProcessors` 注解，动态为 Feign 请求添加处理器（如自动加 token、动态 header、参数加工等）。
- **全局请求拦截器**
  支持全局注册请求拦截器，统一处理所有 Feign 请求。

### 3. 日志记录

- **请求日志拦截器（FeignLogInterceptor）**
  详细记录 Feign 请求与响应的时间、头、体等信息，便于排查和监控。支持配置日志体最大长度，防止日志过大。

### 4. 全局异常处理

- **自定义错误解码器（DefaultErrorDecoder）**
  Feign 调用异常时，自动抛出统一的 `ApiException`，便于全局异常捕获和处理。

### 5. 文件上传/表单支持

- **二进制上传（ByteArrayMultipartFile）**
  支持通过 Feign 进行二进制文件、表单文件上传，兼容 Spring 的 `MultipartFile`。

### 6. OkHttp 支持与高级配置

- 支持 OkHttp 作为底层 HTTP 客户端，支持连接池、超时、重试、心跳等参数配置。
- 支持自定义 OkHttp 拦截器（如 AppInterceptor、NetworkInterceptor）。

### 7. 其他亮点

- 支持自定义全局配置（如 feign.config.yml）。
- 支持多环境配置和灵活扩展。
- 代码高度解耦，易于二次开发。

## 主要配置示例

### Feign 客户端示例

```kotlin
@RequestProcessors(RequestProcessors.Value(AddTokenRequestProcessor::class))
@FeignUnwrapResponse
@FeignUseGlobalInterceptor
@FeignClient(name = "feignJwtTestClient", url = "http://localhost:10003")
interface FeignJwtTestClient : FeignJwtTestApi
```

### 响应拦截器配置

```kotlin
@Bean
private fun unwrapResponseInterceptorProvider(
    @Nullable
    cryptoProvider: CryptoProvider?,
) =
    UnwrapResponseInterceptorProvider(DefaultUnwrapResponseInterceptor(cryptoProvider))
```

### OkHttp 配置

`application.yml` 示例：

```yaml
spring:
  cloud:
    openfeign:
      okhttp:
        callTimeout: 0
        connectTimeout: 10000
        readTimeout: 10000
        writeTimeout: 10000
        pingInterval: 10000
        retryOnConnectionFailure: true
        followRedirects: true
```

### 请求日志配置

```yaml
web:
  log:
    request:
      enabled: true
      requestBodyMaxSize: 50KB
      responseBodyMaxSize: 50KB
```

### 文件上传示例

```kotlin
@PostMapping("/upload")
fun upload(@RequestPart file: MultipartFile): String

// 客户端调用
val file = ByteArrayMultipartFile("test.txt", byteArray, "file", "text/plain")
client.upload(file)
```

## 进阶用法

### 1. 自定义全局请求/响应拦截器

你可以实现自定义的 `RequestInterceptor` 或 `ResponseInterceptor`，并通过 `GlobalRequestInterceptorProvider` 或 `GlobalResponseInterceptorProvider` 注册到全局，统一处理所有 Feign 请求或响应。例如：

```kotlin
@Bean
fun customRequestInterceptorProvider() =
    GlobalRequestInterceptorProvider(MyCustomRequestInterceptor())
```

### 2. 动态请求处理器链

通过 `@RequestProcessors` 注解，可以为不同的 Feign 接口或方法动态指定多个请求处理器，实现如动态 Token、灰度流量标记、参数加密等复杂场景。

```kotlin
@RequestProcessors(
    RequestProcessors.Value(
        AddTokenRequestProcessor::class,
        GrayTagRequestProcessor::class
    )
)
@FeignClient(name = "demo")
interface DemoClient
```

### 3. 响应解包与加密解包

支持自定义 `CryptoProvider`，实现响应体的自动解密与解包，适用于接口加密传输场景。

```kotlin
@Bean
fun unwrapResponseInterceptorProvider(cryptoProvider: CryptoProvider) =
    UnwrapResponseInterceptorProvider(DefaultUnwrapResponseInterceptor(cryptoProvider))
```

### 4. 自定义错误解码与异常体系

可自定义实现 `ErrorDecoder`，对不同 HTTP 状态码或响应内容做更细致的异常映射，结合全局异常处理，提升系统健壮性。

```kotlin
@Bean
fun errorDecoder(): ErrorDecoder = MyCustomErrorDecoder()
```

### 5. Feign 日志自定义与脱敏

实现自定义的 `FeignRequestLogger`，可对请求/响应日志进行脱敏、格式化、异步落盘等高级处理。

```kotlin
@Bean
fun feignRequestLogger(): FeignRequestLogger = MySensitiveFeignRequestLogger()
```

### 6. OkHttp 高级特性集成

可通过自定义 OkHttp `AppInterceptor`、`NetworkInterceptor`，实现请求签名、流量染色、链路追踪等功能。

```kotlin
@Bean
fun myAppInterceptor(): AppInterceptor = MyAppInterceptor()
```

### 7. 多环境/多配置文件支持

支持通过 `feign.config.yml`、`application-*.yml` 等多环境配置，灵活切换不同环境下的 Feign 行为和参数。
