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

package tony.captcha.config

/**
 * tony-boot-dependencies
 * CaptchaConfig
 *
 * @author tangli
 * @date 2022/03/10 19:14
 */
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tony.captcha.CaptchaService
import tony.captcha.DefaultCaptchaServiceImpl
import tony.captcha.NoopCaptchaServiceImpl

/**
 * 验证码配置
 *
 * @author tangli
 * @date 2023/05/25 19:41
 */
@EnableConfigurationProperties(CaptchaProperties::class)
@Configuration(proxyBeanMethods = false)
private class CaptchaConfig(
    private val captchaProperties: CaptchaProperties,
) {
    @ConditionalOnMissingBean(CaptchaService::class)
    @Bean
    private fun captchaService(): CaptchaService =
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
 * @date 2023/05/25 19:42
 */
@ConfigurationProperties(prefix = "captcha")
private data class CaptchaProperties(
    /**
     * captcha mode.
     */
    val mode: CaptchaMode = CaptchaMode.NOOP,
)

public enum class CaptchaMode {
    NOOP,
    DEFAULT,
}
