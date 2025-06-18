# tony-boot-starters

`tony-boot-starters`Spring Boot + Kotlin. Java 21、Spring 6.2.7、Spring Boot 3.5.0 和 Kotlin 2.1.21。

## 项目结构

- **`tony-dependencies`**：管理项目的依赖，生成Pom, 确保子项目使用统一的依赖版本。


- **`build-script`**：包含自定义的 Gradle 插件，用于项目的构建、发布和部署。


- **`tony-core`**：核心模快, 统一响应结构, 分页结构, 枚举及枚举验证方式, 异常.加解密工具类, 及其他文本日期工具类.


- **`tony-interfaces`**：核心注解及接口。


- **`tony-web`**： web 统一日志,统一异常处理, 统一响应结构, 统一枚举处理方式。


- **`tony-web-auth`**：Web 统一身份验证处理方式。


- **`tony-web-crypto`**：Web 加解密请求响应。


- **`tony-mybatis-plus`**： 扩展MyBatis-Plus，增加物理删除、统一分页查询, 统一公共字段填充等。


- **`tony-feign`**： 扩展OpenFeign, 提供统一解包,统一请求响应拦截器处理。


- **`tony-redis`**：Redis，统一调用方式。


- **`tony-alipay`**：支付宝 SDK，提供支付宝支付相关功能。


- **`tony-wechat`**：微信相关功能，如微信支付等。


- **`tony-aliyun-oss`**：集成阿里云 OSS 服务，用于文件存储。


- **`tony-aliyun-sms`**：集成阿里云短信服务，用于发送短信。


- **`tony-snowflake-id`**：提供雪花算法生成分布式唯一 ID 的功能。


- **`tony-knife4j-api`**：集成 Knife4j，用于生成 API 文档，方便开发和测试人员查看和使用 API。


- **`tony-jwt`**：集成了 JWT（JSON Web Token）。


- **`tony-captcha`**：集成了验证码功能。

## 快速开始
### 环境要求
- Java 21 或更高版本
- Gradle 7.x 或更高版本

### 启用 `tony-boot-starters`
```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

### 依赖配置
在你的项目的 `build.gradle.kts` 文件中引入所需的启动器模块，例如引入 `tony-redis` 模块：
```kotlin
dependencies {
    implementation("tony:tony-redis:0.1-SNAPSHOT")
}
```

## 配置
### 代码风格配置
Ktlint 使用 `.editorconfig` 定义具体的规则。

### 通过 Gradle 启动 Ktlint 任务
- **`ktlintCheck`**：检查 Kotlin 代码风格：
```bash
./gradlew ktlintCheck
```
- **`ktlintFormat`**：修复 Kotlin 代码风格：
```bash
./gradlew ktlintFormat
```
