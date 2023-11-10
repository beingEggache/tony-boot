package com.tony.flow.model

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

    public fun getNode(nodeName: String?): FlowNode? =
        flowNode?.getNode(nodeName)

    public fun buildParentNode(rootNode: FlowNode?) {
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
}
