## 概述

`tony-jwt` 是 `tony-boot-starters` 体系下的 JWT 认证模块，提供基于 JWT (JSON Web Token) 的身份认证和授权功能。该模块基于 Auth0 的 java-jwt 库实现，提供了简单易用的 JWT 令牌生成、解析和验证功能，支持自定义声明和过期时间配置。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [JWT 令牌生成](#jwt-令牌生成)
  - [JWT 令牌解析](#jwt-令牌解析)
  - [自动配置](#自动配置)
  - [Web 认证集成](#web-认证集成)
- [配置说明](#配置说明)
- [使用示例](#使用示例)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **其他依赖**: Auth0 java-jwt

### 添加依赖

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-jwt:0.1-SNAPSHOT")
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

### JWT 令牌生成

- **核心特性描述**：提供简单易用的 JWT 令牌生成功能，支持自定义声明和过期时间
- **技术实现**：基于 Auth0 java-jwt 库，使用 HMAC256 算法进行签名
- **使用场景**：用户登录成功后生成身份令牌，用于后续请求的身份验证

```kotlin
// 生成包含用户ID的JWT令牌
val token = JwtToken.gen("userId" to "12345")

// 生成包含多个声明的JWT令牌
val token = JwtToken.gen(
    "userId" to "12345",
    "username" to "john.doe",
    "role" to "admin"
)
```

### JWT 令牌解析

- **功能描述**：解析和验证 JWT 令牌的有效性，提取令牌中的声明信息
- **配置选项**：自动使用配置的密钥进行令牌验证
- **代码示例**：支持从令牌中提取用户信息和自定义声明

```kotlin
// 解析JWT令牌
val decodedJWT = JwtToken.parse(token)

// 获取令牌中的声明
val userId = decodedJWT.getClaim("userId").asString()
val username = decodedJWT.getClaim("username").asString()
val issuedAt = decodedJWT.issuedAt
val expiresAt = decodedJWT.expiresAt
```

### 自动配置

- **高级特性**：提供 Spring Boot 自动配置，无需手动配置 Bean
- **扩展能力**：支持自定义 JWT 密钥和过期时间配置
- **最佳实践**：遵循 Spring Boot 配置属性规范，支持环境变量覆盖

### Web 认证集成

- **功能描述**：与 `tony-web-auth` 模块无缝集成，提供完整的 Web 认证解决方案
- **配置选项**：支持配置不需要登录检查的 URL 路径
- **代码示例**：自动处理 JWT 令牌验证和用户身份提取

## 配置说明

### 基础配置

```yaml
jwt:
  # JWT签名密钥（必填）
  secret: "your-jwt-secret-key"
  # JWT令牌过期时间（分钟），默认一年
  expired-minutes: 525600
```

## 使用示例

### 基础用法

#### 登录接口生成令牌

```kotlin
@RestController
@RequestMapping("/api/auth")
class AuthController {

    @PostMapping("/login")
    @NoLoginCheck
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        // 验证用户名密码
        val user = userService.authenticate(loginRequest.username, loginRequest.password)

        // 生成JWT令牌
        val token = JwtToken.gen(
            "userId" to user.id,
            "username" to user.username,
            "role" to user.role
        )

        return LoginResponse(token, user)
    }
}
```
