package com.tony.flow

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.flow.db.enums.ActorType
import com.tony.flow.db.enums.PerformType
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowProcess
import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.extension.flowThrowIf
import com.tony.flow.extension.flowThrowIfNull
import com.tony.flow.handler.impl.CreateTaskHandler
import com.tony.flow.model.FlowExecution
import com.tony.flow.model.FlowNodeAssignee
import com.tony.flow.model.FlowOperator
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
public class FlowEngineImpl(
    override var context: FlowContext,
) : FlowEngine {
    override fun startInstanceById(
        flowProcessId: String,
        flowOperator: FlowOperator,
        args: Map<String, Any?>
    ): FlowInstance? {
        return startProcess(
            processService.getById(flowProcessId) ?: return null,
            flowOperator,
            args
        )
    }

    override fun startInstanceByName(
        flowProcessName: String,
        processVersion: Int,
        flowOperator: FlowOperator,
        args: Map<String, Any?>
    ): FlowInstance? =
        startProcess(
            processService.getByVersion(flowProcessName, processVersion),
            flowOperator,
            args
        )

    override fun executeTask(
        taskId: String,
        flowOperator: FlowOperator,
        args: MutableMap<String, Any?>?,
    ) {
        execute(taskId, flowOperator, args ?: mutableMapOf()) {
            it.flowProcess.execute(context, it, it.flowTask?.taskName)
        }
    }

    override fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        flowOperator: FlowOperator,
        args: MutableMap<String, Any?>?
    ) {
        execute(taskId, flowOperator, args ?: mutableMapOf()) {
            val model = it.flowProcess.model.flowThrowIfNull("当前任务未找到流程定义模型")
            model
                .getNode(nodeName)
                .flowThrowIfNull("根据节点名称[$nodeName]无法找到节点模型")
                .createTask(context, it)
        }
    }

    protected fun startProcess(
        flowProcess: FlowProcess,
        flowOperator: FlowOperator,
        args: Map<String, Any?>
    ): FlowInstance? {
        val flowExecution = execute(flowProcess, flowOperator, args)
        flowProcess.executeStart(context, flowExecution)
        return flowExecution.flowInstance
    }

    protected fun execute(
        flowProcess: FlowProcess,
        flowOperator: FlowOperator,
        args: Map<String, Any?>
    ): FlowExecution {
        val flowInstance =
            runtimeService
                .createInstance(flowProcess, flowOperator, args)
        return FlowExecution(
            this,
            flowProcess,
            flowInstance,
            args
        ).apply {
            this.creatorId = flowOperator.operatorId
            this.creatorName = flowOperator.operatorName
        }
    }

    protected fun execute(
        taskId: String,
        flowOperator: FlowOperator,
        args: MutableMap<String, Any?>,
        callback: Consumer<FlowExecution>,
    ) {
        val flowTask = taskService.complete(taskId, flowOperator, args)
        val flowInstance =
            queryService
                .instance(flowTask.instanceId)
                .flowThrowIfNull("指定的流程实例[id=${flowTask.instanceId}]已完成或不存在.")
                .apply {
                    updatorId = flowOperator.operatorId
                    updatorName = flowOperator.operatorName
                    updateTime = LocalDateTime.now()
                }
        runtimeService.updateInstance(flowInstance)
        val instanceId = flowInstance.instanceId
        val taskName = flowTask.taskName
        val performType = flowTask.performType
        if (performType == PerformType.COUNTERSIGN) {
            val flowTaskList =
                queryService
                    .listTaskByInstanceIdAndTaskName(
                        instanceId,
                        taskName
                    )
            if (flowTaskList.isNotEmpty()) {
                return
            }
        }

        val flowProcess =
            processService
                .getById(flowInstance.processId)
                .flowThrowIfNull()

        val flowNode = flowProcess.model?.getNode(taskName)
        if (performType == PerformType.VOTE_SIGN) {
            val taskActors = queryService.listTaskActorsByInstanceId(instanceId)
            val passWeight = flowNode?.passWeight.ifNull(50)
            val voteWeight = 100 - taskActors.sumOf { it.weight.ifNull(0) }
            if (voteWeight < passWeight) {
                return
            }
            val result = taskService.completeActiveTasksByInstanceId(instanceId, flowOperator)
            flowThrowIf(!result, "Failed to close voting status")
        }

        flowInstance
            .variable
            .takeIf { !it.isNullOrEmpty() }
            ?.also { variableStr ->
                val instanceVariableMap = variableStr.jsonToObj(object : TypeReference<Map<String, Any?>>() {})
                instanceVariableMap.forEach {
                    args.putIfAbsent(it.key, it.value)
                }
            }
        val flowExecution = FlowExecution(this, flowProcess, flowInstance, args)
            .apply {
                creatorId = flowOperator.operatorId
                creatorName = flowOperator.operatorName
                this.flowTask = flowTask
            }

        if (performType == PerformType.SORT) {
            val nextNodeAssignee: FlowNodeAssignee? =
                flowNode
                    ?.nodeUserList
                    .let { nodeAssigneeList ->
                        if (nodeAssigneeList.isNullOrEmpty()) {
                            val actorList =
                                flowExecution
                                    .taskActorProvider
                                    .listTaskActors(flowNode, flowExecution)
                            val nextNodeAssigneeIndex =
                                actorList
                                    .takeIf {
                                        it.isNotEmpty()
                                    }?.indexOfFirst { taskActor ->
                                        taskActor.actorId == flowOperator.operatorId
                                    }?.plus(1)
                            actorList
                                .getOrNull(nextNodeAssigneeIndex ?: 1)
                                ?.let {
                                    FlowNodeAssignee()
                                        .apply {
                                            this.id = it.actorId.ifNullOrBlank()
                                            this.name = it.actorName.ifNullOrBlank()
                                        }
                                }
                        } else {
                            val nextNodeAssigneeIndex =
                                nodeAssigneeList.indexOfFirst {
                                    it.id == flowOperator.operatorId
                                }.plus(1)
                            nodeAssigneeList.getOrNull(nextNodeAssigneeIndex)
                        }
                    }
            nextNodeAssignee
                ?.also {
                    flowExecution.nextFlowTaskActor =
                        FlowTaskActor()
                            .apply {
                                actorId = it.id
                                actorName = it.name
                                actorType = ActorType.USER
                            }
                    CreateTaskHandler(flowNode).handle(context, flowExecution)
                    return
                }
            callback.accept(flowExecution)
        }
    }
}
