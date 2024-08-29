/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.entity.FlwHisInstance
import com.aizuda.bpm.engine.entity.FlwHisTask
import com.aizuda.bpm.engine.entity.FlwHisTaskActor
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import java.util.Optional

/**
 * 流程相关的查询服务
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface QueryService {
    /**
     * 根据流程实例ID获取流程实例对象
     *
     * @param instanceId 流程实例ID
     * @return Instance 流程实例对象
     */
    public fun getInstance(instanceId: Long?): FlwInstance?

    /**
     * 根据流程实例ID获取历史流程实例对象
     *
     * @param instanceId 历史流程实例ID
     * @return HistoryInstance 历史流程实例对象
     */
    public fun getHistInstance(instanceId: Long?): FlwHisInstance?

    /**
     * 根据任务ID获取任务对象
     *
     * @param taskId 任务ID
     * @return Task 任务对象
     */
    public fun getTask(taskId: Long?): FlwTask?

    /**
     * 根据任务ID获取历史任务对象
     *
     * @param taskId 历史任务ID
     * @return HistoryTask 历史任务对象
     */
    public fun getHistTask(taskId: Long?): FlwHisTask?

    /**
     * 根据任务名称查询历史任务对象列表
     *
     * @param instanceId 流程实例ID
     * @param taskName   任务名称(亦是节点名称)
     * @return 历史任务节点列表
     */
    public fun getHisTasksByName(
        instanceId: Long?,
        taskName: String?,
    ): Optional<List<FlwHisTask>>

    /**
     * 通过流程实例ID获取任务列表
     *
     * @param instanceId 流程实例ID
     * @return 任务对象列表
     */
    public fun getTasksByInstanceId(instanceId: Long?): List<FlwTask>

    public fun getTasksByInstanceIdAndTaskName(
        instanceId: Long?,
        taskName: String?,
    ): List<FlwTask>

    /**
     * 通过流程实例ID和任务key获取任务列表
     *
     * @param instanceId 流程实例ID
     * @param taskKey    任务KEY
     * @return 任务对象列表
     */
    public fun getTasksByInstanceIdAndTaskKey(
        instanceId: Long?,
        taskKey: String?,
    ): List<FlwTask>

    public fun getActiveTasksByInstanceIdAndTaskName(
        instanceId: Long?,
        taskName: String?,
    ): Optional<List<FlwTask>> =
        Optional.ofNullable(this.getTasksByInstanceIdAndTaskName(instanceId, taskName))

    /**
     * 根据 流程实例ID 获取当前活动任务列表
     *
     * @param instanceId 流程实例ID
     * @return 当前活动任务列表
     */
    public fun getActiveTasksByInstanceId(instanceId: Long?): Optional<List<FlwTask>> =
        Optional.ofNullable(this.getTasksByInstanceId(instanceId))

    /**
     * 根据 流程实例ID 获取当前活动任务列表
     *
     * @param instanceId 流程实例ID
     * @return 当前活动任务列表
     */
    public fun getActiveTaskActorsByInstanceId(instanceId: Long?): Optional<List<FlwTaskActor>>

    /**
     * 根据任务ID获取活动任务参与者数组
     *
     * @param taskId 任务ID
     * @return 当前活动任务参与者列表
     */
    public fun getTaskActorsByTaskId(taskId: Long?): List<FlwTaskActor>

    public fun getActiveTaskActorsByTaskId(taskId: Long?): Optional<List<FlwTaskActor>> =
        Optional.ofNullable(this.getTaskActorsByTaskId(taskId))

    /**
     * 根据任务ID获取活动任务参与者数组
     *
     * @param taskId  任务ID
     * @param actorId 任务参与者ID
     * @return 当前活动任务参与者列表
     */
    public fun getTaskActorsByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): List<FlwTaskActor>

    /**
     * 根据任务ID获取历史任务参与者数组
     *
     * @param taskId 历史任务ID
     * @return 当前活动任务参与者列表
     */
    public fun getHisTaskActorsByTaskId(taskId: Long?): List<FlwHisTaskActor>

    /**
     * 根据任务ID获取历史任务参与者数组
     *
     * @param taskId  历史任务ID
     * @param actorId 任务参与者ID
     * @return 当前活动任务参与者列表
     */
    public fun getHisTaskActorsByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): List<FlwHisTaskActor?>?

    /**
     * 根据实例ID和任务节点名称获取当前节点激活的任务
     *
     * @param instanceId 实例ID
     * @param taskNames  任务节点名称
     * @return 子任务列表
     */
    public fun getActiveTasks(
        instanceId: Long?,
        taskNames: List<String?>?,
    ): List<FlwTask?>?

    /**
     * 根据实例ID获取实例所有历史任务，时间倒序
     *
     *
     *
     * 额外根据唯一的ID进行排序，防止低版本数据库时间重复的情况。（注：ID 是时间增长的，也是有时间顺序的）
     *
     *
     * @param instanceId 实例ID
     * @return 历史任务列表
     */
    public fun getHisTasksByInstanceId(instanceId: Long?): List<FlwHisTask>
}
