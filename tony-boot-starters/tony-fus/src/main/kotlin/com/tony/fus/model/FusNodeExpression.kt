package com.tony.fus.model

/**
 * 节点表达式条件.
 * @author tangli
 * @date 2023/10/24 17:35
 * @since 1.0.0
 */
public class FusNodeExpression {
    /**
     * 名称
     */
    public var name: String = ""

    /**
     * 属性
     */
    public var field: String = ""

    /**
     * 操作
     */
    public var operator: String = ""

    /**
     * 内容
     */
    public var value: String = ""
}
