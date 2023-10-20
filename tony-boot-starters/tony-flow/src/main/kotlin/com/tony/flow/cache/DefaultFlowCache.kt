package com.tony.flow.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.utils.asTo
import java.util.concurrent.ConcurrentHashMap

/**
 * DefaultFlowCache is
 * @author tangli
 * @date 2023/10/19 10:47
 * @since 1.0.0
 */
public class DefaultFlowCache : FlowCache {

    private val cache = ConcurrentHashMap<String, Any?>()

    override fun <T : Any> set(key: String, value: T) {
        cache[key] = value
    }

    override fun <T : Any> get(key: String, typeReference: TypeReference<T>): T? = cache[key].asTo()

    override fun delete(key: String) {
        cache.remove(key)
    }
}
