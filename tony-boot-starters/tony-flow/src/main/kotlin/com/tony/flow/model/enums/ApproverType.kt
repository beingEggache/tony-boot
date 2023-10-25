package com.tony.flow.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 审核人类型.
 * 批准人类型
 * @author Tang Li
 * @date 2023/10/25 10:59
 * @since 1.0.0
 */
public enum class ApproverType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 指定.
     */
    ASSIGN(1),

    /**
     * 主管.
     */
    MANAGER(2),

    /**
     * 角色.
     */
    ROLE(3),

    /**
     * 发起人自选.
     */
    INITIATOR_ASSIGN(4),

    /**
     * 发起人自己.
     */
    INITIATOR(5),

    /**
     * 连续多级主管.
     */
    MULTISTAGE_MANAGER(6),
    ;

    internal companion object : EnumCreator<ApproverType, Int>(ApproverType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) = super.create(value)
    }
}
