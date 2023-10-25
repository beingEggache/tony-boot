package com.tony.flow.model

/**
 * 操作人员
 * @author Tang Li
 * @date 2023/10/09 14:48
 * @since 1.0.0
 */
public data class FlowOperator(
    /**
     * 创建人ID
     */
    val operatorId: Long,
    /**
     * 创建人名称
     */
    val operatorName: String,
    /**
     * 租户ID
     */
    val tenantId: String? = null,
)
