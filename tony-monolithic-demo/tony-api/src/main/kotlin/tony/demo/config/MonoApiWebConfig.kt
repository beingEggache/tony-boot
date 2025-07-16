package tony.demo.config

import com.fasterxml.jackson.databind.BeanProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tony.core.jackson.InjectableValueSupplier
import tony.demo.MonoApiWebContext.tenantId
import tony.web.WebContext
import tony.web.auth.WebContextExtensions.userId

/**
 * MonoApiWebConfig is
 * @author tangli
 * @date 2023/07/06 19:23
 */
@Configuration(proxyBeanMethods = false)
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
        ""
    // WebContext.appId
}
