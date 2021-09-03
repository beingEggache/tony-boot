package com.tony.webcore.config

import com.tony.webcore.converter.EnumIntValueConverterFactory
import com.tony.webcore.converter.EnumStringValueConverterFactory
import com.tony.webcore.log.DefaultRequestTraceLogger
import com.tony.webcore.log.RequestTraceLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnWebApplication
@ServletComponentScan(basePackages = ["com.tony.webcore.log"])
internal class WebConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(EnumIntValueConverterFactory())
        registry.addConverterFactory(EnumStringValueConverterFactory())
    }

    @ConditionalOnMissingBean(RequestTraceLogger::class)
    @Bean
    fun defaultRequestTraceLogger() = DefaultRequestTraceLogger()
}
