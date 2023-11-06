package com.tony.flow.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 流程引擎监听类型.
 * @author Tang Li
 * @date 2023/10/25 10:59
 * @since 1.0.0
 */
public enum class EventType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 创建.
     */
    CREATE(1),

    /**
     * 分配.
     */
    ASSIGNMENT(2),

    /**
     * 完成.
     */
    COMPLETE(3),

    /**
     * 终止.
     */
    TERMINATE(4),

    /**
     * 更新.
     */
    UPDATE(5),

    /**
     * 删除.
     */
    DELETE(6),

    /**
     * 驳回.
     *
     */
    REJECT(7),

    /**
     * 超时.
     */
    EXPIRED(8),
    ;

    internal companion object : EnumCreator<EventType, Int>(EventType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
