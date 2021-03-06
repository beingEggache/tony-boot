@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object Version {
    const val knife4jVersion = "2.0.8"
    const val springfoxSwagger2Version = "2.10.5"
    const val byteBuddyVersion = "1.11.1"
    const val jacksonVersion = "2.12.3"
    const val kotlinVersion = "1.5.10"
    const val mybatisSpringStarterVersion = "2.2.0"
    const val mybatisPlusVersion = "3.4.3"
    const val nettyVersion = "4.1.65.Final"
    const val reactorVersion = "3.4.6"
    const val slf4jVersion = "1.7.30"
    const val springBootVersion = "2.5.1"
    const val springVersion = "5.3.8"
    const val swaggerVersion = "1.6.2"
}

object Deps {

    object Jackson {
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations:${Version.jacksonVersion}"
        const val core = "com.fasterxml.jackson.core:jackson-core:${Version.jacksonVersion}"
        const val databind = "com.fasterxml.jackson.core:jackson-databind:${Version.jacksonVersion}"
        const val datatypeJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Version.jacksonVersion}"
        const val datatypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Version.jacksonVersion}"
        const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Version.jacksonVersion}"
        const val moduleParameterNames =
            "com.fasterxml.jackson.module:jackson-module-parameter-names:${Version.jacksonVersion}"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlinVersion}"
        const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${Version.kotlinVersion}"
        const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlinVersion}"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlinVersion}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlinVersion}"
        const val test = "org.jetbrains.kotlin:kotlin-test:${Version.kotlinVersion}"
        const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlinVersion}"
    }

    object Netty {
        const val all = "io.netty:netty-all:${Version.nettyVersion}"
        const val buffer = "io.netty:netty-buffer:${Version.nettyVersion}"
        const val common = "io.netty:netty-common:${Version.nettyVersion}"
        const val resolver = "io.netty:netty-resolver:${Version.nettyVersion}"
        const val transport = "io.netty:netty-transport:${Version.nettyVersion}"
        const val transportNativeUnixCommon = "io.netty:netty-transport-native-unix-common:${Version.nettyVersion}"
        const val transportNativeEpoll = "io.netty:netty-transport-native-epoll:${Version.nettyVersion}"
        const val handler = "io.netty:netty-handler:${Version.nettyVersion}"
        const val handlerProxy = "io.netty:netty-handler-proxy:${Version.nettyVersion}"
        const val codec = "io.netty:netty-codec:${Version.nettyVersion}"
        const val codecHttp = "io.netty:netty-codec-http:${Version.nettyVersion}"
        const val codecHttp2 = "io.netty:netty-codec-http2:${Version.nettyVersion}"
        const val codecSocks = "io.netty:netty-codec-socks:${Version.nettyVersion}"
    }

    object Spring {
        const val aop = "org.springframework:spring-aop:${Version.springVersion}"
        const val beans = "org.springframework:spring-beans:${Version.springVersion}"
        const val context = "org.springframework:spring-context:${Version.springVersion}"
        const val contextSupport = "org.springframework:spring-context-support:${Version.springVersion}"
        const val core = "org.springframework:spring-core:${Version.springVersion}"
        const val expression = "org.springframework:spring-expression:${Version.springVersion}"
        const val jcl = "org.springframework:spring-jcl:${Version.springVersion}"
        const val jdbc = "org.springframework:spring-jdbc:${Version.springVersion}"
        const val orm = "org.springframework:spring-orm:${Version.springVersion}"
        const val oxm = "org.springframework:spring-oxm:${Version.springVersion}"
        const val test = "org.springframework:spring-test:${Version.springVersion}"
        const val tx = "org.springframework:spring-tx:${Version.springVersion}"
        const val web = "org.springframework:spring-web:${Version.springVersion}"
        const val webmvc = "org.springframework:spring-webmvc:${Version.springVersion}"
    }

    object SpringData {
        const val springDataCommon = "org.springframework.data:spring-data-common:${Version.springBootVersion}"
        const val springDataRedis = "org.springframework.data:spring-data-redis:${Version.springBootVersion}"
        const val lettuce = "io.lettuce:lettuce-core:6.1.2.RELEASE"
    }

    object SpringBoot {
        const val springBoot = "org.springframework.boot:spring-boot:${Version.springBootVersion}"
        const val autoconfigure = "org.springframework.boot:spring-boot-autoconfigure:${Version.springBootVersion}"
        const val configurationProcessor =
            "org.springframework.boot:spring-boot-configuration-processor:${Version.springBootVersion}"
        const val devtools = "org.springframework.boot:spring-boot-devtools:${Version.springBootVersion}"
        const val starterActuator = "org.springframework.boot:spring-boot-starter-actuator:${Version.springBootVersion}"
        const val starterAmqp = "org.springframework.boot:spring-boot-starter-amqp:${Version.springBootVersion}"
        const val starterAop = "org.springframework.boot:spring-boot-starter-aop:${Version.springBootVersion}"
        const val starterDataRedis =
            "org.springframework.boot:spring-boot-starter-data-redis:${Version.springBootVersion}"
        const val starterJdbc = "org.springframework.boot:spring-boot-starter-jdbc:${Version.springBootVersion}"
        const val starterJson = "org.springframework.boot:spring-boot-starter-json:${Version.springBootVersion}"
        const val starterLogging = "org.springframework.boot:spring-boot-starter-logging:${Version.springBootVersion}"
        const val starterTest = "org.springframework.boot:spring-boot-starter-test:${Version.springBootVersion}"
        const val starterTomcat = "org.springframework.boot:spring-boot-starter-tomcat:${Version.springBootVersion}"
        const val starterUndertow = "org.springframework.boot:spring-boot-starter-undertow:${Version.springBootVersion}"
        const val starterValidation =
            "org.springframework.boot:spring-boot-starter-validation:${Version.springBootVersion}"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web:${Version.springBootVersion}"
        const val starterWebsocket =
            "org.springframework.boot:spring-boot-starter-websocket:${Version.springBootVersion}"
    }

    object Test {
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Version.kotlinVersion}"
        const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test:${Version.kotlinVersion}"
        const val springBootStarterTest =
            "org.springframework.boot:spring-boot-starter-test:${Version.springBootVersion}"
    }

    object Other {
        const val pageHelperStarter = "com.github.pagehelper:pagehelper-spring-boot-starter:1.3.0"
        const val swaggerAnnotations = "io.swagger:swagger-annotations:${Version.swaggerVersion}"
        const val swaggerModels = "io.swagger:swagger-models:${Version.swaggerVersion}"
        const val mybatis = "org.mybatis:mybatis:3.5.7"
        const val mybatisStarter =
            "org.mybatis.spring.boot:mybatis-spring-boot-starter:${Version.mybatisSpringStarterVersion}"
        const val mybatisTypehandlersJsr310 = "org.mybatis:mybatis-typehandlers-jsr310:1.0.2"
        const val validationApi = "jakarta.validation:jakarta.validation-api:2.0.2"
        const val annotationApi = "jakarta.annotation:jakarta.annotation-api:1.3.5"
        const val activationApi = "jakarta.activation:jakarta.activation-api:1.2.2"
        const val elApi = "jakarta.el:jakarta.el-api:3.0.3"
        const val websocketApi = "jakarta.websocket:jakarta.websocket-api:1.1.2"
        const val bindApi = "jakarta.xml.bind:jakarta.xml.bind-api:2.3.3"
        const val reactorNetty = "io.projectreactor.netty:reactor-netty:1.0.7"
        const val gson = "com.google.code.gson:gson:2.8.6"
        const val xstream = "com.thoughtworks.xstream:xstream:1.4.17"
        const val httpclient = "org.apache.httpcomponents:httpclient:4.5.13"
        const val okhttp = "com.squareup.okhttp3:okhttp:4.9.1"
        const val commonsCodec = "commons-codec:commons-codec:1.15"
        const val guava = "com.google.guava:guava:30.1.1-jre"
        const val javaJwt = "com.auth0:java-jwt:3.16.0"
        const val alipaySdkJava = "com.alipay.sdk:alipay-sdk-java:4.13.58.ALL"
        const val aliyunJavaSdkCore = "com.aliyun:aliyun-java-sdk-core:4.5.20"
        const val aliyunSdkOss = "com.aliyun.oss:aliyun-sdk-oss:3.12.0"
        const val aliyunJavaSdkDysmsapi = "com.aliyun:aliyun-java-sdk-dysmsapi:2.1.0"
        const val springfoxSwagger2 = "io.springfox:springfox-swagger2:${Version.springfoxSwagger2Version}"
        const val knife4jApi = "com.github.xiaoymin:knife4j-micro-spring-boot-starter:${Version.knife4jVersion}"
        const val knife4j = "com.github.xiaoymin:knife4j-spring-boot-starter:${Version.knife4jVersion}"
        const val postgresql = "org.postgresql:postgresql:42.2.20"
        const val HikariCP = "com.zaxxer:HikariCP:4.0.3"
        const val byteBuddy = "net.bytebuddy:byte-buddy:${Version.byteBuddyVersion}"
        const val byteBuddyAgent = "net.bytebuddy:byte-buddy-agent:${Version.byteBuddyVersion}"
        const val slf4JApi = "org.slf4j:slf4j-api:${Version.slf4jVersion}"
        const val julToSlf4J = "org.slf4j:jul-to-slf4j:${Version.slf4jVersion}"
        const val jclOverSlf4J = "org.slf4j:jcl-over-slf4j:${Version.slf4jVersion}"
        const val classmate = "com.fasterxml:classmate:1.5.1"
        const val reactor = "io.projectreactor:reactor-core:${Version.reactorVersion}"
        const val bcprovJdk15On = "org.bouncycastle:bcprov-jdk15on:1.68"
        const val fastjson = "com.alibaba:fastjson:1.2.76"
        const val mybatisPlusAnnotation = "com.baomidou:mybatis-plus-annotation:${Version.mybatisPlusVersion}"
        const val mybatisPlusExtension = "com.baomidou:mybatis-plus-extension:${Version.mybatisPlusVersion}"
        const val mybatisPlusBootStarter = "com.baomidou:mybatis-plus-boot-starter:${Version.mybatisPlusVersion}"
        const val mybatisPlusGenerator = "com.baomidou:mybatis-plus-generator:${Version.mybatisPlusVersion}"
        const val easyExcel = "com.alibaba:easyexcel:2.2.10"
        const val easyCaptcha = "com.github.whvcse:easy-captcha:1.6.2"
        const val camundaStarter = "org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.14.0"
    }

    val canReplacedDependencies = mapOf(
        "org.apache.tomcat:tomcat-annotations-api" to Other.annotationApi,
        "javax.annotation:javax.annotation-api" to Other.annotationApi,
        "org.jboss.spec.javax.annotation:jboss-annotations-api_1.3_spec" to Other.annotationApi,
        "javax.activation:javax.activation-api" to Other.activationApi,
        "org.glassfish:jakarta.el" to Other.elApi,
        "org.jboss.spec.javax.websocket:jboss-websocket-api_1.1_spec" to Other.websocketApi,
        "javax.validation:validation-api" to Other.validationApi,
        "javax.xml.bind:jaxb-api" to Other.bindApi,
        "commons-logging:commons-logging" to Spring.jcl
    )
}

fun DependencyHandler.addTestDependencies(configuration: String = "testImplementation") {
    Deps.Test::class.staticFieldValues().forEach {
        add(configuration, it)
    }
}

private fun KClass<*>.staticFieldValues() = this.java.declaredFields.filter {
    it.name != "INSTANCE" && Modifier.isStatic(it.modifiers)
}.map {
    it.get(null)
}

fun Project.forceDepsVersion() {
    configurations.all {
        resolutionStrategy {
            //disable cache
            cacheChangingModulesFor(0, TimeUnit.NANOSECONDS)
            dependencySubstitution {
                Deps.canReplacedDependencies.forEach { (sourceDependency, targetDependency) ->
                    substitute(module(sourceDependency)).with(module(targetDependency))
                }
            }

            Deps::class.nestedClasses.flatMap {
                it.staticFieldValues()
            }.forEach {
                force(it)
            }
        }
    }
}
