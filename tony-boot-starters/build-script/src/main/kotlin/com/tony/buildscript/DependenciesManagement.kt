@file:Suppress("unused", "SpellCheckingInspection", "ConstPropertyName")

package com.tony.buildscript

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyConstraintHandlerScope

internal object DepsManagement {

    object Jackson {
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations:${VersionManagement.JACKSON_VERSION}"
        const val core = "com.fasterxml.jackson.core:jackson-core:${VersionManagement.JACKSON_VERSION}"
        const val databind = "com.fasterxml.jackson.core:jackson-databind:${VersionManagement.JACKSON_VERSION}"
        const val datatypeJdk8 =
            "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${VersionManagement.JACKSON_VERSION}"
        const val datatypeJsr310 =
            "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${VersionManagement.JACKSON_VERSION}"
        const val moduleKotlin =
            "com.fasterxml.jackson.module:jackson-module-kotlin:${VersionManagement.JACKSON_VERSION}"
        const val moduleParameterNames =
            "com.fasterxml.jackson.module:jackson-module-parameter-names:${VersionManagement.JACKSON_VERSION}"
    }

    object Kotlin {
        const val bom = "org.jetbrains.kotlin:kotlin-bom:${VersionManagement.KOTLIN_VERSION}"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${VersionManagement.KOTLIN_VERSION}"
        const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${VersionManagement.KOTLIN_VERSION}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${VersionManagement.KOTLIN_VERSION}"
        const val test = "org.jetbrains.kotlin:kotlin-test:${VersionManagement.KOTLIN_VERSION}"
        const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:${VersionManagement.KOTLIN_VERSION}"
    }

    object Netty {
        const val all = "io.netty:netty-all:${VersionManagement.NETTY_VERSION}"
        const val buffer = "io.netty:netty-buffer:${VersionManagement.NETTY_VERSION}"
        const val common = "io.netty:netty-common:${VersionManagement.NETTY_VERSION}"
        const val resolver = "io.netty:netty-resolver:${VersionManagement.NETTY_VERSION}"
        const val resolverDns = "io.netty:netty-resolver-dns:${VersionManagement.NETTY_VERSION}"
        const val resolverDnsMacOs = "io.netty:netty-resolver-dns-native-macos:${VersionManagement.NETTY_VERSION}"
        const val transport = "io.netty:netty-transport:${VersionManagement.NETTY_VERSION}"
        const val transportNativeUnixCommon =
            "io.netty:netty-transport-native-unix-common:${VersionManagement.NETTY_VERSION}"
        const val transportNativeEpoll = "io.netty:netty-transport-native-epoll:${VersionManagement.NETTY_VERSION}"
        const val handler = "io.netty:netty-handler:${VersionManagement.NETTY_VERSION}"
        const val handlerProxy = "io.netty:netty-handler-proxy:${VersionManagement.NETTY_VERSION}"
        const val codec = "io.netty:netty-codec:${VersionManagement.NETTY_VERSION}"
        const val codecHttp = "io.netty:netty-codec-http:${VersionManagement.NETTY_VERSION}"
        const val codecHttp2 = "io.netty:netty-codec-http2:${VersionManagement.NETTY_VERSION}"
        const val codecSocks = "io.netty:netty-codec-socks:${VersionManagement.NETTY_VERSION}"
    }

    object Spring {
        const val aop = "org.springframework:spring-aop:${VersionManagement.SPRING_VERSION}"
        const val aspects = "org.springframework:spring-aspects:${VersionManagement.SPRING_VERSION}"
        const val beans = "org.springframework:spring-beans:${VersionManagement.SPRING_VERSION}"
        const val context = "org.springframework:spring-context:${VersionManagement.SPRING_VERSION}"
        const val contextSupport = "org.springframework:spring-context-support:${VersionManagement.SPRING_VERSION}"
        const val contextIndexer = "org.springframework:spring-context-indexer:${VersionManagement.SPRING_VERSION}"
        const val core = "org.springframework:spring-core:${VersionManagement.SPRING_VERSION}"
        const val expression = "org.springframework:spring-expression:${VersionManagement.SPRING_VERSION}"
        const val jcl = "org.springframework:spring-jcl:${VersionManagement.SPRING_VERSION}"
        const val jdbc = "org.springframework:spring-jdbc:${VersionManagement.SPRING_VERSION}"
        const val orm = "org.springframework:spring-orm:${VersionManagement.SPRING_VERSION}"
        const val oxm = "org.springframework:spring-oxm:${VersionManagement.SPRING_VERSION}"
        const val test = "org.springframework:spring-test:${VersionManagement.SPRING_VERSION}"
        const val tx = "org.springframework:spring-tx:${VersionManagement.SPRING_VERSION}"
        const val web = "org.springframework:spring-web:${VersionManagement.SPRING_VERSION}"
        const val webmvc = "org.springframework:spring-webmvc:${VersionManagement.SPRING_VERSION}"
        const val webflux = "org.springframework:spring-webflux:${VersionManagement.SPRING_VERSION}"
    }

