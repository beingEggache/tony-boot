package com.tony

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

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
}
