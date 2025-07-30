## 概述

`tony-knife4j-api` 是 `tony-boot-starters` 体系下的 API 文档增强模块，基于 `Knife4j` 和 `SpringDoc`，提供开箱即用的 API 文档界面、自动响应结构包装、统一枚举处理、属性扁平化展示等企业级 API 文档解决方案，极大提升开发体验与接口可读性。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [开箱即用的 API 文档界面](#1-开箱即用的-api-文档界面)
  - [自动响应结构包装](#2-自动响应结构包装)
  - [统一枚举处理](#3-统一枚举处理)
  - [属性扁平化展示](#4-属性扁平化展示)
  - [文件下载支持](#5-文件下载支持)
- [配置说明](#配置说明)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**

### 添加依赖

```kotlin
dependencies {
    implementation("tony:tony-knife4j-api:0.1-SNAPSHOT")
}
```

### 启用模块

```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

## 主要功能

### 1. 开箱即用的 API 文档界面

- **Knife4j UI 集成**：提供美观、功能丰富的 API 文档界面，支持在线调试
- **自动配置**：无需额外配置，启动即可访问 `/doc.html` 查看 API 文档
- **多种功能**：支持接口分组、参数编辑、响应预览、代码生成等

### 2. 自动响应结构包装

- **`WrapResponseBodyOperationCustomizer`**：自动识别控制器返回值，在 API 文档中显示为统一的 `ApiResult<T>` 结构
- **智能判断**：自动区分简单类型、集合类型、下载类型，采用不同的包装策略
- **白名单支持**：支持配置排除路径，特定接口不进行包装处理

### 3. 统一枚举处理

- **`EnumValueCustomizer`**：自动识别实现 `EnumValue` 接口的枚举类型，在文档中显示枚举可选值
- **参数支持**：支持请求参数、请求体属性、响应字段中的枚举自动展示
- **类型安全**：区分 `IntEnumValue` 和 `StringEnumValue`，正确显示数据类型

### 4. 属性扁平化展示

- **`FlattenPropertiesOpenApiCustomizer`**：优化复杂对象的文档展示，支持属性扁平化
- **可读性提升**：减少嵌套层级，提高 API 文档的可读性

### 5. 文件下载支持

- **`OctetStreamResponseOperationCustomizer`**：自动识别文件下载接口，正确显示响应类型
- **媒体类型支持**：支持 `application/octet-stream`、Excel、图片等多种文件类型

## 配置说明

### 基础配置

```yaml
knife4j:
  enabled: true  # 是否启用 Knife4j
  extension:
    title: "Tony-Api"           # API 文档标题
    version: "1.0"              # API 版本
    description: ""             # API 描述
    contact:                    # 联系信息
      name: "开发团队"
      email: "dev@example.com"
```

### 响应包装排除配置

```yaml
web:
  wrap-response-body-enabled: true        # 启用响应包装
  wrapResponseExcludePatterns:           # 排除包装的路径
    - "/download/**"
    - "/export/**"
```

## 适用场景

- 需要完善 API 文档的企业级 Spring Boot 项目
- 要求前后端协作规范、接口文档标准化的团队开发
- 需要在线调试、接口测试功能的开发环境
- 使用统一响应结构和枚举规范的项目

## 注意事项

1. **枚举处理**：枚举类必须实现 `IntEnumValue` 或 `StringEnumValue` 接口，并提供 `@JsonCreator` 标注的工厂方法
2. **响应包装**：如需排除特定接口的自动包装，请配置 `wrapResponseExcludePatterns`
3. **性能考虑**：生产环境建议通过 `gradle`条件编译去掉`knife4j`,`springdoc`,`swagger`依赖.
