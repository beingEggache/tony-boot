package com.tony.demo

import com.tony.annotation.EnableTonyBoot
import com.tony.config.PowerJobConfig
import com.tony.demo.config.DbConfig
import com.tony.demo.permission.PermissionInterceptor
import com.tony.web.WebContext
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

fun main(args: Array<String>) {
    runApplication<MonoApiWebApp>(*args) {
        setHeadless(true)
    }
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
