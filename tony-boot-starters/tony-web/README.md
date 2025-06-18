
# 概述

`tony-web` 是基于 Spring Boot Web 扩展的核心模块，提供标准化的 Web 开发能力，集成了统一响应处理、异常封装、跨域配置及全链路日志追踪等企业级特性。模块通过 Kotlin 语法糖简化开发流程，同时深度整合 Logback 实现高效日志管理。

## 如何使用
- Java 21 或更高版本


在 `build.gradle.kts` 中添加：
```kotlin
dependencies {
    implementation("tony:tony-web:0.1-SNAPSHOT")
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


## 核心功能特性

### 1. 统一响应处理
- **标准化响应体**：所有接口返回统一格式 `ApiResult<T>`，包含状态码、消息及数据字段
- **自动包装机制**：通过 `WrapResponseBodyAdvice` 实现控制器返回值的自动封装，无需手动组装响应对象


### 2. 异常处理机制
- **全局异常处理器**：捕获所有未处理异常，统一转换为 `ApiResult` 格式响应

### 3. 跨域与安全配置
- **统一跨域处理**：可通过配置文件修改跨域规则


## 统一日志记录
### 1. 日志核心组件
- **`TraceLogFilter`**：拦截请求并生成唯一追踪 ID（`X-B3-Trace-Id`），注入日志上下文
- **`TraceLogger`**：基于 Logback 实现的日志工具，支持请求链路追踪与结构化日志输出

### 2. 日志格式说明
#### 基础日志模板
```xml
<property name="FILE_LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} ${APP_NAME:-[null]} %-5level %logger %X{X-B3-Trace-Id:-[null]} - %msg%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"/>
```
- `%d`：日志时间戳
- `${APP_NAME}`：应用名称（取自 `spring.application.name`）
- `%X{X-B3-Trace-Id}`：请求追踪 ID，用于全链路问题定位
- `%msg`：日志内容，支持自定义参数（如通过 `%ip` 记录客户端 IP）

#### HTTP 追踪日志模板
```xml
<property name="HTTP_TRACE_PATTERN" value="${HTTP_TRACE_PATTERN:-%d{yyyy-MM-dd HH:mm:ss.SSS}|${APP_NAME:-[null]}|%X{X-B3-Trace-Id:-[null]}|%msg|%ip%n}"/>
```
- 额外包含 `%ip` 占位符，通过自定义 `LogIpConversion` 类解析客户端 IP 地址

### 3. 日志分级存储
| 日志级别 | 存储文件               | 策略                                                                 |
|----------|------------------------|----------------------------------------------------------------------|
| **INFO** | `stdout.log`           | 异步输出，按时间及文件大小滚动（默认保留 30 天，单文件 10MB）         |
| **WARN** | `warn.log`             | 仅记录 WARN级别日志，异步处理，存储策略同INFO日志                     |
| **ERROR** | `error.log`            | 记录ERROR及以上级别日志，独立存储，支持快速定位异常场景               |
| **TRACE** | `trace.log`            | 记录请求全链路追踪日志（如接口入参、响应时间），用于性能分析         |
| **FEIGN** | `request.log`          | 单独记录Feign远程调用日志，包含请求URL、参数及响应结果，便于微服务调试 |

### 4. 日志格式定义
%msg为单行文本，字段间以 `|` 分隔，具体格式如下：

```text
[耗时]|
[结果码]|
[结果状态]|
[协议]|
[HTTP方法]|
[原始URL]|
[请求路径]|
[查询参数]|
[请求头]|
[响应头]|
[请求体]|
[响应体]|
[客户端IP]
```

### 5. 字段含义说明

| 字段顺序 | 字段名称       | 说明                                                                 |
|----------|----------------|----------------------------------------------------------------------|
| 1        | 耗时（ms）     | 请求处理的总耗时，单位为毫秒                                          |
| 2        | 结果码         | 结合响应体 `code` 和 HTTP 状态码生成的业务状态码（如 `200`、`404`）   |
| 3        | 结果状态       | 状态文本（如 `OK`、`NOT_FOUND`）                                      |
| 4        | 协议           | 请求协议（`http` 或 `https`）                                         |
| 5        | HTTP方法       | 请求方法（`GET`、`POST`、`PUT` 等）                                   |
| 6        | 原始URL        | `requestURL.toString()` 的结果                                        |
| 7        | 请求路径       | 去除上下文路径后的路径（如 `/api/v1/users`）                          |
| 8        | 查询参数       | `queryString`，无参数时为 `[null]`                                    |
| 9        | 请求头         | 键值对以 `;;` 分隔（如 `Content-Type:application/json;;User-Agent:curl`） |
| 10       | 响应头         | 格式同上，响应头信息                                                  |
| 11       | 请求体         | - 非文本类型显示 `[媒体类型]`<br>- 文本类型且为 POST 时显示内容（超过长度显示 `[too long]`） |
| 12       | 响应体         | 处理逻辑同请求体，显示响应内容                                         |
| 13       | 客户端IP       | 解析后的客户端IP地址                                                  |

### 6. 日志完整示例
```text
2025-06-16 15:20:04.406|tony-api|F67D3255E9B94E99B9353A51D2BEEEF5|11|20000|OK|http|POST|http://localhost:10000/sys/employee/detail|/sys/employee/detail|abc=123|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:35;;Content-Type:application/json|Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:F67D3255E9B94E99B9353A51D2BEEEF5;;Content-Type:application/json|{  "employeeId": "9253054075269"}|{"success":true,"code":20000,"message":"操作成功","data":{"employeeId":"9253054075269","account":"sxc001","realName":"孙笑川001","employeeMobile":"13984842001","createTime":"2024-09-19 11:09:32","remark":"","enabled":true,"deptIds":[],"roleIds":[]}}|172.24.0.1|172.24.0.4
2025-06-16 15:20:11.457|tony-api|7C79A39A4A8A4A1DAE0A522A9C944CBF|52|50000|INTERNAL_SERVER_ERROR|http|POST|http://localhost:10000/excel/export|/excel/export|[null]|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:0;;Content-Type:application/x-www-form-urlencoded|X-Error-Code:50000;;Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:7C79A39A4A8A4A1DAE0A522A9C944CBF;;Content-Type:application/json|[null]|{"success":false,"code":50000,"message":"访客太多，请稍后重试","data":{}}|172.24.0.1|172.24.0.4
2025-06-16 15:25:24.816|tony-api|CE206007BD6A4E5D9ACC70BB2B2A91AD|855|20000|OK|http|POST|http://localhost:10000/excel/export|/excel/export|[null]|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:0;;Content-Type:application/x-www-form-urlencoded|Content-Disposition:attachment; filename="test.xlsx";;Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:CE206007BD6A4E5D9ACC70BB2B2A91AD;;Content-Type:application/octet-stream;;Content-Length:3916|[null]|[application/octet-stream]|172.24.0.1|172.24.0.4
```

### 7. 日志处理逻辑
- **请求体处理**：仅记录 POST 请求的文本类型内容，非文本类型显示媒体类型（如 `[application/octet-stream]`）
- **大小限制**：超过 `requestBodyMaxSize`/`responseBodyMaxSize` 时显示 `[too long content]`
- **结果码生成**：优先取响应体中的 `code`，无则根据 HTTP 状态码映射（如 200 → `ApiProperty.okCode`）
- **状态映射**：将状态码转换为可读文本（如 404 → `NOT_FOUND`）




## 配置说明

### 1. 跨域规则配置
在 `application.yml` 中设置：
```yaml
tony:
  web:
    cors:
      enabled: true          # 启用跨域
      allowed-origins: "*"   # 允许所有来源（生产环境建议指定具体域名）
      allowed-methods: "GET,POST,PUT,DELETE"
      allowed-headers: "*"   # 允许所有请求头
```
