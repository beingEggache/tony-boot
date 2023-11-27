package com.tony.fus.model

/**
 * FusProcessModel is
 * @author tangli
 * @date 2023/10/25 16:30
 * @since 1.0.0
 */
public data class FusProcessModel(
    public val name: String,
    public val key: String,
    public val node: FusNode?,
) {
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
