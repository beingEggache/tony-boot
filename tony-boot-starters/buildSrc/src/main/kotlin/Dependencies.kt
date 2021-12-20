@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.util.concurrent.TimeUnit

object Version {
    const val kotlinVersion = "1.6.10"

    const val springVersion = "5.3.14"
    const val springBootVersion = "2.6.1"

    const val springCloudAlibabaVersion = "2021.1"
    const val springCloudVersion = "2021.0.0"

    const val alipaySdkJavaVersion = "4.20.1.ALL"
    const val aliyunJavaSdkCoreVersion = "4.5.30"
    const val aliyunSdkOssVersion = "3.13.2"
}

object Deps {

    object SpringCloudDeps {
        const val springCloudAlibabaDenpendencies =
            "com.alibaba.cloud:spring-cloud-alibaba-dependencies:${Version.springCloudAlibabaVersion}"
        const val springCloudDependencies =
            "org.springframework.cloud:spring-cloud-dependencies:${Version.springCloudVersion}"
    }

    object Aliyun {
        const val alipaySdkJava = "com.alipay.sdk:alipay-sdk-java:${Version.alipaySdkJavaVersion}"
        const val aliyunJavaSdkCore = "com.aliyun:aliyun-java-sdk-core:${Version.aliyunJavaSdkCoreVersion}"
        const val aliyunSdkOss = "com.aliyun.oss:aliyun-sdk-oss:${Version.aliyunSdkOssVersion}"
        const val aliyunJavaSdkDysmsapi = "com.aliyun:aliyun-java-sdk-dysmsapi:2.1.0"
    }

    object Jackson {
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations"
        const val core = "com.fasterxml.jackson.core:jackson-core"
        const val databind = "com.fasterxml.jackson.core:jackson-databind"
        const val datatypeJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8"
        const val datatypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
        const val moduleParameterNames =
            "com.fasterxml.jackson.module:jackson-module-parameter-names"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib"
        const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common"
        const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect"
        const val test = "org.jetbrains.kotlin:kotlin-test"
        const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit"
    }

    object Netty {
        const val all = "io.netty:netty-all"
        const val buffer = "io.netty:netty-buffer"
        const val common = "io.netty:netty-common"
        const val resolver = "io.netty:netty-resolver"
        const val resolverDns = "io.netty:netty-resolver-dns"
        const val resolverDnsMacOs = "io.netty:netty-resolver-dns-native-macos"
        const val transport = "io.netty:netty-transport"
        const val transportNativeUnixCommon = "io.netty:netty-transport-native-unix-common"
        const val transportNativeEpoll = "io.netty:netty-transport-native-epoll"
        const val handler = "io.netty:netty-handler"
        const val handlerProxy = "io.netty:netty-handler-proxy"
        const val codec = "io.netty:netty-codec"
        const val codecHttp = "io.netty:netty-codec-http"
        const val codecHttp2 = "io.netty:netty-codec-http2"
        const val codecSocks = "io.netty:netty-codec-socks"
    }

    object Spring {
        const val aop = "org.springframework:spring-aop"
        const val beans = "org.springframework:spring-beans"
        const val context = "org.springframework:spring-context"
        const val contextSupport = "org.springframework:spring-context-support"
        const val contextIndexer = "org.springframework:spring-context-indexer"
        const val core = "org.springframework:spring-core"
        const val expression = "org.springframework:spring-expression"
        const val jcl = "org.springframework:spring-jcl"
        const val jdbc = "org.springframework:spring-jdbc"
        const val orm = "org.springframework:spring-orm"
        const val oxm = "org.springframework:spring-oxm"
        const val test = "org.springframework:spring-test"
        const val tx = "org.springframework:spring-tx"
        const val web = "org.springframework:spring-web"
        const val webmvc = "org.springframework:spring-webmvc"
        const val webflux = "org.springframework:spring-webflux"
    }

