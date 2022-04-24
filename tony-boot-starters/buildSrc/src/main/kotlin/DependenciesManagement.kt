@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.kotlin.dsl.DependencyConstraintHandlerScope
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

object VersionManagement {
    const val kotlinVersion = "1.6.21"

    const val springVersion = "5.3.19"
    const val springBootVersion = "2.6.7"

    const val openFeignVersion = "11.8"

    const val postgresqlVersion = "42.3.4"
    const val mysqlVersion = "8.0.28"
    const val hikariCPVersion = "5.0.1"
    const val mybatisPlusVersion = "3.5.1"
    const val lettuceCoreVersion = "6.1.8.RELEASE"

    const val jacksonVersion = "2.13.2"

    const val gsonVersion = "2.9.0"
    const val fastjsonVersion = "1.2.80"
    const val nettyVersion = "4.1.76.Final"

    const val reactorVersion = "3.4.17"
    const val reactorKotlinExtensions = "1.1.6"
    const val reactorNettyVersion = "1.0.18"
    const val slf4jVersion = "1.7.36"

    const val byteBuddyVersion = "1.12.9"
    const val jasyptVersion = "1.9.3"
    const val bouncycastleVersion = "1.70"
    const val javaJwtVersion = "3.19.1"
    const val guavaVersion = "31.1-jre"

    const val swaggerV3Version = "2.2.0"
    const val springdocVersion = "1.6.7"

}

object DepsManagement {

    object Jackson {
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations:${VersionManagement.jacksonVersion}"
        const val core = "com.fasterxml.jackson.core:jackson-core:${VersionManagement.jacksonVersion}"
        const val databind = "com.fasterxml.jackson.core:jackson-databind:${VersionManagement.jacksonVersion}"
        const val datatypeJdk8 =
            "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${VersionManagement.jacksonVersion}"
        const val datatypeJsr310 =
            "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${VersionManagement.jacksonVersion}"
        const val moduleKotlin =
            "com.fasterxml.jackson.module:jackson-module-kotlin:${VersionManagement.jacksonVersion}"
        const val moduleParameterNames =
            "com.fasterxml.jackson.module:jackson-module-parameter-names:${VersionManagement.jacksonVersion}"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${VersionManagement.kotlinVersion}"
        const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${VersionManagement.kotlinVersion}"
        const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${VersionManagement.kotlinVersion}"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${VersionManagement.kotlinVersion}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${VersionManagement.kotlinVersion}"
        const val test = "org.jetbrains.kotlin:kotlin-test:${VersionManagement.kotlinVersion}"
        const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:${VersionManagement.kotlinVersion}"
    }

    object Netty {
        const val all = "io.netty:netty-all:${VersionManagement.nettyVersion}"
        const val buffer = "io.netty:netty-buffer:${VersionManagement.nettyVersion}"
        const val common = "io.netty:netty-common:${VersionManagement.nettyVersion}"
        const val resolver = "io.netty:netty-resolver:${VersionManagement.nettyVersion}"
        const val resolverDns = "io.netty:netty-resolver-dns:${VersionManagement.nettyVersion}"
        const val resolverDnsMacOs = "io.netty:netty-resolver-dns-native-macos:${VersionManagement.nettyVersion}"
        const val transport = "io.netty:netty-transport:${VersionManagement.nettyVersion}"
        const val transportNativeUnixCommon =
            "io.netty:netty-transport-native-unix-common:${VersionManagement.nettyVersion}"
        const val transportNativeEpoll = "io.netty:netty-transport-native-epoll:${VersionManagement.nettyVersion}"
        const val handler = "io.netty:netty-handler:${VersionManagement.nettyVersion}"
        const val handlerProxy = "io.netty:netty-handler-proxy:${VersionManagement.nettyVersion}"
        const val codec = "io.netty:netty-codec:${VersionManagement.nettyVersion}"
        const val codecHttp = "io.netty:netty-codec-http:${VersionManagement.nettyVersion}"
        const val codecHttp2 = "io.netty:netty-codec-http2:${VersionManagement.nettyVersion}"
        const val codecSocks = "io.netty:netty-codec-socks:${VersionManagement.nettyVersion}"
    }

