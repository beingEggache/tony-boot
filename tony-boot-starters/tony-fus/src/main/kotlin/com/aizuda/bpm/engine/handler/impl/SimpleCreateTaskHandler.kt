/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.handler.impl

import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.handler.CreateTaskHandler
import com.aizuda.bpm.engine.model.NodeModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 默认流程任务创建处理器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class SimpleCreateTaskHandler public constructor() : CreateTaskHandler {
    /**
     * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中
     */
    override fun handle(
        flowLongContext: FlowLongContext?,
        execution: Execution,
        nodeModel: NodeModel,
    ): Boolean {
        try {
            val flwTasks = execution.engine.taskService()?.createTask(nodeModel, execution)
            if (null != flwTasks) {
                execution.addTasks(flwTasks)
            }
            return true
        } catch (e: Exception) {
            log.error("SimpleCreateTaskHandler createTask failed. {}", e.message)
            throw e
        }
    }

    public companion object {
        private val log: Logger = LoggerFactory.getLogger(SimpleCreateTaskHandler::class.java)
        private var createTaskHandler: SimpleCreateTaskHandler? = null

        public val instance: SimpleCreateTaskHandler
            get() {
                if (null == createTaskHandler) {
                    synchronized(SimpleCreateTaskHandler::class.java) {
                        createTaskHandler = SimpleCreateTaskHandler()
                    }
                }
                return createTaskHandler!!
            }
    }
}