    object OpenFeign {
        const val openFeignCore = "io.github.openfeign:feign-core"
        const val openFeignSl4j = "io.github.openfeign:feign-slf4j"
        const val openFeignOkhttp = "io.github.openfeign:feign-okhttp"
        const val openFeignJackson = "io.github.openfeign:feign-jackson"
    }

    object SpringBoot {
        const val springBoot = "org.springframework.boot:spring-boot"
        const val springBootStarter = "org.springframework.boot:spring-boot-starter"
        const val autoconfigure = "org.springframework.boot:spring-boot-autoconfigure"
        const val configurationProcessor =
            "org.springframework.boot:spring-boot-configuration-processor"
        const val devtools = "org.springframework.boot:spring-boot-devtools"
        const val starterActuator = "org.springframework.boot:spring-boot-starter-actuator"
        const val starterAmqp = "org.springframework.boot:spring-boot-starter-amqp"
        const val starterAop = "org.springframework.boot:spring-boot-starter-aop"
        const val starterCache = "org.springframework.boot:spring-boot-starter-cache"
        const val starterDataRedis =
            "org.springframework.boot:spring-boot-starter-data-redis"
        const val starterDataRedisReactive =
            "org.springframework.boot:spring-boot-starter-data-redis-reactive"
        const val starterJdbc = "org.springframework.boot:spring-boot-starter-jdbc"
        const val starterJson = "org.springframework.boot:spring-boot-starter-json"
        const val starterLogging = "org.springframework.boot:spring-boot-starter-logging"
        const val starterTest = "org.springframework.boot:spring-boot-starter-test"
        const val starterTomcat = "org.springframework.boot:spring-boot-starter-tomcat"
        const val starterUndertow = "org.springframework.boot:spring-boot-starter-undertow"
        const val starterValidation =
            "org.springframework.boot:spring-boot-starter-validation"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web"
        const val starterWebflux = "org.springframework.boot:spring-boot-starter-webflux"
        const val starterWebsocket =
            "org.springframework.boot:spring-boot-starter-websocket"
    }

    object Test {
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test"
        const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test"
        const val springBootStarterTest =
            "org.springframework.boot:spring-boot-starter-test"
    }

    object Other {
        const val lettuce = "io.lettuce:lettuce-core"

        const val mybatis = "org.mybatis:mybatis"
        const val mybatisPlusAnnotation = "com.baomidou:mybatis-plus-annotation"
        const val mybatisPlusExtension = "com.baomidou:mybatis-plus-extension"
        const val mybatisPlusBootStarter = "com.baomidou:mybatis-plus-boot-starter"
        const val mybatisPlusGenerator = "com.baomidou:mybatis-plus-generator"

        const val mybatisTypehandlersJsr310 = "org.mybatis:mybatis-typehandlers-jsr310"
        const val validationApi = "jakarta.validation:jakarta.validation-api"
        const val annotationApi = "jakarta.annotation:jakarta.annotation-api"
        const val activationApi = "jakarta.activation:jakarta.activation-api"
        const val elApi = "jakarta.el:jakarta.el-api"
        const val websocketApi = "jakarta.websocket:jakarta.websocket-api"
        const val bindApi = "jakarta.xml.bind:jakarta.xml.bind-api"

        const val gson = "com.google.code.gson:gson"
        const val fastjson = "com.alibaba:fastjson"

        const val xstream = "com.thoughtworks.xstream:xstream"
        const val httpclient = "org.apache.httpcomponents:httpclient"
        const val okhttp = "com.squareup.okhttp3:okhttp"
        const val commonsCodec = "commons-codec:commons-codec"

        const val guava = "com.google.guava:guava"
        const val javaJwt = "com.auth0:java-jwt"

        const val postgresql = "org.postgresql:postgresql"
        const val mysql = "mysql:mysql-connector-java"
        const val HikariCP = "com.zaxxer:HikariCP"

        const val slf4JApi = "org.slf4j:slf4j-api"
        const val julToSlf4J = "org.slf4j:jul-to-slf4j"
        const val jclOverSlf4J = "org.slf4j:jcl-over-slf4j"

        const val byteBuddy = "net.bytebuddy:byte-buddy"
        const val byteBuddyAgent = "net.bytebuddy:byte-buddy-agent"

