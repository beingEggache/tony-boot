/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.assist.Assert
import com.aizuda.bpm.engine.entity.FlwHisTask
import com.tony.mybatis.dao.BaseDao

/**
 * 历史任务数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwHisTaskDao : BaseDao<FlwHisTask> {
    public fun deleteByInstanceIds(instanceIds: List<Long?>?): Boolean =
        ktUpdate()
            .`in`(FlwHisTask::instanceId, instanceIds)
            .remove()

    public fun selectCheckById(id: Long?): FlwHisTask {
        val flwHisTask = selectById(id)
        Assert.isNull(flwHisTask!!, "The specified hisTask [id=$id] does not exist")
        return flwHisTask
    }

    public fun selectListByInstanceIdAndTaskName(
        instanceId: Long?,
        taskName: String?,
    ): List<FlwHisTask> =
        ktQuery()
            .eq(FlwHisTask::instanceId, instanceId)
            .eq(FlwHisTask::taskName, taskName)
            .list()

    public fun selectListByInstanceId(instanceId: Long?): List<FlwHisTask> =
        ktQuery()
            .eq(FlwHisTask::instanceId, instanceId)
            .list()

    public fun selectListByCallProcessIdAndCallInstanceId(
        callProcessId: Long?,
        callInstanceId: Long?,
    ): List<FlwHisTask> =
        ktQuery()
            .eq(FlwHisTask::callProcessId, callProcessId)
            .eq(FlwHisTask::callInstanceId, callInstanceId)
            .list()

    public fun selectListByParentTaskId(parentTaskId: Long?): List<FlwHisTask> =
        ktQuery()
            .eq(FlwHisTask::parentTaskId, parentTaskId)
            .list()

    public fun selectListByInstanceIdAndTaskNameAndParentTaskId(
        instanceId: Long?,
        taskName: String?,
        parentTaskId: Long?,
    ): Collection<FlwHisTask> =
        ktQuery()
            .eq(FlwHisTask::instanceId, instanceId)
            .eq(FlwHisTask::taskName, taskName)
            .eq(FlwHisTask::parentTaskId, parentTaskId)
            .list()
}