    object OpenFeign {
        const val openFeignCore = "io.github.openfeign:feign-core:${VersionManagement.OPEN_FEIGN_VERSION}"
        const val openFeignSl4j = "io.github.openfeign:feign-slf4j:${VersionManagement.OPEN_FEIGN_VERSION}"
        const val openFeignOkhttp = "io.github.openfeign:feign-okhttp:${VersionManagement.OPEN_FEIGN_VERSION}"
        const val openFeignJackson = "io.github.openfeign:feign-jackson:${VersionManagement.OPEN_FEIGN_VERSION}"
    }

    object Knife4j {
        const val core = "com.github.xiaoymin:knife4j-core:${VersionManagement.KNIFE4J_VERSION}"
        const val openapi3SpringBootStarter =
            "com.github.xiaoymin:knife4j-openapi3-spring-boot-starter:${VersionManagement.KNIFE4J_VERSION}"
        const val openapi3JakartaSpringBootStarter =
            "com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:${VersionManagement.KNIFE4J_VERSION}"
        const val openapi3Ui =
            "com.github.xiaoymin:knife4j-openapi3-ui:${VersionManagement.KNIFE4J_VERSION}"
    }

    object SpringBoot {
        const val springBoot = "org.springframework.boot:spring-boot:${VersionManagement.SPRING_BOOT_VERSION}"
        const val springBootStarter =
            "org.springframework.boot:spring-boot-starter:${VersionManagement.SPRING_BOOT_VERSION}"
        const val autoconfigure =
            "org.springframework.boot:spring-boot-autoconfigure:${VersionManagement.SPRING_BOOT_VERSION}"
        const val configurationProcessor =
            "org.springframework.boot:spring-boot-configuration-processor:${VersionManagement.SPRING_BOOT_VERSION}"
        const val autoconfigureProcessor =
            "org.springframework.boot:spring-boot-autoconfigure-processor:${VersionManagement.SPRING_BOOT_VERSION}"
        const val devtools = "org.springframework.boot:spring-boot-devtools:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterActuator =
            "org.springframework.boot:spring-boot-starter-actuator:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterAmqp =
            "org.springframework.boot:spring-boot-starter-amqp:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterAop = "org.springframework.boot:spring-boot-starter-aop:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterCache =
            "org.springframework.boot:spring-boot-starter-cache:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterJdbc =
            "org.springframework.boot:spring-boot-starter-jdbc:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterJson =
            "org.springframework.boot:spring-boot-starter-json:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterLogging =
            "org.springframework.boot:spring-boot-starter-logging:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterReactorNetty =
            "org.springframework.boot:spring-boot-starter-reactor-netty:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterTest =
            "org.springframework.boot:spring-boot-starter-test:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterTomcat =
            "org.springframework.boot:spring-boot-starter-tomcat:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterUndertow =
            "org.springframework.boot:spring-boot-starter-undertow:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterValidation =
            "org.springframework.boot:spring-boot-starter-validation:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterWebflux =
            "org.springframework.boot:spring-boot-starter-webflux:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterWebsocket =
            "org.springframework.boot:spring-boot-starter-websocket:${VersionManagement.SPRING_BOOT_VERSION}"
    }

    object SpringData {
        const val starterRedis =
            "org.springframework.boot:spring-boot-starter-data-redis:${VersionManagement.SPRING_BOOT_VERSION}"
        const val starterRedisReactive =
            "org.springframework.boot:spring-boot-starter-data-redis-reactive:${VersionManagement.SPRING_BOOT_VERSION}"
    }

