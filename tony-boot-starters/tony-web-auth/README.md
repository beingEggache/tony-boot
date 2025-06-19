## 概述

`tony-web-auth` 是 `tony-boot-starters` 体系下的 Web 统一认证模块，专注于为 Spring Boot 应用提供安全、灵活、可扩展的身份认证与登录校验能力。支持 JWT（JSON Web Token）认证、会话扩展、免登录白名单、拦截器自定义等，极大简化了企业级 Web 项目的认证开发。

---

## 主要功能

### 1. 统一登录校验拦截

- 内置 `LoginCheckInterceptor`，自动拦截所有请求，校验用户登录态。
- 支持通过注解 `@NoLoginCheck` 标记无需登录校验的 Controller 或方法。
- 支持配置免登录校验的 URL 白名单（`web.auth.noLoginCheckUrl`）。

### 2. JWT 认证与会话管理

- 默认支持 JWT 认证（需配置 `jwt.secret`），自动解析请求头中的 Token 并校验。
- 提供 `WebSession` 接口，默认实现包括 `JwtWebSession`（JWT模式）和 `NoopWebSession`（无会话模式）。
- 可自定义会话实现，支持多租户、扩展用户信息等。

### 3. 拦截器与会话可插拔

- 支持自定义登录校验拦截器：实现 `LoginCheckInterceptor` 并注入 Spring 容器即可。
- 支持自定义会话实现：实现 `WebSession` 并注入 Spring 容器即可。

### 4. 配置灵活

- 通过配置文件灵活指定免登录校验的 URL。
- 支持与 `tony-jwt`、`tony-core`、`tony-web` 等模块无缝集成。

---

## 快速开始

### 1. 添加依赖

```kotlin
dependencies {
    implementation("tony:tony-web-auth:0.1-SNAPSHOT")
}
```

### 2. 启用模块

在 Spring Boot 应用主类上添加 `@EnableTonyBoot` 注解：

```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

### 3. 配置免登录校验 URL

```yaml
web:
  auth:
    noLoginCheckUrl:
      - "/public/**"
      - "/health"
```

### 4. JWT 配置（如需启用 JWT 认证）

```yaml
jwt:
  secret: "your-jwt-secret"
```

---

## 进阶用法

### 1. 自定义登录校验拦截器

实现 `LoginCheckInterceptor`，并注入 Spring 容器：

```kotlin
@Bean
fun customLoginCheckInterceptor(): LoginCheckInterceptor = object : LoginCheckInterceptor {
    // 自定义 preHandle 逻辑
}
```

### 2. 自定义会话实现

实现 `WebSession`，并注入 Spring 容器：

```kotlin
@Bean
fun customWebSession(): WebSession = object : WebSession {
    override val userId: String get() = // 自定义获取逻辑
    // ...
}
```

### 3. 注解免登录校验

在 Controller 或方法上加 `@NoLoginCheck`，可跳过登录校验：

```kotlin
@NoLoginCheck
@GetMapping("/public/info")
fun publicInfo(): String = "无需登录"
```

---

## 适用场景

- 需要统一登录校验、支持 JWT 的 Spring Boot Web 项目
- 需要灵活扩展认证方式、会话管理的企业级应用
- 需要接口免登录白名单、注解灵活控制的场景

