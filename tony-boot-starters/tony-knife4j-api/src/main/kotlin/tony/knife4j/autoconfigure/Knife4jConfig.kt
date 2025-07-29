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

package tony.knife4j.autoconfigure

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.xingfudeshi.knife4j.spring.annotations.EnableKnife4j
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springdoc.core.models.GroupedOpenApi
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import tony.core.misc.YamlPropertySourceFactory
import tony.knife4j.customizers.EnumValueCustomizer
import tony.knife4j.customizers.FlattenPropertiesOpenApiCustomizer
import tony.knife4j.customizers.OctetStreamResponseOperationCustomizer
import tony.knife4j.customizers.WrapResponseBodyOperationCustomizer

@EnableKnife4j
@ConditionalOnBooleanProperty(prefix = "knife4j", value = ["enabled"], matchIfMissing = true)
@PropertySource("classpath:knife4j.config.yml", factory = YamlPropertySourceFactory::class)
@EnableConfigurationProperties(Knife4jExtensionProperties::class)
@AutoConfiguration
private class Knife4jExtensionAutoConfiguration(
    private val knife4jExtensionProperties: Knife4jExtensionProperties,
) {
    private val logger = LoggerFactory.getLogger(Knife4jExtensionAutoConfiguration::class.java)

    @PostConstruct
    private fun init() {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(JsonIgnore::class.java)
    }

    @Bean
    private fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title(knife4jExtensionProperties.title)
                    .version(knife4jExtensionProperties.version)
                    .description(knife4jExtensionProperties.description)
                    .contact(knife4jExtensionProperties.contact)
            )

    @Bean
    private fun api(): GroupedOpenApi {
        logger.info("Knife4j is Enabled")
        return GroupedOpenApi
            .builder()
            .group("default")
            .addOpenApiCustomizer(FlattenPropertiesOpenApiCustomizer())
            .build()
    }

    @Bean
    private fun enumValueCustomizer(): EnumValueCustomizer =
        EnumValueCustomizer()

    @Bean
    private fun octetStreamResponseOperationCustomizer(): OctetStreamResponseOperationCustomizer =
        OctetStreamResponseOperationCustomizer()

    @ConditionalOnBooleanProperty(
        prefix = "web",
        value = ["wrap-response-body-enabled"],
        matchIfMissing = true
    )
    @Bean
    private fun wrapResponseBodyOperationCustomizer(): WrapResponseBodyOperationCustomizer =
        WrapResponseBodyOperationCustomizer()
}

@ConfigurationProperties(prefix = "knife4j.extension")
private data class Knife4jExtensionProperties(
    @DefaultValue("Tony-Api")
    val title: String,
    @DefaultValue("1.0")
    val version: String,
    @DefaultValue("")
    val description: String,
    val contact: Contact = Contact(),
)
