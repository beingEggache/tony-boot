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

package com.tony.fus

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNodeAssignee
import com.tony.utils.ifNull
import com.tony.utils.jsonToObj
import java.util.function.Consumer

/**
 * FusEngineImpl is
 * @author tangli
 * @date 2023/11/10 19:31
 * @since 1.0.0
 */
public class FusEngineImpl(
    override val context: FusContext,
) : FusEngine {
    override fun startInstanceById(
        processId: String,
        userId: String,
        args: Map<String, Any?>,
    ): FusInstance =
        startProcess(
            processService
                .getById(processId),
            userId,
            args
        )

    override fun executeTask(
        taskId: String,
        userId: String,
        args: MutableMap<String, Any?>?,
    ) {
        execute(taskId, userId, args ?: mutableMapOf()) {
            it.process.execute(context, it, it.task?.taskName)
        }
    }

    override fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
        args: MutableMap<String, Any?>?,
    ) {
        execute(taskId, userId, args ?: mutableMapOf()) { execution ->
            val node =
                execution
                    .process
                    .model()
                    .fusThrowIfNull("当前任务未找到流程定义模型")
                    .getNode(nodeName)
                    .fusThrowIfNull("根据节点名称[$nodeName]无法找到节点模型")
            context.createTask(execution, node)
        }
    }

    private fun startProcess(
        process: FusProcess,
        userId: String,
        args: Map<String, Any?>,
    ): FusInstance {
        val execution =
            FusExecution(
                this,
                process,
                userId,
                runtimeService
                    .createInstance(process.processId, userId, args),
                args
            )
        process.executeStart(context, execution)
        return execution.instance
    }

    private fun execute(
        taskId: String,
        userId: String,
        args: MutableMap<String, Any?>,
        callback: Consumer<FusExecution>,
    ) {
        val task = taskService.complete(taskId, userId, args)
        val instance =
            queryService
                .instance(task.instanceId)
                .fusThrowIfNull("指定的流程实例[id=${task.instanceId}]已完成或不存在.")
                .apply {
                    updatorId = userId
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

        val node = process.model().getNode(taskName)
        if (performType == PerformType.VOTE_SIGN) {
            val taskActors = queryService.listTaskActorsByInstanceId(instanceId)
            val passWeight = node?.passWeight.ifNull(50)
            val voteWeight = 100 - taskActors.sumOf { it.weight }
            if (voteWeight < passWeight) {
                return
            }
            val result = taskService.completeActiveTasksByInstanceId(instanceId, userId)
            fusThrowIf(!result, "Failed to close voting status")
        }

        instance
            .variable
            .takeIf { it.isNotEmpty() }
            ?.also { variableStr ->
                val instanceVariableMap = variableStr.jsonToObj(object : TypeReference<Map<String, Any?>>() {})
                instanceVariableMap.forEach {
                    args.putIfAbsent(it.key, it.value)
                }
            }
        val execution =
            FusExecution(
                this,
                process,
                userId,
                instance,
                args
            ).apply {
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
                                        taskActor.actorId == userId
                                    }?.plus(1)
                            actorList
                                .getOrNull(nextNodeAssigneeIndex ?: 1)
                                ?.let {
                                    FusNodeAssignee(it.actorId, it.actorName)
                                }
                        } else {
                            val nextNodeAssigneeIndex =
                                nodeAssigneeList
                                    .indexOfFirst {
                                        it.id == userId
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
                    context.createTask(execution, node)
                    return
                }
        }
        callback.accept(execution)
    }
}
