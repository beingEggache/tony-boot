package com.tony.alipay.config

import com.tony.alipay.AlipayManager
import com.tony.exception.ApiException
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@Configuration
@EnableConfigurationProperties(AlipayProperties::class)
internal class AlipayAutoConfiguration(
    private val alipayProperties: AlipayProperties
) {

    private val resourceResolver = PathMatchingResourcePatternResolver()

    @Bean
    fun alipayService() = let {
        val appId = alipayProperties.appId
        val publicKey = getKey(alipayProperties.publicKeyPath, "publicKey")
        val privateKey = getKey(alipayProperties.privateKeyPath, "privateKey")
        val aliPublicKey = getKey(alipayProperties.aliPublicKeyPath, "aliPublicKey")

        AlipayManager(
            appId,
            publicKey,
            privateKey,
            aliPublicKey
        )
    }

    private fun getKey(path: String?, name: String): String {
        val resourceLocation = path ?: throw ApiException("$name Path must not be null")
        val resource = resourceResolver.getResources(resourceLocation).firstOrNull()
            ?: throw ApiException("$name resource not found")
        return resource.file.readText()
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "alipay")
internal data class AlipayProperties(
    val appId: String,
    val publicKeyPath: String,
    val privateKeyPath: String,
    val aliPublicKeyPath: String
)
