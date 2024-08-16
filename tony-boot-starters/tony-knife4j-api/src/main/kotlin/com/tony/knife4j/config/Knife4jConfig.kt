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

package com.tony.knife4j.config

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j
import com.tony.misc.YamlPropertySourceFactory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@EnableKnife4j
@ConditionalOnExpression("\${knife4j.enable:true}")
@PropertySource("classpath:knife4j.config.yml", factory = YamlPropertySourceFactory::class)
@EnableConfigurationProperties(Knife4jExtensionProperties::class)
@Configuration
internal class Knife4jExtensionConfig(
    private val knife4jExtensionProperties: Knife4jExtensionProperties,
) {
    private val logger = LoggerFactory.getLogger(Knife4jExtensionConfig::class.java)

    @Bean
    internal fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title(knife4jExtensionProperties.title)
                    .version(knife4jExtensionProperties.version)
                    .description(knife4jExtensionProperties.description)
                    .contact(knife4jExtensionProperties.contact)
            )

    @Bean
    internal fun api(): GroupedOpenApi {
        logger.info("Knife4j is Enabled")
        return GroupedOpenApi
            .builder()
            .group("default")
            .addOperationCustomizer { operation, _ ->
                operation.responses = ApiResponses().addApiResponse("200", operation.responses["200"])
                operation
            }.addOpenApiMethodFilter {
                it
                    .annotations
                    .map { annotation ->
                        annotation.annotationClass
                    }.contains(Operation::class)
            }.build()
    }
}

@ConfigurationProperties(prefix = "knife4j.extension")
public data class Knife4jExtensionProperties(
    @DefaultValue("Tony-Api")
    val title: String,
    @DefaultValue("1.0")
    val version: String,
    @DefaultValue("")
    val description: String,
    val contact: Contact = Contact(),
)
