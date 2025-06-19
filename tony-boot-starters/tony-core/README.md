## 概述

`tony-core` 是 `tony-boot-starters` 项目的核心基础模块，提供了统一响应、分页结构、异常处理、Spring 上下文工具、丰富的工具类（集合、字符串、数字、日期、反射、加解密等）、枚举及其校验等通用能力，为业务开发提供高效、可靠的底层支撑。

## 主要功能

### 统一响应结构
- 提供 `ApiResult`、`MonoResult`、`FlattenApiResult` 等类，支持多种数据类型的统一响应封装，便于前后端交互。
- 支持属性拉平、响应结构自定义等特性。

### 分页结构
- 提供 `PageResult`、`RowsWrapper`、`PageQuery` 等类，标准化分页查询的请求与响应结构，简化分页开发。

### 枚举及校验
- `Enums.kt` 提供枚举工具方法，`EnumAnnotations.kt`、`EnumValidators.kt` 支持注解驱动的枚举校验，保障枚举参数的安全与规范。

### 异常处理
- `Exceptions.kt` 定义了通用异常体系（如 `ApiException`、`BaseException`、`BizException`），便于全局异常捕获与处理。

### Spring 上下文工具
- `SpringContexts.kt` 支持在非 Spring Bean 环境下获取容器中的 Bean，提升灵活性。

### 丰富的工具类（utils 包）

- **集合工具类（Cols.kt）**：集合判空、集合转换、集合去重、集合分组等常用集合操作。
- **字符串工具类（Strs.kt）**：字符串判空、格式化、类型转换、正则校验（如手机号、数字）、URL 编解码、去除换行、UUID 生成等。
- **数字工具类（Nums.kt）**：数字类型转换、格式化、百分比、随机数生成、BigDecimal 处理等。
- **日期时间工具类（Dates.kt）**：日期格式化、字符串与日期互转、时间区间判断、获取当天剩余秒数、日期加减等。
- **反射工具类（Reflects.kt）**：属性/方法反射、getter/setter 缓存、属性赋值与取值、注解获取等。
- **类型工具类（Types.kt）**：类型判断（是否数字、字符串、数组、集合等）、类型转换等。
- **对象工具类（Objs.kt）**：对象判空、深拷贝、对象属性操作等。
- **函数式工具类（Funcs.kt）**：条件执行、空值处理、断言抛异常、链式调用等函数式编程辅助。
- **JSON 工具类（Jsons.kt）**：全局 ObjectMapper 管理、对象与 JSON 字符串互转、类型安全转换等。
- **日志工具类（Logs.kt）**：日志 MDC 管理、Logger 获取、日志输出辅助等。
- **网络工具类（Nets.kt）**：IP、端口、网络地址等相关操作。
- **加解密工具类（Digests.kt）**：MD5、SHA1 等摘要算法，字符串加密等。
- **注解工具类（Annos.kt）**：注解查找、注解属性获取等。
- **字节数组工具类（ByteArrs.kt）**：字节数组与字符串互转等。

> 工具类均为静态方法，直接调用，无需实例化，极大提升开发效率。

### 异常处理

- **BaseException**：全局基础异常，包含错误码、消息、异常链等属性，所有自定义异常的基类。
- **ApiException**：框架层异常，适用于接口调用、参数校验等通用错误，默认错误码为 `ApiProperty.errorCode`。
- **BizException**：业务异常，适用于业务逻辑校验失败等场景，默认错误码为 `ApiProperty.preconditionFailedCode`。
- **异常工具方法**：
  - `throwIfTrue`/`throwIfFalse`：条件为真/假时抛出指定异常。
  - `throwIfNull`/`throwIfEmpty`：对象/集合/Map 为空时抛出异常。
  - 支持自定义异常类型、错误码和消息，便于链式编程和业务断言。

> 通过丰富的断言和异常体系，保障了业务代码的健壮性和可维护性。

### 枚举统一序列化与反序列化

- **统一接口**：`tony-core` 定义了 `EnumValue<T>`、`IntEnumValue`、`StringEnumValue` 等全局枚举接口，所有业务枚举只需实现对应接口，即可自动支持统一的序列化与反序列化。
- **静态工厂方法要求**：每个实现 `IntEnumValue` 或 `StringEnumValue` 的枚举类，必须提供一个继承自 `IntEnumCreator` 或 `StringEnumCreator` 的 `companion object`，并**重写 `create` 方法**，该方法上需加上 `@JsonCreator` 注解，确保 Jackson 能正确调用进行反序列化。
- **Jackson 支持**：通过 `@JsonValue` 注解和枚举工厂，枚举在 JSON 序列化时自动输出 value 字段，反序列化时自动根据 value 还原为枚举对象，适用于 Web 层参数、响应、Redis 缓存、MyBatis-Plus ORM 等多场景。
- **MyBatis-Plus 支持**：枚举类型字段自动映射数据库，支持 int/string 两种 value 类型，无需额外转换器。
- **Redis 支持**：Redis 相关操作自动识别枚举类型，序列化为 value，反序列化时自动还原为枚举对象，兼容 Jackson、Protostuff 等多种序列化方式。
- **校验注解**：内置多种枚举参数校验注解（如 `@IntEnum`、`@StringEnum`），保障接口参数的安全与规范。

**典型用法示例：**
```kotlin
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class Gender(@get:JsonValue override val value: Int) : IntEnumValue {
    MALE(1), FEMALE(2);

    companion object : IntEnumCreator(Gender::class.java) {
        @JvmStatic
        @JsonCreator
        override fun create(value: Int): Gender? = super.create(value)
    }
}
```

> 只要实现上述模式，无论是 Web 层参数、JSON、Redis、MyBatis-Plus，均可自动完成枚举的序列化与反序列化，无需手动注册额外转换器。

## 适用场景

- 适用于 Spring Boot 项目中需要统一响应、分页、异常、工具类等通用能力的场景。
- 可作为其他业务模块的基础依赖，提升开发效率，减少重复造轮子。
