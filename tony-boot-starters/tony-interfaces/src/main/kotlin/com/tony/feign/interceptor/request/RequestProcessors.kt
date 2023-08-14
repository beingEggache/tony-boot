package com.tony.feign.interceptor.request

import feign.RequestTemplate

public fun interface RequestProcessor {
    public operator fun invoke(template: RequestTemplate)
}

public enum class BeanType {
    CLASS,
    NAME,
}
