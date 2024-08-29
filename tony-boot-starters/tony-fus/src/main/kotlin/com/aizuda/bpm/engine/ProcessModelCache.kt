/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.assist.Assert
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.model.ProcessModel

/**
 * JSON BPM 模型缓存处理接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface ProcessModelCache {
    /**
     * 流程模型缓存KEY
     *
     * @return 缓存 KEY
     */
    public fun modelCacheKey(): String

    /**
     * 流程模型内容
     *
     * @return 缓存内容
     */
    public val modelContent: String?

    /**
     * JSON BPM 模型
     *
     * @return JSON BPM 模型
     */
    public fun model(): ProcessModel? {
        val modelContent = this.modelContent
        Assert.isEmpty(modelContent, "The process modelContent is Empty.")
        return FlowLongContext.parseProcessModel(modelContent, this.modelCacheKey(), false)
    }
}
