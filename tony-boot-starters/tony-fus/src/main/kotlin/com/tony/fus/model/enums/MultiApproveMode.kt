package com.tony.fus.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 多人审批时审批方式
 * @author Tang Li
 * @date 2023/09/29 16:19
 * @since 1.0.0
 */
public enum class MultiApproveMode(
    override val value: Int,
) : IntEnumValue {
    /**
     * 按顺序依次审批
     */
    SORT(2),

    /**
     * 会签 (可同时审批，每个人必须审批通过)
     */
    COUNTERSIGN(3),

    /**
     * 或签 (有一人审批通过即可)
     */
    OR_SIGN(4),

    /**
     * 票签(总权重大于 50% 表示通过)
     */
    VOTE_SIGN(5),
    ;

    internal companion object : EnumCreator<MultiApproveMode, Int>(MultiApproveMode::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
