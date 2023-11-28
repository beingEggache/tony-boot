package com.tony.fus.service.impl

import com.tony.fus.FusTaskPermission
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.enums.TaskState
import com.tony.fus.db.enums.TaskType
import com.tony.fus.db.mapper.FusHistoryTaskActorMapper
import com.tony.fus.db.mapper.FusHistoryTaskMapper
import com.tony.fus.db.mapper.FusTaskActorMapper
import com.tony.fus.db.mapper.FusTaskCcMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.db.po.FusHistoryTask
import com.tony.fus.db.po.FusHistoryTaskActor
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.db.po.FusTaskCc
import com.tony.fus.extension.fusListThrowIfEmpty
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.extension.fusThrowIfEmpty
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.extension.fusThrowIfNullOrEmpty
import com.tony.fus.extension.ofPerformType
import com.tony.fus.listener.TaskListener
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.enums.EventType
import com.tony.fus.model.enums.NodeType
import com.tony.fus.service.TaskService
import com.tony.utils.copyToNotNull
import com.tony.utils.ifNullOrBlank
import com.tony.utils.runIf
import com.tony.utils.toJsonString
import java.time.LocalDateTime
import java.util.function.Consumer
import org.springframework.transaction.annotation.Transactional

/**
 * TaskServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal open class TaskServiceImpl
    @JvmOverloads
    constructor(
        private val taskPermission: FusTaskPermission,
        private val taskMapper: FusTaskMapper,
        private val taskActorMapper: FusTaskActorMapper,
        private val taskCcMapper: FusTaskCcMapper,
        private val historyTaskMapper: FusHistoryTaskMapper,
        private val historyTaskActorMapper: FusHistoryTaskActorMapper,
        private val taskListener: TaskListener? = null,
    ) : TaskService {
        @Transactional(rollbackFor = [Throwable::class])
        override fun execute(
            taskId: String,
            userId: String,
            taskState: TaskState,
            eventType: EventType,
            variable: Map<String, Any?>?,
        ): FusTask {
            val task = taskMapper.fusSelectByIdNotNull(taskId, "指定的任务不存在")
            task.variable = variable.toJsonString().ifNullOrBlank("{}")

            fusThrowIf(
                !hasPermission(task, userId),
                "当前参与者 [$userId}]不允许执行任务[taskId=$taskId]"
            )

            moveToHistoryTask(task, taskState, userId)
            taskListener?.notify(eventType, task)
            return task
        }

        @Transactional(rollbackFor = [Throwable::class])
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
            taskListener?.notify(EventType.UPDATE, task)
        }

        override fun viewTask(
            taskId: String,
            actorId: String,
        ): Boolean =
            taskActorMapper
                .ktQuery()
                .eq(FusTaskActor::actorId, actorId)
                .exists()
                .runIf {
                    taskMapper
                        .ktUpdate()
                        .eq(FusTask::taskId, taskId)
                        .set(FusTask::viewed, true)
                        .update()
                } ?: false

        @Transactional(rollbackFor = [Throwable::class])
        override fun taskExpired(taskId: String): Boolean {
            taskMapper
                .selectById(taskId)
                .takeIf { it != null }
                ?.also {
                    it.copyToNotNull(FusHistoryTask()).apply {
                        this.finishTime = LocalDateTime.now()
                        this.taskState = TaskState.EXPIRED
                        historyTaskMapper.insert(this)
                        taskActorMapper.deleteByTaskId(taskId)
                        taskMapper.deleteById(taskId)

                        taskListener?.notify(EventType.EXPIRED, it)
                    }
                }
            return true
        }

        override fun claimTask(
            taskId: String,
            actorId: String,
        ): FusTask =
            taskMapper
                .fusSelectByIdNotNull("任务不存在(id=$taskId)")
                .also {
                    hasPermission(it, actorId)
                }

        @Transactional(rollbackFor = [Throwable::class])
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
                taskMapper
                    .ktUpdate()
                    .eq(FusTask::taskId, taskId)
                    .set(FusTask::taskType, taskType)
                    .set(FusTask::assignorId, taskActor.actorId)
                    .set(FusTask::assignorName, taskActor.actorName)
                    .update()

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

        @Transactional(rollbackFor = [Throwable::class])
        override fun reclaimTask(
            taskId: String,
            userId: String,
        ): FusTask =
            undoHistoryTask(
                taskId,
                userId
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

        @Transactional(rollbackFor = [Throwable::class])
        override fun withdrawTask(
            taskId: String,
            userId: String,
        ): FusTask =
            undoHistoryTask(
                taskId,
                userId
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

        @Transactional(rollbackFor = [Throwable::class])
        override fun rejectTask(
            task: FusTask,
            userId: String,
            variable: Map<String, Any?>?,
        ): FusTask {
            val parentTaskId =
                task.parentTaskId.fusThrowIfNullOrEmpty("上一步任务ID为空，无法驳回至上一步处理")
            execute(task.taskId, userId, TaskState.REJECTED, EventType.REJECTED, variable)
            return undoHistoryTask(parentTaskId, userId)
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

        @Transactional(rollbackFor = [Throwable::class])
        override fun createTask(
            node: FusNode?,
            execution: FusExecution,
        ): List<FusTask> {
            val nodeType = node?.nodeType
            val task =
                FusTask().apply {
                    this.creatorId = execution.userId
                    this.instanceId = execution.instance.instanceId
                    this.taskName = node?.nodeName.ifNullOrBlank()
                    // ?
                    this.taskType = TaskType.create(nodeType?.value!!).fusThrowIfNull("nodeType null")
                    this.parentTaskId = execution.task?.taskId.ifNullOrBlank()
                }

            val taskActorList = execution.taskActorProvider.listTaskActors(node, execution)
            if (nodeType == NodeType.INITIATOR || nodeType == NodeType.APPROVER) {
                return saveTask(
                    task,
                    // ?
                    node.multiApproveMode.ofPerformType(),
                    taskActorList,
                    execution
                )
            }

            if (nodeType == NodeType.CC) {
                saveTaskCc(node, execution)
                val nextNode = node.childNode ?: FusNode.nextNode(node)
                return nextNode?.run {
                    createTask(nextNode, execution)
                } ?: emptyList()
            }
            if (nodeType == NodeType.CONDITIONAL_APPROVE) {
                val newTask =
                    task
                        .copyToNotNull(FusTask())
                        .apply {
                            taskId = ""
                        }
                return saveTask(
                    newTask,
                    node.multiApproveMode.ofPerformType(),
                    taskActorList,
                    execution
                )
            }
            return emptyList()
        }

        @Transactional(rollbackFor = [Throwable::class])
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
                .map {
                    FusTaskCc().apply {
                        this.creatorId = execution.userId
                        this.instanceId = execution.instance.instanceId
                        this.parentTaskId = parentTaskId.ifNullOrBlank()
                        this.taskName = node.nodeName
                        this.actorId = it.id
                        this.actorName = it.name
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

        @Transactional(rollbackFor = [Throwable::class])
        override fun addTaskActor(
            taskId: String,
            performType: PerformType,
            historyTaskActorList: List<FusHistoryTaskActor>,
        ): Boolean {
            val task = taskMapper.fusSelectByIdNotNull(taskId)
            val actorIdSet =
                taskActorMapper
                    .selectListByTaskId(taskId)
                    .fusThrowIfEmpty()
                    .associateBy { it.actorId }
                    .keys

            historyTaskActorList
                .filter {
                    !actorIdSet.contains(it.actorId)
                }.forEach {
                    assignTask(task.instanceId, taskId, it)
                }

            return taskMapper
                .ktUpdate()
                .eq(FusTask::taskId, taskId)
                .set(FusTask::performType, performType)
                .update()
        }

        @Transactional(rollbackFor = [Throwable::class])
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

        @Transactional(rollbackFor = [Throwable::class])
        override fun cascadeRemoveByInstanceId(instanceId: String) {
            historyTaskMapper
                .ktQuery()
                .select(FusHistoryTask::taskId)
                .eq(FusHistoryTask::instanceId, instanceId)
                .listObj<String>()
                .filterNotNull()
                .takeIf { it.isNotEmpty() }
                ?.also { historyTaskIdList ->
                    historyTaskActorMapper.deleteByTaskIds(historyTaskIdList)
                    historyTaskMapper
                        .ktUpdate()
                        .eq(FusHistoryTask::instanceId, instanceId)
                        .remove()
                }

            taskMapper
                .ktQuery()
                .select(FusTask::taskId)
                .eq(FusTask::instanceId, instanceId)
                .listObj<String>()
                .filterNotNull()
                .takeIf { it.isNotEmpty() }
                ?.also { taskIdList ->
                    taskActorMapper.deleteByTaskIds(taskIdList)
                    taskMapper
                        .ktUpdate()
                        .eq(FusTask::instanceId, instanceId)
                        .remove()
                }
            // TODO 删除任务抄送
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
                        finishTime = LocalDateTime.now()
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
                        !taskActorMapper.deleteByTaskId(task.taskId),
                        "Delete FusTaskActor table failed"
                    )
                }?.forEach {
                    fusThrowIf(
                        (
                            historyTaskActorMapper
                                .insert(it.copyToNotNull(FusHistoryTaskActor())) <= 0
                        ),
                        "Migration to FusHistoryTaskActor table failed"
                    )
                }
            return taskMapper.deleteById(task) > 0
        }

        private fun undoHistoryTask(
            historyTaskId: String,
            userId: String,
            callback: Consumer<FusHistoryTask>? = null,
        ): FusTask {
            val historyTask =
                historyTaskMapper
                    .fusSelectByIdNotNull(historyTaskId, "指定的任务不存在")
            callback?.accept(historyTask)
            val task =
                historyTask.copyToNotNull(FusTask()).apply {
                    taskId = ""
                    creatorId = userId
                }
            taskMapper.insert(task)
            historyTaskActorMapper
                .ktQuery()
                .eq(FusHistoryTaskActor::taskId, historyTaskId)
                .list()
                .map {
                    FusTaskActor().apply {
                        tenantId = it.tenantId
                        instanceId = it.instanceId
                        taskId = task.taskId
                        actorType = it.actorType
                        actorId = it.actorId
                        actorName = it.actorName
                    }
                }.takeIf {
                    it.isNotEmpty()
                }?.also {
                    taskActorMapper.insertBatch(it)
                }
            return task
        }

        private fun saveTask(
            task: FusTask,
            performType: PerformType,
            taskActorList: Collection<FusTaskActor>,
            execution: FusExecution,
        ): List<FusTask> {
            task.performType = performType
            if (performType == PerformType.UNKNOWN) {
                task.variable =
                    execution
                        .variable
                        .toJsonString()
                        .ifNullOrBlank("{}")
                taskMapper.insert(task)

                taskActorList
                    .forEach {
                        assignTask(task.instanceId, task.taskId, it)
                    }
                return listOf(task)
            }

            taskActorList.fusThrowIfEmpty("任务参与者不能为空")
            if (performType == PerformType.OR_SIGN) {
                taskMapper.insert(task)
                taskActorList.forEach {
                    assignTask(task.instanceId, task.taskId, it)
                }
                taskListener?.notify(EventType.CREATE, task)
                return listOf(task)
            }

            if (performType == PerformType.SORT) {
                taskMapper.insert(task)
                assignTask(
                    task.instanceId,
                    task.taskId,
                    execution.nextTaskActor ?: taskActorList.first()
                )
                taskListener?.notify(EventType.CREATE, task)
                return listOf(task)
            }

            return taskActorList.map {
                val newTask = task.copyToNotNull(FusTask()).apply { taskId = "" }
                taskMapper.insert(newTask)
                assignTask(newTask.instanceId, newTask.taskId, it)
                taskListener?.notify(EventType.CREATE, newTask)
                newTask
            }
        }
    }
