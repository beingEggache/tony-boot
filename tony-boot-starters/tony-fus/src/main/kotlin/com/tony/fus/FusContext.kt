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
import com.tony.SpringContexts
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.expression.FusExpressionEvaluator
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.handler.CreateTaskHandler
import com.tony.fus.handler.impl.DefaultCreateTaskHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNodeAssignee
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService
import com.tony.utils.ifNull
import com.tony.utils.jsonToObj
import java.util.function.Consumer

/**
 * FusContext is
 * @author tangli
 * @date 2023/10/19 19:35
 * @since 1.0.0
 */
public object FusContext {
    @JvmStatic
    public val processService: ProcessService by SpringContexts.getBeanByLazy<ProcessService>()

    @JvmStatic
    public val queryService: QueryService by SpringContexts.getBeanByLazy<QueryService>()

    @JvmStatic
    public val runtimeService: RuntimeService by SpringContexts.getBeanByLazy<RuntimeService>()

    @JvmStatic
    public val taskService: TaskService by SpringContexts.getBeanByLazy<TaskService>()

    @JvmStatic
    public val expressionEvaluator: FusExpressionEvaluator by SpringContexts.getBeanByLazy<FusExpressionEvaluator>()

    @JvmStatic
    public val taskPermission: FusTaskPermission by SpringContexts.getBeanByLazy<FusTaskPermission>()

    @JvmStatic
    public val interceptors: List<FusInterceptor> by SpringContexts.getBeanListByLazy<FusInterceptor>()

    @JvmStatic
    public val taskActorProvider: FusTaskActorProvider by SpringContexts.getBeanByLazy<FusTaskActorProvider>()

    @JvmStatic
    public val createTaskHandler: CreateTaskHandler = DefaultCreateTaskHandler

    @JvmStatic
    public val processModelParser: FusProcessModelParser = DefaultFusProcessModelParser()

    /**
     * 按id启动实例
     * @param [processId] 流程id
     * @param [userId] 操作人id
     * @param [args] variable
     * @return [FusInstance]?
     * @author Tang Li
     * @date 2023/10/20 19:31
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    public fun startInstanceById(
        processId: String,
        userId: String,
        businessKey: String = "",
        args: Map<String, Any?> = mapOf(),
    ): FusInstance =
        startProcess(
            processService.getById(processId),
            userId,
            businessKey,
            args
        )

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 19:32
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    public fun executeTask(
        taskId: String,
        userId: String,
        args: MutableMap<String, Any?>? = null,
    ) {
        execute(taskId, userId, args ?: mutableMapOf()) {
            it.process.execute(it, it.task?.taskName)
        }
    }

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/10/20 19:33
     * @since 1.0.0
     */
    @JvmStatic
    public fun executeJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
    ) {
        taskService.executeJumpTask(
            taskId,
            nodeName,
            userId
        ) { task ->
            val instance =
                queryService
                    .instance(task.instanceId)
                    .apply {
                        updatorId = userId
                    }
            runtimeService.updateInstance(instance)
            val process = processService.getById(instance.processId)
            FusExecution(process, instance, userId, mutableMapOf())
        }
    }

    private fun startProcess(
        process: FusProcess,
        userId: String,
        businessKey: String,
        args: Map<String, Any?>,
    ): FusInstance =
        process.executeStart(
            userId
        ) { node ->
            FusExecution(
                process,
                runtimeService.createInstance(
                    process.processId,
                    userId,
                    node.nodeName,
                    businessKey,
                    args
                ),
                userId,
                args
            )
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

        val process = processService.getById(instance.processId)

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
                process,
                instance,
                userId,
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
                                taskActorProvider
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
                                ?.let { taskActor ->
                                    FusNodeAssignee(
                                        taskActor.actorId,
                                        taskActor.actorName,
                                        taskActor.weight
                                    )
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
                                actorId = nextNodeAssignee.id
                                actorName = nextNodeAssignee.name
                                actorType = ActorType.USER
                            }
                    createTaskHandler.handle(execution, node)
                    return
                }
        }
        callback.accept(execution)
    }
}
