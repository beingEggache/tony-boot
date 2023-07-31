package com.tony.jwt.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Configuration

/**
 * JwtConfig
 * @author tangli
 * @since 2023/5/25 15:55
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(JwtProperties::class)
internal class JwtConfig

/**
 * JwtProperties
 * @author tangli
 * @since 2023/5/25 15:56
 */
@ConfigurationProperties(prefix = "jwt")
public data class JwtProperties
    @ConstructorBinding
    constructor(
        @DefaultValue("")
        val secret: String,
        /**
         * jwt token expired minutes, default value is one year.
         */
        @DefaultValue("525600")
        val expiredMinutes: Long,
    )
