## 概述

`tony-aliyun-oss` 是 `tony-boot-starters` 体系下的阿里云对象存储集成模块，专注于为 Spring Boot 应用提供简单、高效的阿里云 OSS 文件上传能力。支持文件上传、元数据管理、URL 生成等核心功能，极大简化了阿里云 OSS 的集成开发。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [文件上传](#1-文件上传)
  - [元数据管理](#2-元数据管理)
  - [URL 生成](#3-url-生成)
  - [路径处理](#4-路径处理)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **阿里云 OSS SDK**: 3.18.2

### 添加依赖

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-aliyun-oss:0.1-SNAPSHOT")
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

### 1. 文件上传

- **核心特性描述**：提供简单易用的文件上传接口，支持 InputStream 和元数据上传
- **技术实现**：基于阿里云 OSS SDK，封装 `putObject` 方法
- **使用场景**：文件上传、图片存储、文档管理等场景

### 2. 元数据管理

- **功能描述**：支持自定义文件元数据，如 Content-Type、Content-Length 等
- **配置选项**：通过 `ObjectMetadata` 对象设置文件属性
- **代码示例**：提供元数据设置的完整示例

### 3. URL 生成

- **高级特性**：自动生成文件的访问 URL，支持 CDN 加速
- **扩展能力**：基于配置的 endpoint 和 bucket 生成标准 URL
- **最佳实践**：推荐使用 HTTPS 协议确保传输安全

### 4. 路径处理

- **路径标准化**：自动处理文件路径，确保路径格式正确
- **安全处理**：防止路径遍历攻击，确保文件存储安全
- **兼容性**：支持不同操作系统的路径分隔符

## 配置说明

### 基础配置

```yaml
aliyun:
  oss:
    access-key-id: "your-access-key-id"
    access-key-secret: "your-access-key-secret"
    bucket-name: "your-bucket-name"
    endpoint: "oss-cn-hangzhou.aliyuncs.com"
```

## 使用示例

### 基础用法

```kotlin
@Service
class FileService {

    // 简单文件上传
    fun uploadFile(file: MultipartFile, path: String): String {
        return AliYunOssManager.putObject(
            path = path,
            name = file.originalFilename ?: "file",
            inputStream = file.inputStream
        )
    }

    // 带元数据的文件上传
    fun uploadFileWithMetadata(
        file: MultipartFile,
        path: String,
        contentType: String
    ): String {
        val metadata = ObjectMetadata().apply {
            this.contentType = contentType
            this.contentLength = file.size
        }

        return AliYunOssManager.putObject(
            path = path,
            name = file.originalFilename ?: "file",
            inputStream = file.inputStream,
            metadata = metadata
        )
    }

    // 上传字节数组
    fun uploadBytes(bytes: ByteArray, path: String, fileName: String): String {
        return AliYunOssManager.putObject(
            path = path,
            name = fileName,
            inputStream = bytes.inputStream()
        )
    }
}
```


## 适用场景

- **文件存储服务**：需要云存储能力的应用系统
- **图片上传**：用户头像、商品图片、内容图片等上传场景
- **文档管理**：PDF、Word、Excel 等文档的云端存储
- **媒体文件**：视频、音频等大文件的存储和分发
- **静态资源**：网站静态资源、CDN 加速等场景

## 注意事项

- **重要提醒1**：请妥善保管阿里云 AccessKey，不要泄露或提交到代码仓库
- **重要提醒2**：生产环境建议使用 RAM 用户的 AccessKey，并设置最小权限
- **重要提醒3**：文件上传前建议进行文件类型和大小验证，防止恶意文件上传
