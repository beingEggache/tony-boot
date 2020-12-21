package com.tony.swagger.config

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.RequestHandler
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc
import java.util.function.Predicate

@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration::class)
@EnableConfigurationProperties(SwaggerProperties::class)
internal class SwaggerConfig(
    private val swaggerProperties: SwaggerProperties,
    private val openApiExtensionResolver: OpenApiExtensionResolver
) {

    private fun apiInfo() = ApiInfoBuilder()
        .contact(swaggerProperties.contact)
        .title(swaggerProperties.title)
        .version(swaggerProperties.version)
        .description(swaggerProperties.description)
        .build()

    @Bean
    fun docket(): Docket = Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .apiInfo(apiInfo())
        .select()
        .apis(basePackages(swaggerProperties.basePackages))
        .paths(PathSelectors.any())
        .build()
        .extensions(openApiExtensionResolver.buildExtensions(null))

    private fun basePackages(basePackages: Array<String>): Predicate<RequestHandler> = Predicate { input ->
        basePackages.any {
            @Suppress("DEPRECATION")
            input.declaringClass().packageName.startsWith(it)
        }
    }
}

@Suppress("ArrayInDataClass")
@ConstructorBinding
@ConfigurationProperties(prefix = "swagger")
internal data class SwaggerProperties(
    val title: String = "Tony-Api",
    val version: String = "1.0",
    val description: String = "",
    val contact: Contact = Contact("", "", ""),
    val basePackages: Array<String> = arrayOf()
)
