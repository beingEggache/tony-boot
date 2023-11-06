package com.tony.flow.service.impl

import com.tony.flow.TaskPermission
import com.tony.flow.db.enums.PerformType
import com.tony.flow.db.enums.TaskState
import com.tony.flow.db.enums.TaskType
import com.tony.flow.db.mapper.FlowHistoryTaskActorMapper
import com.tony.flow.db.mapper.FlowHistoryTaskMapper
import com.tony.flow.db.mapper.FlowInstanceMapper
import com.tony.flow.db.mapper.FlowProcessMapper
import com.tony.flow.db.mapper.FlowTaskActorMapper
import com.tony.flow.db.mapper.FlowTaskCcMapper
import com.tony.flow.db.mapper.FlowTaskMapper
import com.tony.flow.db.po.FlowHistoryTask
import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.exception.FlowException
import com.tony.flow.extension.flowListThrowIfNullOrEmpty
import com.tony.flow.extension.flowSelectByIdNotNull
import com.tony.flow.extension.flowThrowIf
import com.tony.flow.extension.flowThrowIfNull
import com.tony.flow.extension.flowThrowIfNullOrEmpty
import com.tony.flow.model.FlowExecution
import com.tony.flow.model.FlowNode
import com.tony.flow.model.FlowOperator
import com.tony.flow.model.enums.EventType
import com.tony.flow.service.TaskService
import com.tony.utils.copyToNotNull
import com.tony.utils.defaultIfBlank
import com.tony.utils.throwIfNull
import com.tony.utils.throwIfNullOrEmpty
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
internal open class TaskServiceImpl(
    private val taskPermission: TaskPermission,
    private val flowProcessMapper: FlowProcessMapper,
    private val flowInstanceMapper: FlowInstanceMapper,
    private val flowTaskMapper: FlowTaskMapper,
    private val flowTaskActorMapper: FlowTaskActorMapper,
    private val flowTaskCcMapper: FlowTaskCcMapper,
    private val flowHistoryTaskMapper: FlowHistoryTaskMapper,
    private val flowHistoryTaskActorMapper: FlowHistoryTaskActorMapper,
) : TaskService {
    override fun complete(
        taskId: String?,
        flowOperator: FlowOperator,
        variable: Map<String, Any?>?,
    ): FlowTask =
        execute(taskId.throwIfNull(), flowOperator, TaskState.COMPLETE, EventType.COMPLETE, variable)

    override fun updateTaskById(flowTask: FlowTask) {
        flowTaskMapper.updateById(flowTask)
        // TODO notify
    }

    override fun viewTask(
        taskId: String,
        taskActor: FlowTaskActor,
    ): Boolean =
        flowTaskActorMapper
            .ktQuery()
            .eq(FlowTaskActor::actorId, taskActor.actorId)
            .exists()
            .takeIf { it }
            ?.run {
                flowTaskMapper.updateById(
                    FlowTask().apply {
                        this.taskId = taskId
                        this.viewed = true
                    }
                ) > 0
            } ?: false

    @Transactional(rollbackFor = [Throwable::class])
    override fun taskExpired(taskId: String): Boolean {
        flowTaskMapper
            .selectById(taskId)
            .takeIf { it != null }
            ?.also {
                it.copyToNotNull(FlowHistoryTask()).apply {
                    this.finishTime = LocalDateTime.now()
                    this.taskState = TaskState.EXPIRED
                    flowHistoryTaskMapper.insert(this)
                    flowTaskActorMapper.deleteByTaskId(taskId)
                    flowTaskMapper.deleteById(taskId)

                    // TODO notify
                }
            }
        return true
    }

    override fun claimTask(
        taskId: String,
        flowHistoryTaskActor: FlowHistoryTaskActor,
    ): FlowTask {
        return flowTaskMapper
            .flowSelectByIdNotNull("任务不存在(id=$taskId)")
            .also {
                hasPermission(it, flowHistoryTaskActor.actorId)
            }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun assignTask(
        taskId: String,
        taskType: TaskType,
        flowTaskActor: FlowTaskActor,
        assignee: FlowTaskActor,
    ): Boolean {
        val flowTaskActors =
            flowTaskActorMapper
                .ktQuery()
                .eq(FlowTaskActor::taskId, taskId)
                .eq(FlowTaskActor::actorId, flowTaskActor.actorId)
                .flowListThrowIfNullOrEmpty("无权转办该任务.")

        val flowTask =
            FlowTask().apply {
                this.taskId = taskId
                this.taskType = taskType
                this.assignorId = flowTaskActor.actorId
                this.assignorName = flowTaskActor.actorName
            }
        flowTaskMapper.updateById(flowTask)
        flowTaskActorMapper.deleteBatchIds(flowTaskActors.map { it.taskActorId })
        assignTask(flowTaskActors.first().instanceId, taskId, assignee)

        return true
    }

    protected fun assignTask(
        instanceId: String?,
        taskId: String?,
        flowTaskActor: FlowTaskActor,
    ) {
        flowTaskActor.taskActorId = null
        flowTaskActor.instanceId = instanceId
        flowTaskActor.taskId = taskId
        flowTaskActorMapper.insert(flowTaskActor)
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun reclaimTask(
        taskId: String,
        flowOperator: FlowOperator,
    ): FlowTask =
        undoHistoryTask(
            taskId,
            flowOperator
        ) { flowHistoryTask ->
            flowTaskMapper
                .selectListByInstanceId(flowHistoryTask.instanceId)
                .mapNotNull {
                    it.taskId
                }.also { taskIdList ->
                    flowTaskMapper.deleteBatchIds(taskIdList)
                    flowTaskActorMapper.deleteByTaskIds(taskIdList)
                }
        }

    override fun withdrawTask(
        taskId: String,
        flowOperator: FlowOperator,
    ): FlowTask? =
        undoHistoryTask(
            taskId,
            flowOperator
        ) { flowHistoryTask ->
            val flowTaskIdList =
                if (flowHistoryTask.performType == PerformType.COUNTERSIGN) {
                    flowTaskMapper
                        .ktQuery()
                        .eq(FlowTask::parentTaskId, flowHistoryTask.taskId)
                        .list()
                } else {
                    val flowTasks =
                        flowHistoryTaskMapper
                            .ktQuery()
                            .select(FlowHistoryTask::taskId)
                            .eq(FlowHistoryTask::instanceId, flowHistoryTask.instanceId)
                            .eq(FlowHistoryTask::taskName, flowHistoryTask::taskName)
                            .eq(FlowHistoryTask::parentTaskId, flowHistoryTask.parentTaskId)
                            .listObj<String>()
                            .let { flowHistoryTaskIdList ->
                                flowTaskMapper.selectBatchIds(flowHistoryTaskIdList)
                            }
                    flowTasks
                }.flowThrowIfNullOrEmpty("后续活动任务已完成或不存在，无法撤回.")
                    .map { it.taskId }

            flowTaskActorMapper
                .ktQuery()
                .`in`(FlowTaskActor::taskId, flowTaskIdList)
                .list()
                .takeIf { it.isNotEmpty() }
                ?.also { flowTaskActorMapper.deleteBatchIds(it) }

            flowTaskMapper.deleteBatchIds(flowTaskIdList)
        }

    override fun rejectTask(
        flowTask: FlowTask,
        flowOperator: FlowOperator,
        variable: Map<String, Any?>?,
    ): FlowTask {
        val parentTaskId =
            flowTask.parentTaskId.throwIfNullOrEmpty("上一步任务ID为空，无法驳回至上一步处理", ex = ::FlowException)
        execute(flowTask.taskId.defaultIfBlank(), flowOperator, TaskState.REJECT, EventType.REJECT, variable)
        return undoHistoryTask(parentTaskId, flowOperator)
    }

    override fun hasPermission(
        flowTask: FlowTask,
        userId: String?,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun createTask(
        flowNode: FlowNode?,
        flowExecution: FlowExecution,
    ): List<FlowTask> {

        FlowTask().apply {
            this.creatorId = flowExecution.creatorId
            this.creatorName = flowExecution.creatorName
            this.createTime = LocalDateTime.now()
            this.instanceId = flowExecution.flowInstance?.instanceId
            this.taskName = flowNode?.nodeName
            this.displayName = flowNode?.nodeName
            // TODO ?
            this.taskType = TaskType.create(flowNode?.nodeType?.value!!)
        }

        TODO("Not yet implemented")
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun createNewTask(
        taskId: String,
        taskType: TaskType,
        taskActors: Collection<FlowTaskActor>,
    ): List<FlowTask> {
        taskActors.flowThrowIfNullOrEmpty()
        return flowTaskMapper
            .flowSelectByIdNotNull(taskId)
            .copyToNotNull(FlowTask())
            .apply {
                this.taskType = taskType
                this.parentTaskId = taskId
            }.let {
                saveTask(it, PerformType.SORT, taskActors)
            }
    }

    override fun listExpiredOrRemindTasks(): List<FlowTask> =
        LocalDateTime.now().let { now ->
            flowTaskMapper
                .ktQuery()
                .le(FlowTask::expireTime, now)
                .or()
                .le(FlowTask::remindTime, now)
                .list()
        }


    override fun getTaskNode(taskId: String): FlowNode =
        flowTaskMapper
            .flowSelectByIdNotNull(taskId)
            .let {
                flowInstanceMapper.flowSelectByIdNotNull(it.instanceId)
            }.let {
                flowProcessMapper.flowSelectByIdNotNull(it.processId)
            }.model
            ?.flowNode
            .flowThrowIfNull("任务ID无法找到节点模型.")

    @Transactional(rollbackFor = [Throwable::class])
    override fun addTaskActor(
        taskId: String,
        performType: PerformType,
        flowHistoryTaskActors: List<FlowHistoryTaskActor>,
    ): Boolean {
        val flowTask = flowTaskMapper.flowSelectByIdNotNull(taskId)
        val actorIdSet = flowTaskActorMapper
            .selectListByTaskId(taskId)
            .flowThrowIfNullOrEmpty()
            .associateBy { it.actorId }
            .keys

        flowHistoryTaskActors
            .filter {
                !actorIdSet.contains(it.actorId)
            }.forEach {
                assignTask(flowTask.instanceId, taskId, it)
            }
        return flowTaskMapper.updateById(FlowTask().apply {
            this.taskId = taskId
            this.performType = performType
        }) > 0
    }

    override fun removeTaskActor(
        taskId: String,
        taskActorIds: Collection<String>,
    ): Unit {

        val flowTaskActorList = flowTaskActorMapper
            .selectListByTaskId(taskId)
            .flowThrowIfNullOrEmpty()
        flowThrowIf(flowTaskActorList.size == taskActorIds.size, "illegal")
        flowTaskActorMapper
            .ktUpdate()
            .eq(FlowTaskActor::taskId, taskId)
            .`in`(FlowTaskActor::actorId, taskActorIds)
            .remove()
    }

    override fun cascadeRemoveByInstanceId(instanceId: String) {
        TODO("Not yet implemented")
    }

    protected fun execute(
        taskId: String,
        flowOperator: FlowOperator,
        taskState: TaskState,
        eventType: EventType,
        variable: Map<String, Any?>?,
    ): FlowTask {
        val flowTask = flowTaskMapper.flowSelectByIdNotNull(taskId, "指定的任务不存在")
        flowTask.variable = variable.toJsonString().defaultIfBlank("{}")
        val flowHistoryTask =
            flowTask.copyToNotNull(FlowHistoryTask()).apply {
                finishTime = LocalDateTime.now()
                this.taskState = taskState
                this.creatorId = flowOperator.operatorId
                this.creatorName = flowOperator.operatorName
            }
        flowHistoryTaskMapper.insert(flowHistoryTask)
        flowTaskActorMapper
            .selectListByTaskId(taskId)
            .map {
                it.copyToNotNull(FlowHistoryTaskActor())
            }.apply {
                flowHistoryTaskActorMapper.insertBatch(this)
            }
        flowTaskActorMapper.deleteByTaskId(taskId)
        flowTaskMapper.deleteById(taskId)

        // TODO notify
        return flowTask
    }

    protected fun undoHistoryTask(
        flowHistoryTaskId: String,
        flowOperator: FlowOperator,
        callback: Consumer<FlowHistoryTask>? = null,
    ): FlowTask {
        val flowHistoryTask = flowHistoryTaskMapper.flowSelectByIdNotNull(flowHistoryTaskId, "指定的任务不存在")
        callback?.accept(flowHistoryTask)
        val flowTask = flowHistoryTask.undo(flowOperator)
        flowTaskMapper.insert(flowTask)
        flowHistoryTaskActorMapper
            .selectListByTaskId(flowHistoryTaskId)
            .map {
                FlowTaskActor().apply {
                    tenantId = it.tenantId
                    instanceId = it.instanceId
                    taskId = it.taskId
                    actorType = it.actorType
                    actorId = it.actorId
                    actorName = it.actorName
                }
            }.takeIf {
                it.isNotEmpty()
            }?.also {
                flowTaskActorMapper.insertBatch(it)
            }
        return flowTask
    }

    protected fun saveTask(
        flowTask: FlowTask,
        performType: PerformType,
        flowTaskActorList: Collection<FlowTaskActor>,
        flowExecution: FlowExecution? = null,
    ): List<FlowTask> {
        flowTask.performType = performType
        if (performType == PerformType.UNKNOWN) {
            flowTask.variable =
                flowExecution
                    ?.variable
                    .toJsonString()
                    .defaultIfBlank("{}")
            flowTaskMapper.insert(flowTask)

            flowTaskActorList
                .forEach {
                    assignTask(flowTask.instanceId, flowTask.taskId, it)
                }
            return listOf(flowTask)
        }

        flowTaskActorList.flowThrowIfNullOrEmpty("任务参与者不能为空")
        if (performType == PerformType.OR_SIGN) {
            flowTaskMapper.insert(flowTask)
            flowTaskActorList.forEach {
                assignTask(flowTask.instanceId, flowTask.taskId, it)
            }
            //TODO notify
            return listOf(flowTask)
        }

        if (performType == PerformType.SORT) {
            flowTaskMapper.insert(flowTask)
            assignTask(
                flowTask.instanceId,
                flowTask.taskId,
                flowExecution?.nextFlowTaskActor ?: flowTaskActorList.first()
            )
            //TODO notify
            return listOf(flowTask)
        }

        return flowTaskActorList.map {
            val newFlowTask = FlowTask()
            flowTaskMapper.insert(newFlowTask)
            assignTask(newFlowTask.instanceId, newFlowTask.taskId, it)
            //TODO notify
            it.copyToNotNull(newFlowTask)
        }
    }
}
