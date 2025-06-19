## 概述

`tony-web-crypto` 是 `tony-boot-starters` 体系下的 Web 数据加解密模块，专注于为 Spring Boot Web 应用提供请求体解密和响应体加密能力。基于对称加密算法（AES/DES），通过注解驱动的方式，实现对敏感数据的传输保护，确保数据在客户端与服务端之间的安全传输。

---

## 主要功能

### 1. 请求体解密

- **注解驱动**：通过 `@DecryptRequestBody` 注解标记需要解密的接口方法。
- **自动拦截**：`DecryptRequestBodyAdvice` 自动拦截带有注解的请求，对 `@RequestBody` 参数进行解密。
- **JSON 兼容**：自动处理 `application/json` 类型请求中加密字符串的双引号问题。
- **优先级最高**：使用 `PriorityOrdered.HIGHEST_PRECEDENCE` 确保解密在参数绑定前执行。

### 2. 响应体加密

- **注解驱动**：通过 `@EncryptResponseBody` 注解标记需要加密的接口方法。
- **自动处理**：`EncryptResponseBodyAdvice` 自动拦截带有注解的响应，对响应体进行加密。
- **统一结构支持**：对实现 `ApiResultLike` 接口的响应，自动加密 `data` 字段，保持响应结构一致。
- **加密标识**：在响应头中添加 `ENCRYPTED_HEADER_NAME` 标识，便于客户端识别。

### 3. 加密算法支持

- **对称加密**：支持 AES 和 DES 两种对称加密算法。
- **编码方式**：支持多种二进制编码方式（如 Base64），通过 `tony-core` 下的 crypto 功能配置。
- **密钥管理**：通过配置文件统一管理加密密钥和算法参数。

### 4. 可插拔设计

- **自定义 Advice**：支持自定义 `DecryptRequestBodyAdvice` 和 `EncryptResponseBodyAdvice` 实现。
- **条件启用**：通过 `web.crypto.enabled` 配置控制模块是否启用。

### 5. CryptoProvider 统一配置

- **作用**：`CryptoProvider` 是加解密参数（算法、密钥、编码）的统一配置入口，便于在项目中集中管理和切换加密策略。
- **用法**：可通过实现 `CryptoProvider` 接口，灵活指定加密算法、密钥和编码方式，便于在不同环境或业务场景下动态调整。

**典型用法示例：**
```kotlin
import tony.crypto.CryptoProvider
import tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import tony.codec.enums.Encoding

val provider = object : CryptoProvider {
    override val algorithm = SymmetricCryptoAlgorithm.AES
    override val secret = "1234567890123456" // AES密钥
    override val encoding = Encoding.BASE64
}

val encrypted = "hello".encryptToString(provider.algorithm, provider.secret, provider.encoding)
val decrypted = encrypted.decryptToString(provider.algorithm, provider.secret, provider.encoding)
```

---

## 快速开始

### 1. 添加依赖

```kotlin
dependencies {
    implementation("tony:tony-web-crypto:0.1-SNAPSHOT")
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

### 3. 配置加密参数

```yaml
web:
  crypto:
    enabled: true  # 启用加密模块

crypto:
  algorithm: AES  # 加密算法：AES 或 DES
  secret: "your-secret-key"  # 密钥
  encoding: BASE64  # 编码方式
```

### 4. 使用注解

```kotlin
@RestController
class UserController {

    @DecryptRequestBody
    @PostMapping("/user/create")
    fun createUser(@RequestBody user: UserDTO): ApiResult<User> {
        // 请求体会自动解密
        return ApiResult.success(userService.create(user))
    }

    @EncryptResponseBody
    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: String): ApiResult<User> {
        // 响应体会自动加密
        return ApiResult.success(userService.getById(id))
    }
}
```

---

## 注意事项

### 1. 密钥安全

- 确保 `crypto.secret` 密钥长度符合所选加密算法的要求。
- DES 算法要求密钥长度为 8 字节，AES 算法要求密钥长度为 16、24 或 32 字节。
- 生产环境中应使用安全的密钥管理方案。

### 2. 性能考虑

- 加解密操作会增加一定的 CPU 开销，建议只对敏感数据进行加密。
- 可以通过注解精确控制需要加解密的接口，避免不必要的性能损耗。

### 3. 兼容性

- 确保客户端与服务端使用相同的加密算法和密钥。
- 注意处理加密字符串中的特殊字符（如双引号）问题。

---

## 适用场景

- 需要保护敏感数据传输的 Web 应用
- 金融、医疗、政务等对数据安全要求较高的场景
- 前后端分离架构中需要端到端加密的场景
- 需要与第三方系统进行安全数据交换的场景
