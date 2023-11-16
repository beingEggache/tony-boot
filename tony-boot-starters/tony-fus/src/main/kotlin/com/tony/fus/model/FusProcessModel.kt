package com.tony.fus.model

/**
 * FusProcessModel is
 * @author tangli
 * @date 2023/10/25 16:30
 * @since 1.0.0
 */
public class FusProcessModel {
    public var name: String = ""

    public var key: String = ""

    public var instanceUrl: String = ""

    public var node: FusNode? = null

    public fun getNode(nodeName: String?): FusNode? =
        node?.getNode(nodeName)

    public fun buildParentNode(rootNode: FusNode?) {
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
