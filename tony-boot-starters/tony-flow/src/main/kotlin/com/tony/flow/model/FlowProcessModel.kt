package com.tony.flow.model

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.flow.FlowContext
import com.tony.utils.jsonToObj

/**
 * FlowProcessModel is
 * @author tangli
 * @date 2023/10/25 16:30
 * @since 1.0.0
 */
public class FlowProcessModel {
    public var name: String? = null

    public var instanceUrl: String? = null

    public var flowNode: FlowNode? = null

    public fun getNode(nodeName: String): FlowNode? =
        flowNode?.getNode(nodeName)

    protected fun buildParentNode(rootNode: FlowNode?) {
        rootNode?.conditionNodes?.forEach { conditionNode ->
            conditionNode.childNode?.also { conditionChildNode ->
                conditionChildNode.parentNode = rootNode
                buildParentNode(conditionChildNode)
            }
        }
        rootNode?.childNode?.also {
            it.parentNode = rootNode
            buildParentNode(it)
        }
    }

    public companion object {
        public fun parse(
            content: String,
            processId: Long?,
        ): FlowProcessModel? {
            if (processId != null) {
                val cacheKey = "flowProcessModel#$processId"
                return FlowContext
                    .flowCache
                    .get(cacheKey, object : TypeReference<FlowProcessModel>() {})
                    .let {
                        it ?: parse(content).apply {
                            FlowContext
                                .flowCache
                                .set(cacheKey, this)
                        }
                    }
            }
            return null
        }

        private fun parse(content: String): FlowProcessModel =
            content.jsonToObj<FlowProcessModel>().apply {
                buildParentNode(flowNode)
            }
    }
}
