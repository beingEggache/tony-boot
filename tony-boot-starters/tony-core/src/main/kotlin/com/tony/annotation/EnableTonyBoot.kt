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

package com.tony.annotation

import com.tony.PROJECT_GROUP
import com.tony.SpringContexts
import com.tony.utils.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

/**
 * 启用 [PROJECT_GROUP] 的 starter
 *
 * @author Tang Li
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

@Configuration
internal class TonyBootConfiguration {
    @Bean
    internal fun applicationContextHolder() =
        SpringContexts.ApplicationContextHolder
}

internal class TonyBootImportSelector : ImportSelector {
    private val logger = getLogger()

    override fun selectImports(importingClassMetadata: AnnotationMetadata) =
        listOf(
            "$PROJECT_GROUP.alipay.config.AlipayConfig",
            "$PROJECT_GROUP.aliyun.oss.config.AliyunOssConfig",
            "$PROJECT_GROUP.aliyun.sms.config.AliyunSmsConfig",
            "$PROJECT_GROUP.cache.config.RedisCacheConfig",
            "$PROJECT_GROUP.captcha.config.CaptchaConfig",
            "$PROJECT_GROUP.feign.config.FeignConfig",
            "$PROJECT_GROUP.id.config.IdConfig",
            "$PROJECT_GROUP.jwt.config.JwtConfig",
            "$PROJECT_GROUP.knife4j.config.Knife4jExtensionConfig",
            "$PROJECT_GROUP.mybatis.config.MybatisPlusConfig",
            "$PROJECT_GROUP.web.config.WebAuthConfig",
            "$PROJECT_GROUP.web.config.WebConfig",
            "$PROJECT_GROUP.wechat.config.WechatConfig",
            "$PROJECT_GROUP.xxljob.config.XxlJobConfig",
            "$PROJECT_GROUP.knife4j.config.Knife4jExtensionConfig",
            "$PROJECT_GROUP.fus.config.FusConfig"
        ).filter(::hasClass).toTypedArray()

    private fun hasClass(className: String) =
        try {
            Class.forName(className)
            logger.debug("$className included.")
            true
        } catch (e: ClassNotFoundException) {
            logger.debug("$className does not exists.")
            false
        }
}
