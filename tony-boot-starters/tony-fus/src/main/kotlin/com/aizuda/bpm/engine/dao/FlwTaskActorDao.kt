/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 任务参与者数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwTaskActorDao : BaseDao<FlwTaskActor> {
    public fun deleteByInstanceIds(instanceIds: List<Long?>?): Boolean =
        ktUpdate()
            .`in`(FlwTaskActor::instanceId, instanceIds)
            .remove()

    public fun deleteByTaskIdAndAgentType(
        taskId: Long?,
        agentType: Int,
    ): Boolean =
        ktUpdate()
            .eq(FlwTaskActor::taskId, taskId)
            .eq(FlwTaskActor::agentType, agentType)
            .remove()

    public fun deleteByTaskIdAndActorIds(
        taskId: Long?,
        actorIds: List<String?>?,
    ): Boolean =
        ktUpdate()
            .eq(FlwTaskActor::taskId, taskId)
            .`in`(FlwTaskActor::actorId, actorIds)
            .remove()

    public fun selectListByInstanceId(instanceId: Long?): List<FlwTaskActor> =
        ktQuery()
            .eq(FlwTaskActor::instanceId, instanceId)
            .list()

    public fun selectListByTaskId(taskId: Long?): List<FlwTaskActor> =
        ktQuery()
            .eq(FlwTaskActor::taskId, taskId)
            .list()

    public fun selectListByTaskIds(taskIds: List<Long?>?): List<FlwTaskActor> =
        ktQuery()
            .`in`(FlwTaskActor::taskId, taskIds)
            .list()

    public fun selectListByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): List<FlwTaskActor> =
        ktQuery()
            .eq(FlwTaskActor::taskId, taskId)
            .eq(FlwTaskActor::actorId, actorId)
            .list()

    public fun selectCountByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): Long =
        ktQuery()
            .eq(FlwTaskActor::taskId, taskId)
            .eq(FlwTaskActor::actorId, actorId)
            .count()
}
