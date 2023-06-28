@file:JvmName("MiscUtils")

package com.tony.utils

import org.slf4j.MDC

/**
 * MiscUtils is
 * @author tangli
 * @since 2023/06/28 17:17
 */
public fun mdcPutOrGetDefault(key: String, default: String = uuid(), source: (() -> String?)? = null): String {
    val sourceTraceId = source?.invoke().defaultIfBlank(MDC.get(key).defaultIfBlank(default))
    MDC.put(key, sourceTraceId)
    return sourceTraceId
}
