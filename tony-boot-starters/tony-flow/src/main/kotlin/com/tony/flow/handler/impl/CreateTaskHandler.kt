package com.tony.flow.handler.impl

import com.tony.flow.FlowContext
import com.tony.flow.exception.FlowException
import com.tony.flow.handler.FlowHandler
import com.tony.flow.model.FlowExecution
import com.tony.flow.model.FlowNode
import com.tony.utils.getLogger

/**
 * 任务创建操作
 * @author tangli
 * @date 2023/10/25 10:02
 * @since 1.0.0
 */
public class CreateTaskHandler(
    public val flowNode: FlowNode?
) : FlowHandler {
    private val logger = getLogger()

    /**
     * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中.
     *
     * @param [flowContext] 流上下文
     * @param [flowExecution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 10:03
     * @since 1.0.0
     */
    override fun handle(flowContext: FlowContext, flowExecution: FlowExecution) {
        val taskList = flowExecution.flowEngine.taskService.createTask(flowNode, flowExecution)
        flowExecution.addTasks(taskList)

        try {
            flowContext.interceptors.forEach { it.handle(flowContext, flowExecution) }
        } catch (e: Exception) {
            logger.error("interceptor error", e)
            throw FlowException(e.message, cause = e)
        }
    }
}
