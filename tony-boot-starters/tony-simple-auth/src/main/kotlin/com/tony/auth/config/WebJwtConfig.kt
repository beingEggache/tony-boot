package com.tony.auth.config

import com.auth0.jwt.JWT
import com.tony.auth.ApiSession
import com.tony.auth.DefaultLoginCheckInterceptor
import com.tony.auth.JwtApiSession
import com.tony.auth.LoginCheckInterceptor
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
internal class WebJwtConfig : WebMvcConfigurer {

    @ConditionalOnMissingBean(LoginCheckInterceptor::class)
    @Bean
    fun authenticationInterceptor() = DefaultLoginCheckInterceptor()

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
    @ConditionalOnExpression("\${web.jwt.enabled:false}")
    @Bean
    fun apiSession() = JwtApiSession()
}