    object Spring {
        const val aop = "org.springframework:spring-aop:${VersionManagement.springVersion}"
        const val beans = "org.springframework:spring-beans:${VersionManagement.springVersion}"
        const val context = "org.springframework:spring-context:${VersionManagement.springVersion}"
        const val contextSupport = "org.springframework:spring-context-support:${VersionManagement.springVersion}"
        const val contextIndexer = "org.springframework:spring-context-indexer:${VersionManagement.springVersion}"
        const val core = "org.springframework:spring-core:${VersionManagement.springVersion}"
        const val expression = "org.springframework:spring-expression:${VersionManagement.springVersion}"
        const val jcl = "org.springframework:spring-jcl:${VersionManagement.springVersion}"
        const val jdbc = "org.springframework:spring-jdbc:${VersionManagement.springVersion}"
        const val orm = "org.springframework:spring-orm:${VersionManagement.springVersion}"
        const val oxm = "org.springframework:spring-oxm:${VersionManagement.springVersion}"
        const val test = "org.springframework:spring-test:${VersionManagement.springVersion}"
        const val tx = "org.springframework:spring-tx:${VersionManagement.springVersion}"
        const val web = "org.springframework:spring-web:${VersionManagement.springVersion}"
        const val webmvc = "org.springframework:spring-webmvc:${VersionManagement.springVersion}"
        const val webflux = "org.springframework:spring-webflux:${VersionManagement.springVersion}"
    }

    object OpenFeign {
        const val openFeignCore = "io.github.openfeign:feign-core:${VersionManagement.openFeignVersion}"
        const val openFeignSl4j = "io.github.openfeign:feign-slf4j:${VersionManagement.openFeignVersion}"
        const val openFeignOkhttp = "io.github.openfeign:feign-okhttp:${VersionManagement.openFeignVersion}"
        const val openFeignJackson = "io.github.openfeign:feign-jackson:${VersionManagement.openFeignVersion}"
    }

    object SpringBoot {
        const val springBoot = "org.springframework.boot:spring-boot:${VersionManagement.springBootVersion}"
        const val springBootStarter =
            "org.springframework.boot:spring-boot-starter:${VersionManagement.springBootVersion}"
        const val autoconfigure =
            "org.springframework.boot:spring-boot-autoconfigure:${VersionManagement.springBootVersion}"
        const val configurationProcessor =
            "org.springframework.boot:spring-boot-configuration-processor:${VersionManagement.springBootVersion}"
        const val autoconfigureProcessor =
            "org.springframework.boot:spring-boot-autoconfigure-processor:${VersionManagement.springBootVersion}"
        const val devtools = "org.springframework.boot:spring-boot-devtools:${VersionManagement.springBootVersion}"
        const val starterActuator =
            "org.springframework.boot:spring-boot-starter-actuator:${VersionManagement.springBootVersion}"
        const val starterAmqp =
            "org.springframework.boot:spring-boot-starter-amqp:${VersionManagement.springBootVersion}"
        const val starterAop = "org.springframework.boot:spring-boot-starter-aop:${VersionManagement.springBootVersion}"
        const val starterCache =
            "org.springframework.boot:spring-boot-starter-cache:${VersionManagement.springBootVersion}"
        const val starterDataRedis =
            "org.springframework.boot:spring-boot-starter-data-redis:${VersionManagement.springBootVersion}"
        const val starterDataRedisReactive =
            "org.springframework.boot:spring-boot-starter-data-redis-reactive:${VersionManagement.springBootVersion}"
        const val starterJdbc =
            "org.springframework.boot:spring-boot-starter-jdbc:${VersionManagement.springBootVersion}"
        const val starterJson =
            "org.springframework.boot:spring-boot-starter-json:${VersionManagement.springBootVersion}"
        const val starterLogging =
            "org.springframework.boot:spring-boot-starter-logging:${VersionManagement.springBootVersion}"
        const val starterTest =
            "org.springframework.boot:spring-boot-starter-test:${VersionManagement.springBootVersion}"
        const val starterTomcat =
            "org.springframework.boot:spring-boot-starter-tomcat:${VersionManagement.springBootVersion}"
        const val starterUndertow =
            "org.springframework.boot:spring-boot-starter-undertow:${VersionManagement.springBootVersion}"
        const val starterValidation =
            "org.springframework.boot:spring-boot-starter-validation:${VersionManagement.springBootVersion}"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web:${VersionManagement.springBootVersion}"
        const val starterWebflux =
            "org.springframework.boot:spring-boot-starter-webflux:${VersionManagement.springBootVersion}"
        const val starterWebsocket =
            "org.springframework.boot:spring-boot-starter-websocket:${VersionManagement.springBootVersion}"
    }

    object Test {
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${VersionManagement.kotlinVersion}"
        const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test:${VersionManagement.kotlinVersion}"
        const val springBootStarterTest =
            "org.springframework.boot:spring-boot-starter-test:${VersionManagement.springBootVersion}"
    }

    object Other {
        const val lettuce = "io.lettuce:lettuce-core:${VersionManagement.lettuceCoreVersion}"

        const val mybatis = "org.mybatis:mybatis:3.5.9"
        const val mybatisPlusAnnotation = "com.baomidou:mybatis-plus-annotation:${VersionManagement.mybatisPlusVersion}"
        const val mybatisPlusExtension = "com.baomidou:mybatis-plus-extension:${VersionManagement.mybatisPlusVersion}"
        const val mybatisPlusBootStarter =
            "com.baomidou:mybatis-plus-boot-starter:${VersionManagement.mybatisPlusVersion}"
        const val mybatisPlusGenerator = "com.baomidou:mybatis-plus-generator:${VersionManagement.mybatisPlusVersion}"

