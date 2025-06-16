## 概述
`tony-web-crypto` 是一个为 Spring Boot Web 应用提供请求体解密和响应体加密功能的模块。它基于对称加密算法，支持 AES 和 DES 两种加密方式，可对请求和响应的数据进行安全处理，保护数据在传输过程中的安全性。

## 功能特性

### 1. 请求体解密
- **注解支持**：通过 `@DecryptRequestBody` 注解，可指定对特定的请求方法的请求体进行解密操作。
- **自动处理**：`DecryptRequestBodyAdvice` 会自动拦截带有 `@DecryptRequestBody` 注解的请求，对请求体进行解密，并将解密后的数据传递给控制器方法。
- **支持 JSON 格式**：对于 `application/json` 类型的请求，会自动处理加密字符串中的双引号问题。

### 2. 响应体加密
- **注解支持**：通过 `@EncryptResponseBody` 注解，可指定对特定的响应方法的响应体进行加密操作。
- **自动处理**：`EncryptResponseBodyAdvice` 会自动拦截带有 `@EncryptResponseBody` 注解的响应，对响应体进行加密，并在响应头中添加 `ENCRYPTED_HEADER_NAME` 标识。
- **支持统一结构**：对于实现了 `ApiResultLike` 接口的响应数据，会对其中的 `data` 字段进行加密。

### 3. 加密算法支持
- **对称加密**：支持 AES 和 DES 两种对称加密算法，可通过 `CryptoProvider` 进行配置。
- **编码支持**：支持多种二进制编码方式，如 Base64 等，可通过 `CryptoProvider` 进行配置。

## 注意事项
- 确保 `CryptoProvider` 中的 `secret` 密钥长度符合所选加密算法的要求，如 DES 算法要求密钥长度为 8 字节。
- 在使用 `@DecryptRequestBody` 和 `@EncryptResponseBody` 注解时，确保请求和响应的数据格式符合要求，否则可能会导致解密或加密失败。
