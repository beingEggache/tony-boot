## 概述

`tony-web-crypto` 是 `tony-boot-starters` 体系下的 Web 数据加解密模块，专注于为 Spring Boot Web 应用提供请求体解密和响应体加密能力。该模块基于对称加密算法（AES/DES），通过注解驱动的方式，实现对敏感数据的传输保护，确保数据在客户端与服务端之间的安全传输。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [请求体解密](#1-请求体解密)
  - [响应体加密](#2-响应体加密)
  - [加密算法支持](#3-加密算法支持)
  - [可插拔设计](#4-可插拔设计)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [进阶用法](#进阶用法)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **其他依赖**: tony-web, tony-core

### 添加依赖

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-web-crypto:0.1-SNAPSHOT")
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

### 1. 请求体解密

- **核心特性描述**：通过 `@DecryptRequestBody` 注解标记需要解密的接口方法，自动拦截并解密请求体
- **技术实现**：基于 Spring Boot 的 `RequestBodyAdvice` 机制，使用最高优先级确保解密在参数绑定前执行
- **使用场景**：需要保护敏感数据传输的 Web 接口

### 2. 响应体加密

- **核心特性描述**：通过 `@EncryptResponseBody` 注解标记需要加密的接口方法，自动加密响应体
- **技术实现**：基于 Spring Boot 的 `ResponseBodyAdvice` 机制，支持统一响应结构的数据字段加密
- **使用场景**：需要保护敏感数据返回的 Web 接口

### 3. 加密算法支持

- **对称加密**：支持 AES 和 DES 两种对称加密算法
- **编码方式**：支持多种二进制编码方式（如 Base64、Hex），通过 `tony-core` 下的 crypto 功能配置
- **密钥管理**：通过 `CryptoProvider` 接口统一管理加密密钥和算法参数

### 4. 可插拔设计

- **自定义 Advice**：支持自定义 `DecryptRequestBodyAdvice` 和 `EncryptResponseBodyAdvice` 实现
- **条件启用**：通过 `web.crypto.enabled` 配置控制模块是否启用
- **扩展能力**：可灵活扩展更多加密算法和编码实现

## 配置说明

### 基础配置

```yaml
web:
  crypto:
    enabled: true  # 启用加密模块
```

### CryptoProvider 配置

```kotlin
@Bean
fun cryptoProvider(): CryptoProvider =
    object : CryptoProvider {
        override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.AES
        override val secret: String = "1234567890123456" // AES密钥需16/24/32字节
        override val encoding: Encoding = Encoding.BASE64
    }
```

## 使用示例

### 基础用法

#### 请求体解密

```kotlin
@RestController
@RequestMapping("/api/user")
class UserController {

    @DecryptRequestBody
    @PostMapping("/create")
    fun createUser(@RequestBody user: UserDTO): User {
        // 请求体会自动解密
        return userService.create(user)
    }
}
```

#### 响应体加密

```kotlin
@EncryptResponseBody
@GetMapping("/{id}")
fun getUser(@PathVariable id: String): User {
    // 响应体会自动加密
    return userService.getById(id)
}
```

#### 同时使用加解密

```kotlin
@DecryptRequestBody
@EncryptResponseBody
@PostMapping("/secure")
fun secureApi(@RequestBody request: SecureRequest): SecureResponse {
    // 请求体自动解密，响应体自动加密
    val response = processSecureData(request)
    return response
}
```

### 进阶用法

#### 自定义加密配置

```kotlin
@Configuration
class CustomCryptoConfig {

    @Bean
    fun customCryptoProvider(): CryptoProvider =
        object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.DES
            override val secret: String = "xvwe23dvxs" // DES密钥8字节
            override val encoding: Encoding = Encoding.HEX
        }
}
```

## 进阶用法

### 自定义扩展

#### 环境变量配置

```kotlin
@Bean
fun cryptoProvider(): CryptoProvider =
    object : CryptoProvider {
        override val algorithm: SymmetricCryptoAlgorithm =
            SymmetricCryptoAlgorithm.valueOf(System.getenv("CRYPTO_ALGORITHM") ?: "AES")
        override val secret: String = System.getenv("CRYPTO_SECRET") ?: "default-secret"
        override val encoding: Encoding =
            Encoding.valueOf(System.getenv("CRYPTO_ENCODING") ?: "BASE64")
    }
```


## 适用场景

- **金融应用**：银行、支付等对数据安全要求极高的场景
- **医疗系统**：患者隐私数据保护
- **政务系统**：敏感政务信息传输
- **企业应用**：商业机密数据保护
- **前后端分离**：需要端到端加密的架构
- **第三方集成**：与外部系统进行安全数据交换

## 注意事项

1. **密钥安全**：生产环境请使用环境变量配置敏感信息，避免硬编码密钥
2. **密钥长度**：DES 算法要求密钥长度为 8 字节，AES 算法要求密钥长度为 16、24 或 32 字节
3. **性能考虑**：加解密操作会增加 CPU 开销，建议只对敏感数据进行加密
4. **兼容性**：确保客户端与服务端使用相同的加密算法和密钥
5. **JSON 处理**：自动处理 `application/json` 类型请求中加密字符串的双引号问题
6. **优先级设置**：解密使用最高优先级，加密使用最低优先级，确保正确的执行顺序
7. **错误处理**：加解密失败时会抛出异常，需要妥善处理
8. **HTTPS 传输**：生产环境必须使用 HTTPS 协议传输加密数据
