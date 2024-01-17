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

import com.tony.fus.FusContext
import com.tony.fus.FusTaskPermission
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.enums.TaskState
import com.tony.fus.db.enums.TaskType
import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusHistoryTaskActorMapper
import com.tony.fus.db.mapper.FusHistoryTaskMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusTaskActorMapper
import com.tony.fus.db.mapper.FusTaskCcMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.db.po.FusHistoryInstance
import com.tony.fus.db.po.FusHistoryTask
import com.tony.fus.db.po.FusHistoryTaskActor
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.db.po.FusTaskCc
import com.tony.fus.extension.fusListThrowIfEmpty
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.extension.fusThrowIfEmpty
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.extension.ofPerformType
import com.tony.fus.listener.TaskListener
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.enums.EventType
import com.tony.fus.model.enums.NodeType
import com.tony.utils.copyToNotNull
import com.tony.utils.ifNullOrBlank
import com.tony.utils.runIf
import com.tony.utils.throwIf
import com.tony.utils.toJsonString
import java.time.LocalDateTime
import java.util.function.Consumer
import java.util.function.Function

/**
 * 任务业务类接口
 * 任务服务
 * @author Tang Li
 * @date 2023/10/10 19:05
 * @since 1.0.0
 */
public sealed interface TaskService {
    /**
     * 完成任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/10/10 19:48
     * @since 1.0.0
     */
    public fun complete(
        taskId: String,
        userId: String,
    ): FusTask =
        executeTask(
            taskId,
            userId,
            TaskState.COMPLETED,
            EventType.COMPLETED,
            mapOf()
        )

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @param [taskState] 任务状态
     * @param [eventType] 事件类型
     * @return [FusTask]
     * @author Tang Li
     * @date 2023/11/24 19:51
     * @since 1.0.0
     */
    public fun executeTask(
        taskId: String,
        userId: String,
        taskState: TaskState,
        eventType: EventType,
        args: Map<String, Any?>?,
    ): FusTask

