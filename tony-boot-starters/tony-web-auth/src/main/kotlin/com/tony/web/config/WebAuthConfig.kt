package com.tony.web.config

import com.tony.jwt.config.JwtProperties
import com.tony.web.ApiSession
import com.tony.web.JwtApiSession
import com.tony.web.NoopApiSession
import com.tony.web.WebApp
import com.tony.web.interceptor.DefaultLoginCheckInterceptor
import com.tony.web.interceptor.LoginCheckInterceptor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * WebAuthConfig
 *
 * @author tangli
 * @since 2023/5/25 15:13
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(JwtProperties::class, WebAuthProperties::class)
internal class WebAuthConfig(
    private val jwtProperties: JwtProperties,
    private val webAuthProperties: WebAuthProperties,
) : WebMvcConfigurer {

    private val logger = LoggerFactory.getLogger(WebAuthConfig::class.java)

    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @Bean
    internal fun loginCheckInterceptor(): LoginCheckInterceptor = DefaultLoginCheckInterceptor()

    @ConditionalOnMissingBean(ApiSession::class)
    @Bean
    internal fun apiSession(): ApiSession =
        if (jwtProperties.secret.isNotBlank()) {
            logger.info("Jwt auth is enabled")
            JwtApiSession()
        } else {
            NoopApiSession()
        }

    override fun addInterceptors(registry: InterceptorRegistry) {
        logger.info("noLoginCheckUrl:${webAuthProperties.noLoginCheckUrl}")
        registry.addInterceptor(loginCheckInterceptor())
            .excludePathPatterns(*WebApp.whiteUrlPatterns.plus(webAuthProperties.noLoginCheckUrl).toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }
}

/**
 * WebAuthConfig
 *
 * @author tangli
 * @since 2023/5/25 15:13
 */
@ConstructorBinding
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = "web.auth")
public data class WebAuthProperties(
    /**
     * 不需要登录校验的地址.
     */
    val noLoginCheckUrl: Set<String> = setOf(),
)
