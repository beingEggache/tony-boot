package com.tony.config

import com.fasterxml.jackson.databind.BeanProperty
import com.tony.MonoApiWebContext.tenantId
import com.tony.jackson.InjectableValueSupplier
import com.tony.web.WebContext
import com.tony.web.WebContextExtensions.appId
import com.tony.web.WebContextExtensions.userId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * MonoApiWebConfig is
 * @author tangli
 * @date 2023/07/06 19:23
 */
@Configuration
class MonoApiWebConfig {
    @Bean
    fun tenantIdInjector(): TenantIdInjector =
        TenantIdInjector()

    @Bean
    fun appIdInjector(): AppIdInjector =
        AppIdInjector()

    @Bean
    fun employeeIdInjector(): EmployeeIdInjector =
        EmployeeIdInjector()
}

class EmployeeIdInjector(
    override val name: String = "userId",
) : InjectableValueSupplier {
    override fun value(
        property: BeanProperty?,
        instance: Any?,
    ): String =
        WebContext.userId
}

class TenantIdInjector(
    override val name: String = "tenantId",
) : InjectableValueSupplier {
    override fun value(
        property: BeanProperty?,
        instance: Any?,
    ): String =
        WebContext.tenantId
}

class AppIdInjector(
    override val name: String = "appId",
) : InjectableValueSupplier {
    override fun value(
        property: BeanProperty?,
        instance: Any?,
    ): String =
        WebContext.appId
}