    /**
     * 执行节点跳转任务
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [userId] 用户id
     * @param [func] 回调
     * @author Tang Li
     * @date 2023/12/21 14:24
     * @since 1.0.0
     */
    public fun executeJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
        func: Function<FusTask, FusExecution>,
    )

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
     * @param [userId] 用户id
     * @return [FusTask]
     * @author Tang Li
     * @date 2023/10/10 19:12
     * @since 1.0.0
     */
    public fun claimTask(
        taskId: String,
        userId: String,
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
        taskActor: FusTaskActor,
        assignee: FusTaskActor,
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
     * 唤醒历史任务
     * @param [taskId] 任务id
     * @param [userId] 用户id
     * @return [FusTask]
     * @author Tang Li
     * @date 2024/01/02 10:20
     * @since 1.0.0
     */
    public fun resumeTask(
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
     * @author Tang Li
     * @date 2023/10/25 19:05
     * @since 1.0.0
     */
    public fun createTask(
        node: FusNode?,
        execution: FusExecution,
    )

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
     * @param [taskActorList] 流历史任务参与者
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/25 19:25
     * @since 1.0.0
     */
    public fun addTaskActor(
        taskId: String,
        performType: PerformType,
        taskActorList: List<FusTaskActor>,
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
        taskActor: FusTaskActor,
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
     * @param [instanceIds] 实例id
     * @author Tang Li
     * @date 2023/10/25 19:28
     * @since 1.0.0
     */
    public fun cascadeRemoveByInstanceIds(instanceIds: Collection<String>)

    /**
     * 结束外部流程任务
     * @param [outProcessId] 进程外id
     * @param [outInstanceId] out实例id
     * @author Tang Li
     * @date 2024/01/16 18:00
     * @since 1.0.0
     */
    public fun endOutProcessTask(
        outProcessId: String,
        outInstanceId: String,
    )
}

/**
 * TaskServiceImpl is
 * @author tangli
 * @date 2023/10/26 19:44
 * @since 1.0.0
 */
internal open class TaskServiceImpl(
    private val taskPermission: FusTaskPermission,
    private val taskMapper: FusTaskMapper,
    private val taskActorMapper: FusTaskActorMapper,
    private val taskCcMapper: FusTaskCcMapper,
    private val historyTaskMapper: FusHistoryTaskMapper,
    private val historyTaskActorMapper: FusHistoryTaskActorMapper,
    private val instanceMapper: FusInstanceMapper,
    private val historyInstanceMapper: FusHistoryInstanceMapper,
    private val taskListener: TaskListener? = null,
) : TaskService {
    override fun executeTask(
        taskId: String,
        userId: String,
        taskState: TaskState,
        eventType: EventType,
        args: Map<String, Any?>?,
    ): FusTask {
        val task =
            getHasPermissionTask(
                taskId,
                userId,
                taskState
            ).also {
                args?.apply {
                    it.variable = args.toJsonString()
                }
            }
        moveToHistoryTask(task, taskState, userId)
        taskListener?.notify(eventType) { task }
        return task
    }

    override fun executeJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
        func: Function<FusTask, FusExecution>,
    ) {
        val task = getHasPermissionTask(taskId, userId)
        val execution = func.apply(task)
        val node =
            execution
                .process
                .model()
                .getNode(nodeName)
                .fusThrowIfNull("无法找到节点[$nodeName]")

        taskMapper
            .ktQuery()
            .eq(FusTask::instanceId, task.instanceId)
            .list()
            .forEach {
                moveToHistoryTask(it, TaskState.JUMP, userId)
            }

        if (node.nodeType == NodeType.INITIATOR) {
            val initTask =
                FusTask()
                    .apply {
                        creatorId = execution.userId
                        instanceId = execution.instance.instanceId
                        taskName = node.nodeName.ifNullOrBlank()
                        // ? 搞不清楚
                        taskType = TaskType.create(node.nodeType.value).fusThrowIfNull("nodeType null")
                        parentTaskId = execution.task?.taskId.ifNullOrBlank()
                        performType = PerformType.START
                    }
            taskMapper.insert(initTask)
            taskActorMapper.insert(
                FusTaskActor()
                    .apply {
                        this.actorId = execution.instance.creatorId
                        this.actorType = ActorType.USER
                        this.instanceId = execution.instance.instanceId
                        this.taskId = initTask.taskId
                    }
            )
        } else {
            createTask(node, execution)
        }
        taskListener?.notify(EventType.JUMP) { task }
    }

    override fun completeActiveTasksByInstanceId(
        instanceId: String,
        userId: String,
    ): Boolean {
        taskMapper
            .ktQuery()
            .eq(FusTask::instanceId, instanceId)
            .list()
            .forEach {
                if (!moveToHistoryTask(it, TaskState.TERMINATED, userId)) {
                    return false
                }
            }
        return true
    }

    override fun updateTaskById(task: FusTask) {
        taskMapper.updateById(task)
        taskListener?.notify(EventType.UPDATE) { task }
    }

    override fun viewTask(
        taskId: String,
        actorId: String,
    ): Boolean =
        taskActorMapper
            .ktQuery()
            .eq(FusTaskActor::taskId, taskId)
            .eq(FusTaskActor::actorId, actorId)
            .exists()
            .runIf {
                taskMapper.updateById(
                    FusTask()
                        .apply {
                            this.taskId = taskId
                            this.viewed = true
                        }
                ) > 0
            } ?: false

    override fun claimTask(
        taskId: String,
        userId: String,
    ): FusTask =
        taskMapper
            .fusSelectByIdNotNull("任务不存在(id=$taskId)")
            .also {
                hasPermission(it, userId)
                taskActorMapper.deleteByTaskIds(listOf(taskId))
                taskActorMapper.insert(
                    FusTaskActor()
                        .apply {
                            this.actorId = userId
                            this.actorType = ActorType.USER
                            this.taskId = taskId
                            // TODO name provider
                            // TODO actorName = nameProvider.get(userId)
                        }
                )
            }

    override fun assignTask(
        taskId: String,
        taskType: TaskType,
        taskActor: FusTaskActor,
        assignee: FusTaskActor,
    ): Boolean {
        val taskActorList =
            taskActorMapper
                .ktQuery()
                .eq(FusTaskActor::taskId, taskId)
                .eq(FusTaskActor::actorId, taskActor.actorId)
                .fusListThrowIfEmpty("无权转办该任务.")

        val result =
            taskMapper.updateById(
                FusTask()
                    .apply {
                        this.taskId = taskId
                        this.taskType = taskType
                        this.assignorId = taskActor.actorId
                        this.assignorName = taskActor.actorName
                    }
            ) > 0

        fusThrowIf(!result, "update task assignor failed.")

        taskActorMapper.deleteBatchIds(taskActorList.map { it.taskActorId })
        assignTask(taskActorList.first().instanceId, taskId, assignee)
        return true
    }

    private fun assignTask(
        instanceId: String,
        taskId: String,
        taskActor: FusTaskActor,
    ) {
        taskActor.taskActorId = ""
        taskActor.instanceId = instanceId
        taskActor.taskId = taskId
        taskActorMapper.insert(taskActor)
    }

    override fun reclaimTask(
        taskId: String,
        userId: String,
    ): FusTask =
        undoHistoryTask(
            taskId
        ) { historyTask ->
            taskMapper
                .ktQuery()
                .eq(FusTask::instanceId, historyTask.instanceId)
                .list()
                .map {
                    it.taskId
                }.also { taskIdList ->
                    taskMapper.deleteBatchIds(taskIdList)
                    taskActorMapper.deleteByTaskIds(taskIdList)
                }
        }

    override fun resumeTask(
        taskId: String,
        userId: String,
    ): FusTask =
        historyTaskMapper
            .fusSelectByIdNotNull(
                taskId
            ).let { historyTask ->
                throwIf(
                    historyTask.creatorId != userId,
                    "当前参与者[$userId]不允许唤醒历史任务[$taskId]"
                )
                val instanceId = historyTask.instanceId
                instanceMapper
                    .fusSelectByIdNotNull(
                        instanceId,
                        "已结束流程任务不支持唤醒"
                    )
                val task = historyTask.copyToNotNull(FusTask())
                taskMapper.insert(task)
                assignTask(
                    instanceId,
                    taskId,
                    FusTaskActor()
                        .apply {
                            this.actorId = userId
                            this.actorType = ActorType.USER
                            this.taskId = taskId
                            // TODO name provider
                            // TODO actorName = nameProvider.get(userId)
                        }
                )
                updateCurrentNode(instanceId, task.taskName, task.creatorId)
                task
            }

    override fun withdrawTask(
        taskId: String,
        userId: String,
    ): FusTask =
        undoHistoryTask(
            taskId
        ) { historyTask ->
            val taskIdList =
                if (historyTask.performType == PerformType.COUNTERSIGN) {
                    taskMapper
                        .ktQuery()
                        .eq(FusTask::parentTaskId, historyTask.taskId)
                        .list()
                } else {
                    val taskList =
                        historyTaskMapper
                            .ktQuery()
                            .select(FusHistoryTask::taskId)
                            .eq(FusHistoryTask::instanceId, historyTask.instanceId)
                            .eq(FusHistoryTask::taskName, historyTask.taskName)
                            .eq(FusHistoryTask::parentTaskId, historyTask.parentTaskId)
                            .listObj<String>()
                            .let { historyTaskIdList ->
                                taskMapper
                                    .ktQuery()
                                    .`in`(FusTask::parentTaskId, historyTaskIdList)
                                    .list()
                            }
                    taskList
                }.fusThrowIfEmpty("后续活动任务已完成或不存在，无法撤回.")
                    .map { it.taskId }

            taskActorMapper
                .ktQuery()
                .`in`(FusTaskActor::taskId, taskIdList)
                .list()
                .takeIf { it.isNotEmpty() }
                ?.also { taskActorMapper.deleteBatchIds(it) }

            taskMapper.deleteBatchIds(taskIdList)
        }

    override fun rejectTask(
        task: FusTask,
        userId: String,
        variable: Map<String, Any?>?,
    ): FusTask {
        fusThrowIf(task.atStartNode, "上一步任务ID为空，无法驳回至上一步处理")
        executeTask(task.taskId, userId, TaskState.REJECTED, EventType.REJECTED, variable ?: mapOf())
        return undoHistoryTask(task.parentTaskId)
    }

    override fun hasPermission(
        task: FusTask,
        userId: String,
    ): Boolean {
        if (task.creatorId.isEmpty()) {
            return true
        }

        if (userId.isEmpty()) {
            return true
        }
        val taskActorList =
            taskActorMapper.selectListByTaskId(task.taskId)

        if (taskActorList.isEmpty()) {
            return true
        }

        return taskPermission.hasPermission(userId, taskActorList)
    }

    override fun createTask(
        node: FusNode?,
        execution: FusExecution,
    ) {
        val nodeType = node?.nodeType
        val task =
            FusTask().apply {
                this.creatorId = execution.userId
                this.instanceId = execution.instance.instanceId
                this.taskName = node?.nodeName.ifNullOrBlank()
                // ? 搞不清楚
                this.taskType = TaskType.create(nodeType?.value!!).fusThrowIfNull("nodeType null")
                this.parentTaskId = execution.task?.taskId.ifNullOrBlank()
            }

        val taskActorList =
            FusContext
                .taskActorProvider()
                .listTaskActors(node, execution)

        when (nodeType) {
            NodeType.INITIATOR ->
                saveTask(
                    task,
                    PerformType.START,
                    taskActorList,
                    execution
                ).also {
                    node
                        .nextNode()
                        ?.also { nextNode ->
                            FusContext.executeNode(nextNode, execution)
                        }
                }

            NodeType.APPROVER ->
                saveTask(
                    task,
                    // ? 搞不清楚
                    node.multiApproveMode.ofPerformType(),
                    taskActorList,
                    execution
                )

            NodeType.CC -> {
                saveTaskCc(node, execution)
                node.nextNode()?.also { FusContext.executeNode(it, execution) }
            }

            NodeType.CONDITIONAL_APPROVE -> {
                val newTask =
                    task
                        .copyToNotNull(FusTask())
                        .apply {
                            taskId = ""
                        }
                saveTask(
                    newTask,
                    node.multiApproveMode.ofPerformType(),
                    taskActorList,
                    execution
                )
            }

            NodeType.SUB_PROCESS -> {
                execution.userId
                val subInstance =
                    FusContext.startProcessByKey(
                        node.outProcessKey,
                        execution.userId
                    ) { instance ->
                        instance.parentInstanceId = task.instanceId
                    }
                historyTaskMapper.insert(
                    FusHistoryTask().apply {
                        creatorId = subInstance.creatorId
                        creatorName = subInstance.creatorName
                        createTime = subInstance.createTime
                        instanceId = subInstance.parentInstanceId
                        taskName = node.nodeName
                        outProcessId = subInstance.processId
                        outInstanceId = subInstance.instanceId
                        // ? 搞不清楚
                        performType = PerformType.UNKNOWN
                        // ? 搞不清楚
                        taskType = TaskType.create(node.nodeType.value).fusThrowIfNull("nodeType null")
                    }
                )
            }

            else -> {}
        }
        if (nodeType != NodeType.CC) {
            updateCurrentNode(task.instanceId, task.taskName, task.creatorId)
        }
    }

    open fun saveTaskCc(
        node: FusNode?,
        execution: FusExecution,
    ) {
        val nodeUserList = node?.nodeUserList
        if (nodeUserList.isNullOrEmpty()) {
            return
        }
        val parentTaskId = execution.task?.parentTaskId
        nodeUserList
            .filter { nodeAssignee ->
                !taskCcMapper
                    .ktQuery()
                    .eq(FusTaskCc::instanceId, execution.instance.instanceId)
                    .eq(FusTaskCc::actorId, nodeAssignee.id)
                    .exists()
            }.map { nodeAssignee ->
                FusTaskCc().apply {
                    this.creatorId = execution.userId
                    this.instanceId = execution.instance.instanceId
                    this.parentTaskId = parentTaskId.ifNullOrBlank()
                    this.taskName = node.nodeName
                    this.actorId = nodeAssignee.id
                    this.actorName = nodeAssignee.name
                    this.actorType = ActorType.USER
                    this.taskState = TaskState.ACTIVE
                }
            }.also { taskCcList ->
                taskCcMapper.insertBatch(taskCcList)
            }
    }

    override fun listExpiredOrRemindTasks(): List<FusTask> =
        LocalDateTime.now().let { now ->
            taskMapper
                .ktQuery()
                .le(FusTask::expireTime, now)
                .or()
                .le(FusTask::remindTime, now)
                .list()
        }

    override fun addTaskActor(
        taskId: String,
        performType: PerformType,
        taskActorList: List<FusTaskActor>,
    ): Boolean {
        val task = taskMapper.fusSelectByIdNotNull(taskId)
        val actorIdSet =
            taskActorMapper
                .selectListByTaskId(taskId)
                .fusThrowIfEmpty()
                .map { it.actorId }
                .toSet()

        taskActorList
            .filter {
                !actorIdSet.contains(it.actorId)
            }.forEach {
                assignTask(task.instanceId, taskId, it)
            }
        return taskMapper.updateById(task.apply { this.performType = performType }) > 0
    }

    override fun endOutProcessTask(
        outProcessId: String,
        outInstanceId: String,
    ) {
        historyTaskMapper
            .ktQuery()
            .eq(FusHistoryTask::outProcessId, outProcessId)
            .eq(FusHistoryTask::outInstanceId, outInstanceId)
            .last("limit 1")
            .one()
            ?.also { historyTask ->
                historyTaskMapper.updateById(
                    FusHistoryTask()
                        .apply {
                            this.taskId = historyTask.taskId
                            this.createTime = historyTask.createTime
                            this.taskState = TaskState.COMPLETED
                            this.setEndTimeAndDuration()
                        }
                )
            }
    }

    override fun removeTaskActor(
        taskId: String,
        taskActorIdList: Collection<String>,
    ) {
        val taskActorList =
            taskActorMapper
                .selectListByTaskId(taskId)
                .fusThrowIfEmpty()
        fusThrowIf(taskActorList.size == taskActorIdList.size, "illegal")
        taskActorMapper
            .ktUpdate()
            .eq(FusTaskActor::taskId, taskId)
            .`in`(FusTaskActor::actorId, taskActorIdList)
            .remove()
    }

    override fun cascadeRemoveByInstanceIds(instanceIds: Collection<String>) {
        historyTaskActorMapper
            .ktUpdate()
            .`in`(FusHistoryTaskActor::instanceId, instanceIds)
            .remove()
        historyTaskMapper
            .ktUpdate()
            .`in`(FusTask::instanceId, instanceIds)
            .remove()
        taskActorMapper
            .ktUpdate()
            .`in`(FusTaskActor::instanceId, instanceIds)
            .remove()
        taskMapper
            .ktUpdate()
            .`in`(FusTask::instanceId, instanceIds)
            .remove()
        taskCcMapper
            .ktUpdate()
            .`in`(FusTaskCc::instanceId, instanceIds)
            .remove()
    }

    private fun getHasPermissionTask(
        taskId: String,
        userId: String,
        taskState: TaskState? = null,
    ): FusTask {
        val task =
            taskMapper
                .fusSelectByIdNotNull(
                    taskId,
                    "指定的任务不存在"
                )
        if (
            taskState == null ||
            taskState == TaskState.ACTIVE ||
            taskState == TaskState.JUMP ||
            taskState == TaskState.COMPLETED
        ) {
            fusThrowIf(
                !hasPermission(task, userId),
                "当前参与者[$userId]不允许执行任务[$taskId]"
            )
        }
        return task
    }

    private fun moveToHistoryTask(
        task: FusTask,
        taskState: TaskState,
        userId: String,
    ): Boolean {
        val historyTask =
            task
                .copyToNotNull(FusHistoryTask())
                .apply {
                    this.setEndTimeAndDuration()
                    this.taskState = taskState
                    this.creatorId = userId
                }

        fusThrowIf(
            (historyTaskMapper.insert(historyTask) <= 0),
            "Migration to FusHistoryTask table failed"
        )
        taskActorMapper
            .selectListByTaskId(task.taskId)
            .takeIf { it.isNotEmpty() }
            ?.apply {
                fusThrowIf(
                    !taskActorMapper
                        .ktUpdate()
                        .eq(FusTaskActor::taskId, task.taskId)
                        .remove(),
                    "Delete FusTaskActor table failed"
                )
            }?.forEach { taskActor ->
                fusThrowIf(
                    historyTaskActorMapper
                        .insert(taskActor.copyToNotNull(FusHistoryTaskActor())) <= 0,
                    "Migration to FusHistoryTaskActor table failed"
                )
            }
        return taskMapper.deleteById(task) > 0
    }

    private fun undoHistoryTask(
        historyTaskId: String,
        callback: Consumer<FusHistoryTask>? = null,
    ): FusTask {
        val historyTask =
            historyTaskMapper
                .fusSelectByIdNotNull(historyTaskId, "指定的任务不存在")
        callback?.accept(historyTask)
        val task =
            historyTask.copyToNotNull(FusTask()).apply {
                taskId = ""
            }
        taskMapper.insert(task)
        if (task.atStartNode) {
            taskActorMapper.insert(
                FusTaskActor().apply {
                    this.tenantId = task.tenantId
                    this.actorId = task.creatorId
                    this.actorName = task.creatorName
                    this.taskId = task.taskId
                    this.instanceId = task.instanceId
                }
            )
        } else {
            historyTaskActorMapper
                .ktQuery()
                .eq(FusHistoryTaskActor::taskId, historyTaskId)
                .list()
                .map { historyTaskActor ->
                    FusTaskActor().apply {
                        tenantId = historyTaskActor.tenantId
                        instanceId = historyTaskActor.instanceId
                        taskId = task.taskId
                        actorType = historyTaskActor.actorType
                        actorId = historyTaskActor.actorId
                        actorName = historyTaskActor.actorName
                    }
                }.also { taskActorList ->
                    taskActorMapper.insertBatch(taskActorList)
                }
        }
        updateCurrentNode(task.instanceId, task.taskName, task.creatorId)
        return task
    }

    private fun updateCurrentNode(
        instanceId: String,
        nodeName: String,
        updatorId: String,
    ) {
        instanceMapper
            .ktUpdate()
            .eq(FusInstance::instanceId, instanceId)
            .set(FusInstance::nodeName, nodeName)
            .set(FusInstance::updatorId, updatorId)
            .update()
        historyInstanceMapper
            .ktUpdate()
            .eq(FusHistoryInstance::instanceId, instanceId)
            .set(FusHistoryInstance::nodeName, nodeName)
            .set(FusHistoryInstance::updatorId, updatorId)
            .update()
    }

    private fun saveTask(
        task: FusTask,
        performType: PerformType,
        taskActorList: Collection<FusTaskActor>,
        execution: FusExecution,
    ) {
        task.performType = performType
        if (performType == PerformType.START) {
            val historyTask =
                task
                    .copyToNotNull(FusHistoryTask())
                    .apply {
                        taskState = TaskState.COMPLETED
                        this.setEndTimeAndDuration()
                    }
            historyTaskMapper.insert(historyTask)
            execution.task = historyTask
            taskActorList
                .takeIf { actorList -> actorList.isNotEmpty() }
                ?.forEach { taskActor ->
                    val historyTaskActor =
                        taskActor
                            .copyToNotNull(FusHistoryTaskActor())
                            .apply {
                                taskActorId = ""
                                instanceId = task.instanceId
                                taskId = historyTask.taskId
                            }
                    historyTaskActorMapper.insert(historyTaskActor)
                }
            return
        }
        if (performType == PerformType.UNKNOWN) {
            task.variable =
                execution
                    .variable
                    .toJsonString()
                    .ifNullOrBlank("{}")
            taskMapper.insert(task)

            taskActorList
                .forEach { taskActor ->
                    assignTask(task.instanceId, task.taskId, taskActor)
                }
            return
        }

        taskActorList.fusThrowIfEmpty("任务参与者不能为空")
        if (performType == PerformType.OR_SIGN) {
            taskMapper.insert(task)
            taskActorList.forEach {
                assignTask(task.instanceId, task.taskId, it)
            }
            taskListener?.notify(EventType.CREATE) { task }
            return
        }

        if (performType == PerformType.SORT) {
            taskMapper.insert(task)
            assignTask(
                task.instanceId,
                task.taskId,
                execution.nextTaskActor ?: taskActorList.first()
            )
            taskListener?.notify(EventType.CREATE) { task }
            return
        }

        taskActorList.map {
            val newTask = task.copyToNotNull(FusTask()).apply { taskId = "" }
            taskMapper.insert(newTask)
            assignTask(newTask.instanceId, newTask.taskId, it)
            taskListener?.notify(EventType.CREATE) { newTask }
            newTask
        }
    }
}
