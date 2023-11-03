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
     * 用户 1
     */
    USER(1),

    /**
     * 角色 2
     */
    ROLE(2),

    /**
     * 部门 3
     */
    DEPT(3),
    ;

    internal companion object : EnumCreator<ActorType, Int>(ActorType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
