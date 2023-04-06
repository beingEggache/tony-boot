package com.tony

import com.tony.exception.ApiException
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Environment

/**
 * Beans
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public object SpringContexts : ApplicationContext by ApplicationContextHolder.springContext {

    @JvmStatic
    public inline fun <reified T : Any> getBeanByLazy(): Lazy<T> = lazy { getBean() }

    internal object ApplicationContextHolder : ApplicationContextAware {

        @JvmStatic
        lateinit var springContext: ApplicationContext

        override fun setApplicationContext(applicationContext: ApplicationContext) {
            springContext = applicationContext
        }
    }

    public object Env : Environment by SpringContexts.environment {

        @JvmStatic
        public inline fun <reified T> getPropertyByLazy(key: String, defaultValue: T): Lazy<T> = lazy {
            getProperty(key, T::class.java, defaultValue!!) ?: throw ApiException("$key bean not exists")
        }
    }
}
