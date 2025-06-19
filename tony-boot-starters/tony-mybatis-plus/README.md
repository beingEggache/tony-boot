# 概述

`tony-mybatis-plus` 是基于 MyBatis-Plus 的增强扩展模块，提供更丰富的数据访问能力，包括物理删除、统一分页结构、链式查询增强、字段自动填充、枚举类型处理等，极大提升开发效率和代码健壮性。

## 如何使用

- 依赖 Java 21 或更高版本

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-mybatis-plus:0.1-SNAPSHOT")
}
```

### 启用 `tony-boot-starters`

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

### 1. 物理删除能力

- 支持通过 `BaseDao` 直接进行物理删除（非逻辑删除），包括按条件、按ID、按ID集合。
- 内置 SQL 注入器自动注册物理删除方法。

### 2. 查询结果非空断言

- 查询方法如 `selectByIdNotNull`、`selectOneNotNull`，结果为空时自动抛出异常，保障业务健壮性。

### 3. 统一分页结构

- 分页查询结果统一封装为 `PageResultLike`，便于前后端数据交互和统一处理。

### 4. 链式查询增强（TonyChainQuery）

- 扩展 MyBatis-Plus 的链式查询，支持：
  - 查询单条/多条并断言非空
  - 查询存在/不存在并抛异常
  - 查询并转换结果类型
  - 查询 Map/List/Obj 等多种结构
  - 统一分页结果

### 5. 字段自动填充

- 通过 `@MybatisPlusMetaProperty` 注解和 `DbMetaObjectHandler`，自动填充如创建人、更新人、租户ID等元数据字段，支持自定义关联属性。

### 6. 枚举类型处理

- 内置 `EnumTypeHandler`，支持枚举与数据库字段的自动映射，提升类型安全。

### 7. 便捷工具类

- 提供分页工具、Wrapper 扩展等，简化常用操作。

## 注解与用法示例

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

### 物理删除

```kotlin
val rows = baseDao.physicalDelete(queryWrapper)
val rowsById = baseDao.physicalDeleteById(id)
val rowsByIds = baseDao.physicalDeleteByIds(listOfId)
```

### 链式查询断言

```kotlin
val result = baseDao.query().eq("column", "value").oneNotNull()
val list = baseDao.query().listThrowIfEmpty()
```

### 统一分页

```kotlin
val pageResult = baseDao.selectPageResult(pageQuery, queryWrapper)
```

## 适用场景

- 需要物理删除、字段自动填充、链式查询增强的 Spring Boot/MyBatis-Plus 项目
- 追求统一分页结构、类型安全、开发效率的企业级应用

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
