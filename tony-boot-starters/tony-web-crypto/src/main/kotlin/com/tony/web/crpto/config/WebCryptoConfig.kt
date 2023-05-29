package com.tony.web.crpto.config

import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.getLogger
import com.tony.web.crpto.DecryptRequestAdvice
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
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
internal class WebCryptoConfig {

    private val logger = getLogger(WebCryptoConfig::class.java.name)

    @ConditionalOnExpression("\${web.crypto.enabled:false}")
    @Bean
    internal fun decryptRequestAdvice(): DecryptRequestAdvice {
        logger.info("Request body decrypt is enabled.")
        return DecryptRequestAdvice()
    }
}

/**
 * WebCryptoProperties
 *
 * @author tangli
 * @since 2023/5/26 17:06
 */
@ConstructorBinding
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = "web.crypto")
@ConditionalOnExpression("\${web.crypto.enabled:false}")
internal data class WebCryptoProperties(

    @DefaultValue("false")
    val enabled: Boolean = false,
    /**
     * 加解密算法, 目前只支持 aes/des, 默认des
     */
    @DefaultValue("des")
    val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.DES,
)
