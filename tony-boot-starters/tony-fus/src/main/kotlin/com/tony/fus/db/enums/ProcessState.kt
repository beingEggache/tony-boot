package com.tony.fus.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 参与者类型 0，用户 1，角色 2，部门
 * @author tangli
 * @since 2023/09/29 16:00
 */
public enum class ProcessState(
    override val value: Int,
) : IntEnumValue {
    /**
     * 未启用
     */
    INACTIVE(0),

    /**
     * 启用
     */
    ACTIVE(1),
    ;

    internal companion object : EnumCreator<ProcessState, Int>(ProcessState::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
