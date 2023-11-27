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
    public val creatorId: String,
    public val creatorName: String,
    public val instance: FusInstance,
    args: Map<String, Any?>,
) {
    public var nextTaskActor: FusTaskActor? = null

    public var task: FusTask? = null

    public val taskList: MutableList<FusTask> = mutableListOf()

    public val variable: MutableMap<String, Any?> =
        HashMap<String, Any?>().apply {
            putAll(args)
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
}
