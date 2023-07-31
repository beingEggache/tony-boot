package com.tony.alipay.config

import com.tony.alipay.AlipayManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@Configuration
@EnableConfigurationProperties(AlipayProperties::class)
internal class AlipayConfig(
    private val alipayProperties: AlipayProperties,
) {

    private val resourceResolver = PathMatchingResourcePatternResolver()

    @Bean
    internal fun alipayService() =
        AlipayManager(
            alipayProperties.appId,
            getFrom(alipayProperties.publicKeyPath),
            getFrom(alipayProperties.privateKeyPath),
            getFrom(alipayProperties.aliPublicKeyPath)
        )

    private fun getFrom(path: String): String = resourceResolver.getResource(path).file.readText()
}

@ConfigurationProperties(prefix = "alipay")
internal data class AlipayProperties
    @ConstructorBinding
    constructor(
        val appId: String,
        val publicKeyPath: String,
        val privateKeyPath: String,
        val aliPublicKeyPath: String,
    )
