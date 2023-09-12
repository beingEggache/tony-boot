package com.tony.feign.interceptor.request

import feign.RequestTemplate

/**
 * feign 请求处理.
 *
 * 不用 [feign.RequestInterceptor] 是为了避免自动注册.
 *
 * @author Tang Li
 * @date 2023/8/15 9:53
 */
public fun interface RequestProcessor {
    public operator fun invoke(template: RequestTemplate)
}

/**
 * 获取 [RequestProcessor] 的方式.
 *
 * @author Tang Li
 * @date 2023/8/15 9:54
 */
public enum class BeanType {
    CLASS,
    NAME,
}
