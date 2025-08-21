# tony-boot-starters
![GitHub package.json dependency version (prod)](https://img.shields.io/badge/licence-MIT-green)
![GitHub package.json dependency version (prod)](https://img.shields.io/badge/jdk-21-green)
![GitHub package.json dependency version (prod)](https://img.shields.io/badge/kotlin-2.2.10-orange)
![GitHub package.json dependency version (prod)](https://img.shields.io/badge/gradle-9.0-green)

## 目录结构与模块说明

| 模块名                       | 说明                                 |
|---------------------------|------------------------------------|
| tony-core                 | 基础工具包，提供核心工具类、统一枚举序列化、异常处理等        |
| tony-feign                | Feign 客户端增强，支持全局拦截、日志、异常、认证等       |
| tony-interfaces           | 统一接口定义与通用响应结构                      |
| tony-mybatis-plus         | MyBatis-Plus 增强，自动填充、乐观锁、分页、枚举兼容等  |
| tony-redis                | Redis Starter，支持多数据源、缓存、分布式锁、序列化等  |
| tony-web                  | Web 通用能力，包含全局异常、参数校验、统一响应、拦截器等     |
| tony-web-auth             | Web 权限认证，支持 JWT、注解式鉴权、会话管理等        |
| tony-web-crypto           | Web 请求体解密/响应体加密，注解驱动，支持 AES/DES    |
| tony-wechat               | 微信公众号/小程序/支付集成，支持多应用、消息、菜单等        |
| tony-captcha              | 图形验证码生成与校验                         |
| tony-alipay               | 支付宝支付能力集成                          |
| tony-aliyun-oss           | 阿里云 OSS 对象存储集成                     |
| tony-aliyun-sms           | 阿里云短信服务集成                          |
| tony-snowflake-id         | 分布式唯一 ID 生成器（雪花算法）                 |
| tony-knife4j-api          | Knife4j 文档增强                       |
| build-script              | Gradle 插件集，统一构建、依赖、代码规范、Docker、发布等 |
| tony-dependencies         | 依赖版本管理                             |
| tony-dependencies-catalog | 依赖版本 catalog 文件                    |

---

## 文档与支持

- 各模块均内置详细 README.md，包含功能说明、配置方法、典型用法、进阶用法、注意事项等
- 如需针对某个模块的详细说明，请查阅对应子目录下的 README.md。
