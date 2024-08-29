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
public enum class TaskState(
    public val value: Int,
) {
    /**
     * 活动
     */
    ACTIVE(0),

    /**
     * 跳转
     */
    JUMP(1),

    /**
     * 完成
     */
    COMPLETE(2),

    /**
     * 拒绝
     */
    REJECT(3),

    /**
     * 撤销审批
     */
    REVOKE(4),

    /**
     * 超时
     */
    TIMEOUT(5),

    /**
     * 终止
     */
    TERMINATE(6),

    /**
     * 驳回终止
     */
    REJECT_END(7),

    /**
     * 自动完成
     */
    AUTO_COMPLETE(8),

    /**
     * 自动驳回
     */
    AUTO_REJECT(9),

    /**
     * 自动跳转
     */
    AUTO_JUMP(10),
    ;

    public fun ne(value: Int?): Boolean =
        !eq(value)

    public fun eq(value: Int?): Boolean =
        this.value == value

    public companion object {
        public fun get(value: Int): TaskState =
            Arrays
                .stream(entries.toTypedArray())
                .filter { s: TaskState ->
                    s.value == value
                }.findFirst()
                .orElseGet(null)

        public fun of(instanceState: InstanceState): TaskState {
            if (instanceState == InstanceState.REJECT) {
                return REJECT_END
            }
            if (instanceState == InstanceState.REVOKE) {
                return REVOKE
            }
            if (instanceState == InstanceState.TIMEOUT) {
                return TIMEOUT
            }
            if (instanceState == InstanceState.TERMINATE) {
                return TERMINATE
            }
            return COMPLETE
        }

        public fun allowedCheck(taskState: TaskState): Boolean =
            ACTIVE == taskState || JUMP == taskState || COMPLETE == taskState
    }
}
