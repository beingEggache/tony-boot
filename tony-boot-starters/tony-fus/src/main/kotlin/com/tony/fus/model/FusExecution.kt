package com.tony.fus.model

import com.tony.fus.FusEngine
import com.tony.fus.FusTaskActorProvider
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor

/**
 * FusExecution is
 * @author tangli
 * @date 2023/10/19 10:16
 * @since 1.0.0
 */
public class FusExecution {
    public val engine: FusEngine

    public val process: FusProcess

    public var instance: FusInstance? = null

    public var parentInstance: FusInstance? = null

    public var nextTaskActor: FusTaskActor? = null

    public var parentNodeName: String? = null

    public var childInstanceId: Long? = null

    public val variable: MutableMap<String, Any?> = HashMap()

    public var creatorId: String? = null

    public var creatorName: String? = null

    public var task: FusTask? = null

    public val taskList: MutableList<FusTask> = mutableListOf()

    public val merged: Boolean = false

    public val taskActorProvider: FusTaskActorProvider
        get() =
            engine
                .context
                .taskActorProvider

    public constructor(
        engine: FusEngine,
        fusProcess: FusProcess,
        fusInstance: FusInstance,
        args: Map<String, Any?>,
    ) {
        this.engine = engine
        this.process = fusProcess
        this.instance = fusInstance
        this
            .variable
            .putAll(args)
    }

    internal constructor(
        execution: FusExecution,
        process: FusProcess,
        parentNodeName: String,
    ) {
        this.engine = execution.engine
        this.process = process
        this
            .variable
            .putAll(execution.variable)
        this.parentInstance = execution.instance
        this.parentNodeName = parentNodeName
        this.creatorId = execution.creatorId
        this.creatorName = execution.creatorName
    }

    public fun createSubExecution(
        execution: FusExecution,
        process: FusProcess,
        parentNodeName: String,
    ): FusExecution =
        FusExecution(execution, process, parentNodeName)

    public fun addTasks(taskList: Collection<FusTask>) {
        this
            .taskList
            .addAll(taskList)
    }

    public fun addTask(task: FusTask) {
        this
            .taskList
            .add(task)
    }
}
