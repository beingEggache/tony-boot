package com.tony.swagger.config

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver
import io.swagger.annotations.ApiOperation
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc

@EnableSwagger2WebMvc
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration::class)
@EnableConfigurationProperties(Knife4jExtensionProperties::class)
@ConditionalOnExpression("\${knife4j.enable:false}")
@Configuration
internal class Knife4jExtensionConfig(
    private val knife4jExtensionProperties: Knife4jExtensionProperties,
    private val openApiExtensionResolver: OpenApiExtensionResolver
) {

    private fun apiInfo() = ApiInfoBuilder()
        .contact(knife4jExtensionProperties.contact)
        .title(knife4jExtensionProperties.title)
        .version(knife4jExtensionProperties.version)
        .description(knife4jExtensionProperties.description)
        .build()

    @Bean
    fun docket(): Docket = Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .apiInfo(apiInfo())
        .pathMapping("/")
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation::class.java))
        .build()
        .extensions(openApiExtensionResolver.buildExtensions(null))

}

@Suppress("ArrayInDataClass")
@ConstructorBinding
@ConfigurationProperties(prefix = "knife4j.extension")
internal data class Knife4jExtensionProperties(
    val title: String = "Tony-Api",
    val version: String = "1.0",
    val description: String = "",
    val contact: Contact = Contact("", "", "")
)