    object Test {
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${VersionManagement.KOTLIN_VERSION}"
        const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:${VersionManagement.KOTLIN_VERSION}"
        const val springBootStarterTest =
            "org.springframework.boot:spring-boot-starter-test:${VersionManagement.SPRING_BOOT_VERSION}"
    }

    object Other {
        const val lettuce = "io.lettuce:lettuce-core:${VersionManagement.LETTUCE_CORE_VERSION}"

        const val mybatis = "org.mybatis:mybatis:${VersionManagement.MYBATIS_VERSION}"
        const val mybatisSpring = "org.mybatis:mybatis-spring:${VersionManagement.MYBATIS_SPRING_VERSION}"
        const val mybatisPlusAnnotation = "com.baomidou:mybatis-plus-annotation:${VersionManagement.MYBATIS_PLUS_VERSION}"
        const val mybatisPlusExtension = "com.baomidou:mybatis-plus-extension:${VersionManagement.MYBATIS_PLUS_VERSION}"
        const val mybatisPlusBootStarter =
            "com.baomidou:mybatis-plus-boot-starter:${VersionManagement.MYBATIS_PLUS_VERSION}"
        const val mybatisPlusBootStarter3 =
            "com.baomidou:mybatis-plus-spring-boot3-starter:${VersionManagement.MYBATIS_PLUS_VERSION}"
        const val mybatisPlusGenerator = "com.baomidou:mybatis-plus-generator:${VersionManagement.MYBATIS_PLUS_VERSION}"

        const val mybatisTypehandlersJsr310 = "org.mybatis:mybatis-typehandlers-jsr310:1.0.2"
        const val validationApi = "jakarta.validation:jakarta.validation-api:${VersionManagement.VALIDATION_API_VERSION}"
        const val annotationApi = "jakarta.annotation:jakarta.annotation-api:${VersionManagement.ANNOTATION_API_VERSION}"
        const val activationApi = "jakarta.activation:jakarta.activation-api:${VersionManagement.ACTIVATION_API_VERSION}"
        const val elApi = "jakarta.el:jakarta.el-api:${VersionManagement.EL_API_VERSION}"
        const val websocketApi = "jakarta.websocket:jakarta.websocket-api:${VersionManagement.WEBSOCKET_API_VERSION}"
        const val bindApi = "jakarta.xml.bind:jakarta.xml.bind-api:${VersionManagement.BIND_API_VERSION}"

        const val gson = "com.google.code.gson:gson:${VersionManagement.GSON_VERSION}"
        const val fastjson = "com.alibaba:fastjson:${VersionManagement.FASTJSON_VERSION}"

        const val xstream = "com.thoughtworks.xstream:xstream:1.4.20"
        const val httpclient = "org.apache.httpcomponents:httpclient:4.5.14"
        const val httpcore = "org.apache.httpcomponents:httpcore:4.4.16"
        const val httpcoreNio = "org.apache.httpcomponents:httpcore-nio:4.4.16"
        const val httpasyncclient = "org.apache.httpcomponents:httpasyncclient:4.1.5"
        const val okhttp = "com.squareup.okhttp3:okhttp:4.12.0"

        const val commonFileUpload = "commons-fileupload:commons-fileupload:1.5"
        const val commonsCodec = "commons-codec:commons-codec:1.16.0"
        const val commonsIo = "commons-io:commons-io:2.14.0"
        const val commonsCompress = "org.apache.commons:commons-compress:1.24.0"
        const val commonsPool2 = "org.apache.commons:commons-pool2:2.12.0"
        const val commonsLang3 = "org.apache.commons:commons-lang3:3.13.0"

        const val guava = "com.google.guava:guava:${VersionManagement.GUAVA_VERSION}"
        const val findbugsJsr305 = "com.google.code.findbugs:jsr305:3.0.2"
        const val javaJwt = "com.auth0:java-jwt:${VersionManagement.JAVA_JWT_VERSION}"

        const val postgresql = "org.postgresql:postgresql:${VersionManagement.POSTGRESQL_VERSION}"
        const val mysql = "com.mysql:mysql-connector-j:${VersionManagement.MYSQL_VERSION}"
        const val HikariCP = "com.zaxxer:HikariCP:${VersionManagement.HIKARI_CP_VERSION}"

        const val xxlJob = "com.xuxueli:xxl-job-core:${VersionManagement.XXL_JOB_VERSION}"

