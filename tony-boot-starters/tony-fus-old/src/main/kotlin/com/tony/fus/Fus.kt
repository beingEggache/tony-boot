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
import com.tony.fus.db.enums.TaskType
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.extension.fusThrowIfEmpty
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.extension.fusThrowIfTrue
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.FusNode.Companion.hasDuplicateNodeNames
import com.tony.fus.model.FusNodeAssignee
import com.tony.fus.model.enums.NodeType
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.RuntimeServiceImpl
import com.tony.fus.service.TaskService
import com.tony.fus.service.TaskServiceImpl
import com.tony.utils.applyIf
import com.tony.utils.asToNotNull
import com.tony.utils.ifNull
import com.tony.utils.jsonToObj
import java.util.function.Consumer

/**
 * FusContext is
 * @author tangli
 * @date 2023/10/19 19:35
 * @since 1.0.0
 */
public data object Fus {
    @get:JvmName("processService")
    @JvmStatic
    public val processService: ProcessService by SpringContexts.getBeanByLazy<ProcessService>()

    @get:JvmName("queryService")
    @JvmStatic
    public val queryService: QueryService by SpringContexts.getBeanByLazy<QueryService>()

    @get:JvmName("runtimeService")
    @JvmStatic
    public val runtimeService: RuntimeService by SpringContexts.getBeanByLazy<RuntimeService>()

    @get:JvmName("taskService")
    @JvmStatic
    public val taskService: TaskService by SpringContexts.getBeanByLazy<TaskService>()

    @get:JvmSynthetic
    @JvmStatic
    internal val interceptors: List<FusInterceptor> by SpringContexts.getBeanListByLazy<FusInterceptor>()

    @get:JvmSynthetic
    @JvmStatic
    internal val taskActorProvider: FusTaskActorProvider by SpringContexts.getBeanByLazy<FusTaskActorProvider>()

    @get:JvmSynthetic
    @JvmStatic
    internal val conditionVariableHandler: FusConditionVariableHandler by SpringContexts
        .getBeanByLazy<FusConditionVariableHandler>()

    private fun startProcess(
        process: FusProcess,
        userId: String,
        args: Map<String, Any?>,
        instance: FusInstance,
    ): FusInstance =
        process
            .model()
            .node
            .fusThrowIfNull("流程定义[processName=${process.processName}, processVersion=${process.processVersion}]没有开始节点")
            .let { node ->
                (!taskActorProvider.hasPermission(node, userId))
                    .fusThrowIfTrue("No permission to execute")
                (node.hasDuplicateNodeNames())
                    .fusThrowIfTrue("There are duplicate node names present")

                val execution =
                    FusExecution(
                        process.model(),
                        runtimeService
                            .asToNotNull<RuntimeServiceImpl>()
                            .createInstance(
                                process,
                                userId,
                                node.nodeName,
                                args,
                                instance
                            ),
                        userId,
                        args
                    )
                taskService
                    .asToNotNull<TaskServiceImpl>()
                    .createTask(node, execution)
                interceptors
                    .forEach { interceptor ->
                        interceptor.handle(execution)
                    }
                execution.instance
            }

