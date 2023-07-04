package com.tony

import com.tony.exception.ApiException
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Environment

/**
 * 全局单例静态 [ApplicationContext].
 *
 * 新增了一些 kotlin 的 lazy方法. 其余的委托到 [ApplicationContext] 实现.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public object SpringContexts : ApplicationContext by ApplicationContextHolder.springContext {

    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> getBeanByLazy(): Lazy<T> = lazy { getBean() }

    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> getBeanByLazy(name: String): Lazy<T> = lazy { getBean(name) as T }

    internal object ApplicationContextHolder : ApplicationContextAware {

        @JvmStatic
        lateinit var springContext: ApplicationContext

        override fun setApplicationContext(applicationContext: ApplicationContext) {
            springContext = applicationContext
        }
    }

    /**
     * 全局单例静态 [Environment].
     *
     * 委托给 [ApplicationContext.getEnvironment].
     */
    public object Env : Environment by SpringContexts.environment {

        @JvmSynthetic
        @JvmStatic
        public inline fun <reified T> getPropertyByLazy(key: String, defaultValue: T): Lazy<T> = lazy {
            getProperty(key, T::class.java, defaultValue!!) ?: throw ApiException("$key bean not exists")
        }
    }
}
