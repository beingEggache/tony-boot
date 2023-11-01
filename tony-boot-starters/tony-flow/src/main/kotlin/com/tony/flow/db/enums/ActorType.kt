package com.tony.flow.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 参与者类型 0，用户 1，角色 2，部门
 * @author tangli
 * @since 2023/09/29 16:00
 */
public enum class ActorType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 用户 0
     */
    USER(0),

    /**
     * 角色 1
     */
    ROLE(1),

    /**
     * 部门 2
     */
    DEPT(2),
    ;

    internal companion object : EnumCreator<ActorType, Int>(ActorType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
