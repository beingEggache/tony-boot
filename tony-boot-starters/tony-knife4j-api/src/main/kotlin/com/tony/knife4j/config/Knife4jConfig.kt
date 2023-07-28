package com.tony.knife4j.config

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j
import com.tony.misc.YamlPropertySourceFactory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.slf4j.LoggerFactory
import org.springdoc.core.GroupedOpenApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.annotation.AnnotationUtils

@EnableKnife4j
@EnableConfigurationProperties(Knife4jExtensionProperties::class)
@ConditionalOnExpression("\${knife4j.enable:true}")
@PropertySource("classpath:knife4j.config.yml", factory = YamlPropertySourceFactory::class)
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
            .addOpenApiMethodFilter {
                AnnotationUtils.getAnnotation(it, Operation::class.java) != null
            }
            .build()
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "knife4j.extension")
internal data class Knife4jExtensionProperties(
    val title: String = "Tony-Api",
    val version: String = "1.0",
    val description: String = "",
    val contact: Contact = Contact(),
)
