package com.tony.fus

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.fus.cache.DefaultFusCache
import com.tony.fus.cache.FusCache
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.model.FusProcessModel
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
     * @return [FusProcessModel]
     * @author Tang Li
     * @date 2023/11/02 09:16
     * @since 1.0.0
     */
    public fun parse(
        content: String,
        processId: String?,
        redeploy: Boolean,
    ): FusProcessModel
}

internal class DefaultFlowProcessModelParser(
    private val cache: FusCache = DefaultFusCache(),
) : FlowProcessModelParser {
    override fun parse(
        content: String,
        processId: String?,
        redeploy: Boolean,
    ): FusProcessModel {
        if (processId == null) {
            return parse(content)
        }
        val cacheKey = "flowProcessModel#$processId"
        return cache
            .get(cacheKey, object : TypeReference<FusProcessModel>() {})
            .let {
                if (it == null || redeploy) {
                    parse(content).apply {
                        cache
                            .set(cacheKey, this)
                    }
                } else {
                    it
                }
            }
    }

    private fun parse(content: String): FusProcessModel {
        val processModel =
            content
                .jsonToObj<FusProcessModel>()
                .fusThrowIfNull("fus process model parse error")

        processModel.buildParentNode(processModel.node)
        return processModel
    }
}
