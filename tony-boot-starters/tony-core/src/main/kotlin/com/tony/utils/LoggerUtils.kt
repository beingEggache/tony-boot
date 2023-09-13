@file:JvmName("LoggerUtils")

package com.tony.utils

import org.slf4j.MDC

/**
 * MiscUtils is
 * @param [key]
 * @param [default]
 * @param [source]
 * @return [String]
 * @author Tang Li
 * @date 2023/09/13 10:22
 * @since 1.0.0
 */
public fun mdcPutOrGetDefault(key: String, default: String = uuid(), source: (() -> String?)? = null): String {
    val sourceTraceId = source?.invoke().defaultIfBlank(MDC.get(key).defaultIfBlank(default))
    MDC.put(key, sourceTraceId)
    return sourceTraceId
}
