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
public class FusExecution public constructor(
    public val engine: FusEngine,
    public val process: FusProcess,
    instance: FusInstance,
    args: Map<String, Any?>,
) {
    public var instance: FusInstance? = instance

    public var parentInstance: FusInstance? = null

    public var nextTaskActor: FusTaskActor? = null

    public var parentNodeName: String = ""

    public var childInstanceId: String = ""

    public var creatorId: String = ""

    public var creatorName: String = ""

    public var task: FusTask? = null

    public val taskList: MutableList<FusTask> = mutableListOf()

    public val merged: Boolean = false

    public val variable: MutableMap<String, Any?> = HashMap()

    init {
        this
            .variable
            .putAll(args)
    }

    public val taskActorProvider: FusTaskActorProvider
        get() =
            engine
                .context
                .taskActorProvider

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
