/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.ProcessModelParser
import com.aizuda.bpm.engine.assist.Assert
import com.aizuda.bpm.engine.cache.FlowCache
import com.aizuda.bpm.engine.cache.FlowSimpleCache
import com.aizuda.bpm.engine.model.ProcessModel
import com.tony.utils.jsonToObj

/**
 * FlowLong 默认流程模型解析器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class DefaultProcessModelParser public constructor(
    flowCache: FlowCache?,
) : ProcessModelParser {
    /**
     * 流程缓存处理类，默认 ConcurrentHashMap 实现
     * 使用其它缓存框架可在初始化时赋值该静态属性
     */
    public override var flowCache: FlowCache? = null

    init {
        if (null == flowCache) {
            this.flowCache = FlowSimpleCache()
        } else {
            this.flowCache = flowCache
        }
    }

    override fun parse(
        content: String?,
        cacheKey: String?,
        redeploy: Boolean,
    ): ProcessModel {
        // 缓存解析逻辑
        if (null != cacheKey) {
            val flowCache = this.flowCache
            var processModel = flowCache!!.get<ProcessModel>(cacheKey)
            if (null == processModel || redeploy) {
                processModel = parseProcessModel(content)
                flowCache.put(cacheKey, processModel)
            }
            return processModel
        }

        // 未缓存解析逻辑
        return parseProcessModel(content)
    }

    private fun parseProcessModel(content: String?): ProcessModel {
        val processModel = content?.jsonToObj<ProcessModel>()
        Assert.isNull(processModel, "process model json parser error")
        processModel!!.buildParentNode(processModel.nodeConfig)
        return processModel
    }

    override fun invalidate(cacheKey: String?) {
        flowCache!!.remove(cacheKey)
    }
}
