package com.tony.fus

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.handler.impl.CreateTaskHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNodeAssignee
import com.tony.fus.model.FusOperator
import com.tony.utils.ifNull
import com.tony.utils.ifNullOrBlank
import com.tony.utils.jsonToObj
import java.time.LocalDateTime
import java.util.function.Consumer

/**
 * FlowEngineImpl is
 * @author tangli
 * @date 2023/11/10 11:31
 * @since 1.0.0
 */
public class FusEngineImpl(
    override var context: FusContext,
) : FusEngine {
    override fun startInstanceById(
        processId: String,
        operator: FusOperator,
        args: Map<String, Any?>
    ): FusInstance? {
        return startProcess(
            processService.getById(processId) ?: return null,
            operator,
            args
        )
    }

    override fun startInstanceByName(
        processName: String,
        processVersion: Int,
        operator: FusOperator,
        args: Map<String, Any?>
    ): FusInstance? =
        startProcess(
            processService.getByVersion(processName, processVersion),
            operator,
            args
        )

    override fun executeTask(
        taskId: String,
        operator: FusOperator,
        args: MutableMap<String, Any?>?,
    ) {
        execute(taskId, operator, args ?: mutableMapOf()) {
            it.process.execute(context, it, it.task?.taskName)
        }
    }

    override fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        operator: FusOperator,
        args: MutableMap<String, Any?>?
    ) {
        execute(taskId, operator, args ?: mutableMapOf()) {
            val model = it.process.model.fusThrowIfNull("当前任务未找到流程定义模型")
            model
                .getNode(nodeName)
                .fusThrowIfNull("根据节点名称[$nodeName]无法找到节点模型")
                .createTask(context, it)
        }
    }

    protected fun startProcess(
        process: FusProcess,
        operator: FusOperator,
        args: Map<String, Any?>
    ): FusInstance? {
        val execution = execute(process, operator, args)
        process.executeStart(context, execution)
        return execution.instance
    }

    protected fun execute(
        process: FusProcess,
        operator: FusOperator,
        args: Map<String, Any?>
    ): FusExecution {
        val instance =
            runtimeService
                .createInstance(process, operator, args)
        return FusExecution(
            this,
            process,
            instance,
            args
        ).apply {
            this.creatorId = operator.operatorId
            this.creatorName = operator.operatorName
        }
    }

    protected fun execute(
        taskId: String,
        operator: FusOperator,
        args: MutableMap<String, Any?>,
        callback: Consumer<FusExecution>,
    ) {
        val task = taskService.complete(taskId, operator, args)
        val instance =
            queryService
                .instance(task.instanceId)
                .fusThrowIfNull("指定的流程实例[id=${task.instanceId}]已完成或不存在.")
                .apply {
                    updatorId = operator.operatorId
                    updatorName = operator.operatorName
                    updateTime = LocalDateTime.now()
                }
        runtimeService.updateInstance(instance)
        val instanceId = instance.instanceId
        val taskName = task.taskName
        val performType = task.performType
        if (performType == PerformType.COUNTERSIGN) {
            val taskList =
                queryService
                    .listTaskByInstanceIdAndTaskName(
                        instanceId,
                        taskName
                    )
            if (taskList.isNotEmpty()) {
                return
            }
        }

        val process =
            processService
                .getById(instance.processId)
                .fusThrowIfNull()

        val node = process.model?.getNode(taskName)
        if (performType == PerformType.VOTE_SIGN) {
            val taskActors = queryService.listTaskActorsByInstanceId(instanceId)
            val passWeight = node?.passWeight.ifNull(50)
            val voteWeight = 100 - taskActors.sumOf { it.weight.ifNull(0) }
            if (voteWeight < passWeight) {
                return
            }
            val result = taskService.completeActiveTasksByInstanceId(instanceId, operator)
            fusThrowIf(!result, "Failed to close voting status")
        }

        instance
            .variable
            .takeIf { !it.isNullOrEmpty() }
            ?.also { variableStr ->
                val instanceVariableMap = variableStr.jsonToObj(object : TypeReference<Map<String, Any?>>() {})
                instanceVariableMap.forEach {
                    args.putIfAbsent(it.key, it.value)
                }
            }
        val execution = FusExecution(this, process, instance, args)
            .apply {
                creatorId = operator.operatorId
                creatorName = operator.operatorName
                this.task = task
            }

        if (performType == PerformType.SORT) {
            val nextNodeAssignee: FusNodeAssignee? =
                node
                    ?.nodeUserList
                    .let { nodeAssigneeList ->
                        if (nodeAssigneeList.isNullOrEmpty()) {
                            val actorList =
                                execution
                                    .taskActorProvider
                                    .listTaskActors(node, execution)
                            val nextNodeAssigneeIndex =
                                actorList
                                    .takeIf {
                                        it.isNotEmpty()
                                    }?.indexOfFirst { taskActor ->
                                        taskActor.actorId == operator.operatorId
                                    }?.plus(1)
                            actorList
                                .getOrNull(nextNodeAssigneeIndex ?: 1)
                                ?.let {
                                    FusNodeAssignee()
                                        .apply {
                                            this.id = it.actorId.ifNullOrBlank()
                                            this.name = it.actorName.ifNullOrBlank()
                                        }
                                }
                        } else {
                            val nextNodeAssigneeIndex =
                                nodeAssigneeList.indexOfFirst {
                                    it.id == operator.operatorId
                                }.plus(1)
                            nodeAssigneeList.getOrNull(nextNodeAssigneeIndex)
                        }
                    }
            nextNodeAssignee
                ?.also {
                    execution.nextTaskActor =
                        FusTaskActor()
                            .apply {
                                actorId = it.id
                                actorName = it.name
                                actorType = ActorType.USER
                            }
                    CreateTaskHandler(node).handle(context, execution)
                    return
                }
            callback.accept(execution)
        }
    }
}
