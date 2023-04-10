package com.tony.jwt.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnWebApplication
@ConditionalOnExpression("'\${jwt.secret:}'.trim() > ''")
@EnableConfigurationProperties(JwtProperties::class)
internal class JwtConfig

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public data class JwtProperties(
    @DefaultValue("")
    val secret: String = "",
    /**
     * jwt token expired minutes, default value is one year.
     */
    @DefaultValue("525600")
    val expiredMinutes: Long = 525600L,
)
