package com.tony.web.crpto.config

import com.tony.crypto.symmetric.enums.CryptoEncoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.getLogger
import com.tony.web.crpto.DecryptRequestBodyAdvice
import com.tony.web.crpto.EncryptResponseBodyAdvice
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * WebCryptoConfig is
 * @author tangli
 * @since 2023/05/26 17:00
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(value = [WebCryptoProperties::class])
internal class WebCryptoConfig(
    private val webCryptoProperties: WebCryptoProperties,
) {

    private val logger = getLogger(WebCryptoConfig::class.java.name)

    @ConditionalOnExpression("\${web.crypto.enabled:false}")
    @Bean
    internal fun decryptRequestBodyAdvice(): DecryptRequestBodyAdvice {
        logger.info("Request body decrypt is enabled.")
        return DecryptRequestBodyAdvice(webCryptoProperties)
    }

    @ConditionalOnExpression("\${web.crypto.enabled:false}")
    @Bean
    internal fun encryptResponseBodyAdvice(): EncryptResponseBodyAdvice {
        logger.info("Response body encrypt is enabled.")
        return EncryptResponseBodyAdvice(webCryptoProperties)
    }
}

/**
 * WebCryptoProperties
 *
 * @author tangli
 * @since 2023/5/26 17:06
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = "web.crypto")
@ConditionalOnExpression("\${web.crypto.enabled:false}")
internal data class WebCryptoProperties
    @ConstructorBinding
    constructor(

        @DefaultValue("false")
        val enabled: Boolean,
        /**
         * 加解密算法, 目前只支持 aes/des, 默认des
         */
        @DefaultValue("des")
        val algorithm: SymmetricCryptoAlgorithm,

        /**
         * 秘钥
         */
        @DefaultValue("")
        val secret: String,

        /**
         * 二进制编码
         */
        @DefaultValue("base64")
        val encoding: CryptoEncoding,
    )
