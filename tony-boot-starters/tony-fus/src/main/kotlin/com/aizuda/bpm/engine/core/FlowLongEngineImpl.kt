/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core

import com.aizuda.bpm.engine.FlowLongEngine
import com.aizuda.bpm.engine.assist.Assert.isFalse
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.assist.ObjectUtils.getArgs
import com.aizuda.bpm.engine.assist.ObjectUtils.isEmpty
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.core.enums.NodeSetType
import com.aizuda.bpm.engine.core.enums.PerformType
import com.aizuda.bpm.engine.core.enums.TaskState
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwProcess
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.model.NodeAssignee
import com.aizuda.bpm.engine.model.NodeModel
import com.aizuda.bpm.engine.model.ProcessModel
import java.util.Optional
import java.util.function.Function
import java.util.function.Supplier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 基本的流程引擎实现类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlowLongEngineImpl : FlowLongEngine {
    /**
     * 配置对象
     */
    public override var context: FlowLongContext? = null
        protected set

    override fun configure(flowLongContext: FlowLongContext?): FlowLongEngine {
        this.context = flowLongContext
        return this
    }

    /**
     * 根据流程定义ID，创建人，参数列表启动流程实例
     */
    public override fun startInstanceById(
        id: Long?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        supplier: Supplier<FlwInstance>?,
    ): Optional<FlwInstance> {
        val process = processService()!!.getProcessById(id)
        return this.startProcessInstance(process!!.checkState(), flowCreator, args, supplier)
    }

    /**
     * 根据流程定义key、版本号、创建人、参数列表启动流程实例
     */
    override fun startInstanceByProcessKey(
        processKey: String?,
        version: Int?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        supplier: Supplier<FlwInstance>?,
    ): Optional<FlwInstance> {
        val process = processService()!!.getProcessByVersion(flowCreator?.tenantId, processKey, version)
        return this.startProcessInstance(process, flowCreator, args, supplier)
    }

    /**
     * 根据流程对象启动流程实例
     *
     * @param process     流程定义对象
     * @param flowCreator 流程创建者
     * @param args        执行参数
     * @param supplier    初始化流程实例提供者
     * @return [FlwInstance] 流程实例
     */
    override fun startProcessInstance(
        process: FlwProcess?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        supplier: Supplier<FlwInstance>?,
    ): Optional<FlwInstance> {
        // 执行启动模型
        return process!!.executeStartModel(context!!, flowCreator) { nodeModel ->
            val flwInstance = runtimeService()!!.createInstance(process, flowCreator, args, nodeModel, supplier)
            if (log.isDebugEnabled) {
                log.debug("start process instanceId={}", flwInstance.id)
            }
            Execution(this, process.model()!!, flowCreator, flwInstance, args)
        }
    }

    /**
     * 重启流程实例（从当前所在节点currentNode位置开始）
     */
    override fun restartProcessInstance(
        id: Long?,
        currentNodeKey: String?,
        execution: Execution?,
    ) {
        val process = processService()!!.getProcessById(id)
        val nodeModel = process!!.model()!!.getNode(currentNodeKey)
        nodeModel?.nextNode()?.ifPresent { childNode: NodeModel -> childNode.execute(context!!, execution!!) }
    }

    /**
     * 根据任务ID，创建人，参数列表执行任务
     */
    override fun executeTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): Boolean {
        // 完成任务，并且构造执行对象
        val flwTask = taskService()!!.complete(taskId, flowCreator, getArgs(args))
        if (log.isDebugEnabled) {
            log.debug("Execute complete taskId={}", taskId)
        }
        return afterDoneTask(flowCreator, flwTask, args) { execution: Execution ->
            // 执行节点模型
            execution.executeNodeModel(context!!, execution.flwTask!!.taskKey!!)
        }
    }

    /**
     * 自动跳转任务
     */
    override fun autoJumpTask(
        taskId: Long?,
        args: MutableMap<String?, Any?>?,
        flowCreator: FlowCreator?,
    ): Boolean =
        executeTask(taskId, flowCreator, args, TaskState.AUTO_JUMP, EventType.AUTO_JUMP)

    protected fun executeTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        taskState: TaskState?,
        eventType: EventType?,
    ): Boolean {
        val flwTask = taskService()!!.executeTask(taskId, flowCreator, getArgs(args), taskState, eventType)
        if (log.isDebugEnabled) {
            log.debug("Auto execute taskId={}", taskId)
        }
        // 完成任务后续逻辑
        return afterDoneTask(
            flowCreator,
            flwTask,
            args
        ) { execution: Execution -> execution.executeNodeModel(context!!, execution.flwTask!!.taskKey!!) }
    }

    /**
     * 自动完成任务
     */
    override fun autoCompleteTask(
        taskId: Long?,
        args: MutableMap<String?, Any?>?,
    ): Boolean =
        executeTask(taskId, FlowCreator.ADMIN, null, TaskState.AUTO_COMPLETE, EventType.AUTO_COMPLETE)

    /**
     * 自动拒绝任务
     */
    override fun autoRejectTask(
        taskId: Long?,
        args: MutableMap<String?, Any?>?,
    ): Boolean {
        val flwTask =
            taskService()!!.executeTask(
                taskId,
                FlowCreator.ADMIN,
                getArgs(args),
                TaskState.AUTO_COMPLETE,
                EventType.AUTO_COMPLETE
            )
        if (log.isDebugEnabled) {
            log.debug("Auto reject taskId={}", taskId)
        }
        return null != flwTask
    }

    /**
     * 执行任务并跳转到指定节点
     */
    override fun executeJumpTask(
        taskId: Long?,
        nodeKey: String?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): Boolean {
        // 执行任务跳转归档
        return taskService()!!.executeJumpTask(
            taskId,
            nodeKey,
            flowCreator,
            args
        ) { flwTask ->
            val flwInstance = this.getFlwInstance(flwTask.instanceId, flowCreator?.createBy)
            val processModel = runtimeService()!!.getProcessModelByInstanceId(flwInstance.id)
            val execution = Execution(this, processModel!!, flowCreator, flwInstance, flwInstance.variableToMap())
            // 传递父节点信息
            execution.flwTask = flwTask
            execution
        }
    }

    override fun createNewTask(
        taskId: Long?,
        taskType: TaskType?,
        performType: PerformType?,
        taskActors: List<FlwTaskActor>,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): List<FlwTask> =
        taskService()!!.createNewTask(
            taskId,
            taskType,
            performType,
            taskActors,
            flowCreator
        ) { flwTask ->

            /*
             * 流程模型
             */
            val processModel = runtimeService()!!.getProcessModelByInstanceId(flwTask!!.instanceId)

            // 当前流程实例
            val flwInstance = this.getFlwInstance(flwTask.instanceId, flowCreator?.createBy)
            this.createExecution(processModel!!, flwInstance, flwTask, flowCreator, args!!)
        }

    override fun executeAppendNodeModel(
        taskId: Long?,
        nodeModel: NodeModel?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        beforeAfter: Boolean,
    ): Boolean {
        // 追加指定节点模型
        runtimeService()!!.appendNodeModel(taskId, nodeModel, beforeAfter)

        // 前置加签、执行任务并跳转到指定节点
        if (beforeAfter) {
            return executeJumpTask(taskId, nodeModel?.nodeKey, flowCreator, args)
        }

        // 后置加签无需处理任务流转，当前正常任务审批后进入后置加签节点模型
        return true
    }

    /**
     * 获取流程实例
     *
     * @param instanceId 流程实例ID
     * @param updateBy   更新人
     * @return [FlwInstance]
     */
    protected fun getFlwInstance(
        instanceId: Long?,
        updateBy: String?,
    ): FlwInstance {
        val flwInstance = queryService()!!.getInstance(instanceId)
        isNull(flwInstance, "process instance [ id=$instanceId ] completed or not present")
        flwInstance!!.lastUpdateBy = updateBy
        flwInstance.lastUpdateTime = currentDate
        runtimeService()!!.updateInstance(flwInstance)
        return flwInstance
    }

    /**
     * 任务完成以后后续任务节点生成，逻辑判断
     */
    private fun afterDoneTask(
        flowCreator: FlowCreator?,
        flwTask: FlwTask,
        args: MutableMap<String?, Any?>?,
        executeNextStep: Function<Execution, Boolean>,
    ): Boolean {
        if (TaskType.AGENT.eq(flwTask.taskType)) {
            // 代理人完成任务，结束后续执行
            return true
        }

        val flwInstance = this.getFlwInstance(flwTask.instanceId, flowCreator?.createBy)
        val performType = PerformType.get(flwTask.performType)
        if (performType == PerformType.COUNTERSIGN) {
            /*
             * 会签未全部完成，不继续执行节点模型
             */
            val flwTaskList = queryService()!!.getTasksByInstanceIdAndTaskKey(flwInstance.id, flwTask.taskKey)
            if (isNotEmpty(flwTaskList)) {
                return true
            }
        }

        /*
         * 流程模型
         */
        val processModel = runtimeService()!!.getProcessModelByInstanceId(flwInstance.id)

        /*
         * 票签（ 总权重大于 50% 表示通过 ）
         */
        if (performType == PerformType.VOTE_SIGN) {
            val flwTaskActorsOptional = queryService()!!.getActiveTaskActorsByInstanceId(flwInstance.id)
            if (flwTaskActorsOptional.isPresent) {
                val nodeModel = processModel!!.getNode(flwTask.taskKey)
                val passWeight = if (nodeModel!!.passWeight == null) 50 else nodeModel.passWeight!!
                val votedWeight =
                    100 -
                        flwTaskActorsOptional
                            .get()
                            .stream()
                            .mapToInt { t: FlwTaskActor ->
                                if (t.weight ==
                                    null
                                ) {
                                    0
                                } else {
                                    t.weight!!
                                }
                            }.sum()
                if (votedWeight < passWeight) {
                    // 投票权重小于节点权重继续投票
                    return true
                } else {
                    // 投票完成关闭投票状态，进入下一个节点
                    isFalse(
                        taskService()!!.completeActiveTasksByInstanceId(flwInstance.id, flowCreator),
                        "Failed to close voting status"
                    )
                }
            }
        }

        // 构建执行对象
        val objectMap = getArgs(args)
        val execution = this.createExecution(processModel!!, flwInstance, flwTask, flowCreator, objectMap)

        /*
         * 按顺序依次审批，一个任务按顺序多个参与者依次添加
         */
        if (performType == PerformType.SORT) {
            val nodeModel = processModel.getNode(flwTask.taskKey)
            var findTaskActor = false
            var nextNodeAssignee: NodeAssignee? = null
            val nodeAssigneeList = nodeModel!!.nodeAssigneeList
            // 当前任务实际办理人
            var assigneeId: String? = flowCreator?.createId
            if (NodeSetType.ROLE.eq(nodeModel.setType) || NodeSetType.DEPARTMENT.eq(nodeModel.setType)) {
                // 角色、部门 任务参与者
                val htaList =
                    context!!.queryService!!.getHisTaskActorsByTaskIdAndActorId(
                        flwTask.id,
                        flowCreator?.createId
                    )
                if (isNotEmpty(htaList)) {
                    assigneeId = htaList!![0]!!.agentId
                }
            } else if (TaskType.TRANSFER.value == flwTask.taskType) {
                assigneeId = flwTask.assignorId
            }
            if (isEmpty(nodeAssigneeList)) {
                /*
                 * 模型未设置处理人，那么需要获取自定义参与者
                 */
                val taskActors = execution.taskActorProvider!!.getTaskActors(nodeModel, execution)
                if (isNotEmpty(taskActors)) {
                    for (taskActor in taskActors!!) {
                        if (findTaskActor) {
                            // 找到下一个执行人
                            nextNodeAssignee = NodeAssignee.of(taskActor)
                            break
                        }

                        // 判断找到当前任务实际办理人
                        if (taskActor.actorId == assigneeId) {
                            findTaskActor = true
                        }
                    }
                }
            } else {
                /*
                 * 模型中去找下一个执行者
                 */
                for (nodeAssignee in nodeAssigneeList!!) {
                    if (findTaskActor) {
                        // 找到下一个执行人
                        nextNodeAssignee = nodeAssignee
                        break
                    }
                    if (nodeAssignee.id == assigneeId) {
                        findTaskActor = true
                    }
                }
            }

            // 如果下一个顺序执行人存在，创建顺序审批任务
            if (null != nextNodeAssignee) {
                execution.nextFlwTaskActor = FlwTaskActor.ofNodeAssignee(nextNodeAssignee)
                return context!!.createTask(execution, nodeModel)
            }
        }

        /*
         * 执行触发器任务
         */
        if (performType == PerformType.TRIGGER) {
            taskService()!!.executeTaskTrigger(execution, flwTask)
            return true
        }

        // 执行回调逻辑
        return executeNextStep.apply(execution)
    }

    protected fun createExecution(
        processModel: ProcessModel,
        flwInstance: FlwInstance,
        flwTask: FlwTask?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>,
    ): Execution {
        /*
         * 追加实例参数
         */
        val instanceMaps = flwInstance.variableToMap()
        for ((key, value) in instanceMaps) {
            if (args.containsKey(key)) {
                continue
            }
            args[key] = value
        }
        val execution = Execution(this, processModel, flowCreator, flwInstance, args)
        execution.flwTask = flwTask
        return execution
    }

    public companion object {
        private val log: Logger = LoggerFactory.getLogger(FlowLongEngineImpl::class.java)
    }
}
