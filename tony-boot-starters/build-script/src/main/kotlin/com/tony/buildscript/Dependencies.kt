@file:Suppress("unused", "SpellCheckingInspection")

package com.tony.buildscript

const val projectGroup = "com.tony"
const val projectPrefix = "tony"

object Deps {

    object SpringCloudDeps {
        const val springCloudAlibabaDenpendencies =
            "com.alibaba.cloud:spring-cloud-alibaba-dependencies:${VersionManagement.springCloudAlibabaVersion}"
        const val springCloudDependencies =
            "org.springframework.cloud:spring-cloud-dependencies:${VersionManagement.springCloudVersion}"
    }

    object Aliyun {
        const val alipaySdkJava = "com.alipay.sdk:alipay-sdk-java:${VersionManagement.alipaySdkJavaVersion}"
        const val aliyunJavaSdkCore = "com.aliyun:aliyun-java-sdk-core:${VersionManagement.aliyunJavaSdkCoreVersion}"
        const val aliyunSdkOss = "com.aliyun.oss:aliyun-sdk-oss:${VersionManagement.aliyunSdkOssVersion}"
        const val aliyunJavaSdkDysmsapi = "com.aliyun:aliyun-java-sdk-dysmsapi:2.2.1"
    }

    object Template {
        const val templateDependencies = "$projectGroup:$projectPrefix-dependencies:${VersionManagement.templateVersion}"
        const val templateCore = "$projectGroup:$projectPrefix-core:${VersionManagement.templateVersion}"
        const val templateAnnotations = "$projectGroup:$projectPrefix-annotations:${VersionManagement.templateVersion}"
        const val templateId = "$projectGroup:$projectPrefix-snowflake-id:${VersionManagement.templateVersion}"
        const val templateXxlJob = "$projectGroup:$projectPrefix-xxl-job:${VersionManagement.templateVersion}"
        const val templateWeb = "$projectGroup:$projectPrefix-web:${VersionManagement.templateVersion}"
        const val templateKnife4j = "$projectGroup:$projectPrefix-knife4j-api:${VersionManagement.templateVersion}"
        const val templateJwt = "$projectGroup:$projectPrefix-jwt:${VersionManagement.templateVersion}"
        const val templateWebAuth = "$projectGroup:$projectPrefix-web-auth:${VersionManagement.templateVersion}"
        const val templateMybatisPlus = "$projectGroup:$projectPrefix-mybatis-plus:${VersionManagement.templateVersion}"
        const val templateRedis = "$projectGroup:$projectPrefix-redis:${VersionManagement.templateVersion}"
        const val templateFeign = "$projectGroup:$projectPrefix-feign:${VersionManagement.templateVersion}"
        const val templateWechat = "$projectGroup:$projectPrefix-wechat:${VersionManagement.templateVersion}"
        const val templateAlipay = "$projectGroup:$projectPrefix-alipay:${VersionManagement.templateVersion}"
        const val templateAliyunOss = "$projectGroup:$projectPrefix-aliyun-oss:${VersionManagement.templateVersion}"
        const val templateAliyunSms = "$projectGroup:$projectPrefix-aliyun-sms:${VersionManagement.templateVersion}"
    }

    object Jackson {
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations"
        const val core = "com.fasterxml.jackson.core:jackson-core"
        const val databind = "com.fasterxml.jackson.core:jackson-databind"
        const val datatypeJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8"
        const val datatypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
        const val moduleParameterNames = "com.fasterxml.jackson.module:jackson-module-parameter-names"
    }

    object Kotlin {
        const val bom = "org.jetbrains.kotlin:kotlin-bom"
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
        const val aspects = "org.springframework:spring-aspects"
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

    object Knife4j {
        const val core = "com.github.xiaoymin:knife4j-core"
        const val openapi3SpringBootStarter =
            "com.github.xiaoymin:knife4j-openapi3-spring-boot-starter"
        const val openapi3JakartaSpringBootStarter =
            "com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter"
        const val openapi3Ui =
            "com.github.xiaoymin:knife4j-openapi3-ui"
    }

