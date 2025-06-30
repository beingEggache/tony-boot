## 概述

`tony-web` 是 `tony-boot-starters` 体系下的 Web 能力增强模块，基于 Spring Boot Web，提供统一响应、全局异常、跨域、结构化日志、全链路追踪、枚举自动转换等企业级 Web 开发能力，极大提升开发效率与规范性。

## 目录
- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [统一响应与包装](#1-统一响应与包装)
  - [全局异常处理](#2-全局异常处理)
  - [跨域与安全配置](#3-跨域与安全配置)
  - [结构化日志与全链路追踪](#4-结构化日志与全链路追踪)
  - [枚举自动转换](#5-枚举自动转换)
  - [其他能力](#6-其他能力)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [进阶用法](#进阶用法)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**

### 添加依赖

```kotlin
dependencies {
    implementation("tony:tony-web:0.1-SNAPSHOT")
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

### 1. 统一响应与包装
- **ApiResult<T> 响应结构**：所有接口默认返回统一结构，包含 code、message、data 字段，便于前后端协作。
- **自动包装**：`WrapResponseBodyAdvice` 自动拦截 Controller 返回值，智能封装为 ApiResult，无需手动包装。
- **Null 值自动处理**：支持 String/集合/Object 类型 null 值自动转空串/空数组/空对象，前端无空指针困扰。
- **可配置白名单**：通过配置可排除特定接口（如文件下载）不自动包装。

### 2. 全局异常处理
- **ExceptionHandler**：捕获所有未处理异常，统一转换为 ApiResult 格式，支持自定义业务异常体系。
- **参数校验友好提示**：对参数类型不匹配、必填校验等异常，自动返回友好错误信息。

### 3. 跨域与安全配置
- **全局跨域**：支持配置 allowed-origins、methods、headers 等，满足多场景需求。
- **请求体重复读取**：支持请求体多次读取，便于日志、参数注入等安全增强。

### 4. 结构化日志与全链路追踪
- **TraceLogFilter**：自动生成 traceId，注入日志上下文，实现全链路追踪。
- **TraceLogger**：结构化日志输出，支持分级、异步、分文件存储。
- **日志格式标准化**：字段丰富，便于自动化分析与监控。
- **请求/响应体大小限制**：防止日志过大，支持最大长度配置。

#### 日志格式示例

```text
2025-06-16 15:20:04.406|应用名|traceId|11|20000|OK|http|POST|http://localhost:10000/sys/employee/detail|/sys/employee/detail|abc=123|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:35;;Content-Type:application/json|Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:F67D3255E9B94E99B9353A51D2BEEEF5;;Content-Type:application/json|{  "employeeId": "9253054075269"}|{"success":true,"code":20000,"message":"操作成功","data":{"employeeId":"9253054075269","account":"sxc001","realName":"孙笑川001","employeeMobile":"13984842001","createTime":"2024-09-19 11:09:32","remark":"","enabled":true,"deptIds":[],"roleIds":[]}}|172.24.0.1|172.24.0.4
2025-06-16 15:20:11.457|应用名|traceId|52|50000|INTERNAL_SERVER_ERROR|http|POST|http://localhost:10000/excel/export|/excel/export|[null]|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:0;;Content-Type:application/x-www-form-urlencoded|X-Error-Code:50000;;Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:7C79A39A4A8A4A1DAE0A522A9C944CBF;;Content-Type:application/json|[null]|{"success":false,"code":50000,"message":"访客太多，请稍后重试","data":{}}|172.24.0.1|172.24.0.4
2025-06-16 15:25:24.816|应用名|traceId|855|20000|OK|http|POST|http://localhost:10000/excel/export|/excel/export|[null]|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:0;;Content-Type:application/x-www-form-urlencoded|Content-Disposition:attachment; filename="test.xlsx";;Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:CE206007BD6A4E5D9ACC70BB2B2A91AD;;Content-Type:application/octet-stream;;Content-Length:3916|[null]|[application/octet-stream]|172.24.0.1|172.24.0.4
```

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

### 5. 枚举自动转换
- **EnumIntValueConverterFactory/EnumStringValueConverterFactory**：支持枚举类型在 Controller 参数、DTO、表单、JSON、缓存、数据库等场景自动转换。
- **与 Redis/MyBatis-Plus 兼容**：序列化/反序列化规则一致，保证数据一致性。
- **静态工厂方法要求**：枚举需实现 Creator 并加 @JsonCreator 注解。

### 6. 其他能力
- **WebContext**：便捷获取当前请求、响应、上下文等。
- **WebSession**：支持会话管理与扩展。
- **Servlets 工具类**：简化常用 Servlet 操作。

## 配置说明

### 基础配置
```yaml
web:
  wrapResponseBodyEnabled: true  # 是否包装返回值
  injectRequestBodyEnabled: true  # 是否注入请求
  wrapResponseExcludePatterns: []  # 包装返回值白名单 URL（ant pattern）
  fillResponseNullValueEnabled: true  # 是否处理响应 JSON null 值
```

### 跨域配置
```yaml
web:
  cors:
    enabled: true          # 启用跨域
    allowed-origins: "*"   # 允许所有来源（生产建议指定域名）
    allowed-methods: "GET,POST,PUT,DELETE"
    allowed-headers: "*"
```

### 日志配置
```yaml
web:
  log:
    trace:
      enabled: true  # 是否记录 trace 日志
      excludePatterns: []  # trace 日志排除 URL
      requestBodyMaxSize: 50KB  # trace 日志请求体最大长度
      responseBodyMaxSize: 50KB  # trace 日志响应体最大长度
```

## 使用示例

### Controller 返回值自动包装
```kotlin
@RestController
@RequestMapping("/api")
class DemoController {
    @GetMapping("/hello")
    fun hello(): Map<String,String> = mapOf("hello" to "world") // 自动包装为 ApiResult
}
```

### 枚举自动转换
```kotlin
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class Status(@get:JsonValue override val value: String) : StringEnumValue {
    ENABLED("enabled"), DISABLED("disabled");

    companion object : StringEnumCreator(Status::class.java) {
        @JvmStatic
        @JsonCreator
        override fun create(value: String): Status? = super.create(value.lowercase())
    }
}
```

## 进阶用法
- 支持自定义异常体系，扩展全局异常处理器。
- 支持自定义日志格式、脱敏、异步落盘等。
- 支持复杂跨域、白名单、请求体多次读取等高级场景。

## 适用场景
- 需要统一响应、异常、日志、跨域、枚举转换的企业级 Web 项目
- 追求高规范、易维护、易扩展的 Spring Boot Web 应用

## 注意事项
1. **日志安全**：敏感信息建议脱敏处理，避免泄露。
2. **异常体系**：建议统一异常处理，便于全局监控和定位。
3. **自动包装白名单**：如需返回原始数据（如文件流），请配置白名单。
4. **枚举转换**：枚举需实现 Creator 并加 @JsonCreator 注解。
