## 概述

`tony-interfaces` 是 `tony-boot-starters` 体系中的接口与注解标准模块，聚焦于微服务、Web、Feign、缓存等场景的通用能力抽象。通过统一的接口、注解和拦截器定义，提升各业务模块的解耦性、可扩展性和一致性。

## 主要内容

### 1. Feign 相关接口与注解

- **Feign 拦截器接口**  
  - `RequestProcessor`：自定义 Feign 请求处理器标准接口，支持链式处理、动态参数加工等。
  - `UnwrapResponseInterceptor`：响应抽取拦截器接口，定义统一响应结构的解包标准。

- **Feign 注解**  
  - `@FeignUseGlobalRequestInterceptor`、`@FeignUseGlobalResponseInterceptor`、`@FeignUnwrapResponse`、`@FeignUseGlobalInterceptor`：用于标记 Feign 客户端是否启用全局请求/响应拦截器及解包能力。
  - `@RequestProcessors`：支持为 Feign 接口或方法动态指定多个请求处理器，灵活扩展请求逻辑。

### 2. Web 相关注解

- **加解密注解**  
  - `@DecryptRequestBody`：配合解密 Advice，实现请求体自动解密。
  - `@EncryptResponseBody`：配合加密 Advice，实现响应体自动加密。

- **认证注解**  
  - `@NoLoginCheck`：标记接口或类无需登录校验，常用于开放接口或白名单场景。

### 3. Redis 缓存注解

- `@RedisCacheable`：扩展自 Spring `@Cacheable`，支持缓存键表达式和自定义过期时间。
- `@RedisCacheEvict`：扩展自 Spring `@CacheEvict`，支持批量缓存清理。

### 4. 其他接口

- `ApiSession`：定义了通用的 API 会话能力接口，便于扩展用户上下文、会话管理等场景。

## 适用场景

- 微服务间接口标准化、解耦与扩展
- 统一的 Feign 拦截与注解体系
- Web 层加解密、认证、缓存等通用能力抽象
- 业务模块间共享接口、注解定义

## 示例

```kotlin
// Feign 客户端动态处理器
@RequestProcessors(RequestProcessors.Value(AddTokenRequestProcessor::class))
@FeignUnwrapResponse
@FeignClient(name = "demo")
interface DemoClient

// Web 层加解密
@DecryptRequestBody
@PostMapping("/secure")
fun secureApi(@RequestBody body: String): String

// Redis 缓存
@RedisCacheable(cacheKey = "user:#{#userId}", expire = 3600)
fun getUser(userId: Long): User
``` 