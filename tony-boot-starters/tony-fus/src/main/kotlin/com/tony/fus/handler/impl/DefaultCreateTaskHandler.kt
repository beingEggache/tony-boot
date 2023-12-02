package com.tony.fus.handler.impl

import com.tony.fus.FusContext
import com.tony.fus.exception.FusException
import com.tony.fus.handler.CreateTaskHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.utils.getLogger

/**
 * 任务创建操作
 * @author tangli
 * @date 2023/10/25 10:02
 * @since 1.0.0
 */
public object DefaultCreateTaskHandler : CreateTaskHandler {
    private val logger = getLogger()

    /**
     * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中.
     *
     * @param [context] 流上下文
     * @param [execution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 10:03
     * @since 1.0.0
     */
    override fun handle(
        context: FusContext,
        execution: FusExecution,
        node: FusNode?,
    ) {
        val taskList =
            execution
                .engine
                .taskService
                .createTask(node, execution)
        execution.addTasks(taskList)

        try {
            context
                .interceptors
                .forEach { it.handle(context, execution) }
        } catch (e: Exception) {
            logger.error("interceptor error", e)
            throw FusException(e.message, cause = e)
        }
    }
}
