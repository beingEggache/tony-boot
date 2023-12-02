package com.tony.fus.model

import com.tony.fus.FusEngine
import com.tony.fus.FusTaskActorProvider
import com.tony.fus.db.enums.TaskType
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.extension.fusThrowIf

/**
 * FusExecution is
 * @author tangli
 * @date 2023/10/19 10:16
 * @since 1.0.0
 */
public class FusExecution(
    public val engine: FusEngine,
    public val process: FusProcess,
    public val userId: String,
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

    public fun endInstance() {
        val instanceId = instance.instanceId
        engine
            .queryService
            .listTaskByInstanceId(instanceId)
            .forEach { task ->
                fusThrowIf(task.taskType == TaskType.MAJOR, "存在未完成的主办任务")
                engine.taskService.complete(task.taskId, "ADMIN")
            }
        engine.runtimeService.complete(instanceId)
    }

    public fun addTasks(taskList: Collection<FusTask>) {
        this
            .taskList
            .addAll(taskList)
    }
}
