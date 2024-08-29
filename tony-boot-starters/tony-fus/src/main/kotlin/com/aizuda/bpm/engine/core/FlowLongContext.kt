/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core

import com.aizuda.bpm.engine.Expression
import com.aizuda.bpm.engine.FlowLongEngine
import com.aizuda.bpm.engine.ProcessModelParser
import com.aizuda.bpm.engine.ProcessService
import com.aizuda.bpm.engine.QueryService
import com.aizuda.bpm.engine.RuntimeService
import com.aizuda.bpm.engine.TaskAccessStrategy
import com.aizuda.bpm.engine.TaskActorProvider
import com.aizuda.bpm.engine.TaskCreateInterceptor
import com.aizuda.bpm.engine.TaskReminder
import com.aizuda.bpm.engine.TaskService
import com.aizuda.bpm.engine.TaskTrigger
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.cache.FlowCache
import com.aizuda.bpm.engine.handler.ConditionNodeHandler
import com.aizuda.bpm.engine.handler.CreateTaskHandler
import com.aizuda.bpm.engine.handler.impl.SimpleConditionNodeHandler
import com.aizuda.bpm.engine.handler.impl.SimpleCreateTaskHandler
import com.aizuda.bpm.engine.impl.DefaultProcessModelParser
import com.aizuda.bpm.engine.model.NodeModel
import com.aizuda.bpm.engine.model.ProcessModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * FlowLong流程引擎上下文
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlowLongContext(
    flowCache: FlowCache?,
    processModelParser: ProcessModelParser?,
) {
    /**
     * 注入默认流程模型解析器
     *
     * @param flowCache          流程缓存
     * @param processModelParser 流程模型解析器
     */
    init {
        if (null == processModelParser) {
            FlowLongContext.processModelParser = DefaultProcessModelParser(flowCache)
        } else {
            FlowLongContext.processModelParser = processModelParser
        }
    }

    public var processService: ProcessService? = null
    public var queryService: QueryService? = null
    public var runtimeService: RuntimeService? = null
    public var taskService: TaskService? = null
    public var expression: Expression? = null

    /**
     * 流程任务创建处理器
     */
    public var createTaskHandler: CreateTaskHandler? = null
        /**
         * 获取创建流程任务处理器实现类
         *
         * @return [CreateTaskHandler]
         */
        get() = if (null != field) field else SimpleCreateTaskHandler.instance

    /**
     * 流程执行条件处理器
     */
    public var conditionNodeHandler: ConditionNodeHandler? = null

    /**
     * 流程任务创建拦截器
     */
    public var taskCreateInterceptor: TaskCreateInterceptor? = null

    /**
     * 任务访问策略类
     */
    public var taskAccessStrategy: TaskAccessStrategy? = null

    /**
     * 审批参与者提供者
     */
    public var taskActorProvider: TaskActorProvider? = null

    /**
     * 任务提醒接口
     */
    public var taskReminder: TaskReminder? = null

    /**
     * 审批参与者提供者
     */
    public var taskTrigger: TaskTrigger? = null

    /**
     * 检查并返回条件表达式
     */
    public fun checkExpression(): Expression {
        isNull(expression, "Interface Expression not implemented")
        return this.expression!!
    }

    public val flowConditionHandler: ConditionNodeHandler
        /**
         * 获取创建流程任务处理器实现类
         *
         * @return [CreateTaskHandler]
         */
        get() = if (null != conditionNodeHandler) conditionNodeHandler!! else SimpleConditionNodeHandler.instance

    /**
     * 创建流程任务
     *
     * @param execution 执行对象
     * @param nodeModel 节点模型
     * @return true 执行成功  false 执行失败
     */
    public fun createTask(
        execution: Execution,
        nodeModel: NodeModel,
    ): Boolean {
        // 拦截器前置处理
        if (null != taskCreateInterceptor) {
            taskCreateInterceptor!!.before(this, execution)
        }

        // 执行创建任务
        val result = createTaskHandler!!.handle(this, execution, nodeModel)

        // 拦截器后置处理
        if (null != taskCreateInterceptor) {
            taskCreateInterceptor!!.after(this, execution)
        }
        return result
    }

    /**
     * 默认初始化流程引擎上下文
     *
     * @param configEngine 流程配置引擎
     * @return [FlowLongEngine]
     */
    public fun build(configEngine: FlowLongEngine): FlowLongContext {
        if (log.isInfoEnabled) {
            log.info("FlowLongEngine start......")
        }
        /*
         * 由服务上下文返回流程引擎
         */
        isNull(configEngine, "Unable to discover implementation class for LongEngine")
        if (log.isInfoEnabled) {
            log.info("FlowLongEngine be found {}", configEngine.javaClass)
        }
        configEngine.configure(this)
        return this
    }

    public companion object {
        private val log: Logger = LoggerFactory.getLogger(FlowLongContext::class.java)

        /**
         * 静态注入流程模型解析器
         */
        private var processModelParser: ProcessModelParser? = null

        public fun parseProcessModel(
            content: String?,
            cacheKey: String?,
            redeploy: Boolean,
        ): ProcessModel =
            processModelParser!!.parse(content, cacheKey, redeploy)

        public fun invalidateProcessModel(cacheKey: String?) {
            processModelParser!!.invalidate(cacheKey)
        }

        public fun setProcessModelParser(processModelParser: ProcessModelParser?) {
            Companion.processModelParser = processModelParser
        }
    }
}
