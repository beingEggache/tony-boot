## 概述

`tony-feign` 是 `tony-boot-starters` 体系下基于 Spring Cloud OpenFeign 的增强模块，提供统一响应结构解包、灵活的请求/响应拦截机制、详细请求日志、全局异常处理、二进制/表单上传、OkHttp 支持等能力，助力微服务间高效、规范、安全地进行 HTTP 通信。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
    - [响应拦截与解包](#1-响应拦截与解包)
    - [请求拦截与处理器链](#2-请求拦截与处理器链)
    - [日志与异常处理](#3-日志与异常处理)
    - [文件上传与表单支持](#4-文件上传与表单支持)
    - [OkHttp 支持与高级配置](#5-okhttp-支持与高级配置)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [进阶用法](#进阶用法)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **Spring Cloud OpenFeign 2025.x**

### 添加依赖

```kotlin
dependencies {
    implementation("tony:tony-feign:0.1-SNAPSHOT")
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

### 1. 响应拦截与解包

- **UnwrapResponseInterceptor**：自动解包 `ApiResult` 统一响应结构，只返回 `data` 字段，支持加密响应自动解包。
- **全局响应拦截器**：支持自定义全局响应拦截器，灵活扩展响应处理逻辑。

### 2. 请求拦截与处理器链

- **@RequestProcessors 注解**：支持为 Feign 接口/方法动态指定请求处理器链，实现自动加 token、动态 header、参数加工等。
- **全局请求拦截器**：支持全局注册请求拦截器，统一处理所有 Feign 请求。

### 3. 日志与异常处理

- **FeignLogInterceptor**：详细记录 Feign 请求与响应的时间、头、体、状态码、耗时、远端IP等信息，日志内容结构化，便于排查和监控。支持配置日志体最大长度，防止日志过大。
- **日志脱敏与自定义**：支持自定义 FeignRequestLogger，可实现日志脱敏、格式化、异步落盘等高级处理，保障敏感信息安全。
- **日志分级与异步输出**：日志默认 TRACE 级别输出到 request-logger，支持异步写入，避免主流程阻塞。
- **日志格式与 traceId**：日志包含 traceId，便于全链路追踪，格式与 tony-web 保持一致，便于统一分析。
- **DefaultErrorDecoder**：Feign 调用异常时，自动抛出统一的 `ApiException`，便于全局异常捕获和处理。

### 4. 文件上传与表单支持

- **ByteArrayMultipartFile**：支持二进制文件、表单文件上传，兼容 Spring 的 `MultipartFile`。

### 5. OkHttp 支持与高级配置

- 支持 OkHttp 作为底层 HTTP 客户端，支持连接池、超时、重试、心跳等参数配置。
- 支持自定义 OkHttp 拦截器（如 AppInterceptor、NetworkInterceptor）。

## 配置说明

### Feign 客户端配置

```kotlin
@RequestProcessors(RequestProcessors.Value(AddTokenRequestProcessor::class))
@FeignUnwrapResponse
@FeignClient(name = "demo", url = "http://localhost:10003")
interface DemoClient : DemoApi
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

### 日志配置

```yaml
web:
  log:
    request:
      enabled: true           # 是否记录 Feign 请求日志
      requestBodyMaxSize: 50KB   # 请求体最大日志长度，超出仅显示 ContentType
      responseBodyMaxSize: 50KB  # 响应体最大日志长度，超出仅显示 ContentType
```

- 日志输出到 `request-logger`，可通过 logback 配置异步、分文件、分级输出。
- 日志格式示例：

```text
2025-06-16 15:20:04.406|应用名|F67D3255E9B94E99B9353A51D2BEEEF5|11|20000|OK|http|POST|http://localhost:10000/sys/employee/detail|/sys/employee/detail|abc=123|Origin:http://localhost:10000;;Cookie:...|{  "employeeId": "9253054075269"}|{"success":true,"code":20000,"message":"操作成功","data":{...}}|172.24.0.1|172.24.0.4
```

- 建议敏感信息通过自定义 FeignRequestLogger 实现脱敏。

#### 日志字段说明

| 字段                | 说明                   |
|---------------------|------------------------|
| 时间                | 日志时间戳             |
| 应用名              | 当前服务名             |
| traceId             | 全链路追踪ID           |
| 耗时(ms)            | 请求耗时（毫秒）       |
| 状态码              | HTTP响应码             |
| 状态                | HTTP状态文本           |
| 协议                | http/https             |
| 方法                | 请求方法（GET/POST等） |
| 源地址              | 请求完整URL            |
| 路径                | 请求路径               |
| 查询参数            | URL参数                |
| 请求头              | 请求头部（多对分号分隔）|
| 响应头              | 响应头部（多对分号分隔）|
| 请求体              | 请求体内容             |
| 响应体              | 响应体内容             |
| 客户端IP            | 发起请求的客户端IP      |
| 服务端IP            | 当前服务IP             |

## 使用示例

### 文件上传

```kotlin
@PostMapping("/upload")
fun upload(@RequestPart file: MultipartFile): String

// 客户端调用
val file = ByteArrayMultipartFile("test.txt", byteArray, "file", "text/plain")
client.upload(file)
```

### 自定义全局请求/响应拦截器

```kotlin
@Bean
fun customRequestInterceptorProvider() =
    GlobalRequestInterceptorProvider(MyCustomRequestInterceptor())
```

### 动态请求处理器链

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

### 响应解包与加密解包

```kotlin
@Bean
fun unwrapResponseInterceptorProvider(cryptoProvider: CryptoProvider) =
    UnwrapResponseInterceptorProvider(DefaultUnwrapResponseInterceptor(cryptoProvider))
```

### 自定义错误解码

```kotlin
@Bean
fun errorDecoder(): ErrorDecoder = MyCustomErrorDecoder()
```

## 进阶用法

- 支持自定义 FeignRequestLogger，实现日志脱敏、格式化、异步落盘等高级处理。例如：

```kotlin
@Bean
fun feignRequestLogger(): FeignRequestLogger = MySensitiveFeignRequestLogger()
```

- 日志输出可通过 logback 配置异步、分文件、分级，详见主工程日志配置。
- 支持自定义日志格式、内容、脱敏规则，满足企业安全与合规要求。
- 支持自定义 FeignRequestLogger，实现日志脱敏、格式化、异步落盘等高级处理。
- 支持自定义全局异常体系，细粒度映射 HTTP 状态码与业务异常。
- 支持接口加密传输场景下的自动解密与解包。
- 支持多环境配置与灵活扩展。

## 适用场景

- 微服务间 HTTP 通信
- 需要统一响应结构、自动解包的场景
- 需要灵活请求/响应拦截、日志、异常处理的项目
- 文件/表单上传、接口加密等高级场景

## 注意事项

1. **日志安全**：敏感信息建议脱敏处理，避免泄露。
2. **异常体系**：建议统一异常处理，便于全局监控和定位。
3. **请求处理器链**：如需动态 header/token，建议使用 @RequestProcessors 注解。
4. **加密解包**：如需接口加密，需自定义 CryptoProvider 并配置解包拦截器。
5. **OkHttp 配置**：高并发场景建议合理配置连接池和超时参数。
