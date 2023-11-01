package com.tony.flow.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 节点类型
 * @author tangli
 * @date 2023/10/24 16:54
 * @since 1.0.0
 */
public enum class NodeType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 发起人
     */
    INITIATOR(1),

    /**
     * 审批人
     */
    APPROVER(2),

    /**
     * 抄送人
     */
    CC(3),

    /**
     * 条件审批
     */
    CONDITIONAL_APPROVE(4),

    /**
     * 条件分支
     */
    CONDITIONAL_BRANCH(5),
    ;

    internal companion object : EnumCreator<NodeType, Int>(NodeType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
