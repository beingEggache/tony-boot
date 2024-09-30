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

package com.tony.alipay.config

import com.tony.alipay.AlipayManager
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@EnableConfigurationProperties(AlipayProperties::class)
@Configuration(proxyBeanMethods = false)
private class AlipayConfig(
    private val alipayProperties: AlipayProperties,
) {
    private val resourceResolver = PathMatchingResourcePatternResolver()

    @Bean
    private fun alipayService() =
        AlipayManager(
            alipayProperties.appId,
            getFrom(alipayProperties.publicKeyPath),
            getFrom(alipayProperties.privateKeyPath),
            getFrom(alipayProperties.aliPublicKeyPath)
        )

    private fun getFrom(path: String): String =
        resourceResolver
            .getResource(path)
            .file
            .readText()
}

@ConfigurationProperties(prefix = "alipay")
private data class AlipayProperties(
    val appId: String,
    val publicKeyPath: String,
    val privateKeyPath: String,
    val aliPublicKeyPath: String,
)
