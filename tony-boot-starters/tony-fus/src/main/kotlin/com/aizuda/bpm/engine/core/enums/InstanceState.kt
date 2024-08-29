/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

import java.util.Arrays

/**
 * 流程状态
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author 江涛
 * @since 1.0
 */
public enum class InstanceState(
    public val value: Int,
) {
    /**
     * 审批中
     */
    ACTIVE(0),

    /**
     * 审批通过
     */
    COMPLETE(1),

    /**
     * 审批拒绝【 驳回结束流程 】
     */
    REJECT(2),

    /**
     * 撤销审批
     */
    REVOKE(3),

    /**
     * 超时结束
     */
    TIMEOUT(4),

    /**
     * 强制终止
     */
    TERMINATE(5),
    ;

    public fun ne(value: Int): Boolean =
        !eq(value)

    public fun eq(value: Int): Boolean =
        this.value == value

    public companion object {
        public fun get(value: Int): InstanceState =
            Arrays
                .stream(entries.toTypedArray())
                .filter { s: InstanceState ->
                    s.value == value
                }.findFirst()
                .orElseGet(null)
    }
}
