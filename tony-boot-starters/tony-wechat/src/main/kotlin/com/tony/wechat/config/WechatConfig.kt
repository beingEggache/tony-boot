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

package com.tony.wechat.config

import com.tony.wechat.DefaultWechatApiAccessTokenProvider
import com.tony.wechat.DefaultWechatPropProvider
import com.tony.wechat.WechatApiAccessTokenProvider
import com.tony.wechat.WechatPropProvider
import com.tony.wechat.client.WechatClient
import jakarta.annotation.Resource
import kotlin.reflect.full.findAnnotation
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.FeignClientBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
@EnableConfigurationProperties(WechatProperties::class)
internal class WechatConfig {
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

    @Bean
    internal fun wechatClient(applicationContext: ApplicationContext): WechatClient =
        WechatClient::class.findAnnotation<FeignClient>().let {
            FeignClientBuilder(applicationContext)
                .forType(WechatClient::class.java, it?.value)
                .url(it?.url)
                .build()
        }

    @ConditionalOnMissingBean(WechatApiAccessTokenProvider::class)
    @Bean
    internal fun apiAccessTokenProviderWrapper(): WechatApiAccessTokenProvider =
        DefaultWechatApiAccessTokenProvider()

    @ConditionalOnMissingBean(WechatPropProvider::class)
    @Bean
    internal fun wechatApiPropProvider(wechatProperties: WechatProperties) =
        DefaultWechatPropProvider(wechatProperties)
}

/**
 * 微信配置
 *
 * @author tangli
 * @date 2023/05/25 19:22
 */
@ConfigurationProperties(prefix = "wechat")
public data class WechatProperties
    @ConstructorBinding
    constructor(
        val token: String?,
        val appId: String?,
        val appSecret: String?,
        val mchId: String?,
        val mchSecretKey: String?,
        val app: LinkedHashMap<String, WechatAppProperties>?,
    ) {
        public fun getAppByAppId(appId: String): String? =
            app
                ?.entries
                ?.firstOrNull { it.value.appId == appId }
                ?.key
    }

/**
 * 微信配置
 *
 * @author tangli
 * @date 2023/05/25 19:22
 */
public data class WechatAppProperties(
    val token: String?,
    val appId: String?,
    val appSecret: String?,
    val mchId: String?,
    val mchSecretKey: String?,
)
