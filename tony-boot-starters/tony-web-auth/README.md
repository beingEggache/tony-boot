## 概述
`tony-web-auth` 是 `tony-boot-starters` 项目中的一个模块，专注于提供 Web 统一身份验证处理方式。它简化了项目中身份验证的开发流程，通过集成 JWT（JSON Web Token），为应用提供了安全、便捷的身份验证机制。同时，支持灵活配置不需要登录校验的地址，方便开发人员根据实际需求进行定制。

## 如何使用
- Java 21 或更高版本


在 `build.gradle.kts` 中添加：
```kotlin
dependencies {
    implementation("tony:tony-web-auth:0.1-SNAPSHOT")
}
```
### 启用 `tony-boot-starters`
在 Spring Boot 应用主类上添加 `@EnableTonyBoot` 注解，以启用 `tony-boot-starters` 的功能：
```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    org.springframework.boot.run(YourApplication::class.java, *args)
}
```

### 配置身份验证
支持通过配置文件设置不需要登录校验的地址。在配置文件中添加以下配置：
```yaml
web:
  auth:
    noLoginCheckUrl:
      - "/your-url-1"
      - "/your-url-2"
```

### 自定义拦截器
如果默认的登录校验拦截器无法满足需求，可以自定义登录校验拦截器。只需创建一个实现 `LoginCheckInterceptor` 接口的类，并在配置类中使用 `@Bean` 注解将其注入到 Spring 容器中.

### 自定义会话实现
项目提供了两种默认的会话实现：`NoopWebSession` 和 `JwtWebSession`。如果需要自定义会话实现，只需创建一个实现 `WebSession` 接口的类，并在配置类中使用 `@Bean` 注解将其注入到 Spring 容器中.
