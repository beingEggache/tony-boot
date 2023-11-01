package com.tony.flow.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 任务类型: 0-主办, 1-转办, 2-委派, 3-会签
 * @author tangli
 * @since 2023/09/29 16:00
 */
public enum class TaskType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 主办
     */
    MAJOR(0),

    /**
     * 转办、代理人办理完任务直接进入下一个节点
     */
    TRANSFER(1),

    /**
     * 委派、代理人办理完任务该任务重新归还给原处理人
     */
    DELEGATE(2),

    /**
     * 会签
     */
    COUNTERSIGN(3),
    ;

    internal companion object : EnumCreator<TaskType, Int>(TaskType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
