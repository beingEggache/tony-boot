package com.tony.fus.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 流程实例状态
 * @author Tang Li
 * @date 2023/09/29 17:30
 * @since 1.0.0
 */
public enum class InstanceState(
    override val value: Int,
) : IntEnumValue {
    /**
     * 审批中
     */
    ACTIVE(1),

    /**
     * 审批通过
     */
    COMPLETED(2),

    /**
     * 审批拒绝
     *
     */
    REJECTED(3),

    /**
     * 超时结束
     */
    EXPIRED(4),

    /**
     * 强制终止
     */
    TERMINATED(5),

    /**
     * 撤销审批
     *
     */
    REVOKED(6),
    ;

    internal companion object : EnumCreator<InstanceState, Int>(InstanceState::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
