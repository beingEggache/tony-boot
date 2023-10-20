package com.tony.flow.service

import com.tony.flow.FlowOperator
import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.enums.TaskType

/**
 * 任务业务类接口
 * 任务服务
 * @author Tang Li
 * @date 2023/10/10 11:05
 * @since 1.0.0
 */
public interface TaskService {

    /**
     * 完成任务
     * @param [taskId] 任务id
     * @param [flowOperator] 任务完成者
     * @param [variable] 任务变量
     * @author Tang Li
     * @date 2023/10/10 10:48
     * @since 1.0.0
     */
    public fun complete(taskId: Long, flowOperator: FlowOperator, variable: Any?): FlowTask

    /**
     * 完成任务
     * @param [taskId] 任务id
     * @param [flowOperator] 任务完成者
     * @return [FlowTask]
     * @author Tang Li
     * @date 2023/10/10 10:49
     * @since 1.0.0
     */
    public fun complete(taskId: Long, flowOperator: FlowOperator): FlowTask = complete(taskId, flowOperator, null)

    /**
     * 按id更新任务
     * @param [flowTask] 任务
     * @author Tang Li
     * @date 2023/10/10 10:50
     * @since 1.0.0
     */
    public fun updateTaskById(flowTask: FlowTask)

    /**
     * 查看任务 设置为已阅状态
     * @param [taskId] 任务id
     * @param [taskActor] 任务参与者
     * @author Tang Li
     * @date 2023/10/10 11:04
     * @since 1.0.0
     */
    public fun viewTask(taskId: Long, taskActor: FlowTaskActor)

    /**
     * 任务超时
     * @param [taskId] 任务id
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 11:05
     * @since 1.0.0
     */
    public fun taskTimeout(taskId: Long): Boolean

    /**
     * 认领任务.
     *
     * 删除其它任务参与者
     * @param [taskId] 任务id
     * @param [flowHistoryTaskActor] 任务参与者
     * @return [FlowTask]
     * @author Tang Li
     * @date 2023/10/10 11:12
     * @since 1.0.0
     */
    public fun claimTask(taskId: Long, flowHistoryTaskActor: FlowHistoryTaskActor): FlowTask

    /**
     * 分配任务
     * @param [taskId] 任务id
     * @param [taskType] 任务类型
     * @param [flowTaskActor] 任务参与者
     * @param [assignee] 受让人
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 11:15
     * @since 1.0.0
     */
    public fun assignTask(
        taskId: Long,
        taskType: TaskType,
        flowTaskActor: FlowTaskActor,
        assignee: FlowTaskActor,
    ): Boolean

    /**
     * 转办任务
     * @param [taskId] 任务id
     * @param [taskActor] 任务参与者
     * @param [assignee] 受让人
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 11:17
     * @since 1.0.0
     */
    public fun transferTask(taskId: Long, taskActor: FlowTaskActor, assignee: FlowTaskActor): Boolean =
        assignTask(taskId, TaskType.TRANSFER, taskActor, assignee)

    /**
     * 委派任务.
     *
     * 代理人办理完任务该任务重新归还给原处理人
     * @param [taskId] 任务id
     * @param [taskActor] 任务参与者
     * @param [assignee] 受让人
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 11:19
     * @since 1.0.0
     */
    public fun delegateTask(taskId: Long, taskActor: FlowHistoryTaskActor, assignee: FlowHistoryTaskActor): Boolean =
        assignTask(taskId, TaskType.DELEGATE, taskActor, assignee)

    /**
     * 拿回任务.
     *
     * @param [taskId] 任务id
     * @param [flowOperator] 任务完成者
     * @return [FlowTask]?
     * @author Tang Li
     * @date 2023/10/10 11:20
     * @since 1.0.0
     */
    public fun reclaimTask(taskId: Long, flowOperator: FlowOperator): FlowTask?

    /**
     * 撤回任务
     * @param [taskId] 任务id
     * @param [flowOperator] 任务创建者
     * @return [FlowTask]?
     * @author Tang Li
     * @date 2023/10/10 11:24
     * @since 1.0.0
     */
    public fun withdrawTask(taskId: Long, flowOperator: FlowOperator): FlowTask?

    /**
     * 驳回任务.
     *
     * 驳回至上一步处理
     * @param [flowTask] 任务
     * @param [flowOperator] 任务创建者
     * @param [variable] 变量
     * @return [FlowTask]?
     * @author Tang Li
     * @date 2023/10/10 11:31
     * @since 1.0.0
     */
    public fun rejectTask(flowTask: FlowTask, flowOperator: FlowOperator, variable: Any?): FlowTask?

    /**
     * 驳回任务.
     *
     * 驳回至上一步处理
     * @param [flowTask] 任务
     * @param [flowOperator] 任务创建者
     * @return [FlowTask]?
     * @author Tang Li
     * @date 2023/10/10 11:31
     * @since 1.0.0
     */
    public fun rejectTask(flowTask: FlowTask, flowOperator: FlowOperator): FlowTask? =
        rejectTask(flowTask, flowOperator, null)

    /**
     * 判断可否执行任务.
     *
     * 根据 taskId、creatorId 判断创建人creatorId是否允许执行任务
     * @param [flowTask] 任务
     * @param [userId] 用户id
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 13:59
     * @since 1.0.0
     */
    public fun hasPermission(flowTask: FlowTask, userId: Long): Boolean

    // TODO
    // List<FlwTask> createTask(NodeModel taskModel, Execution execution);
}
