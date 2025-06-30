## 概述

`tony-mybatis-plus` 是 `tony-boot-starters` 体系下基于 MyBatis-Plus 的深度增强模块，面向企业级应用，提供统一分页、物理删除、链式断言、字段自动填充、类型安全枚举等高效数据访问能力。模块兼容 Kotlin/Java，极大提升开发效率与健壮性。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [统一分页处理](#1-统一分页处理)
  - [查询结果非空断言](#2-查询结果非空断言)
  - [物理删除能力](#3-物理删除能力)
  - [字段自动填充](#4-字段自动填充)
  - [链式查询增强](#5-链式查询增强)
  - [类型安全枚举处理](#6-类型安全枚举处理)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [进阶用法](#进阶用法)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **MyBatis-Plus 3.5.x**

### 添加依赖

```kotlin
dependencies {
    implementation("tony:tony-mybatis-plus:0.1-SNAPSHOT")
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

### 1. 统一分页处理

- **PageQuery/PageResult**：标准化分页请求与响应结构，支持排序、总页数、总条数等。
- **BaseDao.selectPageResult**：DAO 层直接接收 `PageQuery`，返回 `PageResult<T>`，简化分页开发。
- **Kotlin/Java 兼容**：Kotlin/Java 项目均可无缝集成。

### 2. 查询结果非空断言

- **NotNull 查询**：`selectByIdNotNull`、`selectOneNotNull`、`oneNotNull` 等方法，查询结果为空自动抛出业务异常，避免 NPE。
- **链式断言**：链式查询支持 `oneNotNull`、`listThrowIfEmpty` 等断言方法。

### 3. 物理删除能力

- **BaseDao.physicalDelete**：支持按条件、按 ID、批量物理删除，区别于 MyBatis-Plus 默认逻辑删除。
- **SQL 注入器**：自动注册物理删除 SQL，兼容原生与扩展用法。

### 4. 字段自动填充

- **@MybatisPlusMetaProperty**：实体字段注解，支持用户ID、用户名、组织ID、租户ID等元数据自动填充。
- **DbMetaObjectHandler**：自动注入会话上下文（如 ApiSession），支持 relativeProp 关联属性联动填充。
- **自定义元数据**：支持自定义 provider，灵活扩展部门、岗位等业务元数据。

### 5. 链式查询增强

- **TonyChainQuery**：扩展 MyBatis-Plus 链式查询，支持 `oneNotNull`、`listThrowIfEmpty`、`throwIfExists` 等断言与便捷方法。
- **类型安全**：链式 API 保证类型安全，提升开发体验。

### 6. 类型安全枚举处理

- **EnumTypeHandler**：自动处理 Java/Kotlin 枚举与数据库字段映射，兼容 Int/String 枚举。
- **全链路兼容**：与 Redis、DTO、数据库等序列化/反序列化规则一致。

## 使用示例

### 分页查询

```kotlin
@RestController
class UserController(private val userDao: UserDao) {
    @PostMapping("/user/page")
    fun page(@RequestBody query: PageQuery): PageResult<User> {
        return userDao.selectPageResult(query)
    }
}
```

### 非空断言查询

```kotlin
val user = userDao.selectByIdNotNull("123")
val one = userDao.query().eq(User::name, "张三").oneNotNull()
```

### 物理删除

```kotlin
userDao.physicalDeleteById("123")
userDao.physicalDelete(queryWrapper)
```

### 字段自动填充

```kotlin
@TableName("sys_role")
class Role {
    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(fill = FieldFill.INSERT)
    var creatorId: String = ""

    @MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")
    @TableField(fill = FieldFill.INSERT)
    var creatorName: String = ""
}
```

### 枚举类型处理

```kotlin
enum class Status(override val value: Int) : IntEnumValue {
    ENABLED(1), DISABLED(0);
    companion object : IntEnumCreator<Status>(Status::class.java){
        @JvmStatic
        @JsonCreator
        override fun create(value: Int): Status? = super.create(value)
    }
}
```

## 进阶用法

### 1. 字段自动填充的高级用法

#### 支持多种元数据类型自动填充

通过 `@MybatisPlusMetaProperty` 注解，可以自动填充如用户ID、用户名、组织ID、组织名、租户ID等字段。只需在实体类字段上加注解，框架会在插入或更新时自动填充对应值。

```kotlin
@TableName("sys_role")
class Role {
    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(fill = FieldFill.INSERT)
    var creatorId: String = ""

    @MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")
    @TableField(fill = FieldFill.INSERT)
    var creatorName: String = ""
}
```

#### 支持 relativeProp 关联属性联动填充

- `relativeProp` 参数可指定另一个属性名，实现如"根据ID自动填充名称"。
- 例如：`@MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")`，会根据 `creatorId` 自动填充 `creatorName`。

#### 支持自定义元数据类型和填充值

- 通过 `metaPropProviders` 可扩展自定义元数据类型的填充值逻辑。
- 例如：可为 `MetaColumn.ORG_ID`、`MetaColumn.ORG_NAME` 提供自定义的获取方式。

#### 支持 insert/update 场景的自动填充

- 插入时自动填充创建人、租户等，更新时自动填充更新人等。
- 通过 `@TableField(fill = FieldFill.INSERT)` 或 `FieldFill.INSERT_UPDATE` 控制填充时机。

---

### 2. 结合自定义 ApiSession 实现复杂会话上下文自动注入

#### ApiSession 机制

- `DefaultMetaObjectHandler` 构造时注入 `ApiSession`，自动从会话中获取当前用户、组织、租户等信息。
- 你可以自定义实现 `ApiSession`，如支持多租户、子账号、扩展上下文等。

```kotlin
class CustomApiSession : ApiSession {
    override val userId: String get() = // 从Token或上下文获取
    override val userName: String get() = // ...
    override val orgId: String get() = // ...
    override val orgName: String get() = // ...
    override val tenantId: String get() = // ...
}
```

#### 注入自定义 ApiSession

- 在 Spring 配置中注入自定义 `ApiSession` 实例，并将其传递给 `DefaultMetaObjectHandler`。

```kotlin
@Bean
fun apiSession(): ApiSession = CustomApiSession()

@Bean
fun metaObjectHandler(apiSession: ApiSession): MetaObjectHandler =
    DefaultMetaObjectHandler(apiSession)
```

#### 支持自定义元数据填充逻辑

- 通过 `metaPropProviders` 参数，可以为特定元数据类型（如部门、岗位等）自定义填充逻辑。
- 例如：根据 `creatorId` 查询数据库获取 `creatorName`，并自动填充。

```kotlin
@Bean
fun metaObjectHandler(apiSession: ApiSession): MetaObjectHandler =
    DefaultMetaObjectHandler(
        apiSession,
        metaPropProviders = mapOf(
            MetaColumn.USER_NAME to Function { userId ->
                // 根据userId查库或缓存获取用户名
                userService.getUserNameById(userId)
            }
        )
    )
```

---

> 字段自动填充不仅支持常规的用户、租户等元数据，还可通过 relativeProp 和自定义 provider 实现复杂的上下文自动注入。结合自定义 `ApiSession`，可灵活适配多种业务场景（如多租户、子账号、动态上下文等），极大提升开发效率和一致性。

---

## 适用场景

- 需要规范化分页、排序、链式查询的企业级项目
- 追求数据访问层健壮性与类型安全
- 需要自动填充元数据、支持多租户/多上下文的系统

## 注意事项

1. **分页参数**：PageQuery 默认 pageNo/pageSize，建议前端统一传参。
2. **自动填充**：自定义 ApiSession 时需保证线程安全。
3. **枚举处理**：枚举需实现 IntEnumValue/StringEnumValue 并注册工厂方法。


