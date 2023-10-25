package com.tony.flow.model

/**
 * 节点表达式条件.
 * @author tangli
 * @date 2023/10/24 17:35
 * @since 1.0.0
 */
public class FlowNodeExpression {
    /**
     * 名称
     */
    public var name: String? = null

    /**
     * 属性
     */
    public var field: String? = null

    /**
     * 操作
     */
    public var operator: String? = null

    /**
     * 内容
     */
    public var value: String? = null
}
