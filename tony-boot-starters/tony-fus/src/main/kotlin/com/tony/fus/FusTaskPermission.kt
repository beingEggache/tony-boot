package com.tony.fus

import com.tony.fus.db.po.FusTaskActor

/**
 * 任务访问策略类.
 *
 * 用于判断给定的创建人员是否允许执行某个任务
 * @author tangli
 * @date 2023/10/19 10:18
 * @since 1.0.0
 */
public fun interface FusTaskPermission {
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
        userId: String,
        taskActorList: List<FusTaskActor>,
    ): Boolean
}

internal class DefaultTaskPermission : FusTaskPermission {
    override fun hasPermission(
        userId: String,
        taskActorList: List<FusTaskActor>,
    ): Boolean {
        if (userId.isEmpty() || taskActorList.isEmpty()) {
            return false
        }
        return taskActorList.any { it.actorId == userId }
    }
}
