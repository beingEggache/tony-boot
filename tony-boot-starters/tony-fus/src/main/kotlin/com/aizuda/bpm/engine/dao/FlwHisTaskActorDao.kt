/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.entity.FlwHisTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 历史任务参与者数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwHisTaskActorDao : BaseDao<FlwHisTaskActor> {
    public fun deleteByInstanceIds(instanceIds: List<Long?>?): Boolean =
        ktUpdate()
            .`in`(FlwHisTaskActor::instanceId, instanceIds)
            .remove()

    public fun deleteByTaskId(taskId: Long?): Boolean =
        ktUpdate()
            .eq(FlwHisTaskActor::taskId, taskId)
            .remove()

    public fun selectListByTaskId(taskId: Long?): List<FlwHisTaskActor> =
        ktQuery()
            .eq(FlwHisTaskActor::taskId, taskId)
            .list()

    public fun selectListByTaskIds(taskIds: List<Long?>?): List<FlwHisTaskActor> =
        ktQuery()
            .`in`(FlwHisTaskActor::taskId, taskIds)
            .list()

    public fun selectListByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): List<FlwHisTaskActor> =
        ktQuery()
            .eq(FlwHisTaskActor::taskId, taskId)
            .eq(FlwHisTaskActor::actorId, actorId)
            .list()
}
