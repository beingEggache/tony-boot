package com.tony.flow

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.flow.cache.DefaultFlowCache
import com.tony.flow.cache.FlowCache
import com.tony.flow.extension.flowThrowIfNull
import com.tony.flow.model.FlowProcessModel
import com.tony.utils.jsonToObj

/**
 * 流程模型解析器
 * @author Tang Li
 * @date 2023/11/02 09:15
 * @since 1.0.0
 */
public fun interface FlowProcessModelParser {

    /**
     * 流程模型 JSON 解析
     * @param [content] 模型内容
     * @param [processId] 流程 ID
     * @param [redeploy] 重新部署
     * @return [FlowProcessModel]
     * @author Tang Li
     * @date 2023/11/02 09:16
     * @since 1.0.0
     */
    public fun parse(content: String, processId: String?, redeploy: Boolean): FlowProcessModel?
}

internal class DefaultFlowProcessModelParser(
    private val flowCache: FlowCache = DefaultFlowCache()
) : FlowProcessModelParser {
    override fun parse(content: String, processId: String?, redeploy: Boolean): FlowProcessModel? {
        if (processId == null) {
            return parse(content)
        }
        val cacheKey = "flowProcessModel#$processId"
        return flowCache
            .get(cacheKey, object : TypeReference<FlowProcessModel>() {})
            .let {
                if (it == null || redeploy) {
                    parse(content).apply {
                        flowCache
                            .set(cacheKey, this)
                    }
                } else {
                    it
                }
            }
    }

    private fun parse(content: String): FlowProcessModel {
        val processModel = content
            .jsonToObj<FlowProcessModel>()
            .flowThrowIfNull("flow process model parse error")

        processModel.buildParentNode(processModel.flowNode)
        return processModel
    }
}
