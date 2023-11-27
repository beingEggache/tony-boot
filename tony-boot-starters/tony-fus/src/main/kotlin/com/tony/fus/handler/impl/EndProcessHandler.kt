package com.tony.fus.handler.impl

import com.tony.fus.ADMIN
import com.tony.fus.FusContext
import com.tony.fus.db.enums.TaskType
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.handler.FusHandler
import com.tony.fus.model.FusExecution

/**
 * EndProcessHandler is
 * @author tangli
 * @date 2023/10/25 17:26
 * @since 1.0.0
 */
public object EndProcessHandler : FusHandler {
    override fun handle(
        context: FusContext,
        execution: FusExecution,
    ) {
        val engine = execution.engine
        val instance = execution.instance

        engine
            .queryService
            .listTaskByInstanceId(execution.instance.instanceId)
            .forEach {
                fusThrowIf(it.taskType == TaskType.MAJOR, "存在未完成的主办任务")
                engine
                    .taskService
                    .complete(it.taskId, ADMIN)
            }

        engine
            .runtimeService
            .complete(instance.instanceId)
    }
}
