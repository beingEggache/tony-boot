package com.tony.fus.model.enums

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
    COMPLETED(3),

    /**
     * 撤销.
     */
    REVOKED(4),

    /**
     * 终止.
     */
    TERMINATED(5),

    /**
     * 更新.
     */
    UPDATE(6),

    /**
     * 删除.
     */
    DELETE(7),

    /**
     * 驳回.
     */
    REJECTED(8),

    /**
     * 超时.
     */
    EXPIRED(9),
    ;

    internal companion object : EnumCreator<EventType, Int>(EventType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
