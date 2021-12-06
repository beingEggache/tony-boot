package com.tony

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
object Env {

    lateinit var environment: Environment

    inline fun <reified T> prop(key: String): T? = environment.getProperty(key, T::class.java)

    inline fun <reified T> prop(key: String, defaultValue: T): T =
        environment.getProperty(key, T::class.java, defaultValue)

    fun prop(key: String, defaultValue: String): String = environment.getProperty(key, defaultValue)

    fun <T> prop(key: String, type: Class<T>, defaultValue: T): T = environment.getProperty(key, type, defaultValue)

    fun activeProfiles(): Array<String> = environment.activeProfiles

    @Resource
    fun environment(environment: Environment) {
        Env.environment = environment
    }
}
