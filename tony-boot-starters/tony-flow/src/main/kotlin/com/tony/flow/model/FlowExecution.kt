package com.tony.flow.model

import com.tony.flow.FlowEngine
import com.tony.flow.FlowTaskActorProvider
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowProcess
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor

/**
 * FlowExecution is
 * @author tangli
 * @date 2023/10/19 10:16
 * @since 1.0.0
 */
public class FlowExecution {

    public val flowEngine: FlowEngine

    public val flowProcess: FlowProcess

    public var flowInstance: FlowInstance? = null

    public var parentFlowInstance: FlowInstance? = null

    public var nextFlowTaskActor: FlowTaskActor? = null

    public var parentNodeName: String? = null

    public var childFlowInstanceId: Long? = null

    public val args: MutableMap<String, Any?> = HashMap()

    public var creatorId: Long? = null

    public var creatorName: String? = null

    public var currentFlowTask: FlowTask? = null

    public val flowTaskList: MutableList<FlowTask> = mutableListOf()

    public val merged: Boolean = false

    public val taskActorProvider:FlowTaskActorProvider
        get() = flowEngine.context.taskActorProvider

    public constructor(
        flowEngine: FlowEngine,
        flowProcess: FlowProcess,
        flowInstance: FlowInstance,
        args: Map<String, Any?>,
    ) {
        this.flowEngine = flowEngine
        this.flowProcess = flowProcess
        this.flowInstance = flowInstance
        this.args.putAll(args)
    }

    internal constructor(
        flowExecution: FlowExecution,
        flowProcess: FlowProcess,
        parentNodeName: String,
    ) {
        this.flowEngine = flowExecution.flowEngine
        this.flowProcess = flowProcess
        this.args.putAll(flowExecution.args)
        this.parentFlowInstance = flowExecution.flowInstance
        this.parentNodeName = parentNodeName
        this.creatorId = flowExecution.creatorId
        this.creatorName = flowExecution.creatorName
    }

    public fun createSubExecution(
        flowExecution: FlowExecution,
        flowProcess: FlowProcess,
        parentNodeName: String
    ): FlowExecution = FlowExecution(flowExecution, flowProcess, parentNodeName)

    public fun addTasks(flowTaskList: Collection<FlowTask>) {
        this.flowTaskList.addAll(flowTaskList)
    }

    public fun addTask(flowTask: FlowTask) {
        this.flowTaskList.add(flowTask)
    }
}
