package com.tony.fus.model

/**
 * 条件节点.
 * @author tangli
 * @date 2023/10/24 17:42
 * @since 1.0.0
 */
public class FusConditionNode {
    public var nodeName: String? = null

    public var nodeType: Int? = null

    public var priority: Int = 0

    public var expressionList: MutableList<MutableList<FusNodeExpression>> = mutableListOf()

    public var childNode: FusNode? = null
}
