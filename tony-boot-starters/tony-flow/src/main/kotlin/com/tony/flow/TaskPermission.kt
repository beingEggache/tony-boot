package com.tony.flow

import com.tony.flow.db.po.FlowTaskActor

/**
 * 任务访问策略类.
 *
 * 用于判断给定的创建人员是否允许执行某个任务
 * @author tangli
 * @date 2023/10/19 10:18
 * @since 1.0.0
 */
public fun interface TaskPermission {
    /**
     * 是否拥有权限.
     *
     * 根据创建人ID、参与者集合判断是否允许访问所属任务
     * @param [userId] 用户id
     * @param [taskActorList] 任务参与者列表
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/19 10:21
     * @since 1.0.0
     */
    public fun hasPermission(
        userId: Long,
        taskActorList: List<FlowTaskActor>,
    ): Boolean
}
