package com.tony.captcha.config
/**
 * tony-boot-dependencies
 * CaptchaConfig
 *
 * @author tangli
 * @since 2022/3/10 15:14
 */
import com.tony.captcha.CaptchaService
import com.tony.captcha.DefaultCaptchaServiceImpl
import com.tony.captcha.NoopCaptchaServiceImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 验证码配置
 *
 * @author tangli
 * @since 2023/5/25 15:41
 */
@Configuration
@EnableConfigurationProperties(CaptchaProperties::class)
internal class CaptchaConfig(
    private val captchaProperties: CaptchaProperties,
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

/**
 * CaptchaProperties
 *
 * @author tangli
 * @since 2023/5/25 15:42
 */
@ConfigurationProperties(prefix = "captcha")
internal data class CaptchaProperties
    @ConstructorBinding
    constructor(
        /**
         * captcha mode.
         */
        @DefaultValue("NOOP")
        val mode: CaptchaMode = CaptchaMode.NOOP,
    )

public enum class CaptchaMode {
    NOOP,
    DEFAULT,
}
