package com.tony

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * tony-dependencies
 * Beans
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
object Beans : ApplicationContext by ApplicationContextHolder.springContext {

    @JvmStatic
    inline fun <reified T : Any> getBeanByLazy(): Lazy<T> = lazy { getBean() }
}

@Component
private object ApplicationContextHolder : ApplicationContextAware {

    @JvmStatic
    lateinit var springContext: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        springContext = applicationContext
    }
}
