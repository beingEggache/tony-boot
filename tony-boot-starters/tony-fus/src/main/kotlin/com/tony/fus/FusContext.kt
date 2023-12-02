package com.tony.fus

import com.tony.fus.handler.impl.DefaultCreateTaskHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusExpressionEvaluator
import com.tony.fus.model.FusNode
import com.tony.fus.model.FusProcessModel
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService

/**
 * FusContext is
 * @author tangli
 * @date 2023/10/19 10:35
 * @since 1.0.0
 */
public class FusContext(
    public val processService: ProcessService,
    public val queryService: QueryService,
    public val runtimeService: RuntimeService,
    public val taskService: TaskService,
    public val expressionEvaluator: FusExpressionEvaluator,
    public val taskPermission: FusTaskPermission,
    public val interceptors: List<FusInterceptor>,
    public val taskActorProvider: FusTaskActorProvider,
) {
    /**
     * 创建任务
     * @param [execution] 执行对象
     * @param [node] 节点模型
     * @author Tang Li
     * @date 2023/12/02 17:15
     * @since 1.0.0
     */
    public fun createTask(
        execution: FusExecution,
        node: FusNode?,
    ): Unit =
        DefaultCreateTaskHandler.handle(this, execution, node)

    public companion object {
        private val processModelParser: FusProcessModelParser = DefaultFusProcessModelParser()

        public fun parse(
            content: String,
            processId: String?,
            redeploy: Boolean,
        ): FusProcessModel =
            processModelParser.parse(content, processId, redeploy)
    }
}
