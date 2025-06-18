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
├── tony-api/ # API 接口层（控制器、RESTful API）
│ ├── Dockerfile # Docker 构建文件
│ └── docker-entrypoint.sh # Docker 容器启动脚本
├── tony-service/ # 业务逻辑层（服务实现、数据访问）
├── tony-job/ # 定时任务模块
├── tony-dto/ # 数据传输对象（DTO、VO、表单验证）
├── db/ # 数据库初始化脚本
└── dockerBuild.sh # Docker 构建和运行脚本
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

### 4. 使用 Docker 构建和运行应用

#### 4.1 应用 Docker 插件
在项目的 `build.gradle.kts` 文件中应用 `tony.gradle.plugin.docker` 插件：
```kotlin
apply(plugin = "tony.gradle.plugin.docker")
```

#### 4.2 配置 Docker 相关信息
Docker 相关配置可以在 `gradle.properties` 中配置，也可以直接通过命令行传递。以下是可配置的参数及其说明：

| 参数名 | 说明 |
| ---- | ---- |
| `dockerRegistry` | Docker 镜像仓库地址 |
| `dockerUserName` | Docker 用户名 |
| `dockerPassword` | Docker 用户密码 |
| `dockerNameSpace` | Docker 命名空间 |
| `projectName` | 项目名称，若指定会覆盖默认的项目根目录名称 |

**在 `gradle.properties` 中配置示例**：
```properties
dockerRegistry=your-docker-registry
dockerUserName=your-docker-username
dockerPassword=your-docker-password
dockerNameSpace=your-docker-namespace
projectName=your-project-name
```

**通过命令行传递参数示例**：
```bash
./gradlew dockerBuild -PdockerRegistry=your-docker-registry -PdockerUserName=your-docker-username -PdockerPassword=your-docker-password -PdockerNameSpace=your-docker-namespace -PprojectName=your-project-name
```

#### 4.3 构建 Docker 镜像
使用 `dockerBuild.sh` 脚本构建 Docker 镜像，脚本支持以下参数：
```bash
-r|--docker-registry    # Docker 镜像仓库地址
-d|--project-dir        # 项目目录
-p|--port               # 应用端口
-n|--project-name       # 项目名称
-P|--profile            # 配置文件环境（默认为 qa）
-N|--docker-org-name    # Docker 组织名称（默认为 publisher）
-t|--image-tag          # Docker 镜像标签（默认为 latest）
-e|--env-file           # 环境变量文件
-o|--overwrite-config   # 是否覆盖配置文件（默认为 false）
```

示例命令：
```bash
./dockerBuild.sh -r your-docker-registry -d /path/to/project -p 8080 -n tony-monolithic-demo -P prod -N your-org -t v1.0
```

#### 4.4 运行 Docker 容器
构建完成后，脚本会自动拉取最新镜像并启动 Docker 容器。启动成功后，容器会在后台运行。

### 5. 启动应用

```bash
# 启动应用
./gradlew :tony-api:bootRun
```

### 6. 访问接口文档

启动成功后，访问：[http://localhost:8080/doc.html](http://localhost:8080/doc.html)

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)，可自由用于商业项目。
