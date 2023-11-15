package com.tony.fus.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 参与者类型 0，用户 1，角色 2，部门
 * @author tangli
 * @since 2023/09/29 16:00
 */
public enum class UseScope(
    override val value: Int,
) : IntEnumValue {
    /**
     * 全员 1
     */
    ALL(1),

    /**
     * 指定人员（业务关联） 2
     */
    SOME_ONE(2),

    /**
     * 均不可提交 3
     */
    NONE(3),
    ;

    internal companion object : EnumCreator<UseScope, Int>(UseScope::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
