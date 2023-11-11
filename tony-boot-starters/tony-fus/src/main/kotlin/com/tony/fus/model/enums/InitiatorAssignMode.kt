package com.tony.fus.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 发起人自选类型.
 * @author tangli
 * @date 2023/10/24 16:54
 * @since 1.0.0
 */
public enum class InitiatorAssignMode(
    override val value: Int,
) : IntEnumValue {
    /**
     * 一个人.
     */
    SINGLE(1),

    /**
     * 多个人.
     */
    MULTI(2),
    ;

    internal companion object : EnumCreator<InitiatorAssignMode, Int>(InitiatorAssignMode::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
