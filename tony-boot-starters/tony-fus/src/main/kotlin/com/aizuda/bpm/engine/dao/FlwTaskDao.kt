/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.assist.Assert
import com.aizuda.bpm.engine.entity.FlwTask
import com.tony.mybatis.dao.BaseDao
import java.util.Date

/**
 * 任务数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwTaskDao : BaseDao<FlwTask> {
    public fun deleteByInstanceIds(instanceIds: List<Long?>?): Boolean =
        ktUpdate()
            .`in`(FlwTask::instanceId, instanceIds)
            .remove()

    public fun selectCheckById(id: Long?): FlwTask {
        val task = selectById(id)
        Assert.isNull(task, "The specified task [id=$id] does not exist")
        return task!!
    }

    public fun selectCountByParentTaskId(parentTaskId: Long?): Long =
        ktQuery()
            .eq(FlwTask::parentTaskId, parentTaskId)
            .count()

    public fun selectListByInstanceId(instanceId: Long?): List<FlwTask> =
        ktQuery()
            .eq(FlwTask::instanceId, instanceId)
            .list()

    public fun selectListByInstanceIdAndTaskName(
        instanceId: Long?,
        taskName: String?,
    ): List<FlwTask> =
        ktQuery()
            .eq(FlwTask::instanceId, instanceId)
            .eq(FlwTask::taskName, taskName)
            .list()

    /**
     * 根据流程实例ID和任务KEY查询任务
     *
     * @param instanceId 流程实例ID
     * @param taskKey    任务KEY
     * @return 任务列表
     */
    public fun selectListByInstanceIdAndTaskKey(
        instanceId: Long?,
        taskKey: String?,
    ): List<FlwTask> =
        ktQuery()
            .eq(FlwTask::instanceId, instanceId)
            .eq(FlwTask::taskKey, taskKey)
            .list()

    public fun selectListByInstanceIdAndTaskNames(
        instanceId: Long?,
        taskNames: List<String?>?,
    ): List<FlwTask> =
        ktQuery()
            .eq(FlwTask::instanceId, instanceId)
            .`in`(FlwTask::taskName, taskNames)
            .list()

    public fun selectListTimeoutOrRemindTasks(currentDate: Date?): List<FlwTask> =
        ktQuery()
            .le(FlwTask::expireTime, currentDate)
            .or()
            .le(FlwTask::remindTime, currentDate)
            .list()

    public fun selectListByParentTaskId(parentTaskId: Long?): List<FlwTask> =
        ktQuery()
            .eq(FlwTask::parentTaskId, parentTaskId)
            .list()

    public fun selectListByParentTaskIds(parentTaskIds: List<Long?>?): List<FlwTask> =
        ktQuery()
            .`in`(FlwTask::parentTaskId, parentTaskIds)
            .list()
}
