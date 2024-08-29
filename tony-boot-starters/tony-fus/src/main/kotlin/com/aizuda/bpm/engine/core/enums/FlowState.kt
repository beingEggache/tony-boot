/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

/**
 * 流程状态
 */
public enum class FlowState(
    public val value: Int,
) {
    /**
     * 启用
     */
    ACTIVE(1),

    /**
     * 未启用
     */
    INACTIVE(0),

    /**
     * 历史版本
     */
    HISTORY(2),
    ;

    public fun ne(value: Int?): Boolean =
        !eq(value)

    public fun eq(value: Int?): Boolean =
        this.value == value
}
