package com.tony.webcore.config

import com.tony.webcore.WebApp
import com.tony.webcore.auth.interceptor.DefaultLoginCheckInterceptor
import com.tony.webcore.auth.interceptor.LoginCheckInterceptor
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.Resource

@Configuration
@ConditionalOnWebApplication
@ConditionalOnExpression("\${web.jwt.enabled:false}")
@EnableConfigurationProperties(WebJwtProperties::class)
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
}

@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web.jwt")
@ConditionalOnExpression("\${web.jwt.enabled:false}")
internal data class WebJwtProperties(
    val secret: String,
    val expiredMinutes: Long = 525600L
)
