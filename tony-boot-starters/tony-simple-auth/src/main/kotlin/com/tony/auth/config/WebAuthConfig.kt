package com.tony.auth.config

import com.tony.auth.ApiSession
import com.tony.auth.DefaultJwtLoginCheckInterceptor
import com.tony.auth.JwtApiSession
import com.tony.auth.LoginCheckInterceptor
import com.tony.auth.NoopApiSession
import com.tony.auth.NoopLoginCheckInterceptor
import com.tony.jwt.config.JwtProperties
import com.tony.webcore.WebApp
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(JwtProperties::class)
internal class WebAuthConfig(
    private val jwtProperties: JwtProperties
) : WebMvcConfigurer {

    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @Bean
    fun loginCheckInterceptor(): LoginCheckInterceptor =
        if (jwtProperties.enabled) DefaultJwtLoginCheckInterceptor() else NoopLoginCheckInterceptor()

    @ConditionalOnMissingBean(ApiSession::class)
    @ConditionalOnWebApplication
    @Bean
    fun apiSession(): ApiSession =
        if (jwtProperties.enabled) JwtApiSession() else NoopApiSession()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginCheckInterceptor())
            .excludePathPatterns(*WebApp.ignoreUrlPatterns(true).toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }
}
