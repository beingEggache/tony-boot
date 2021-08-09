package com.tony.webcore.config

import com.tony.core.BAD_REQUEST
import com.tony.core.BIZ_ERROR
import com.tony.core.INTERNAL_SERVER_ERROR
import com.tony.core.OK
import com.tony.core.UNAUTHORIZED
import com.tony.webcore.converter.EnumIntValueConverterFactory
import com.tony.webcore.converter.EnumStringValueConverterFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebProperties::class)
@ServletComponentScan(basePackages = ["com.tony.webcore.filter"])
internal class WebConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(EnumIntValueConverterFactory())
        registry.addConverterFactory(EnumStringValueConverterFactory())
    }
}

@ConstructorBinding
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "web")
internal data class WebProperties(
    val successCode: Int = OK,
    val errorMsg: String = "访客太多，请稍后重试",
    val errorCode: Int = INTERNAL_SERVER_ERROR,
    val validationErrorMsg: String = "参数有误，请检查后输入",
    val validationErrorCode: Int = BAD_REQUEST,
    val bizErrorCode: Int = BIZ_ERROR,
    val unauthorizedCode: Int = UNAUTHORIZED
)
