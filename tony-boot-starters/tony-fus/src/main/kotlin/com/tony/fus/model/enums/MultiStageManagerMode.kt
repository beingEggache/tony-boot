package com.tony.fus.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 参与类型
 * @author Tang Li
 * @date 2023/09/29 16:19
 * @since 1.0.0
 */
public enum class MultiStageManagerMode(
    override val value: Int,
) : IntEnumValue {
    /**
     * 直到最上级主管
     */
    TOP(1),

    /**
     * 自定义审批终点.
     */
    ASSIGN(2),
    ;

    internal companion object : EnumCreator<MultiStageManagerMode, Int>(MultiStageManagerMode::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
