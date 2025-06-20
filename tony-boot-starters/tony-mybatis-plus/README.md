## 概述

`tony-mybatis-plus` 是一个基于 `MyBatis-Plus` 的深度增强模块，旨在为现代企业级应用提供更高效、更健壮、更便捷的数据访问层解决方案。它不仅继承了 MyBatis-Plus 的所有优点，还引入了统一分页处理、物理删除、链式查询断言、字段自动填充和类型安全的枚举处理等一系列高级功能。

## 目录
- [如何使用](#如何使用)
- [核心功能](#核心功能)
    - [统一分页处理 (请求与响应)](#1-统一分页处理-请求与响应)
    - [查询结果非空断言](#2-查询结果非空断言)
    - [物理删除能力](#3-物理删除能力)
    - [字段自动填充](#4-字段自动填充)
    - [更多功能](#5-更多功能)
- [适用场景](#适用场景)
- [字段自动填充的高级用法](#字段自动填充的高级用法)

## 如何使用

确保你的项目满足以下环境要求：
- **Java 21** 或更高版本
- **Spring Boot 3.x**

在 `build.gradle.kts` 中添加依赖：
```kotlin
dependencies {
    // ... 其他依赖
    implementation("tony:tony-mybatis-plus:0.1-SNAPSHOT")
}
```

## 核心功能

### 1. 统一分页处理 (请求与响应)

本模块对分页查询的**请求参数**和**响应结果**都进行了统一封装和扩展，极大简化了分页功能的开发，并对 Java 项目提供了完整的兼容性支持。

- **统一分页请求 (`PageQuery`)**: 定义了标准的分页请求数据结构，用于在 Controller 层接收前端传递的分页和排序参数。
- **统一分页响应 (`PageResult<T>`)**: 定义了标准的分页响应结构，包含了 `items` (当前页数据列表), `total` (总记录数), `pageNo`, `pageSize`, 和 `pages` (总页数) 等完整的分页信息，便于前后端协作。

- **DAO/Mapper 层集成**: `BaseDao` 接口提供了 `selectPageResult` 等扩展方法，可以直接接收 `PageQuery` 对象来执行分页查询，并自动将查询结果封装成 `PageResult<T>` 类型返回。对于 Java 项目，也提供了如 `PageQuery.toPage()` 的便捷方法与原生 MyBatis-Plus API 协同工作。

### 2. 查询结果非空断言

为避免空指针异常，提供了一系列 `NotNull` 后缀的查询方法。当查询结果为空时，会自动抛出 `BizException`，而不是返回 `null`。

- `selectByIdNotNull(id)`: 根据ID查询，结果为空则抛异常。
- `selectOneNotNull(wrapper)`: 根据条件查询单条记录，结果为空或多于一条则抛异常。
- `baseDao.query().oneNotNull()`: 使用链式查询并断言结果非空。

### 3. 物理删除能力

与 MyBatis-Plus 默认的逻辑删除不同，本模块通过 `BaseDao` 直接提供了物理删除的能力。

- `physicalDeleteById(id)`: 根据ID物理删除。
- `physicalDeleteByIds(ids)`: 根据ID集合批量物理删除。
- `physicalDelete(wrapper)`: 根据条件物理删除。

### 4. 字段自动填充

通过 `@MybatisPlusMetaProperty` 注解和 `DbMetaObjectHandler`，实现了通用字段（如创建人、创建时间、租户ID等）的自动填充，支持关联填充（如根据`userId`自动填充`userName`）。

```kotlin
// 示例：在实体类中配置自动填充
@TableName("sys_role")
class Role {
    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(fill = FieldFill.INSERT)
    var creatorId: String = ""

    // "creatorName" 会根据 "creatorId" 自动填充
    @MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")
    @TableField(fill = FieldFill.INSERT)
    var creatorName: String = ""
}
```

### 5. 更多功能

- **链式查询增强 (`TonyChainQuery`)**: 扩展了 MyBatis-Plus 的链式查询，增加了 `oneNotNull`、`listThrowIfEmpty` 等实用方法。
- **枚举类型处理器**: 内置 `EnumTypeHandler`，自动处理 Java/Kotlin 枚举与数据库字段的映射。

## 适用场景

- 需要快速实现规范化分页功能（包括排序）的 Spring Boot 项目。
- 追求代码健壮性，希望从数据访问层杜绝空指针异常。
- 需要对通用字段（如创建/更新人、租户ID）进行自动化、标准化管理。
- 同时在 Kotlin 和 Java 项目中寻求一致、高效的开发体验。

## 字段自动填充的高级用法

### 1. 支持多种元数据类型自动填充

通过 `@MybatisPlusMetaProperty` 注解，可以自动填充如用户ID、用户名、组织ID、组织名、租户ID等字段。你只需在实体类字段上加注解，框架会在插入或更新时自动填充对应值。

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

### 2. 支持 relativeProp 关联属性联动填充

- `relativeProp` 参数可指定另一个属性名，实现如"根据ID自动填充名称"。
- 例如：`@MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")`，会根据 `creatorId` 自动填充 `creatorName`。

### 3. 支持自定义元数据类型和填充值

- 通过 `metaPropProviders` 可扩展自定义元数据类型的填充值逻辑。
- 例如：可为 `MetaColumn.ORG_ID`、`MetaColumn.ORG_NAME` 提供自定义的获取方式。

### 4. 支持 insert/update 场景的自动填充

- 插入时自动填充创建人、租户等，更新时自动填充更新人等。
- 通过 `@TableField(fill = FieldFill.INSERT)` 或 `FieldFill.INSERT_UPDATE` 控制填充时机。

---

### 结合自定义 ApiSession 实现复杂会话上下文自动注入

#### 1. ApiSession 机制

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

#### 2. 注入自定义 ApiSession

- 在 Spring 配置中注入自定义 `ApiSession` 实例，并将其传递给 `DefaultMetaObjectHandler`。

```kotlin
@Bean
fun apiSession(): ApiSession = CustomApiSession()

@Bean
fun metaObjectHandler(apiSession: ApiSession): MetaObjectHandler =
    DefaultMetaObjectHandler(apiSession)
```

#### 3. 支持自定义元数据填充逻辑

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

如需补充英文版、进阶用法或详细接口说明，请告知！
