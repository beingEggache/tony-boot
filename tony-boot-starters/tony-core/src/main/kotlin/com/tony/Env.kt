package com.tony

import com.tony.exception.ApiException
import org.springframework.beans.factory.getBean
import org.springframework.core.env.Environment

/**
 * Env
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
object Env : Environment by Beans.getBean<Environment>("environment") {

    @JvmStatic
    inline fun <reified T> getPropertyByLazy(key: String, defaultValue: T): Lazy<T> = lazy {
        getProperty(key, T::class.java, defaultValue!!) ?: throw ApiException("$key bean not exists")
    }
}
