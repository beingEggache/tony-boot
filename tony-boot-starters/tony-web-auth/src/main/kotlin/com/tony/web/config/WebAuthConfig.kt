package com.tony.web.config

import com.tony.jwt.config.JwtProperties
import com.tony.utils.getLogger
import com.tony.web.ApiSession
import com.tony.web.HaveWhiteListUrlPattern
import com.tony.web.JwtApiSession
import com.tony.web.NoopApiSession
import com.tony.web.interceptor.DefaultJwtLoginCheckInterceptor
import com.tony.web.interceptor.LoginCheckInterceptor
import com.tony.web.interceptor.NoopLoginCheckInterceptor
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(JwtProperties::class)
internal class WebAuthConfig(
    private val jwtProperties: JwtProperties,
) : WebMvcConfigurer, HaveWhiteListUrlPattern {

    private val logger = getLogger()

    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @Bean
    internal fun loginCheckInterceptor(): LoginCheckInterceptor =
        if (jwtProperties.secret.isNotBlank()) DefaultJwtLoginCheckInterceptor() else NoopLoginCheckInterceptor()

    @ConditionalOnMissingBean(ApiSession::class)
    @ConditionalOnWebApplication
    @Bean
    internal fun apiSession(): ApiSession =
        if (jwtProperties.secret.isNotBlank()) {
            logger.info("Jwt auth is enabled")
            JwtApiSession()
        } else {
            NoopApiSession()
        }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginCheckInterceptor())
            .excludePathPatterns(*whiteUrlPatterns(prefix = "").toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }
}
