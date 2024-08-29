/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

/**
 * 流程引擎监听类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author lizhongyuan
 * @since 1.0
 */
public enum class EventType {
    /**
     * 发起
     */
    START,

    /**
     * 创建
     */
    CREATE,

    /**
     * 再创建，仅用于流程回退
     */
    RECREATE,

    /**
     * 抄送
     */
    CC,

    /**
     * 分配
     */
    ASSIGNMENT,

    /**
     * 委派任务解决
     */
    DELEGATE_RESOLVE,

    /**
     * 任务加签
     */
    ADD_TASK_ACTOR,

    /**
     * 任务减签
     */
    REMOVE_TASK_ACTOR,

    /**
     * 驳回至上一步处理
     */
    REJECT,

    /**
     * 角色认领
     */
    CLAIM_ROLE,

    /**
     * 部门认领
     */
    CLAIM_DEPARTMENT,

    /**
     * 拿回未执行任务
     */
    RECLAIM,

    /**
     * 撤回指定任务
     */
    WITHDRAW,

    /**
     * 唤醒历史任务
     */
    RESUME,

    /**
     * 完成
     */
    COMPLETE,

    /**
     * 撤销
     */
    REVOKE,

    /**
     * 终止
     */
    TERMINATE,

    /**
     * 更新
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 调用外部流程任务【办理子流程】
     */
    CALL_PROCESS,

    /**
     * 超时
     */
    TIMEOUT,

    /**
     * 跳转
     */
    JUMP,

    /**
     * 自动跳转
     */
    AUTO_JUMP,

    /**
     * 自动审批完成
     */
    AUTO_COMPLETE,

    /**
     * 自动审批拒绝
     */
    AUTO_REJECT,

    /**
     * 触发器任务
     */
    TRIGGER,

    /**
     * 结束
     */
    END,

    ;

    public fun eq(eventType: EventType): Boolean =
        this == eventType

    public fun ne(eventType: EventType): Boolean =
        this != eventType
}
