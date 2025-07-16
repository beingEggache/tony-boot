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

package tony.web.auth.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import tony.core.SpringContexts
import tony.core.utils.getLogger
import tony.jwt.config.JwtProperties
import tony.web.WebContext
import tony.web.WebSession
import tony.web.auth.JwtWebSession
import tony.web.auth.NoopWebSession
import tony.web.auth.interceptor.DefaultLoginCheckInterceptor
import tony.web.auth.interceptor.LoginCheckInterceptor

/**
 * WebAuthConfig
 *
 * @author tangli
 * @date 2023/05/25 19:13
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(JwtProperties::class, WebAuthProperties::class)
@Configuration(proxyBeanMethods = false)
private class WebAuthConfig(
    private val jwtProperties: JwtProperties,
    private val webAuthProperties: WebAuthProperties,
) : WebMvcConfigurer {
    private val logger = LoggerFactory.getLogger(WebAuthConfig::class.java)

    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @Bean
    private fun loginCheckInterceptor(): LoginCheckInterceptor =
        DefaultLoginCheckInterceptor()

    @ConditionalOnMissingBean(WebSession::class)
    @Bean
    private fun webSession(): WebSession =
        if (jwtProperties.secret.isNotBlank()) {
            JwtWebSession().apply {
                getLogger().info("Jwt auth is enabled")
            }
        } else {
            NoopWebSession()
        }

    override fun addInterceptors(registry: InterceptorRegistry) {
        logger.info("noLoginCheckUrl:${webAuthProperties.noLoginCheckUrl}")
        registry
            .addInterceptor(SpringContexts.getBean(LoginCheckInterceptor::class.java))
            .excludePathPatterns(
                *WebContext
                    .excludePathPatterns()
                    .plus(webAuthProperties.noLoginCheckUrl)
                    .toTypedArray()
            ).order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }
}

/**
 * WebAuthConfig
 *
 * @author tangli
 * @date 2023/05/25 19:13
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = "web.auth")
private data class WebAuthProperties(
    /**
     * 不需要登录校验的地址.
     */
    val noLoginCheckUrl: Set<String> = setOf(),
)