        const val jasypt = "org.jasypt:jasypt"

        const val bcprovJdk15On = "org.bouncycastle:bcprov-jdk15on"
        const val bcpkixJdk15On = "org.bouncycastle:bcpkix-jdk15on"
        const val bctlsJdk15On = "org.bouncycastle:bctls-jdk15on"
        const val bctspJdk15On = "org.bouncycastle:bctsp-jdk15on"
        const val bcmailJdk15On = "org.bouncycastle:bcmail-jdk15on"

        const val classmate = "com.fasterxml:classmate"
        const val reactor = "io.projectreactor:reactor-core"
        const val reactorNetty = "io.projectreactor.netty:reactor-netty"

        const val caffeine = "com.github.ben-manes.caffeine:caffeine"

        const val swaggerV3Annotaion = "io.swagger.core.v3:swagger-annotations"
        const val springdocUi = "org.springdoc:springdoc-openapi-ui"
        const val springdocCommon = "org.springdoc:springdoc-openapi-common"
        const val springdocKotlin = "org.springdoc:springdoc-openapi-kotlin"
    }
}

fun DependencyHandler.addTestDependencies(configuration: String = "testImplementation") {
    add(configuration, "org.jetbrains.kotlin:kotlin-test")
    add(configuration, "org.springframework.boot:spring-boot-starter-test")
}

fun Project.substituteDeps() {
    configurations.all {
        resolutionStrategy {
            //disable cache
            cacheChangingModulesFor(0, TimeUnit.NANOSECONDS)
            dependencySubstitution {
                canReplacedDependencies.forEach { (sourceDependency, targetDependency) ->
                    substitute(module(sourceDependency)).using(module(targetDependency))
                }
            }
        }
    }
}

private val canReplacedDependencies = mapOf(
    "bouncycastle:bcprov-jdk14" to "org.bouncycastle:bcprov-jdk15on:${VersionManagement.bouncycastleVersion}",
    "bouncycastle:bcmail-jdk14" to "org.bouncycastle:bcmail-jdk15on:${VersionManagement.bouncycastleVersion}",
    "bouncycastle:bctsp-jdk14" to "org.bouncycastle:bctsp-jdk15on:1.46",
    "org.bouncycastle:bcprov-jdk14" to "org.bouncycastle:bcprov-jdk15on:${VersionManagement.bouncycastleVersion}",
    "org.bouncycastle:bcmail-jdk14" to "org.bouncycastle:bcmail-jdk15on:${VersionManagement.bouncycastleVersion}",
    "org.bouncycastle:bctsp-jdk14" to "org.bouncycastle:bctsp-jdk15on:1.46",

    "org.apache.tomcat:tomcat-annotations-api" to "jakarta.annotation:jakarta.annotation-api:1.3.5",
    "javax.annotation:javax.annotation-api" to "jakarta.annotation:jakarta.annotation-api:1.3.5",
    "org.jboss.spec.javax.annotation:jboss-annotations-api_1.3_spec" to "jakarta.annotation:jakarta.annotation-api:1.3.5",
    "javax.activation:javax.activation-api" to "jakarta.activation:jakarta.activation-api:1.2.2",

    "javax.el:el-api" to "jakarta.el:jakarta.el-api:3.0.3",
    "org.glassfish:jakarta.el" to "jakarta.el:jakarta.el-api:3.0.3",
    "org.apache.tomcat.embed:tomcat-embed-el" to "org.glassfish.web:el-impl:2.2",

    "org.jboss.spec.javax.websocket:jboss-websocket-api_1.1_spec" to "jakarta.websocket:jakarta.websocket-api:1.1.2",
    "javax.validation:validation-api" to "jakarta.validation:jakarta.validation-api:2.0.2",
    "javax.xml.bind:jaxb-api" to "jakarta.xml.bind:jakarta.xml.bind-api:2.3.3",
    "commons-logging:commons-logging" to "org.springframework:spring-jcl:${VersionManagement.springVersion}"
)
