package com.tony.fus.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.utils.asTo
import com.tony.utils.asToNotNull
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier

/**
 * DefaultFusCache is
 * @author tangli
 * @date 2023/10/19 10:47
 * @since 1.0.0
 */
public class DefaultFusCache : FusCache {
    private val cache = ConcurrentHashMap<String, Any?>()

    override fun <T : Any> set(
        key: String,
        value: T,
    ) {
        cache[key] = value
    }

    override fun <T : Any> get(
        key: String,
        typeReference: TypeReference<T>,
    ): T? =
        cache[key].asTo()

    override fun <T : Any> getOrPut(
        key: String,
        typeReference: TypeReference<T>,
        defaultValue: Supplier<T>,
    ): T =
        cache
            .getOrPut(key) {
                defaultValue.get()
            }?.asToNotNull() ?: defaultValue.get()

    override fun delete(key: String) {
        cache.remove(key)
    }
}
