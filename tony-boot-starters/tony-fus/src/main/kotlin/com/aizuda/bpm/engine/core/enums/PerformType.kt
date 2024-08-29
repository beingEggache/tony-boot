/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

import java.util.Arrays

/**
 * 参与类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public enum class PerformType(
    public val value: Int,
) {
    /**
     * 发起
     */
    START(0),

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
     * 票签 (总权重大于节点 passWeight 属性)
     */
    VOTE_SIGN(4),

    /**
     * 定时器
     */
    TIMER(6),

    /**
     * 触发器
     */
    TRIGGER(7),

    /**
     * 抄送
     */
    COPY(9),
    ;

    public fun ne(value: Int?): Boolean =
        !eq(value)

    public fun eq(value: Int?): Boolean =
        this.value == value

    public companion object {
        public fun get(value: Int?): PerformType {
            if (null == value) {
                // 默认，按顺序依次审批
                return SORT
            }
            return Arrays
                .stream(entries.toTypedArray())
                .filter { s: PerformType ->
                    s.value == value
                }.findFirst()
                .orElse(SORT)
        }
    }
}
