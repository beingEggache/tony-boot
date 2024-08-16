/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
includeBuild("build-script")
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
//        val privateGradleRepoUrl: String by settings
//        maven(url = privateGradleRepoUrl) {
//            isAllowInsecureProtocol = true
//        }
        mavenCentral()
    }
}

dependencyResolutionManagement {
    defaultLibrariesExtensionName = "tonyLibs"
}

val templatePrefix: String by settings
rootProject.name = "$templatePrefix-boot-starters"
include("$templatePrefix-dependencies")
include("$templatePrefix-dependencies-catalog")
include("$templatePrefix-aliyun-oss")
include("$templatePrefix-aliyun-sms")
include("$templatePrefix-interfaces")
include("$templatePrefix-web")
include("$templatePrefix-core")
include("$templatePrefix-redis")
include("$templatePrefix-alipay")
include("$templatePrefix-wechat")
include("$templatePrefix-jwt")
include("$templatePrefix-web-auth")
include("$templatePrefix-feign")
include("$templatePrefix-mybatis-plus")
include("$templatePrefix-captcha")
include("$templatePrefix-snowflake-id")
include("$templatePrefix-knife4j-api")
include("$templatePrefix-web-crypto")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

