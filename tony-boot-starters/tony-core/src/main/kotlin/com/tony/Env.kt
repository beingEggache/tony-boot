package com.tony

import org.springframework.beans.factory.getBean
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

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
object Env : Environment by Beans.getBean<Environment>("environment") {

    @JvmStatic
    inline fun <reified T> getPropertyByLazy(key: String, defaultValue: T): Lazy<T> = lazy {
        getProperty(key, T::class.java, defaultValue)
    }
}
