# 概述

`tony-web` 是基于 Spring Boot Web 的核心扩展模块，提供标准化的 Web 开发能力，包括统一响应、全局异常处理、跨域配置、全链路日志追踪、结构化日志输出等企业级特性。模块通过 Kotlin 语法糖简化开发流程，并深度整合 Logback 实现高效日志管理。

## 如何使用

- 依赖 Java 21 或更高版本

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-web:0.1-SNAPSHOT")
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

## 主要功能

### 1. 统一响应处理

- **标准化响应体**：所有接口返回统一格式 `ApiResult<T>`，包含状态码、消息及数据字段。
- **自动包装机制**：`WrapResponseBodyAdvice` 实现控制器返回值的自动封装，无需手动组装响应对象。
- **可配置白名单**：支持配置部分接口不自动包装。

### 2. 全局异常处理

- **全局异常处理器**：`ExceptionHandler` 捕获所有未处理异常，统一转换为 `ApiResult` 格式响应，支持自定义异常与业务异常体系集成。

### 3. 跨域与安全配置

- **统一跨域处理**：`WebConfig` 支持全局跨域配置，可通过配置文件灵活调整。
- **安全增强**：支持请求体重复读取、参数注入等安全能力。

### 4. 统一日志记录与全链路追踪

- **TraceLogFilter**：拦截所有请求，生成唯一追踪 ID（`X-B3-Trace-Id`），注入日志上下文，实现全链路追踪。
- **TraceLogger**：基于 Logback 的结构化日志工具，支持请求链路追踪、分级存储、性能分析。
- **日志分级存储**：INFO、WARN、ERROR、TRACE、FEIGN 等日志分文件异步输出，便于问题定位与性能分析。
- **日志格式标准化**：支持自定义日志模板，字段丰富，便于自动化分析与监控。
- **请求体/响应体大小限制**：防止日志过大，支持配置最大长度。

#### 日志格式示例

```text
2025-06-16 15:20:04.406|tony-api|F67D3255E9B94E99B9353A51D2BEEEF5|11|20000|OK|http|POST|http://localhost:10000/sys/employee/detail|/sys/employee/detail|abc=123|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:35;;Content-Type:application/json|Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:F67D3255E9B94E99B9353A51D2BEEEF5;;Content-Type:application/json|{  "employeeId": "9253054075269"}|{"success":true,"code":20000,"message":"操作成功","data":{"employeeId":"9253054075269","account":"sxc001","realName":"孙笑川001","employeeMobile":"13984842001","createTime":"2024-09-19 11:09:32","remark":"","enabled":true,"deptIds":[],"roleIds":[]}}|172.24.0.1|172.24.0.4
2025-06-16 15:20:11.457|tony-api|7C79A39A4A8A4A1DAE0A522A9C944CBF|52|50000|INTERNAL_SERVER_ERROR|http|POST|http://localhost:10000/excel/export|/excel/export|[null]|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:0;;Content-Type:application/x-www-form-urlencoded|X-Error-Code:50000;;Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:7C79A39A4A8A4A1DAE0A522A9C944CBF;;Content-Type:application/json|[null]|{"success":false,"code":50000,"message":"访客太多，请稍后重试","data":{}}|172.24.0.1|172.24.0.4
2025-06-16 15:25:24.816|tony-api|CE206007BD6A4E5D9ACC70BB2B2A91AD|855|20000|OK|http|POST|http://localhost:10000/excel/export|/excel/export|[null]|Origin:http://localhost:10000;;Cookie:Hm_lvt_eb21166668bf766b9d059a6fd1c10777=1722416148,1723021790,1723085642,1723176832; Idea-ef978a62=3027d474-5a9e-433c-b49d-6ab8bd2e9abb; ASP.NET_SessionId=v54ku32pcuvp4cvypq2sy1fo; Theme=standard;;Request-Origion:Knife4j;;Accept:*/*;;Connection:keep-alive;;Referer:http://localhost:10000/doc.html;;User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0;;Sec-Fetch-Site:same-origin;;Sec-Fetch-Dest:empty;;Host:localhost:10000;;Accept-Encoding:gzip, deflate, br, zstd;;DNT:1;;X-Access-Token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzAxNzAxNDIsImV4cCI6MTc2MTcwNjE0MiwidXNlcklkIjoiOTI1MzA1NDA3NTUyNSJ9.HrV_BkxLU9t-nSjAklefGO1wgT_SozwnVFfNgbN7aHA;;Sec-Fetch-Mode:cors;;sec-ch-ua:"Microsoft Edge";v="137", "Chromium";v="137", "Not/A)Brand";v="24";;sec-ch-ua-mobile:?0;;sec-ch-ua-platform:"Windows";;Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6;;Content-Length:0;;Content-Type:application/x-www-form-urlencoded|Content-Disposition:attachment; filename="test.xlsx";;Vary:Origin,Access-Control-Request-Method,Access-Control-Request-Headers;;X-B3-Trace-Id:CE206007BD6A4E5D9ACC70BB2B2A91AD;;Content-Type:application/octet-stream;;Content-Length:3916|[null]|[application/octet-stream]|172.24.0.1|172.24.0.4

```

