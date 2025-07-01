# tony-boot-starters

## 项目简介

`tony-boot-starters` 是一套面向企业级 Kotlin/Java 项目的 Spring Boot 扩展与集成解决方案，涵盖核心工具、分布式组件、Web 能力、安全认证、加密、第三方服务对接等常用场景。各模块均以 Starter 形式提供，开箱即用，助力团队高效开发、统一规范、快速集成。

---

## 目录结构与模块说明

| 模块名                | 说明                                                         |
|----------------------|--------------------------------------------------------------|
| tony-core            | 基础工具包，提供核心工具类、统一枚举序列化、异常处理等        |
| tony-feign           | Feign 客户端增强，支持全局拦截、日志、异常、认证等            |
| tony-interfaces      | 统一接口定义与通用响应结构                                     |
| tony-mybatis-plus    | MyBatis-Plus 增强，自动填充、乐观锁、分页、枚举兼容等         |
| tony-redis           | Redis Starter，支持多数据源、缓存、分布式锁、序列化等         |
| tony-web             | Web 通用能力，包含全局异常、参数校验、统一响应、拦截器等      |
| tony-web-auth        | Web 权限认证，支持 JWT、注解式鉴权、会话管理等                |
| tony-web-crypto      | Web 请求体解密/响应体加密，注解驱动，支持 AES/DES             |
| tony-wechat          | 微信公众号/小程序/支付集成，支持多应用、消息、菜单等          |
| tony-captcha         | 图形验证码生成与校验                                           |
| tony-alipay          | 支付宝支付能力集成                                            |
| tony-aliyun-oss      | 阿里云 OSS 对象存储集成                                       |
| tony-aliyun-sms      | 阿里云短信服务集成                                            |
| tony-snowflake-id    | 分布式唯一 ID 生成器（雪花算法）                              |
| tony-knife4j-api     | Knife4j 文档增强                                             |
| build-script         | Gradle 插件集，统一构建、依赖、代码规范、Docker、发布等        |
| tony-dependencies    | 依赖版本管理                                                  |
| tony-dependencies-catalog | 依赖版本 catalog 文件                                      |

---

## 主要亮点

- **模块化设计**：各模块可独立引入，按需集成，互不强依赖
- **全链路枚举兼容**：统一枚举序列化/反序列化，兼容 Jackson、Redis、MyBatis-Plus
- **注解驱动**：如加密、鉴权、参数校验等均支持注解式开发
- **多环境适配**：支持多数据源、多环境配置，适合复杂企业场景
- **自动化构建**：内置 Gradle 插件，支持一键构建、发布、镜像、代码规范
- **丰富的三方集成**：微信、支付宝、阿里云等主流服务一站式对接

---

## 快速开始

1. 在你的 Spring Boot 项目中引入所需模块（以 Gradle 为例）：

   ```kotlin
   implementation(project(":tony-boot-starters:tony-core"))
   implementation(project(":tony-boot-starters:tony-web"))
   // 按需添加其他模块
   ```

2. 按各模块 README 配置相关参数，参考示例代码和注解用法。

3. 享受统一、自动化的开发体验！

---

## 典型场景

- 企业级多模块项目的统一规范与能力扩展
- 微服务架构下的通用组件复用
- 快速集成第三方服务（如微信、支付宝、阿里云等）
- 需要统一异常、响应、认证、加密、缓存等能力的项目

---

## 文档与支持

- 各模块均内置详细 README.md，包含功能说明、配置方法、典型用法、进阶用法、注意事项等
- 如需针对某个模块的详细说明，请查阅对应子目录下的 README.md。
