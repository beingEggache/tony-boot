package com.tony.alipay.config

import com.tony.alipay.AlipayManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
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

@ConstructorBinding
@ConfigurationProperties(prefix = "alipay")
internal data class AlipayProperties(
    val appId: String,
    val publicKeyPath: String,
    val privateKeyPath: String,
    val aliPublicKeyPath: String,
)