- 字段涵盖耗时、结果码、状态、协议、方法、URL、参数、请求/响应头体、客户端IP等，详见下表：

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
| 9        | 请求头         | 键值对以 `;;` 分隔                                                   |
| 10       | 响应头         | 格式同上，响应头信息                                                  |
| 11       | 请求体         | 文本类型显示内容，非文本类型显示媒体类型，超长显示 `[too long]`        |
| 12       | 响应体         | 处理逻辑同请求体，显示响应内容                                         |
| 13       | 客户端IP       | 解析后的客户端IP地址                                                  |

### 5. 其他能力

- **WebContext**：提供便捷的 Web 上下文访问能力。
- **WebSession**：支持会话管理与扩展。
- **Servlets 工具类**：简化常用 Servlet 操作。

### 6. 枚举统一序列化与反序列化

- **Web 层自动转换**：内置 `EnumIntValueConverterFactory`、`EnumStringValueConverterFactory`，支持枚举类型在 Controller 层参数自动转换，无需手动注册转换器。
- **全链路兼容**：无论是请求参数、响应体、表单、JSON、还是缓存、数据库，均可自动识别并正确处理枚举类型，极大简化开发。
- **与 Redis/MyBatis-Plus 一致**：与 tony-redis、tony-mybatis-plus 保持一致的枚举序列化/反序列化规则，保证数据一致性与兼容性。
- **静态工厂方法要求**：每个实现 `IntEnumValue` 或 `StringEnumValue` 的枚举类，必须提供一个继承自 `IntEnumCreator` 或 `StringEnumCreator` 的 `companion object`，并**重写 `create` 方法**，该方法上需加上 `@JsonCreator` 注解，确保 Jackson 能正确调用进行反序列化。
- **配置无感知**：开发者只需实现上述模式，无需关心底层序列化细节。

**常见场景举例：**
- Controller 接口参数、DTO 字段、表单、JSON 请求体/响应体均可直接使用枚举类型，自动完成类型转换。
- 与 Redis、数据库交互时，枚举类型自动序列化为 value，反序列化时自动还原为枚举对象。

**典型用法示例：**
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

## 配置说明

### 1. web

```yaml
web:
  wrapResponseBodyEnabled: true  # 是否包装返回值
  injectRequestBodyEnabled: true  # 是否注入请求
  wrapResponseExcludePatterns: []  # 包装返回值白名单 URL（ant pattern）
  fillResponseNullValueEnabled: true  # 是否处理响应 JSON null 值
```

### 2. 跨域

```yaml
web:
  cors:
    enabled: true          # 启用跨域
    allowed-origins: "*"   # 允许所有来源（生产环境建议指定具体域名）
    allowed-methods: "GET,POST,PUT,DELETE"
    allowed-headers: "*"   # 允许所有请求头
```

### 3. 日志

```yaml
web:
  log:
    trace:
        enabled: true  # 是否记录 trace 日志
        excludePatterns: []  # trace 日志排除 URL
        requestBodyMaxSize: 50KB  # trace 日志请求体最大长度
        responseBodyMaxSize: 50KB  # trace 日志响应体最大长度
```

