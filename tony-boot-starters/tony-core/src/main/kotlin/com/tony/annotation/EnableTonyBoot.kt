package com.tony.annotation

import com.tony.ApplicationContextHolder
import com.tony.PROJECT_GROUP
import com.tony.utils.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Import(
    value = [
        TonyBootImportSelector::class,
        TonyBootConfiguration::class
    ]
)
annotation class EnableTonyBoot

@Configuration
internal class TonyBootConfiguration {

    @Bean
    fun applicationContextHolder() = ApplicationContextHolder
}

internal class TonyBootImportSelector : ImportSelector {

    private val logger = getLogger()

    override fun selectImports(importingClassMetadata: AnnotationMetadata) = mutableListOf(
        "$PROJECT_GROUP.alipay.config.AlipayConfig",
        "$PROJECT_GROUP.aliyun.oss.config.AliyunOssConfig",
        "$PROJECT_GROUP.aliyun.sms.config.AliyunSmsConfig",
        "$PROJECT_GROUP.cache.config.RedisCacheConfig",
        "$PROJECT_GROUP.feign.config.FeignConfig",
        "$PROJECT_GROUP.jwt.config.JwtConfig",
        "$PROJECT_GROUP.web.config.WebConfig",
        "$PROJECT_GROUP.web.config.WebAuthConfig",
        "$PROJECT_GROUP.wechat.config.WechatConfig"
    ).filter(::hasClass).toTypedArray()

    private fun hasClass(className: String) = try {
        Class.forName(className)
        logger.debug("$className included.")
        true
    } catch (e: ClassNotFoundException) {
        logger.debug("$className does not exists.")
        false
    }
}
