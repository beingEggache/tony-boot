package com.tony.flow.handler.impl

import com.tony.flow.ADMIN
import com.tony.flow.FlowContext
import com.tony.flow.db.enums.TaskType
import com.tony.flow.extension.flowThrowIf
import com.tony.flow.handler.FlowHandler
import com.tony.flow.model.FlowExecution

/**
 * EndProcessHandler is
 * @author tangli
 * @date 2023/10/25 17:26
 * @since 1.0.0
 */
public object EndProcessHandler : FlowHandler {
    override fun handle(
        flowContext: FlowContext,
        flowExecution: FlowExecution,
    ) {
        val flowEngine = flowExecution.flowEngine
        val flowInstance = flowExecution.flowInstance

        flowEngine
            .queryService
            .listTaskByInstanceId(flowInstance?.instanceId)
            .forEach {
                flowThrowIf(it.taskType == TaskType.MAJOR, "存在未完成的主办任务")
                flowEngine
                    .taskService
                    .complete(it.taskId, ADMIN)
            }

        flowEngine
            .runtimeService
            .complete(flowInstance?.instanceId)
    }
}
