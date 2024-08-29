/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core

import com.aizuda.bpm.engine.FlowConstants
import com.aizuda.bpm.engine.FlowLongEngine
import com.aizuda.bpm.engine.TaskActorProvider
import com.aizuda.bpm.engine.assist.Assert.illegal
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.model.NodeModel
import com.aizuda.bpm.engine.model.ProcessModel
import java.io.Serializable
import java.util.LinkedList

/**
 * 流程执行过程中所传递的执行对象，其中包含流程定义、流程模型、流程实例对象、执行参数、返回的任务列表
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class Execution : Serializable {
    /**
     * FlowLongEngine holder
     */
    public var engine: FlowLongEngine

    /**
     * JSON BPM 模型
     */
    public var processModel: ProcessModel

    /**
     * 流程实例对象
     */
    public var flwInstance: FlwInstance? = null

    /**
     * 父流程实例
     */
    public var parentFlwInstance: FlwInstance? = null

    /**
     * 下一个审批参与者
     */
    public var nextFlwTaskActor: FlwTaskActor? = null

    /**
     * 父流程实例节点名称
     */
    public var parentNodeName: String? = null

    /**
     * 子流程实例节点名称
     */
    public var childInstanceId: Long? = null

    /**
     * 执行参数
     */
    public var args: MutableMap<String?, Any?>?

    /**
     * 创建人
     */
    public var flowCreator: FlowCreator?

    /**
     * 当前执行任务
     */
    public var flwTask: FlwTask? = null

    /**
     * 返回的任务列表
     */
    public var flwTasks: MutableList<FlwTask> = ArrayList()

    /**
     * 是否已合并
     * 针对join节点的处理
     */
    public var isMerged: Boolean = false

    /**
     * 用于产生子流程执行对象使用
     *
     * @param execution      [Execution]
     * @param processModel   [ProcessModel]
     * @param parentNodeName 上一节点名称
     */
    internal constructor(execution: Execution, processModel: ProcessModel, parentNodeName: String) {
        this.engine = execution.engine
        this.processModel = processModel
        this.args = execution.args
        this.parentFlwInstance = execution.flwInstance
        this.parentNodeName = parentNodeName
        this.flowCreator = execution.flowCreator
    }

    /**
     * 构造函数，接收流程定义、流程实例对象、执行参数
     *
     * @param engine       [FlowLongEngine]
     * @param processModel [ProcessModel]
     * @param flowCreator  [FlowCreator]
     * @param flwInstance  [FlwInstance]
     * @param args         执行参数
     */
    public constructor(
        engine: FlowLongEngine,
        processModel: ProcessModel,
        flowCreator: FlowCreator?,
        flwInstance: FlwInstance,
        args: MutableMap<String?, Any?>?,
    ) {
        this.engine = engine
        this.processModel = processModel
        this.flowCreator = flowCreator
        this.flwInstance = flwInstance
        this.args = args
    }

    /**
     * 根据当前执行对象execution、子流程定义process、当前节点名称产生子流程的执行对象
     *
     * @param execution      [Execution]
     * @param processModel   [ProcessModel]
     * @param parentNodeName 上一节点名称
     * @return [Execution]
     */
    public fun createSubExecution(
        execution: Execution,
        processModel: ProcessModel,
        parentNodeName: String,
    ): Execution =
        Execution(execution, processModel, parentNodeName)

    /**
     * 执行节点模型
     *
     * @param flowLongContext 流程引擎上下文
     * @param nodeKey         节点key
     * @return 执行节点模型结果 true 成功 false 失败
     */
    public fun executeNodeModel(
        flowLongContext: FlowLongContext,
        nodeKey: String,
    ): Boolean {
        val processModel = this.processModel
        isNull(processModel, "Process model content cannot be empty")
        val nodeModel = processModel.getNode(nodeKey)
        isNull(nodeModel!!, "Not found in the process model, process nodeKey=$nodeKey")

        // 获取当前任务列表，检查并行分支执行情况
        val nodeKeys: MutableList<String?> = LinkedList()
        flowLongContext.queryService!!.getActiveTasksByInstanceId(flwTask!!.instanceId).ifPresent { flwTasks ->
            for (ft in flwTasks) {
                nodeKeys.add(ft.taskKey)
            }
        }
        val executeNodeOptional = nodeModel.nextNode(nodeKeys)
        if (executeNodeOptional.isPresent) {
            // 执行流程节点
            val executeNode = executeNodeOptional.get()
            return executeNode.execute(flowLongContext, this)
        }

        /*
         * 无执行节点流程结束，并且任务列表为空
         */
        if (nodeKeys.isEmpty()) {
            return this.endInstance(nodeModel)
        }

        return true
    }

    /**
     * 执行结束当前流程实例
     *
     * @param endNode 结束节点
     * @return true 执行成功  false 执行失败
     */
    public fun endInstance(endNode: NodeModel?): Boolean {
        val flwTasks = engine.queryService()!!.getTasksByInstanceId(flwInstance!!.id)
        for (flwTask in flwTasks) {
            illegal(flwTask.major(), "There are unfinished major tasks")
            engine.taskService()!!.complete(flwTask.id, this.flowCreator)
        }

        /*
         * 销毁流程实例模型缓存
         */
        FlowLongContext.invalidateProcessModel(FlowConstants.PROCESS_INSTANCE_CACHE_KEY + flwInstance!!.id)

        /*
         * 结束当前流程实例
         */
        return engine.runtimeService()!!.endInstance(this, flwInstance!!.id, endNode)
    }

    /**
     * 重启流程实例（从当前所在节点currentNode位置开始）
     *
     * @param id             流程定义ID
     * @param currentNodeKey 当前所在节点key
     */
    public fun restartProcessInstance(
        id: Long?,
        currentNodeKey: String?,
    ) {
        engine.restartProcessInstance(id, currentNodeKey, this)
    }

    /**
     * 添加任务集合
     *
     * @param flwTasks 流程任务列表
     */
    public fun addTasks(flwTasks: List<FlwTask>) {
        this.flwTasks.addAll(flwTasks)
    }

    /**
     * 添加任务
     *
     * @param flwTask 流程任务
     */
    public fun addTask(flwTask: FlwTask) {
        flwTasks.add(flwTask)
    }

    public val taskActorProvider: TaskActorProvider?
        get() = engine.context!!.taskActorProvider
}
