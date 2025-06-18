# 概述

`tony-monolithic-demo` 是一个基于 **Kotlin + Spring Boot 3.5.0** 的高性能单体应用开发脚手架，集成了主流技术栈并遵循最佳实践，旨在帮助开发者快速搭建企业级应用架构，减少重复工作，提升开发效率。


## 🔥 项目特色

- **全栈技术集成**：一站式集成数据库访问、缓存、定时任务、接口文档等企业级功能
- **模块化设计**：清晰分离 API 层、业务逻辑层、数据传输对象和定时任务
- **开发友好**：内置 Swagger 文档、数据验证和代码规范
- **可扩展性**：预留微服务集成接口，支持无缝升级为微服务架构


## 📦 项目结构

```
tony-monolithic-demo/
├── tony-api/          # API 接口层（控制器、RESTful API）
├── tony-service/      # 业务逻辑层（服务实现、数据访问）
├── tony-job/          # 定时任务模块
├── tony-dto/          # 数据传输对象（DTO、VO、表单验证）
└── db/                # 数据库初始化脚本
```


### 核心模块详解

#### 🔌 `tony-api` - API 接口层
- 基于 Spring Boot Web 构建 RESTful API
- 集成 **Knife4j 4.6.0** 自动生成接口文档（支持在线调试）
- 提供统一响应格式和异常处理
- 支持多环境配置（开发/测试/生产）

#### 💼 `tony-service` - 业务逻辑层
- 使用 **MyBatis-Plus 3.5.12** 简化数据库操作（支持 Lambda 表达式）
- 集成 **Redis**（Lettuce 6.7.1）实现分布式缓存
- 支持 **OpenFeign 13.6** 远程调用（为微服务扩展预留）
- 事务管理与业务逻辑分离

#### 🕰️ `tony-job` - 定时任务模块
- 基于 **PowerJob 5.1.1** 实现分布式定时任务
- 支持任务调度、参数传递和结果回调
- 可视化管理界面（需独立部署 PowerJob Server）

#### 📊 `tony-dto` - 数据传输层
- 定义 API 请求/响应格式
- 使用 **Swagger V3 2.2.33** 注解生成接口文档
- 集成 **Java Validation API 3.1.1** 实现参数校验
- 包含常用数据转换工具


## 🛠️ 技术栈

| 领域         | 技术选型                 | 版本       |
|--------------|--------------------------|------------|
| 编程语言     | Kotlin                   | 2.1.21     |
| 构建工具     | Gradle                   | 8.14.1+    |
| Web 框架     | Spring Boot              | 3.5.0      |
| 定时任务     | PowerJob                 | 5.1.1      |
| 接口文档     | Knife4j                  | 4.6.0      |
| 数据库       | MySQL / PostgreSQL       | 9.3.0 / 42.7.7 |


## 🚀 快速开始

### 1. 环境准备

```bash
# 安装 JDK 21+
# 安装 Gradle 8.14.1+
# 启动 Redis (6.x+)
# 启动 MySQL 或 PostgreSQL
```

### 2. 数据库初始化

```bash
# 执行 db 目录下的脚本（以 MySQL 为例）
mysql -u root -p < db/userAndDatabaseBase.init.mysql.sql
mysql -u root -p your_database_name < db/table.init.mysql.sql
mysql -u root -p your_database_name < db/data.init.mysql.sql
```

### 3. 配置项目

修改 `tony-api/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database_name
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
```

### 4. 启动应用

```bash
# 启动应用
./gradlew :tony-api:bootRun
```

### 5. 访问接口文档

启动成功后，访问：[http://localhost:8080/doc.html](http://localhost:8080/doc.html)


## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)，可自由用于商业项目。
