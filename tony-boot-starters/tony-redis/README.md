## 概述

`tony-redis` 是 `tony-boot-starters` 体系下的 Redis 缓存集成模块，提供统一的 Redis 操作接口、声明式缓存注解与切面、灵活的缓存键生成和过期策略，支持对象自动序列化、分布式锁、批量操作、事务等能力，极大简化分布式缓存和高并发场景下的数据一致性开发。

## 目录

- [如何使用](#如何使用)
- [主要功能](#主要功能)
  - [声明式缓存注解与切面](#1-声明式缓存注解与切面)
  - [灵活的缓存键与表达式](#2-灵活的缓存键与表达式)
  - [多样的过期策略](#3-多样的过期策略)
  - [RedisManager 操作聚合](#4-redismanager-操作聚合)
  - [分布式锁与事务支持](#5-分布式锁与事务支持)
  - [多序列化与类型安全](#6-多序列化与类型安全)
- [配置说明](#配置说明)
- [使用示例](#使用示例)
- [进阶用法](#进阶用法)
- [适用场景](#适用场景)
- [注意事项](#注意事项)

## 如何使用

### 环境要求
- **Java 21** 或更高版本
- **Spring Boot 3.x**
- **Redis 6.x/7.x** 推荐

### 添加依赖

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("tony:tony-redis:0.1-SNAPSHOT")
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

### 1. 声明式缓存注解与切面

- `@RedisCacheable`：方法级缓存，支持动态缓存键、表达式参数、灵活过期策略。
- `@RedisCacheEvict`：方法级缓存清理，支持批量、条件删除。
- 通过 `RedisCacheAspect` 切面自动拦截并管理缓存，无需手动操作 Redis。

### 2. 灵活的缓存键与表达式

- 支持 `%s` 占位符与 `expressions` 参数，自动绑定方法参数或对象属性，缓存键灵活可控。
- 支持 SpEL 表达式解析，复杂对象属性也可作为缓存键一部分。

### 3. 多样的过期策略

- 支持永不过期、配置默认、当天结束、指定秒数等多种过期策略，满足不同业务需求。

### 4. RedisManager 操作聚合

- 提供 `RedisManager.values/maps/lists/keys` 四大操作接口，支持对象、Map、List、Key 等常用 Redis 操作。
- 自动序列化/反序列化，兼容多种数据结构。
- 支持原子操作、批量删除、分布式锁、事务等高级能力。

### 5. 分布式锁与事务支持

- 内置分布式锁实现，支持超时与自旋等待。
- 支持 Redis 事务操作，保障多步操作的原子性。

### 6. 多序列化与类型安全

- 默认采用 Jackson 进行对象序列化，支持复杂对象、泛型安全存取。
- 支持多种序列化模式（如 JDK、STRING、FASTJSON），可自定义切换。

## 配置说明

### 基础配置

```yaml
redis:
  serializer-mode: JACKSON   # 支持JACKSON、JDK、STRING、FASTJSON等
  key-prefix: "myapp"
```

## 使用示例

### 缓存注解

```kotlin
@RedisCacheable(
    cacheKey = "user:detail:%s:%s",
    expressions = ["id", "field"],
    expire = RedisCacheable.TODAY_END
)
fun getUserField(id: String, field: String): String?

@RedisCacheEvict(
    cacheKey = "order:list:%s:%s",
    expressions = ["userId", "status"]
)
fun updateOrderStatus(userId: String, status: String)
```

### RedisManager 基础操作

```kotlin
// 存储对象
RedisManager.values.set("user:1", User("1", "张三"), 3600)

// 获取对象
val user: User? = RedisManager.values.get("user:1")

// 原子自增
val count: Long? = RedisManager.values.increment("counter:order", 1)

// 批量删除
RedisManager.keys.delete(listOf("user:1", "user:2"))

// 分布式锁
val locked = RedisManager.lockKey("lock:order:123", 10)
```

### 复杂对象属性作为缓存键

```kotlin
@RedisCacheable(
    cacheKey = "order:detail:%s:%s:%s",
    expressions = ["order.id", "order.status", "user.id"]
)
fun getOrderDetail(order: Order, user: User): OrderDetail?
```