        const val mybatisTypehandlersJsr310 = "org.mybatis:mybatis-typehandlers-jsr310:1.0.2"
        const val validationApi = "jakarta.validation:jakarta.validation-api:2.0.2"
        const val annotationApi = "jakarta.annotation:jakarta.annotation-api:1.3.5"
        const val activationApi = "jakarta.activation:jakarta.activation-api:1.2.2"
        const val elApi = "jakarta.el:jakarta.el-api:3.0.3"
        const val websocketApi = "jakarta.websocket:jakarta.websocket-api:1.1.2"
        const val bindApi = "jakarta.xml.bind:jakarta.xml.bind-api:2.3.3"

        const val gson = "com.google.code.gson:gson:${VersionManagement.gsonVersion}"
        const val fastjson = "com.alibaba:fastjson:${VersionManagement.fastjsonVersion}"

        const val xstream = "com.thoughtworks.xstream:xstream:1.4.19"
        const val httpclient = "org.apache.httpcomponents:httpclient:4.5.13"
        const val httpcore = "org.apache.httpcomponents:httpcore:4.4.15"
        const val httpcoreNio = "org.apache.httpcomponents:httpcore-nio:4.4.15"
        const val okhttp = "com.squareup.okhttp3:okhttp:4.9.3"
        const val commonsCodec = "commons-codec:commons-codec:1.15"

        const val guava = "com.google.guava:guava:${VersionManagement.guavaVersion}"
        const val javaJwt = "com.auth0:java-jwt:${VersionManagement.javaJwtVersion}"

        const val postgresql = "org.postgresql:postgresql:${VersionManagement.postgresqlVersion}"
        const val mysql = "mysql:mysql-connector-java:${VersionManagement.mysqlVersion}"
        const val HikariCP = "com.zaxxer:HikariCP:${VersionManagement.hikariCPVersion}"

        const val slf4JApi = "org.slf4j:slf4j-api:${VersionManagement.slf4jVersion}"
        const val julToSlf4J = "org.slf4j:jul-to-slf4j:${VersionManagement.slf4jVersion}"
        const val jclOverSlf4J = "org.slf4j:jcl-over-slf4j:${VersionManagement.slf4jVersion}"

        const val byteBuddy = "net.bytebuddy:byte-buddy:${VersionManagement.byteBuddyVersion}"
        const val byteBuddyAgent = "net.bytebuddy:byte-buddy-agent:${VersionManagement.byteBuddyVersion}"

        const val jasypt = "org.jasypt:jasypt:${VersionManagement.jasyptVersion}"

        const val bcprovJdk15On = "org.bouncycastle:bcprov-jdk15on:${VersionManagement.bouncycastleVersion}"
        const val bcpkixJdk15On = "org.bouncycastle:bcpkix-jdk15on:${VersionManagement.bouncycastleVersion}"
        const val bctlsJdk15On = "org.bouncycastle:bctls-jdk15on:${VersionManagement.bouncycastleVersion}"
        const val bctspJdk15On = "org.bouncycastle:bctsp-jdk15on:${VersionManagement.bouncycastleVersion}"
        const val bcmailJdk15On = "org.bouncycastle:bcmail-jdk15on:${VersionManagement.bouncycastleVersion}"

        const val classmate = "com.fasterxml:classmate:1.5.1"
        const val reactor = "io.projectreactor:reactor-core:${VersionManagement.reactorVersion}"
        const val reactorKotlinExtensions = "io.projectreactor.kotlin:reactor-kotlin-extensions:${VersionManagement.reactorKotlinExtensions}"
        const val reactorNetty = "io.projectreactor.netty:reactor-netty:${VersionManagement.reactorNettyVersion}"

        const val caffeine = "com.github.ben-manes.caffeine:caffeine:3.0.6"

        const val swaggerV3Annotaion = "io.swagger.core.v3:swagger-annotations:${VersionManagement.swaggerV3Version}"
        const val springdocUi = "org.springdoc:springdoc-openapi-ui:${VersionManagement.springdocVersion}"
        const val springdocCommon = "org.springdoc:springdoc-openapi-common:${VersionManagement.springdocVersion}"
        const val springdocKotlin = "org.springdoc:springdoc-openapi-kotlin:${VersionManagement.springdocVersion}"

    }
}

private fun KClass<*>.staticFieldValues() = this.java.declaredFields.filter {
    it.name != "INSTANCE" && Modifier.isStatic(it.modifiers)
}.map {
    it.get(null)
}

fun DependencyConstraintHandlerScope.addDepsManagement() {
    DepsManagement::class.nestedClasses.flatMap {
        it.staticFieldValues()
    }.forEach {
        add("api", it)
    }
}
