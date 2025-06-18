## 概述

`tony-redis` 提供统一的 Redis 操作接口和缓存注解，通过标准化的参数处理与 Jackson 类型转换机制简化 Redis 操作，并通过切面实现声明式缓存管理。

## 缓存注解与切面实现

## 如何使用
- Java 21 或更高版本


在 `build.gradle.kts` 中添加：
```kotlin
dependencies {
    implementation("tony:tony-redis:0.1-SNAPSHOT")
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

### 1. 核心注解列表
通过 `RedisCacheAspect` 切面实现以下注解：

```kotlin
// 缓存方法返回值
@RedisCacheable(
    cacheKey = "user:detail:%s:%s",
    expressions = ["id", "field"],
    expire = RedisCacheable.TODAY_END
)
fun getUserField(id: String, field: String): String?

// 删除指定缓存（可重复注解）
@RedisCacheEvict(
    cacheKey = "order:list:%s:%s",
    expressions = ["userId", "status"]
)
fun updateOrderStatus(userId: String, status: String)
```

### 2. 注解参数说明

| 注解              | 核心参数               | 类型    | 说明                                                                 |
|-------------------|------------------------|---------|----------------------------------------------------------------------|
| `@RedisCacheable` | `cacheKey`             | String  | 缓存键模板，使用 `%s` 作为占位符，通过 String.format 生成最终键       |
|                   | `expressions`          | Array   | 表达式数组，按顺序对应 `cacheKey` 中的占位符，直接使用参数名           |
|                   | `expire`               | Long    | 过期时间策略（默认 `-3`，表示到当天结束）                           |
| `@RedisCacheEvict`| `cacheKey`             | String  | 要删除的缓存键模板，使用 `%s` 作为占位符                              |
|                   | `expressions`          | Array   | 表达式数组，按顺序对应 `cacheKey` 中的占位符，直接使用参数名           |

### 3. 缓存键生成逻辑
1. **模板定义**：`cacheKey` 中使用 `%s` 定义占位符（如 `"user:detail:%s:%s"`）
2. **参数绑定**：`expressions` 数组中的每个表达式按顺序绑定占位符
3. **动态替换**：运行时通过方法参数获取值，使用 String.format 替换占位符

```kotlin
@RedisCacheable(
    cacheKey = "user:detail:%s:%s",
    expressions = ["id", "field"]
)
fun getUserField(id: String, field: String): String?
// 当调用 getUserField("123", "name") 时，生成的缓存键为 "user:detail:123:name"
```

### 4. 表达式解析规则
- **简单参数**：直接使用方法参数名（如 `id` 对应方法参数 `id`）
- **对象属性**：使用 `对象名.属性名`（如 `user.id` 对应 `user` 对象的 `id` 属性）

```kotlin
@RedisCacheable(
    cacheKey = "order:detail:%s:%s:%s",
    expressions = ["order.id", "order.status", "user.id"]
)
fun getOrderDetail(order: Order, user: User): OrderDetail?
// 假设 order.id=456, order.status="PAID", user.id=789，生成键为 "order:detail:456:PAID:789"
```

### 5. 超时策略
`expire` 参数支持特殊值：
- `-1`：永不过期
- `-2`：使用配置文件中的默认超时（`redis.default-expire`）
- `-3`：到当天结束（自动计算剩余秒数，如 `RedisCacheable.TODAY_END`）
- 正数：具体超时秒数（如 `3600` 表示 1 小时）


## 核心操作示例

### 1. RedisManager 基础操作
提供统一的 Redis 操作接口，支持自动序列化/反序列化：

```kotlin
// 存储对象
RedisManager.values.set("user:1", User("1", "张三"), 3600)

// 获取对象
val user: User? = RedisManager.values.get("user:1")

// 原子操作
val count: Long = RedisManager.values.increment("counter:order", 1)

// 批量删除
RedisManager.keys.delete(listOf("user:1", "user:2"))
```

### 2. 缓存注解应用示例
```kotlin
@Service
class ProductService {

    @RedisCacheable(
        cacheKey = "product:detail:%s",
        expressions = ["id"],
        expire = 600  // 10 分钟过期
    )
    fun getProductDetail(id: String): ProductDetail? {
        // 数据库查询逻辑，未命中缓存时执行
    }

    @RedisCacheEvict(
        cacheKey = "product:detail:%s",
        expressions = ["product.id"]
    )
    @RedisCacheEvict(
        cacheKey = "product:list:%s",
        expressions = ["product.categoryId"]
    )
    fun updateProduct(product: Product) {
        // 数据库更新逻辑，完成后删除相关缓存
    }
}
```