        const val slf4JApi = "org.slf4j:slf4j-api:${VersionManagement.SLF4J_VERSION}"
        const val julToSlf4J = "org.slf4j:jul-to-slf4j:${VersionManagement.SLF4J_VERSION}"
        const val jclOverSlf4J = "org.slf4j:jcl-over-slf4j:${VersionManagement.SLF4J_VERSION}"

        const val byteBuddy = "net.bytebuddy:byte-buddy:${VersionManagement.BYTE_BUDDY_VERSION}"
        const val byteBuddyAgent = "net.bytebuddy:byte-buddy-agent:${VersionManagement.BYTE_BUDDY_VERSION}"

        const val jasypt = "org.jasypt:jasypt:1.9.3"

        const val bcprovJdk18On = "org.bouncycastle:bcprov-jdk18on:${VersionManagement.BOUNCYCASTLE_VERSION}"
        const val bcpkixJdk18On = "org.bouncycastle:bcpkix-jdk18on:${VersionManagement.BOUNCYCASTLE_VERSION}"
        const val bctlsJdk18On = "org.bouncycastle:bctls-jdk18on:${VersionManagement.BOUNCYCASTLE_VERSION}"
        const val bcmailJdk18On = "org.bouncycastle:bcmail-jdk18on:${VersionManagement.BOUNCYCASTLE_VERSION}"
        const val bctspJdk15On = "org.bouncycastle:bctsp-jdk15on:1.46"

        const val classmate = "com.fasterxml:classmate:1.6.0"
        const val reactor = "io.projectreactor:reactor-core:${VersionManagement.REACTOR_VERSION}"
        const val reactorKotlinExtensions =
            "io.projectreactor.kotlin:reactor-kotlin-extensions:${VersionManagement.REACTOR_KOTLIN_EXTENSIONS_VERSION}"
        const val reactorNetty = "io.projectreactor.netty:reactor-netty:${VersionManagement.REACTOR_NETTY_VERSION}"

        const val caffeine = "com.github.ben-manes.caffeine:caffeine:3.1.8"

        const val swaggerV3Annotaion = "io.swagger.core.v3:swagger-annotations:${VersionManagement.SWAGGER_V3_VERSION}"
        const val swaggerV3Core = "io.swagger.core.v3:swagger-core:${VersionManagement.SWAGGER_V3_VERSION}"
        const val swaggerV3Models = "io.swagger.core.v3:swagger-models:${VersionManagement.SWAGGER_V3_VERSION}"
        const val swaggerV3AnnotaionJarkarta = "io.swagger.core.v3:swagger-annotations-jarkarta:${VersionManagement.SWAGGER_V3_VERSION}"
        const val swaggerV3CoreJarkarta = "io.swagger.core.v3:swagger-core-jarkarta:${VersionManagement.SWAGGER_V3_VERSION}"
        const val swaggerV3ModelsJarkarta = "io.swagger.core.v3:swagger-models-jarkarta:${VersionManagement.SWAGGER_V3_VERSION}"
        const val springdocUi = "org.springdoc:springdoc-openapi-ui:${VersionManagement.SPRINGDOC_VERSION}"
        const val springdocCommon = "org.springdoc:springdoc-openapi-common:${VersionManagement.SPRINGDOC_VERSION}"
        const val springdocStarterCommon = "org.springdoc:springdoc-openapi-starter-common:${VersionManagement.SPRINGDOC_STARTER_VERSION}"
        const val springdocKotlin = "org.springdoc:springdoc-openapi-kotlin:${VersionManagement.SPRINGDOC_VERSION}"

        const val snakeYaml = "org.yaml:snakeyaml:2.2"

        const val yitterIdgenerator = "com.github.yitter:yitter-idgenerator:1.0.6"
        const val easyCaptcha = "com.github.whvcse:easy-captcha:1.6.2"
    }
}

private fun KClass<*>.staticFieldValues() = this.java.declaredFields.filter {
    it.name != "INSTANCE" && Modifier.isStatic(it.modifiers)
}.map {
    it.get(null).toString()
}

fun DependencyConstraintHandlerScope.addDepsManagement() {
    DepsManagement::class.nestedClasses.flatMap {
        it.staticFieldValues()
    }.sorted().forEach {
        add("api", it)
    }
}

fun DependencyHandler.addTestDependencies(configuration: String = "testImplementation") {
    add(configuration, Deps.Test.springBootStarterTest)
    add(configuration, Deps.Test.kotlinTest)
}
