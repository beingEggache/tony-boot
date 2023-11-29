package com.tony.fus.cache

import com.fasterxml.jackson.core.type.TypeReference

/**
 * FusCache is
 * @author tangli
 * @date 2023/10/19 10:41
 * @since 1.0.0
 */
public interface FusCache {
    /**
     * 设值.
     *
     * @param [key] 键
     * @param [value] 值
     * @author Tang Li
     * @date 2023/10/19 10:43
     * @since 1.0.0
     */
    public fun <T : Any> set(
        key: String,
        value: T,
    )

    /**
     * 获值
     * @param [key] 键
     * @param [typeReference] 类型参考
     * @return [T]?
     * @author Tang Li
     * @date 2023/10/19 10:45
     * @since 1.0.0
     */
    public fun <T : Any> get(
        key: String,
        typeReference: TypeReference<T>,
    ): T?

    public fun <T : Any> getOrPut(
        key: String,
        typeReference: TypeReference<T>,
        defaultValue: java.util.function.Supplier<T>,
    ): T

    /**
     * 删.
     * @param [key] 键
     * @return [Long]
     * @author Tang Li
     * @date 2023/10/19 10:45
     * @since 1.0.0
     */
    public fun delete(key: String)
}
