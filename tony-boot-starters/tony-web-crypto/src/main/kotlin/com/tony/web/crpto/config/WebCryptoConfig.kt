package com.tony.web.crpto.config

import com.tony.crypto.symmetric.enums.CryptoEncoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.getLogger
import com.tony.web.crpto.DecryptRequestBodyAdvice
import com.tony.web.crpto.DefaultDecryptRequestBodyAdvice
import com.tony.web.crpto.DefaultEncryptResponseBodyAdvice
import com.tony.web.crpto.EncryptResponseBodyAdvice
import jakarta.annotation.Resource
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

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

    @Resource
    private fun initMappingJackson2HttpMessageConverter(
        mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter,
    ) {
        if (!webCryptoProperties.enabled) {
            return
        }
        val supportedMediaTypes = mappingJackson2HttpMessageConverter
            .supportedMediaTypes
            .toMutableSet()
            .apply { add(MediaType.TEXT_PLAIN) }
            .toTypedArray()
        mappingJackson2HttpMessageConverter.supportedMediaTypes = listOf(*supportedMediaTypes)
    }

    @ConditionalOnExpression("\${web.crypto.enabled:false}")
    @ConditionalOnMissingBean(DecryptRequestBodyAdvice::class)
    @Bean
    internal fun decryptRequestBodyAdvice(): DecryptRequestBodyAdvice =
        DefaultDecryptRequestBodyAdvice(
            webCryptoProperties.algorithm,
            webCryptoProperties.secret,
            webCryptoProperties.encoding
        ).apply {
            getLogger(this::class.java.name).info("Request body decrypt is enabled.")
        }

    @ConditionalOnExpression("\${web.crypto.enabled:false}")
    @ConditionalOnMissingBean(EncryptResponseBodyAdvice::class)
    @Bean
    internal fun encryptResponseBodyAdvice(): EncryptResponseBodyAdvice =
        DefaultEncryptResponseBodyAdvice(
            webCryptoProperties.algorithm,
            webCryptoProperties.secret,
            webCryptoProperties.encoding
        ).apply {
            getLogger(this::class.java.name).info("Response body encrypt is enabled.")
        }
}

/**
 * WebCryptoProperties
 *
 * @author tangli
 * @since 2023/5/26 17:06
 */
@ConfigurationProperties(prefix = "web.crypto")
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
