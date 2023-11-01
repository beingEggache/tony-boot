package com.tony.flow.model

/**
 * 条件节点.
 * @author tangli
 * @date 2023/10/24 17:42
 * @since 1.0.0
 */
public class FlowConditionNode {
    public var nodeName: String? = null

    public var nodeType: Int? = null

    public var priority: Int = 0

    public var conditionMode: Int? = null

    public var expressionList: MutableList<FlowNodeExpression> = mutableListOf()

    public var childNode: FlowNode? = null

    public val expression: String?
        get() =
            expressionList
                .takeIf { it.any() }
                ?.joinToString(if (0 == conditionMode) " && " else " || ") {
                    "#${it.field}${it.operator}${it.value}"
                }
}
