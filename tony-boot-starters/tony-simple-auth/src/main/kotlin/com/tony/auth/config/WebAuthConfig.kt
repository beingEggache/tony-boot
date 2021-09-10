package com.tony.auth.config

import com.auth0.jwt.JWT
import com.tony.auth.ApiSession
import com.tony.auth.DefaultJwtLoginCheckInterceptor
import com.tony.auth.JwtApiSession
import com.tony.auth.LoginCheckInterceptor
import com.tony.auth.NoopApiSession
import com.tony.auth.NoopLoginCheckInterceptor
import com.tony.webcore.WebApp
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.Resource

@Configuration
@ConditionalOnWebApplication
@ConditionalOnExpression("\${jwt.enabled:false}")
internal class WebAuthConfig : WebMvcConfigurer {

    @ConditionalOnClass(JWT::class)
    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @ConditionalOnExpression("\${jwt.enabled:false}")
    @Bean
    fun jwtLoginCheckInterceptor(): LoginCheckInterceptor = DefaultJwtLoginCheckInterceptor()

    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @ConditionalOnExpression("\${!jwt.enabled:false}")
    @Bean
    fun noopLoginCheckInterceptor(): LoginCheckInterceptor = NoopLoginCheckInterceptor()

    @Lazy
    @Resource
    lateinit var loginCheckInterceptor: LoginCheckInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginCheckInterceptor)
            .excludePathPatterns(*WebApp.ignoreUrlPatterns(true).toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }

    @ConditionalOnClass(JWT::class)
    @ConditionalOnMissingBean(ApiSession::class)
    @ConditionalOnWebApplication
    @ConditionalOnExpression("\${jwt.enabled:false}")
    @Bean
    fun jwtApiSession(): ApiSession = JwtApiSession()

    @ConditionalOnMissingBean(ApiSession::class)
    @ConditionalOnWebApplication
    @ConditionalOnExpression("\${!jwt.enabled:false}")
    @Bean
    fun noopApiSession(): ApiSession = NoopApiSession()
}
