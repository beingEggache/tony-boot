package com.tony.flow.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 参与类型
 * @author Tang Li
 * @date 2023/09/29 16:19
 * @since 1.0.0
 */
public enum class PerformType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 发起、其它
     */
    UNKNOWN(0),

    /**
     * 按顺序依次审批
     */
    SORT(1),

    /**
     * 会签 (可同时审批，每个人必须审批通过)
     */
    COUNTERSIGN(2),

    /**
     * 或签 (有一人审批通过即可)
     */
    OR_SIGN(3),

    /**
     * 抄送
     */
    COPY(4),
    ;

    internal companion object : EnumCreator<PerformType, Int>(PerformType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) = super.create(value)
    }
}
