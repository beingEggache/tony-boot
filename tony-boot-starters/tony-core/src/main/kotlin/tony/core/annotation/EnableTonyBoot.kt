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

package tony.core.annotation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata
import tony.core.PROJECT_GROUP
import tony.core.SpringContexts
import tony.core.utils.getLogger

/**
 * 启用 [PROJECT_GROUP] 的 starter
 *
 * @author tangli
 * @date 2023/05/24 19:04
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Import(
    value = [
        TonyBootImportSelector::class,
        TonyBootConfiguration::class
    ]
)
public annotation class EnableTonyBoot

@Configuration(proxyBeanMethods = false)
private class TonyBootConfiguration {
    @Bean
    private fun applicationContextHolder() =
        SpringContexts.ApplicationContextHolder
}

private class TonyBootImportSelector : ImportSelector {
    private val logger = getLogger()

    override fun selectImports(importingClassMetadata: AnnotationMetadata) =
        listOf(
            "$PROJECT_GROUP.web.autoconfigure.WebAutoConfiguration",
            "$PROJECT_GROUP.jwt.autoconfigure.JwtAutoConfiguration",
            "$PROJECT_GROUP.web.auth.autoconfigure.WebAuthAutoConfiguration",
            "$PROJECT_GROUP.mybatis.autoconfigure.MybatisPlusAutoConfiguration",
            "$PROJECT_GROUP.id.autoconfigure.IdAutoConfiguration",
            "$PROJECT_GROUP.knife4j.autoconfigure.Knife4jExtensionAutoConfiguration",
            "$PROJECT_GROUP.redis.autoconfigure.RedisAutoConfiguration",
            "$PROJECT_GROUP.captcha.autoconfigure.CaptchaAutoConfiguration",
            "$PROJECT_GROUP.feign.autoconfigure.FeignAutoConfiguration",
            "$PROJECT_GROUP.web.crypto.autoconfigure.WebCryptoAutoConfiguration",
            "$PROJECT_GROUP.aliyun.oss.autoconfigure.AliyunOssAutoConfiguration",
            "$PROJECT_GROUP.aliyun.sms.autoconfigure.AliyunSmsAutoConfiguration",
            "$PROJECT_GROUP.wechat.autoconfigure.WechatAutoConfiguration",
            "$PROJECT_GROUP.alipay.autoconfigure.AlipayAutoConfiguration"
        ).filter(::hasClass).toTypedArray()

    private fun hasClass(className: String) =
        try {
            Class.forName(className)
            logger.debug("$className included.")
            true
        } catch (_: ClassNotFoundException) {
            logger.debug("$className does not exists.")
            false
        }
}