    /**
     * 按id启动实例
     * @param [processId] 流程id
     * @param [userId] 操作人id
     * @param [businessKey] 业务KEY
     * @param [args] variable
     * @return [FusInstance]?
     * @author tangli
     * @date 2023/10/20 19:31
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun startProcessById(
        processId: String,
        userId: String,
        args: Map<String, Any?> = mapOf(),
        businessKey: String = "",
    ): FusInstance =
        startProcess(
            processService.getById(processId),
            userId,
            args,
            FusInstance().apply {
                this.businessKey = businessKey
            }
        )

    /**
     * 按唯一标识[processKey]启动进程
     * @param [processKey] 唯一标识
     * @param [userId] 操作人id
     * @param [businessKey] 业务KEY
     * @param [args] variable
     * @param [version] 流程版本
     * @return [FusInstance]
     * @author tangli
     * @date 2024/01/17 10:40
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun startProcessByKey(
        processKey: String,
        userId: String,
        args: Map<String, Any?> = mapOf(),
        businessKey: String = "",
        version: Int? = null,
    ): FusInstance =
        startProcess(
            processService.getByKey(processKey, version),
            userId,
            args,
            FusInstance().apply {
                this.businessKey = businessKey
            }
        )

    /**
     * 按唯一标识[processKey]启动进程
     * @param [processKey] 唯一标识
     * @param [userId] 操作人id
     * @param [businessKey] 业务KEY
     * @param [args] variable
     * @param [version] 流程版本
     * @return [FusInstance]
     * @author tangli
     * @date 2024/01/17 10:40
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmOverloads
    @JvmStatic
    internal fun internalStartProcessByKey(
        processKey: String,
        userId: String,
        args: Map<String, Any?> = mapOf(),
        businessKey: String = "",
        version: Int? = null,
        consumer: Consumer<FusInstance>? = null,
    ): FusInstance =
        startProcess(
            processService.getByKey(processKey, version),
            userId,
            args,
            FusInstance().apply {
                this.businessKey = businessKey
                consumer?.accept(this)
            }
        )

    /**
     * 重启流程, 从[nodeName] 开始
     * @param [processId] 流程id
     * @param [nodeName] 节点名称
     * @param [execution] 执行对象
     * @author tangli
     * @date 2024/01/16 17:05
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    internal fun restartProcess(
        processId: String,
        nodeName: String,
        execution: FusExecution,
    ) {
        processService
            .getById(processId)
            .model()
            .getNode(nodeName)
            ?.also { node ->
                node
                    .nextNode()
                    ?.also { nextNode ->
                        executeNode(nextNode, execution)
                    }
            }
    }

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [userId] 操作人id
     * @param [args] variable
     * @author tangli
     * @date 2023/10/20 19:32
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    public fun executeTask(
        taskId: String,
        userId: String,
        args: MutableMap<String, Any?> = mutableMapOf(),
    ) {
        val task = taskService.complete(taskId, userId)
        if (TaskType.AGENT == task.taskType) {
            return
        }

        val instance =
            queryService
                .instance(task.instanceId)
                .apply {
                    updatorId = userId
                }
        runtimeService.asToNotNull<RuntimeServiceImpl>().updateInstance(instance)
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

        val processModel =
            runtimeService
                .asToNotNull<RuntimeServiceImpl>()
                .processModelByInstanceId(instanceId)

        val node = processModel.getNode(taskName)
        if (performType == PerformType.VOTE_SIGN) {
            val taskActors = queryService.listTaskActorsByInstanceId(instanceId)
            val passWeight = node?.passWeight.ifNull(50)
            val voteWeight = 100 - taskActors.sumOf { it.weight }
            if (voteWeight < passWeight) {
                return
            }
            val result = taskService.completeActiveTasksByInstanceId(instanceId, userId)
            (!result).fusThrowIfTrue("Failed to close voting status")
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
                processModel,
                instance,
                userId,
                args
            ).apply {
                this.task = task
            }

        if (performType == PerformType.SORT) {
            val assigneeId =
                if (task.taskType == TaskType.TRANSFER) {
                    task.assignorId
                } else {
                    userId
                }

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
                                        taskActor.actorId == assigneeId
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
                                        it.id == assigneeId
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
                    taskService
                        .asToNotNull<TaskServiceImpl>()
                        .createTask(node, execution)
                    interceptors
                        .forEach { interceptor ->
                            interceptor.handle(execution)
                        }
                    return
                }
        }
        execution
            .processModel
            .also { model ->
                val node1 =
                    model
                        .getNode(execution.task?.taskName)
                        .fusThrowIfNull("流程模型中未发现，流程节点:${execution.task?.taskName}")
                node1
                    .nextNode()
                    ?.also { executeNode ->
                        executeNode(executeNode, execution)
                    } ?: endInstance(execution, node1.nodeName)
            }
    }

    /**
     * 按任务实例id [instanceId] 执行任务
     * @param [instanceId] 任务实例id
     * @param [userId] 操作人id
     * @param [args] variable
     * @author tangli
     * @date 2024/02/18 16:46
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    public fun executeTaskByInstanceId(
        instanceId: String,
        userId: String,
        args: MutableMap<String, Any?> = mutableMapOf(),
    ) {
        queryService
            .taskByInstanceIdAndActorId(
                instanceId,
                userId
            ).also { task ->
                executeTask(
                    task.taskId,
                    userId,
                    args
                )
            }
    }

    /**
     * 按任务实例id [instanceId] 委派任务.
     *
     * 代理人办理完任务该任务重新归还给原处理人
     * @param [instanceId] 任务实例id
     * @param [actorId] 任务参与者id
     * @param [assigneeId] 受让人id
     * @return [Boolean]
     * @author tangli
     * @date 2023/10/10 19:19
     * @since 1.0.0
     */
    @JvmStatic
    public fun delegateTaskByInstanceId(
        instanceId: String,
        actorId: String,
        assigneeId: String,
    ) {
        queryService
            .taskByInstanceIdAndActorId(instanceId, actorId)
            .also { task ->
                taskService
                    .delegateTask(
                        task.taskId,
                        actorId,
                        assigneeId
                    )
            }
    }

    /**
     * 按任务实例id [instanceId] 转办任务
     * @param [instanceId] 任务实例id
     * @param [actorId] 任务参与者id
     * @param [assigneeId] 受让人id
     * @return [Boolean]
     * @author tangli
     * @date 2023/10/10 19:17
     * @since 1.0.0
     */
    @JvmStatic
    public fun transferTaskByInstanceId(
        instanceId: String,
        actorId: String,
        assigneeId: String,
    ) {
        queryService
            .taskByInstanceIdAndActorId(instanceId, actorId)
            .also { task ->
                taskService
                    .transferTask(
                        task.taskId,
                        actorId,
                        assigneeId
                    )
            }
    }

