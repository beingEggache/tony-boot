package com.tony.fus.model

/**
 * 节点表达式条件.
 * @author tangli
 * @date 2023/10/24 17:35
 * @since 1.0.0
 */
public data class FusNodeExpression(
    /**
     * 名称
     */
    public val name: String,
    /**
     * 属性
     */
    public val field: String,
    /**
     * 操作
     */
    public val operator: String,
    /**
     * 内容
     */
    public val value: String,
)
