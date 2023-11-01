package com.tony.flow.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 任务状态: 0-活动, 1-完成, 2-拒绝, 3-超时, 4-终止
 * @author tangli
 * @since 2023/09/29 16:00
 */
public enum class TaskState(
    override val value: Int,
) : IntEnumValue {
    /**
     * 活动
     */
    ACTIVE(0),

    /**
     * 完成
     */
    COMPLETE(1),

    /**
     * 拒绝
     */
    REJECT(2),

    /**
     * 超时
     */
    TIMEOUT(3),

    /**
     * 终止
     */
    TERMINATION(4),
    ;

    internal companion object : EnumCreator<TaskState, Int>(TaskState::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
