# 概述

`tony-monolithic-demo` 是一个基于 **Kotlin + Spring Boot 3.5.0+** 的高性能单体应用开发脚手架，集成主流企业级技术栈与最佳实践，助力开发者快速搭建规范、易扩展的业务系统。

---

## 🚩 项目特色

- **全栈集成**：数据库、缓存、定时任务、接口文档、参数校验等一站式集成
- **模块清晰**：API 层、业务层、DTO、定时任务分层明确，便于维护
- **开发友好**：内置 Swagger/Knife4j 文档、数据校验、代码规范
- **易于扩展**：预留微服务接口，支持平滑升级为微服务架构
- **容器化支持**：内置 Dockerfile 和一键构建脚本，方便部署

---

## 📦 项目结构

```
tony-monolithic-demo/
├── tony-api/         # API 接口层（控制器、RESTful API、文档）
│   ├── Dockerfile
│   └── docker-entrypoint.sh
├── tony-service/     # 业务逻辑层（服务实现、数据访问）
├── tony-job/         # 定时任务模块
├── tony-dto/         # 数据传输对象（DTO、VO、表单验证）
├── db/               # 数据库初始化脚本
└── dockerBuild.sh    # Docker 构建和运行脚本
```

---

## 🔍 核心模块说明

- **tony-api**：基于 Spring Boot Web，集成 Knife4j，统一响应与异常处理，支持多环境配置
- **tony-service**：MyBatis-Plus 简化数据库操作，集成 Redis 缓存，预留 OpenFeign 扩展
- **tony-job**：基于 PowerJob 的分布式定时任务，支持调度、回调、可视化管理
- **tony-dto**：定义 API 请求/响应格式，集成 Swagger 注解与参数校验
- **db/**：MySQL/PostgreSQL 建表与初始化脚本

---

## 🛠️ 技术栈

| 领域         | 技术选型                 | 版本             |
|--------------|--------------------------|----------------|
| 编程语言     | Kotlin                   | 2.1.21+        |
| 构建工具     | Gradle                   | 8.14.1+        |
| Web 框架     | Spring Boot              | 3.5.0+         |
| 定时任务     | PowerJob                 | 5.1.1          |
| 接口文档     | Knife4j                  | 4.6.0          |
| 数据库       | MySQL / PostgreSQL       | 9.3.0 / 42.7.7 |

---

## 🚀 快速开始

### 1. 环境准备

- JDK 21+
- Gradle 8.14.1+
- Redis 6.x+
- MySQL 或 PostgreSQL

### 2. 数据库初始化

```bash
# 以 MySQL 为例
mysql -u root -p < db/userAndDatabaseBase.init.mysql.sql
mysql -u root -p your_database_name < db/table.init.mysql.sql
mysql -u root -p your_database_name < db/data.init.mysql.sql
```

### 3. 配置项目

编辑 `tony-api/src/main/resources/application.yml`：

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
./gradlew :tony-api:bootRun
```

### 5. Docker 构建与运行

- 应用 `tony.gradle.plugin.docker` 插件（见 build.gradle.kts）
- 配置 `gradle.properties` 或命令行参数（见下表）

| 参数名           | 说明                   |
|------------------|------------------------|
| dockerRegistry   | 镜像仓库地址           |
| dockerUserName   | 用户名                 |
| dockerPassword   | 密码                   |
| dockerNameSpace  | 命名空间               |
| projectName      | 项目名（可选）         |

- 构建镜像示例：

```bash
./dockerBuild.sh -r your-docker-registry -d /path/to/project -p 8080 -n tony-monolithic-demo -P prod -N your-org -t v1.0
```

- 运行容器：

```bash
docker run -d -p 8080:8080 --name tony-demo tony-monolithic-demo
```

### 6. 访问接口文档

[http://localhost:8080/doc.html](http://localhost:8080/doc.html)

---

## 💡 典型场景

- 中小型项目的单体架构模板
- 业务原型/POC 快速开发
- 单体向微服务演进的起点
- 企业级统一能力最佳实践

---

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)，可自由用于商业项目。

---

如需进一步个性化内容或有疑问，欢迎 issue 反馈或联系维护者！
