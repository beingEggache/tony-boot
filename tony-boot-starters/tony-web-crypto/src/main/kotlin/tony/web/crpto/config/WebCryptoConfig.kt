/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.web.crpto.config

import jakarta.annotation.Resource
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import tony.crypto.CryptoProvider
import tony.web.crpto.DecryptRequestBodyAdvice
import tony.web.crpto.DefaultDecryptRequestBodyAdvice
import tony.web.crpto.DefaultEncryptResponseBodyAdvice
import tony.web.crpto.EncryptResponseBodyAdvice

/**
 * WebCryptoConfig is
 * @author tangli
 * @date 2023/05/26 19:00
 */
@ConditionalOnWebApplication(
    type =
        ConditionalOnWebApplication
            .Type
            .SERVLET
)
@ConditionalOnExpression("\${web.crypto.enabled:true}")
@Configuration(proxyBeanMethods = false)
internal class WebCryptoConfig {
    @Resource
    private fun initMappingJackson2HttpMessageConverter(
        mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter,
    ) {
        val supportedMediaTypes =
            mappingJackson2HttpMessageConverter
                .supportedMediaTypes
                .toMutableSet()
                .apply { add(MediaType.TEXT_PLAIN) }
                .toTypedArray()
        mappingJackson2HttpMessageConverter.supportedMediaTypes = listOf(*supportedMediaTypes)
    }

    @ConditionalOnMissingBean(DecryptRequestBodyAdvice::class)
    @Bean
    private fun decryptRequestBodyAdvice(cryptoProvider: CryptoProvider): DecryptRequestBodyAdvice =
        DefaultDecryptRequestBodyAdvice(cryptoProvider)

    @ConditionalOnMissingBean(EncryptResponseBodyAdvice::class)
    @Bean
    private fun encryptResponseBodyAdvice(cryptoProvider: CryptoProvider): EncryptResponseBodyAdvice =
        DefaultEncryptResponseBodyAdvice(cryptoProvider)
}