    /**
     * 按任务实例id [instanceId] 解决委派任务
     * @param [instanceId] 任务实例id
     * @param [actorId] 任务参与者id
     * @author tangli
     * @date 2024/02/01 17:35
     * @since 1.0.0
     */
    @JvmStatic
    public fun resolveTaskByInstanceId(
        instanceId: String,
        actorId: String,
    ) {
        queryService
            .taskByInstanceIdAndActorId(
                instanceId,
                actorId
            ).also { task ->
                taskService
                    .resolveTask(
                        task.taskId,
                        actorId
                    )
            }
    }

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [userId] 操作人id
     * @author tangli
     * @date 2023/10/20 19:33
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun executeJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
        args: Map<String, Any?>? = null,
    ) {
        SpringContexts
            .getBean(TaskServiceImpl::class.java)
            .executeJumpTask(
                taskId,
                nodeName,
                userId,
                args
            ) { task, _ ->
                val instanceId = task.instanceId
                val instance =
                    queryService
                        .instance(instanceId)
                        .apply {
                            updatorId = userId
                        }
                val runtimeService = runtimeService.asToNotNull<RuntimeServiceImpl>()
                runtimeService.updateInstance(instance)
                val processModel = runtimeService.processModelByInstanceId(instanceId)
                FusExecution(
                    processModel,
                    instance,
                    userId,
                    args ?: mapOf()
                ).apply {
                    this.task = task
                }
            }
    }

    /**
     * 执行插入节点
     * @param [taskId] 任务id
     * @param [node] 节点
     * @param [userId] 操作人id
     * @param [prepend] 前插
     * @author tangli
     * @date 2024/02/01 17:25
     * @since 1.0.0
     */
    @JvmOverloads
    @JvmStatic
    public fun executeInsertNode(
        taskId: String,
        node: FusNode,
        userId: String,
        args: Map<String, Any?>? = null,
        prepend: Boolean,
    ) {
        runtimeService
            .asToNotNull<RuntimeServiceImpl>()
            .insertNode(taskId, node, prepend)
        if (prepend) {
            executeJumpTask(taskId, node.nodeName, userId, args)
        }
    }

    /**
     * 执行流程节点.
     * @param [node] 流程节点
     * @param [execution] 执行对象
     * @author tangli
     * @date 2023/10/24 19:48
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    internal fun executeNode(
        node: FusNode,
        execution: FusExecution,
    ) {
        node
            .conditionNodes
            .applyIf(node.conditionNodes.isNotEmpty()) {
                val conditionNode =
                    node
                        .conditionNodes
                        .sortedBy { it.priority }
                        .firstOrNull { conditionNode ->
                            FusExpressionEvaluator
                                .eval(
                                    conditionNode.expressionList,
                                    conditionVariableHandler
                                        .handle(node, execution)
                                        .fusThrowIfEmpty("Execution parameter cannot be empty")
                                )
                        }.ifNull {
                            node.conditionNodes.firstOrNull {
                                it.expressionList.isEmpty()
                            }
                        }.fusThrowIfNull("Not found executable ConditionNode")

                val nodeChildNode = conditionNode.childNode ?: node.childNode
                if (nodeChildNode != null) {
                    executeNode(nodeChildNode, execution)
                } else {
                    endInstance(execution, node.nodeName)
                }
            }
        if (node.nodeType == NodeType.CC ||
            node.nodeType == NodeType.APPROVER ||
            node.nodeType == NodeType.SUB_PROCESS
        ) {
            taskService
                .asToNotNull<TaskServiceImpl>()
                .createTask(node, execution)
            interceptors
                .forEach { interceptor ->
                    interceptor.handle(execution)
                }
        }

        if (node.nodeType == NodeType.END) {
            endInstance(execution, node.nodeName)
        }

        if (node.childNode == null &&
            node.conditionNodes.isEmpty() &&
            node.nextNode() == null &&
            node.nodeType != NodeType.APPROVER
        ) {
            endInstance(execution, node.nodeName)
        }
    }

    /**
     * 结束流程实例
     * @param [execution] 执行对象
     * @author tangli
     * @date 2024/01/16 16:58
     * @since 1.0.0
     */
    private fun endInstance(
        execution: FusExecution,
        nodeName: String,
    ) {
        val instanceId = execution.instance.instanceId
        queryService
            .listTaskByInstanceId(instanceId)
            .forEach { task ->
                (task.taskType == TaskType.MAJOR).fusThrowIfTrue("存在未完成的主办任务")
                taskService.complete(task.taskId, execution.userId)
            }
        FusProcessModelParser.invalidate("FUS_PROCESS_INSTANCE_MODEL:$instanceId")
        runtimeService.asToNotNull<RuntimeServiceImpl>().complete(execution, nodeName)
    }
}
