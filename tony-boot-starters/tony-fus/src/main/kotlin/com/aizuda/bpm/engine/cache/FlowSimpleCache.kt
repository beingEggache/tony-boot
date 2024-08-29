/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * 流程缓存简单实现类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlowSimpleCache : FlowCache {
    private val localCache: MutableMap<String?, Any> = ConcurrentHashMap()

    override fun put(
        key: String?,
        value: Any,
    ) {
        localCache[key] = value
    }

    override fun <T> get(key: String?): T? =
        localCache[key] as T?

    override fun remove(key: String?) {
        localCache.remove(key)
    }
}
