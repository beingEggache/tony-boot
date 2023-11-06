package com.tony.flow.db.enums

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
     * 活动
     */
    ACTIVE(1),

    /**
     * 完成
     */
    COMPLETE(2),

    /**
     * 超时
     */
    EXPIRED(3),

    /**
     * 终止
     */
    TERMINATED(4),
    ;

    internal companion object : EnumCreator<InstanceState, Int>(InstanceState::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
