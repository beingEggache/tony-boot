package com.tony

import com.tony.utils.asTo
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * tony-dependencies
 * Env
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
@Component
object Env : ApplicationContextAware {

    @JvmStatic
    lateinit var environment: Environment

    @JvmStatic
    lateinit var springContext: ApplicationContext

    @JvmStatic
    inline fun <reified T> prop(key: String): T? = environment.getProperty(key, T::class.java)

    @JvmStatic
    inline fun <reified T> prop(key: String, defaultValue: T): T =
        environment.getProperty(key, T::class.java, defaultValue)

    @JvmStatic
    fun prop(key: String, defaultValue: String): String = environment.getProperty(key, defaultValue)

    @JvmStatic
    fun <T> prop(key: String, type: Class<T>, defaultValue: T): T = environment.getProperty(key, type, defaultValue)

    @JvmStatic
    inline fun <reified T> bean(): T = springContext.getBean(T::class.java)

    @JvmStatic
    fun <T> bean(type: Class<T>): T = springContext.getBean(type)

    @JvmStatic
    fun <T : Any> bean(name: String): T? = springContext.getBean(name).asTo()

    @JvmStatic
    fun activeProfiles(): Array<String> = environment.activeProfiles

    @Resource
    fun environment(environment: Environment) {
        Env.environment = environment
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        springContext = applicationContext
    }
}