    object SpringBoot {
        const val springBoot = "org.springframework.boot:spring-boot"
        const val springBootStarter = "org.springframework.boot:spring-boot-starter"
        const val autoconfigure = "org.springframework.boot:spring-boot-autoconfigure"
        const val configurationProcessor = "org.springframework.boot:spring-boot-configuration-processor"
        const val devtools = "org.springframework.boot:spring-boot-devtools"
        const val starterActuator = "org.springframework.boot:spring-boot-starter-actuator"
        const val starterAmqp = "org.springframework.boot:spring-boot-starter-amqp"
        const val starterAop = "org.springframework.boot:spring-boot-starter-aop"
        const val starterCache = "org.springframework.boot:spring-boot-starter-cache"
        const val starterJdbc = "org.springframework.boot:spring-boot-starter-jdbc"
        const val starterJson = "org.springframework.boot:spring-boot-starter-json"
        const val starterLogging = "org.springframework.boot:spring-boot-starter-logging"
        const val starterReactorNetty = "org.springframework.boot:spring-boot-starter-reactor-netty"
        const val starterTest = "org.springframework.boot:spring-boot-starter-test"
        const val starterTomcat = "org.springframework.boot:spring-boot-starter-tomcat"
        const val starterUndertow = "org.springframework.boot:spring-boot-starter-undertow"
        const val starterValidation = "org.springframework.boot:spring-boot-starter-validation"
        const val starterWeb = "org.springframework.boot:spring-boot-starter-web"
        const val starterWebflux = "org.springframework.boot:spring-boot-starter-webflux"
        const val starterWebsocket = "org.springframework.boot:spring-boot-starter-websocket"
    }

    object SpringData {
        const val starterRedis = "org.springframework.boot:spring-boot-starter-data-redis"
        const val starterRedisReactive = "org.springframework.boot:spring-boot-starter-data-redis-reactive"
    }

    object Test {
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test"
        const val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test"
        const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test"
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
        const val commonsIo = "commons-io:commons-io"
        const val commonsCompress = "org.apache.commons:commons-compress"
        const val commonsPool2 = "org.apache.commons:commons-pool2"
        const val commonsLang3 = "org.apache.commons:commons-lang3"

        const val findbugsJsr305 = "com.google.code.findbugs:jsr305"
        const val guava = "com.google.guava:guava"
        const val javaJwt = "com.auth0:java-jwt"

        const val postgresql = "org.postgresql:postgresql"
        const val mysql = "com.mysql:mysql-connector-j"
        const val HikariCP = "com.zaxxer:HikariCP"

        const val xxlJob = "com.xuxueli:xxl-job-core"

        const val slf4JApi = "org.slf4j:slf4j-api"
        const val julToSlf4J = "org.slf4j:jul-to-slf4j"
        const val jclOverSlf4J = "org.slf4j:jcl-over-slf4j"

        const val byteBuddy = "net.bytebuddy:byte-buddy"
        const val byteBuddyAgent = "net.bytebuddy:byte-buddy-agent"

        const val jasypt = "org.jasypt:jasypt"

        const val bcprovJdk18On = "org.bouncycastle:bcprov-jdk18on"
        const val bcpkixJdk18On = "org.bouncycastle:bcpkix-jdk18on"
        const val bctlsJdk18On = "org.bouncycastle:bctls-jdk18on"
        const val bcmailJdk18On = "org.bouncycastle:bcmail-jdk18on"
        const val bctspJdk15On = "org.bouncycastle:bctsp-jdk15on"

        const val classmate = "com.fasterxml:classmate"
        const val reactor = "io.projectreactor:reactor-core"
        const val reactorKotlinExtensions = "io.projectreactor.kotlin:reactor-kotlin-extensions"
        const val reactorNetty = "io.projectreactor.netty:reactor-netty"

        const val caffeine = "com.github.ben-manes.caffeine:caffeine"

        const val swaggerV3Annotaion = "io.swagger.core.v3:swagger-annotations"
        const val springdocUi = "org.springdoc:springdoc-openapi-ui"
        const val springdocCommon = "org.springdoc:springdoc-openapi-common"
        const val springdocStarterCommon = "org.springdoc:springdoc-openapi-starter-common"
        const val springdocKotlin = "org.springdoc:springdoc-openapi-kotlin"

        const val yitterIdgenerator = "com.github.yitter:yitter-idgenerator"
        const val easyCaptcha = "com.github.whvcse:easy-captcha"
    }
}
