/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus.service

import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.enums.TaskState
import com.tony.fus.db.enums.TaskType
import com.tony.fus.db.po.FusHistoryTaskActor
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.enums.EventType

/**
 * 任务业务类接口
 * 任务服务
 * @author Tang Li
 * @date 2023/10/10 19:05
 * @since 1.0.0
 */
public interface TaskService {
    /**
     * 完成任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @param [variable] 任务变量
     * @author Tang Li
     * @date 2023/10/10 19:48
     * @since 1.0.0
     */
    public fun complete(
        taskId: String,
        userId: String,
        variable: Map<String, Any?>?,
    ): FusTask =
        execute(
            taskId,
            userId,
            TaskState.COMPLETED,
            EventType.COMPLETED,
            variable
        )

    /**
     * 完成任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @return [FusTask]
     * @author Tang Li
     * @date 2023/10/10 19:49
     * @since 1.0.0
     */
    public fun complete(
        taskId: String,
        userId: String,
    ): FusTask =
        complete(taskId, userId, null)

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @param [taskState] 任务状态
     * @param [eventType] 事件类型
     * @param [variable] 变量
     * @return [FusTask]
     * @author Tang Li
     * @date 2023/11/24 19:51
     * @since 1.0.0
     */
    public fun execute(
        taskId: String,
        userId: String,
        taskState: TaskState,
        eventType: EventType,
        variable: Map<String, Any?>?,
    ): FusTask

    /**
     * 完成指定实例ID活动任务
     * @param [instanceId] 实例id
     * @param [userId] 操作人id
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/11/07 19:08
     * @since 1.0.0
     */
    public fun completeActiveTasksByInstanceId(
        instanceId: String,
        userId: String,
    ): Boolean

    /**
     * 按id更新任务
     * @param [task] 任务
     * @author Tang Li
     * @date 2023/10/10 19:50
     * @since 1.0.0
     */
    public fun updateTaskById(task: FusTask)

    /**
     * 查看任务 设置为已阅状态
     * @param [taskId] 任务id
     * @param [actorId] 参与者id
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/11/03 19:55
     * @since 1.0.0
     */
    public fun viewTask(
        taskId: String,
        actorId: String,
    ): Boolean

    /**
     * 认领任务.
     *
     * 删除其它任务参与者
     * @param [taskId] 任务id
     * @param [actorId] 参与者id
     * @return [FusTask]
     * @author Tang Li
     * @date 2023/10/10 19:12
     * @since 1.0.0
     */
    public fun claimTask(
        taskId: String,
        actorId: String,
    ): FusTask

    /**
     * 分配任务
     * @param [taskId] 任务id
     * @param [taskType] 任务类型
     * @param [taskActor] 任务参与者
     * @param [assignee] 受让人
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 19:15
     * @since 1.0.0
     */
    public fun assignTask(
        taskId: String,
        taskType: TaskType,
        taskActor: FusTaskActor,
        assignee: FusTaskActor,
    ): Boolean

    /**
     * 转办任务
     * @param [taskId] 任务id
     * @param [taskActor] 任务参与者
     * @param [assignee] 受让人
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 19:17
     * @since 1.0.0
     */
    public fun transferTask(
        taskId: String,
        taskActor: FusTaskActor,
        assignee: FusTaskActor,
    ): Boolean =
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
     * @date 2023/10/10 19:19
     * @since 1.0.0
     */
    public fun delegateTask(
        taskId: String,
        taskActor: FusHistoryTaskActor,
        assignee: FusHistoryTaskActor,
    ): Boolean =
        assignTask(taskId, TaskType.DELEGATE, taskActor, assignee)

    /**
     * 拿回任务.
     *
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @return [FusTask]?
     * @author Tang Li
     * @date 2023/10/10 19:20
     * @since 1.0.0
     */
    public fun reclaimTask(
        taskId: String,
        userId: String,
    ): FusTask

    /**
     * 撤回任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @return [FusTask]?
     * @author Tang Li
     * @date 2023/10/10 19:24
     * @since 1.0.0
     */
    public fun withdrawTask(
        taskId: String,
        userId: String,
    ): FusTask

    /**
     * 驳回任务.
     *
     * 驳回至上一步处理
     * @param [task] 任务
     * @param [userId] 操作人id
     * @param [variable] 变量
     * @return [FusTask]?
     * @author Tang Li
     * @date 2023/10/10 19:31
     * @since 1.0.0
     */
    public fun rejectTask(
        task: FusTask,
        userId: String,
        variable: Map<String, Any?>?,
    ): FusTask

    /**
     * 驳回任务.
     *
     * 驳回至上一步处理
     * @param [task] 任务
     * @param [userId] 操作人id
     * @return [FusTask]?
     * @author Tang Li
     * @date 2023/10/10 19:31
     * @since 1.0.0
     */
    public fun rejectTask(
        task: FusTask,
        userId: String,
    ): FusTask =
        rejectTask(task, userId, null)

    /**
     * 判断可否执行任务.
     *
     * 根据 taskId、userId 判断创建人creatorId是否允许执行任务
     * @param [task] 任务
     * @param [userId] 操作人id
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/10 19:59
     * @since 1.0.0
     */
    public fun hasPermission(
        task: FusTask,
        userId: String,
    ): Boolean

    /**
     * 创建任务
     * @param [node] 节点
     * @param [execution] 流程执行
     * @return [List<FusTask>]
     * @author Tang Li
     * @date 2023/10/25 19:05
     * @since 1.0.0
     */
    public fun createTask(
        node: FusNode?,
        execution: FusExecution,
    ): List<FusTask>

    /**
     * 列出过期或提醒任务
     * @return [List<FusTask>]
     * @author Tang Li
     * @date 2023/10/25 19:23
     * @since 1.0.0
     */
    public fun listExpiredOrRemindTasks(): List<FusTask>

    /**
     * 添加任务参与者【加签】
     * @param [taskId] 任务id
     * @param [performType] 执行类型
     * @param [historyTaskActorList] 流历史任务参与者
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/25 19:25
     * @since 1.0.0
     */
    public fun addTaskActor(
        taskId: String,
        performType: PerformType,
        historyTaskActorList: List<FusHistoryTaskActor>,
    ): Boolean

    /**
     * 添加任务参与者【加签】
     * @param [taskId] 任务id
     * @param [performType] 执行类型
     * @param [taskActor] 流历史任务参与者
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/25 19:25
     * @since 1.0.0
     */
    public fun addTaskActor(
        taskId: String,
        performType: PerformType,
        taskActor: FusHistoryTaskActor,
    ): Boolean =
        addTaskActor(taskId, performType, listOf(taskActor))

    /**
     * 删除任务参与者【减签】
     * @param [taskId] 任务id
     * @param [taskActorIdList] 任务参与者ID
     * @author Tang Li
     * @date 2023/10/25 19:27
     * @since 1.0.0
     */
    public fun removeTaskActor(
        taskId: String,
        taskActorIdList: Collection<String>,
    )

    /**
     * 删除任务参与者【减签】
     * @param [taskId] 任务id
     * @param [taskActorId] 任务参与者id
     * @author Tang Li
     * @date 2023/10/25 19:25
     * @since 1.0.0
     */
    public fun removeTaskActor(
        taskId: String,
        taskActorId: String,
    ): Unit =
        removeTaskActor(taskId, setOf(taskActorId))

    /**
     * 按实例id级联删除.
     *
     * 级联删除 fus_history_task, fus_history_task_actor, fus_task, fus_task_actor.
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/10/25 19:28
     * @since 1.0.0
     */
    public fun cascadeRemoveByInstanceId(instanceId: String)
}
