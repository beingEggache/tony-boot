package tony.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import tony.config.PowerJobConfig
import tony.core.annotation.EnableTonyBoot
import tony.demo.config.DbConfig
import tony.demo.permission.PermissionInterceptor
import tony.web.WebContext

fun main(args: Array<String>) {
    runApplication<MonoApiWebApp>(*args)
}

// @Profile(value = ["!prod"])
// @Component
// class InitApp(
//     private val userService: UserService,
// ) : CommandLineRunner {
//
//     @Transactional
//     override fun run(vararg args: String?) {
//         userService.initSuperAdmin(WebApp.appId)
//     }
// }

@EnableTonyBoot
@Import(
    value = [
        DbConfig::class,
        PowerJobConfig::class
    ]
)
@SpringBootApplication(proxyBeanMethods = false)
class MonoApiWebApp(
    private val permissionInterceptor: PermissionInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(permissionInterceptor)
            .excludePathPatterns(*WebContext.excludePathPatterns(WebContext.contextPath).toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE + 1)
    }
}
