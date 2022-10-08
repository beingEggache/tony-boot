/**
 * Interceptors
 *
 * @author tangli
 * @since 2021/12/16 10:15
 */
package com.tony.feign.interceptor

import okhttp3.Interceptor

public interface NetworkInterceptor : Interceptor

public interface AppInterceptor : Interceptor
