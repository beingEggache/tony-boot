## 概述
`tony-captcha` 是 `tony-boot-starters` 项目中的验证码模块，提供可配置的验证码生成与校验功能。支持多种验证模式，并深度集成 Redis 实现验证码状态管理。

## 如何使用
- Java 21 或更高版本

在 [build.gradle.kts](file://C:\workspace\tony-projects\tony-boot-starters\build.gradle.kts) 中添加依赖：
```kotlin
dependencies {
    implementation("tony:tony-captcha:0.1-SNAPSHOT")
}
```
### 启用模块
在 Spring Boot 主类添加 `@EnableTonyBoot` 注解：
```kotlin
@EnableTonyBoot
@SpringBootApplication
class YourApplication

    fun main(args: Array<String>) {
    runApplication<YourApplication>(*args)
}
```
## 功能特性

### 1. 多模式验证码支持
- **DEFAULT 模式**：实际生成并校验验证码（默认启用）
- **NOOP 模式**：空实现模式，直接返回验证成功（用于测试环境）

### 2. 验证码生命周期管理
- 基于 Redis 存储验证码文本
- 支持自定义过期时间（秒/分钟/小时）
- 自动清理过期验证码

### 3. 高度可配置
- 通过 CaptchaProperties 配置验证模式：
```yaml
captcha:
    mode: DEFAULT # 或 NOOP
```
- 支持自定义验证码生成规则（尺寸、长度等）

## 核心组件

### 1. 验证码服务接口 `CaptchaService`
```kotlin
public fun interface CaptchaService {
    public fun verify(vo: CaptchaVo): Boolean
}
```
### 2. 验证码对象 CaptchaVo
```kotlin
public open class CaptchaVo(
    public val captchaId: String,      // 验证码ID
    public val captcha: String,         // 验证码内容(Base64)
    public val type: String,            // 验证码类型
    @JsonIgnore
    public val captchaKeyRule: (CaptchaVo) -> String // Redis键生成规则
)
```
### 3. 验证码管理器 `CaptchaManager`

```kotlin
public data object CaptchaManager {
    public fun genCaptcha(
    type: String,
    captchaKeyRule: (CaptchaVo) -> String,
    timeout: Long,
    timeUnit: TimeUnit = TimeUnit.SECONDS
    ): CaptchaVo
    public fun <R> verify(vo: CaptchaVo, func: () -> R): R
}
```
## 使用示例

### 生成验证码

```kotlin
val captchaVo = CaptchaManager.genCaptcha(
    type = "LOGIN",
    captchaKeyRule = { vo -> "CAPTCHA:${vo.type}:${vo.captchaId}" },
    timeout = 300,  // 5分钟过期
    timeUnit = TimeUnit.SECONDS
)
```
### 校验验证码
```kotlin
fun login(vo: CaptchaVo) = CaptchaManager.verify(vo) {
    // 验证通过后执行业务逻辑
    userService.login(vo)
}
```
### 自定义验证码实现
1. 实现 `CaptchaService` 接口：
```kotlin
class CustomCaptchaService : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean {
    // 自定义验证逻辑
    }
}
```
2. 注册为 Spring Bean：
```kotlin
@Bean
fun customCaptchaService() = CustomCaptchaService()
```
## 注意事项
1. 生产环境务必使用 `DEFAULT` 模式
2. 验证码 Redis 键规则应包含业务场景标识（如登录/注册）
3. 建议结合 `tony-web` 模块的请求重复读取过滤器使用

```yaml
# 在 logback-spring.xml 添加验证码日志追踪
<logger name="tony.captcha" level="DEBUG"/>
```
> 验证码模块默认使用 `SpecCaptcha` 实现（130x30像素，4位字符），如需修改请扩展 `DefaultCaptchaServiceImpl`
```kotlin
class CustomCaptchaService : DefaultCaptchaServiceImpl() {
    override fun generateCaptcha(): SpecCaptcha {
        return SpecCaptcha(200, 50, 6) // 自定义尺寸和长度
    }
}
```
