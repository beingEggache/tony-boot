/**
 * Interceptors
 *
 * @author tangli
 * @since 2021/12/16 10:15
 */
package com.tony.feign.interceptor

import okhttp3.Interceptor

/**
 * okhttp网络层拦截器.
 *
 * 会暴露出 [Interceptor.Chain.connection]
 *
 * @author tangli
 * @since 2023/5/25 15:45
 */
public interface NetworkInterceptor : Interceptor

/**
 * okhttp应用层拦截器.
 *
 * @author tangli
 * @since 2023/5/25 15:47
 */
public interface AppInterceptor : Interceptor
