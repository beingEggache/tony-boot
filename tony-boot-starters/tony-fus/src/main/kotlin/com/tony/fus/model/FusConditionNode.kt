package com.tony.fus.model

import com.tony.fus.model.enums.NodeType

/**
 * 条件节点.
 * @author tangli
 * @date 2023/10/24 17:42
 * @since 1.0.0
 */
public class FusConditionNode {
    public val nodeName: String = ""

    public val nodeType: NodeType? = null

    public val priority: Int = 0

    public val expressionList: List<List<FusNodeExpression>> = emptyList()

    public val childNode: FusNode? = null
}
