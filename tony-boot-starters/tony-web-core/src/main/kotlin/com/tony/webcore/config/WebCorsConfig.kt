package com.tony.webcore.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebCorsProperties::class)
@ConditionalOnExpression("\${web.cors.enabled:false}")
internal class WebCorsConfig(
    private val webCorsProperties: WebCorsProperties
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*webCorsProperties.allowedOrigins)
            .allowedMethods("*")
            .allowedHeaders(*webCorsProperties.allowedHeaders)
            .allowCredentials(webCorsProperties.allowCredentials)
    }
}

@Suppress("ArrayInDataClass")
@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web.cors")
@ConditionalOnExpression("\${web.cors.enabled:false}")
internal data class WebCorsProperties(
    val allowedOrigins: Array<String>,
    val allowedHeaders: Array<String>,
    val allowCredentials: Boolean = false
)
