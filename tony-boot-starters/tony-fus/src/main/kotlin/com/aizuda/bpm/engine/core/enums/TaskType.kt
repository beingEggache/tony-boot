/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

import java.util.Arrays

/**
 * 任务类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public enum class TaskType(
    public val value: Int,
) {
    /**
     * 结束节点
     */
    END(-1),

    /**
     * 主办
     */
    MAJOR(0),

    /**
     * 审批
     */
    APPROVAL(1),

    /**
     * 抄送
     */
    CC(2),

    /**
     * 条件审批
     */
    CONDITION_NODE(3),

    /**
     * 条件分支
     */
    CONDITION_BRANCH(4),

    /**
     * 调用外部流程任务【办理子流程】
     */
    CALL_PROCESS(5),

    /**
     * 定时器任务
     */
    TIMER(6),

    /**
     * 触发器任务
     */
    TRIGGER(7),

    /**
     * 并行分支
     */
    PARALLEL_BRANCH(8),

    /**
     * 包容分支
     */
    INCLUSIVE_BRANCH(9),

    /**
     * 转办、代理人办理完任务直接进入下一个节点
     */
    TRANSFER(10),

    /**
     * 委派、代理人办理完任务该任务重新归还给原处理人
     */
    DELEGATE(11),

    /**
     * 委派归还任务
     */
    DELEGATE_RETURN(12),

    /**
     * 代理人任务
     */
    AGENT(13),

    /**
     * 代理人归还的任务
     */
    AGENT_RETURN(14),

    /**
     * 代理人协办完成的任务
     */
    AGENT_ASSIST(15),

    /**
     * 被代理人自己完成任务
     */
    AGENT_OWN(16),
    ;

    public fun ne(value: Int?): Boolean =
        !eq(value)

    public fun eq(value: Int?): Boolean =
        this.value == value

    public companion object {
        public fun get(value: Int): TaskType? =
            Arrays
                .stream(entries.toTypedArray())
                .filter { s: TaskType? ->
                    s!!.value == value
                }.findFirst()
                .orElse(null)
    }
}
