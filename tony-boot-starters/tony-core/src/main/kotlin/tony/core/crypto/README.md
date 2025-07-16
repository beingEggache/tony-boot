## 概述

`tony-core` 的 `crypto` 包为项目提供了高效、易用的对称加密与解密能力，支持 AES、DES 两种主流对称加密算法，并内置 Base64、Hex 等多种二进制编码方式。通过统一的 `CryptoProvider` 配置接口，便于在各类业务场景下安全地进行数据加解密。

---

## 主要功能

### 1. 对称加密/解密
- 支持 AES（高级加密标准）和 DES（数据加密标准）两种算法。
- 提供统一的加密、解密工具方法，支持字符串与字节数组的互转。
- AES 默认采用 CBC 模式和 PKCS7Padding，内置 IV。

### 2. 编码支持
- 支持 Base64、Hex 等常用二进制编码方式，便于加密结果的安全传输与存储。

### 3. 配置与扩展
- 通过 `CryptoProvider` 接口统一配置加密算法、密钥、编码方式。
- 可灵活扩展更多加密算法和编码实现。

---

## 典型用法

### 1. 直接加解密

```kotlin
import tony.core.crypto.symmetric.SymmetricCryptos.encryptToString
import tony.core.crypto.symmetric.SymmetricCryptos.decryptToString
import tony.core.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import tony.codec.enums.Encoding

val secret = "1234567890123456" // AES密钥需16/24/32字节，DES需8字节
val plainText = "hello, world!"

// 加密为Base64字符串
val encrypted = plainText.encryptToString(
    SymmetricCryptoAlgorithm.AES,
    secret,
    Encoding.BASE64
)

// 解密回明文
val decrypted = encrypted.decryptToString(
    SymmetricCryptoAlgorithm.AES,
    secret,
    Encoding.BASE64
)
```

### 2. 使用CryptoProvider

```kotlin
import tony.core.crypto.CryptoProvider
import tony.core.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import tony.codec.enums.Encoding

val provider = object : CryptoProvider {
    override val algorithm = SymmetricCryptoAlgorithm.DES
    override val secret = "12345678" // DES密钥8字节
    override val encoding = Encoding.HEX
}

val encrypted = "hello".encryptToString(provider.algorithm, provider.secret, provider.encoding)
val decrypted = encrypted.decryptToString(provider.algorithm, provider.secret, provider.encoding)
```

---

## 注意事项

- **密钥长度要求**：
  - AES 密钥长度必须为 16、24 或 32 字节。
  - DES 密钥长度必须为 8 字节。
- **安全建议**：生产环境请使用高强度密钥，并妥善管理密钥，避免硬编码。
- **兼容性**：加密算法、模式、填充方式需与客户端/第三方系统保持一致。

---

## 适用场景

- Web 接口数据加密、解密
- 敏感信息本地加密存储
- 与第三方系统安全数据交换
- 需要自定义加密算法、密钥、编码的业务场景
