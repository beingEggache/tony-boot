/**
 * ktwl-boot-dependencies
 * CaptchaConfig
 *
 * @author tangli
 * @since 2022/7/12 11:38
 */
package com.tony.captcha.config

import com.tony.captcha.CaptchaService
import com.tony.captcha.DefaultCaptchaServiceImpl
import com.tony.captcha.NoopCaptchaServiceImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * ktwl-sale CaptchaConfig
 *
 * @author tangli
 * @since 2022/3/10 15:14
 */
@Configuration
@EnableConfigurationProperties(CaptchaProperties::class)
class CaptchaConfig(
    private val captchaProperties: CaptchaProperties
) {

    @ConditionalOnMissingBean(CaptchaService::class)
    @Bean
    fun captchaService(): CaptchaService =
        if (captchaProperties.mode == CaptchaMode.DEFAULT) {
            DefaultCaptchaServiceImpl()
        } else {
            NoopCaptchaServiceImpl()
        }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "captcha")
data class CaptchaProperties(
    /**
     * captcha mode.
     */
    val mode: CaptchaMode = CaptchaMode.NOOP
)

enum class CaptchaMode {
    NOOP,
    DEFAULT
}
