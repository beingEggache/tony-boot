package com.tony.flow.service

import com.tony.flow.db.po.FlowHistoryInstance
import com.tony.flow.db.po.FlowHistoryTask
import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor

/**
 * 流程相关的查询服务
 * 查询服务
 * @author Tang Li
 * @date 2023/10/20 16:29
 * @since 1.0.0
 */
public interface QueryService {

    /**
     * 获取流程实例
     * @param [instanceId] 流程实例id
     * @return [FlowInstance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 09:22
     * @since 1.0.0
     */
    public fun instance(instanceId: Long): FlowInstance

    /**
     * 获取历史流程实例
     * @param [instanceId] 流程实例id
     * @return [FlowHistoryInstance] 历史流程实例
     * @author Tang Li
     * @date 2023/10/10 09:23
     * @since 1.0.0
     */
    public fun historyInstance(instanceId: Long): FlowHistoryInstance

    /**
     * 获取任务
     * @param [taskId] 任务id
     * @return [FlowTask] 任务对象
     * @author Tang Li
     * @date 2023/10/10 09:24
     * @since 1.0.0
     */
    public fun task(taskId: Long): FlowTask

    /**
     * 列出任务
     * @param [instanceId] 实例id
     * @param [taskNames] 任务名称
     * @return [List<FlowTask>]
     * @author Tang Li
     * @date 2023/10/10 09:38
     * @since 1.0.0
     */
    public fun listTask(instanceId: Long, taskNames: Collection<String>): List<FlowTask>

    /**
     * 按实例id列出任务
     * @param [instanceId] 实例id
     * @return [List<FlowTask>]
     * @author Tang Li
     * @date 2023/10/10 09:31
     * @since 1.0.0
     */
    public fun listTaskByInstanceId(instanceId: Long?): List<FlowTask>

    /**
     * 获取历史任务
     * @param [taskId] 历史任务id
     * @return [FlowTask] 历史任务对象
     * @author Tang Li
     * @date 2023/10/10 09:24
     * @since 1.0.0
     */
    public fun historyTask(taskId: Long): FlowHistoryTask

    /**
     * 列出历史任务
     * @param [instanceId] 实例id
     * @return [List<FlowHistoryTask>]
     * @author Tang Li
     * @date 2023/10/10 09:39
     * @since 1.0.0
     */
    public fun listHistoryTask(instanceId: Long): List<FlowHistoryTask>

    /**
     * 按名称列出历史任务
     * @param [instanceId] 实例id
     * @param [taskName] 任务名称
     * @return [List<FlowHistoryTask>]
     * @author Tang Li
     * @date 2023/10/10 09:30
     * @since 1.0.0
     */
    public fun listHistoryTaskByName(instanceId: Long, taskName: String): List<FlowHistoryTask>

    /**
     * 按任务id列出任务参与者
     * @param [taskId] 任务id
     * @return [List<FlowTaskActor>]
     * @author Tang Li
     * @date 2023/10/10 09:32
     * @since 1.0.0
     */
    public fun listTaskActorByTaskId(taskId: Long): List<FlowTaskActor>

    /**
     * 按任务id列出任务参与者
     * @param [taskId] 历史任务ID
     * @return [List<FlowTaskActor>]
     * @author Tang Li
     * @date 2023/10/10 09:32
     * @since 1.0.0
     */
    public fun listHistoryTaskActorByTaskId(taskId: Long): List<FlowHistoryTaskActor>
}
