package com.tony

import com.tony.exception.ApiException
import org.springframework.core.env.Environment

/**
 * Env
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public object Env : Environment by SpringContexts.environment {

    @JvmStatic
    public inline fun <reified T> getPropertyByLazy(key: String, defaultValue: T): Lazy<T> = lazy {
        getProperty(key, T::class.java, defaultValue!!) ?: throw ApiException("$key bean not exists")
    }
}
