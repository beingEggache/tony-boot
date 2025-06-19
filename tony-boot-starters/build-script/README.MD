## 概述

`tony-build` 项目提供了一系列自定义的 Gradle 插件，用于简化项目的构建、依赖管理、代码检查和发布等流程。以下是各个插件的详细介绍、用法、配置和代码示例。

## 插件列表及使用说明

### 1. `tony.gradle.plugin.build`
#### 功能描述
该插件提供了一些通用的项目属性获取方法，方便在项目中统一配置和使用模板信息。

#### 用法
在项目的 `build.gradle.kts` 文件中应用该插件：
```kotlin
apply(plugin = "tony.gradle.plugin.build")
```

#### 配置说明
可通过系统属性或 Gradle 属性来配置模板信息，例如在 `gradle.properties` 中设置：
```properties
templateGroup=com.example
templatePrefix=tony
templateVersion=1.0.0
```

#### 代码示例
在 `build.gradle.kts` 中获取模板信息：
```kotlin
group = templateGroup()
version = templateVersion()
```

### 2. `tony.gradle.plugin.dep-configurations`
#### 功能描述
该插件用于统一配置项目的依赖排除和替换策略，避免依赖冲突。

#### 用法
在项目的 `build.gradle.kts` 文件中应用该插件：
```kotlin
apply(plugin = "tony.gradle.plugin.dep-configurations")
```
插件内部已经定义了一系列依赖排除规则，例如排除某些日志库、Kotlin 标准库变体等。

### 3. `tony.gradle.plugin.ktlint`
#### 功能描述
该插件集成了 Ktlint 代码检查工具，用于规范 Kotlin 代码风格。

#### 用法
在项目的 `build.gradle.kts` 文件中应用该插件：
```kotlin
apply(plugin = "tony.gradle.plugin.ktlint")
```

#### 配置说明
可以在 `.editorconfig` 中配置具体规则，例如：
```editorconfig
root = true

[*]
charset = utf-8
indent_style = space
indent_size = 4
trim_trailing_whitespace = true
insert_final_newline = true
end_of_line = lf

[*.{yml,yaml}]
indent_size = 2

# noinspection EditorConfigKeyCorrectness
[/*/src/main/**.{kt,kts}]
ktlint_code_style = ktlint_official

#  Disable wildcard imports entirely
ij_kotlin_name_count_to_use_star_import = 2147483647
ij_kotlin_name_count_to_use_star_import_for_members = 2147483647
ij_kotlin_packages_to_use_import_on_demand = unset
```

插件会自动在项目中添加 Ktlint 任务，可通过以下命令运行代码检查或格式化：
```sh
./gradlew ktlintCheck
./gradlew ktlintFormat
```

### 4. `tony.gradle.plugin.docker`
#### 功能描述
该插件用于构建和发布 Docker 镜像。

#### 用法
在项目的 `build.gradle.kts` 文件中应用该插件：
```kotlin
apply(plugin = "tony.gradle.plugin.docker")
```

#### 配置说明
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

#### 代码示例
插件会自动添加 Docker 构建和发布任务，可通过以下命令构建和发布 Docker 镜像：
```sh
./gradlew dockerBuild
./gradlew dockerPush
```

## 适用场景

- 多模块、多环境的企业级 Kotlin/Java 项目
- 需要统一依赖、构建、发布、镜像、代码规范的团队协作开发
- 需要自动化 CI/CD、DevOps 流程的项目

## 常见问题

- **依赖冲突/排除**：如遇依赖冲突，可通过 `dep-configurations` 插件自动排除或替换。
- **Docker 构建失败**：请确保 Dockerfile、bootJar 产物、相关参数配置正确。
- **代码风格检查失败**：请检查 `.editorconfig` 配置和 Ktlint 规则设置。
