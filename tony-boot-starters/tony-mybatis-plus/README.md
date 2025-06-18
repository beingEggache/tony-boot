# 概述

`tony-mybatis-plus` 是基于 MyBatis-Plus 进行扩展的模块，旨在为项目提供更丰富的数据访问功能和便捷的操作方式。以下是该模块扩展功能的详细介绍。

## 如何使用
- Java 21 或更高版本


在 `build.gradle.kts` 中添加：
```kotlin
dependencies {
    implementation("tony:tony-mybatis-plus:0.1-SNAPSHOT")
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

## 功能特性
### 1. 物理删除功能
在 `BaseDao` 接口中增加了物理删除的方法，支持根据查询条件、ID 以及 ID 列表进行物理删除操作。

```kotlin
// 根据查询条件进行物理删除
fun physicalDelete(@Param(Constants.WRAPPER) queryWrapper: Wrapper<T>): Int

// 按 ID 进行物理删除
fun physicalDeleteById(id: Serializable): Int

// 按 ID 列表进行物理删除
fun physicalDeleteByIds(@Param(Constants.COLL) idList: Collection<*>): Int
```

### 2. 查询结果非空检查
提供了多个查询方法，当查询结果为空时会抛出异常，确保业务逻辑的健壮性。

```kotlin
// 根据 ID 查询，结果为 null 时抛错
fun selectByIdNotNull(id: Serializable): T

// 根据 entity 条件查询一条记录，结果为 null 时抛错
fun selectOneNotNull(@Param(Constants.WRAPPER) queryWrapper: Wrapper<T>): T
```

### 3. 分页查询统一结构
将分页查询结果封装为全局统一的结构 `PageResultLike`，方便前端处理。

```kotlin
// 分页查询出全局统一结构
fun selectPageResult(page: PageQueryLike<*>, @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>?): PageResultLike<T>
```

### 4. `TonyChainQuery` 新增方法
`TonyChainQuery` 扩展了 `ChainQuery`，新增了许多实用方法，用于处理查询结果和异常情况。

```kotlin
// 查询单条记录，为 null 时抛错
fun oneNotNull(): T
fun oneNotNull(message: String): T
fun oneNotNull(message: String, ex: (message: String, code: Int) -> BaseException): T

// 查询某个条件是否存在，存在就抛错
fun throwIfExists(message: String)
fun throwIfExists(message: String, ex: (message: String, code: Int) -> BaseException)

// 查询某个条件是否不存在，不存在就抛错
fun throwIfNotExists(message: String)
fun throwIfNotExists(message: String, ex: (message: String, code: Int) -> BaseException)

// 列表查询，并根据转换器对结果进行转换
fun <R> list(transformer: java.util.function.Function<T, R>): List<R>

// 当列表为 null 或者空时，抛出异常
fun listThrowIfEmpty(): List<T>
fun listThrowIfEmpty(message: String): List<T>
fun listThrowIfEmpty(message: String, ex: (message: String, code: Int) -> BaseException): List<T>

// 分页查询出全局统一结构
fun pageResult(page: PageQueryLike<*>): PageResultLike<T>

// 查询全局统一分页结构
fun mapPageResult(page: PageQueryLike<*>): PageResultLike<Map<String, Any?>>

// 查询全部记录，只返回第一个字段的值
fun <E> listObj(): List<E?>
fun <E> listObjThrowIfEmpty(): List<E?>
fun <E> listObjThrowIfEmpty(message: String): List<E?>
fun <E> listObjThrowIfEmpty(message: String, ex: (message: String, code: Int) -> BaseException): List<E?>

// 查询单条记录，只返回第一个字段的值
fun <E> oneObj(): E?
fun <E> oneObjNotNull(): E
fun <E> oneObjNotNull(message: String): E
fun <E> oneObjNotNull(message: String, ex: (message: String, code: Int) -> BaseException): E

// 查询全部记录
fun listMap(): List<Map<String, Any?>>
fun listMapThrowIfEmpty(): List<Map<String, Any?>>
fun listMapThrowIfEmpty(message: String): List<Map<String, Any?>>
fun listMapThrowIfEmpty(message: String, ex: (message: String, code: Int) -> BaseException): List<Map<String, Any?>>

// 查询单条记录
fun oneMap(): Map<String, Any?>?
```

### 5. 字段自动填充
通过 `DbMetaObjectHandler` 实现了字段的自动填充功能，根据注解和会话信息自动填充相关字段。

## 字段自动填充注解用法
### `@MybatisPlusMetaProperty` 注解
该注解用于标记需要自动填充的字段，并指定填充的元数据类型。

```kotlin
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class MybatisPlusMetaProperty(
    val value: MetaColumn,
    val relativeProp: String = ""
)
```

### `MetaColumn` 枚举
定义了可自动填充的元数据类型：

```kotlin
enum class MetaColumn {
    USER_ID,         // 当前用户ID
    USER_NAME,       // 当前用户名
    DEPT_ID,         // 当前用户部门ID
    DEPT_NAME,       // 当前用户部门名称
    TENANT_ID,       // 当前租户ID
}
```

### 注解使用示例
```kotlin
@TableName("sys_role")
class Role {

    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorId: String = ""

    @MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorName: String = ""

    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(
        fill = FieldFill.INSERT_UPDATE,
        updateStrategy = FieldStrategy.NOT_NULL
    )
    var updaterId: String = ""

    @MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "updaterId")
    @TableField(
        fill = FieldFill.INSERT_UPDATE,
        updateStrategy = FieldStrategy.NOT_NULL
    )
    var updaterName: String = ""
}
```

### 注解参数说明
- **`value`**: 指定要填充的元数据类型，如 `USER_ID`、`USER_NAME` 等。
- **`relativeProp`**: 指定关联的属性名，例如当填充 `USER_NAME` 时，可关联到 `creatorId` 或 `updaterId`，确保名称与ID的一致性。

## 使用示例
### 物理删除示例
```kotlin
val baseDao: BaseDao<YourEntity> = ...
val queryWrapper = QueryWrapper<YourEntity>().eq("column", "value")
val rows = baseDao.physicalDelete(queryWrapper)
```

### `TonyChainQuery` 示例
```kotlin
val chainQuery: TonyChainQuery<YourEntity> = baseDao.query()
val result = chainQuery.eq("column", "value").oneNotNull()
```
