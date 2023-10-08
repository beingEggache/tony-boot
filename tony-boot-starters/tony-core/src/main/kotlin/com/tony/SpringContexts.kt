/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Environment

/**
 * 全局单例静态 [ApplicationContext].
 *
 * 新增了一些 kotlin 的 lazy方法. 其余的委托到 [ApplicationContext] 实现.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public object SpringContexts : ApplicationContext by ApplicationContextHolder.springContext {
    /**
     * 惰性获取 bean
     * @return [Lazy<T>]
     * @author Tang Li
     * @date 2023/09/13 10:32
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> getBeanByLazy(): Lazy<T> = lazy(LazyThreadSafetyMode.PUBLICATION) { getBean() }

    /**
     * 惰性获取 bean
     * @param [name] 名称
     * @return [Lazy<T>]
     * @author Tang Li
     * @date 2023/09/13 10:32
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    public inline fun <reified T : Any> getBeanByLazy(name: String): Lazy<T> =
        lazy(LazyThreadSafetyMode.PUBLICATION) { getBean(name) as T }

    internal object ApplicationContextHolder : ApplicationContextAware {
        @JvmStatic
        internal lateinit var springContext: ApplicationContext

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
        /**
         * 惰性获取配置参数
         * @param [key] 钥匙
         * @param [defaultValue] 默认值
         * @return [Lazy<T>]
         * @author Tang Li
         * @date 2023/09/13 10:32
         * @since 1.0.0
         */
        @JvmSynthetic
        @JvmStatic
        public inline fun <reified T : Any> getPropertyByLazy(key: String, defaultValue: T): Lazy<T> =
            lazy(LazyThreadSafetyMode.PUBLICATION) {
                getProperty(key, T::class.java, defaultValue)
            }
    }
}
