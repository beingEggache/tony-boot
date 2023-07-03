package com.tony.api

import com.tony.annotation.EnableTonyBoot
import com.tony.api.permission.PermissionInterceptor
import com.tony.db.config.DbConfig
import com.tony.db.service.UserService
import com.tony.web.WebApp
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.core.PriorityOrdered
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

fun main(args: Array<String>) {
    runApplication<MonoApiWebApp>(*args) {
        setHeadless(true)
    }
}

@Profile(value = ["!prod"])
@Component
class InitApp(
    private val userService: UserService,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        userService.initSuperAdmin(WebApp.appId)
    }
}

@EnableTonyBoot
@Import(value = [DbConfig::class])
@SpringBootApplication
class MonoApiWebApp(
    private val permissionInterceptor: PermissionInterceptor,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(permissionInterceptor)
            .excludePathPatterns(*WebApp.whiteUrlPatternsWithContextPath.toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE + 1)
    }
}
