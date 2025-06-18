## 概述
`tony-feign` 扩展 Feign ，提供统一的响应、请求拦截器处理机制、日志记录。

## 如何使用
- Java 21 或更高版本


在 `build.gradle.kts` 中添加：
```kotlin
dependencies {
    implementation("tony:tony-feign:0.1-SNAPSHOT")
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

## 功能特性

### 1. 响应拦截器
#### 1.1 解包拦截器（`UnwrapResponseInterceptor`）
- **功能**：读取 `tony-core` 里全局统一结构 `ApiResult` 里的 `Data`，与 `tony-web` 里的 `WrapResponseBodyAdvice` 包装成统一结构对应。当响应为 JSON 且符合特定格式时，自动抽取具体的返回数据，简化调用方的处理逻辑。
- **实现类**：`DefaultUnwrapResponseInterceptor`
- **配置**：通过 `UnwrapResponseInterceptorProvider` 进行配置，可根据需要传入 `CryptoProvider` 以支持加密响应的解包。

#### 1.2 全局响应拦截器
- **功能**：避免自动注册，通过 `GlobalResponseInterceptorProvider` 提供全局响应拦截器的管理。

### 2. 请求拦截器
#### 2.1 请求处理器拦截器（`UseRequestProcessorsRequestInterceptor`）
- **功能**：根据 `RequestProcessors` 注解，动态应用请求处理器对请求进行处理，如添加请求头、修改请求参数等。
- **使用方式**：在 Feign 客户端接口上使用 `@RequestProcessors` 注解指定请求处理器。

#### 2.2 全局请求拦截器
- **功能**：避免自动注册，通过 `GlobalRequestInterceptorProvider` 提供全局请求拦截器的管理。

### 3. 日志记录
#### 3.1 请求日志拦截器（`FeignLogInterceptor`）
- **功能**：记录 Feign 请求的详细信息，包括请求时间、响应时间、请求头、响应头、请求体、响应体等，方便调试和监控。
- **配置**：可通过配置 `requestBodyMaxSize` 和 `responseBodyMaxSize` 控制日志中请求体和响应体的显示长度。


## 使用示例

### 1. 配置 Feign 客户端
```kotlin
@RequestProcessors(RequestProcessors.Value(AddTokenRequestProcessor::class))
@FeignUnwrapResponse
@FeignUseGlobalInterceptor
@FeignClient(name = "feignJwtTestClient", url = "http://localhost:10003")
interface FeignJwtTestClient : FeignJwtTestApi
```

### 2. 配置响应拦截器
```kotlin
@Bean
private fun unwrapResponseInterceptorProvider(
    @Nullable
    cryptoProvider: CryptoProvider?,
) =
    UnwrapResponseInterceptorProvider(DefaultUnwrapResponseInterceptor(cryptoProvider))
```
### OkHttp 配置
OkHttp 客户端的连接参数，可在 `application.yml` 中进行配置：

```yaml
spring:
  cloud:
    openfeign:
      okhttp:
        callTimeout: 0  # 调用超时时间（秒）
        connectTimeout: 10000  # 连接超时时间（秒）
        readTimeout: 10000  # 读取超时时间（秒）
        writeTimeout: 10000  # 写入超时时间（秒）
        pingInterval: 10000  # 心跳间隔时间（秒）
        retryOnConnectionFailure: true  # 是否在连接失败时重试
        followRedirects: true  # 是否跟随重定向
```

### 请求日志 配置
Feign 请求日志的相关属性，可在 `application.yml` 中进行配置：

```yaml
web:
  log:
    request:
      enabled: true  # 是否记录请求日志
      requestBodyMaxSize: 50KB  # 请求体最大记录长度，超过只显示 ContentType
      responseBodyMaxSize: 50KB  # 响应体最大记录长度，超过只显示 ContentType
```
