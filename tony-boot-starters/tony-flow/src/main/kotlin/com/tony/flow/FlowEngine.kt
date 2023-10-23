package com.tony.flow

import com.tony.flow.db.po.FlowInstance
import com.tony.flow.service.ProcessService
import com.tony.flow.service.QueryService
import com.tony.flow.service.RuntimeService
import com.tony.flow.service.TaskService

/**
 * 流程引擎接口
 * @author tangli
 * @date 2023/10/19 14:33
 * @since 1.0.0
 */
public interface FlowEngine {

    public var context: FlowContext

    /**
     * 流程定义服务
     */
    public val processService: ProcessService
        get() = context.processService

    /**
     * 查询服务
     */
    public val queryService: QueryService
        get() = context.queryService

    /**
     * 实例服务
     */
    public val runtimeService: RuntimeService
        get() = context.runtimeService

    /**
     * 任务服务
     */
    public val taskService: TaskService
        get() = context.taskService

    /**
     * 按id启动实例
     * @param [flowProcessId] 流程id
     * @param [flowOperator] 任务创建者
     * @param [args] args
     * @return [FlowInstance?]
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceById(
        flowProcessId: Long,
        flowOperator: FlowOperator,
        args: Map<String, Any?>?,
    ): FlowInstance?

    /**
     * 按id启动实例
     * @param [flowProcessId] 流程id
     * @param [flowOperator] 任务创建者
     * @return [FlowInstance?]
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceById(flowProcessId: Long, flowOperator: FlowOperator): FlowInstance? =
        startInstanceById(flowProcessId, flowOperator, null)

    /**
     * 按名称启动实例
     * @param [flowProcessName] 流程名称
     * @param [processVersion] 流程版本
     * @param [flowOperator] 任务创建者
     * @param [args] args
     * @return [FlowInstance]?
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceByName(
        flowProcessName: String,
        processVersion: Int,
        flowOperator: FlowOperator,
        args: Map<String, Any?>?,
    ): FlowInstance?

    /**
     * 按名称启动实例
     * @param [flowProcessName] 流程名称
     * @param [processVersion] 流程版本
     * @param [flowOperator] 任务创建者
     * @return [FlowInstance]?
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun startInstanceByName(
        flowProcessName: String,
        processVersion: Int,
        flowOperator: FlowOperator,
    ): FlowInstance? = startInstanceByName(flowProcessName, processVersion, flowOperator, null)

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [flowOperator] 处理人员
     * @param [args] args
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun executeTask(taskId: Long, flowOperator: FlowOperator, args: Map<String, Any?>?)

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [flowOperator] 处理人员
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun executeTask(taskId: Long, flowOperator: FlowOperator) {
        executeTask(taskId, flowOperator, null)
    }

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [flowOperator] 处理人员
     * @param [args] args
     * @author Tang Li
     * @date 2023/10/20 16:33
     * @since 1.0.0
     */
    public fun executeAndJumpTask(taskId: Long, nodeName: String, flowOperator: FlowOperator, args: Map<String, Any?>?)

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [flowOperator] 处理人员
     * @author Tang Li
     * @date 2023/10/20 16:33
     * @since 1.0.0
     */
    public fun executeAndJumpTask(taskId: Long, nodeName: String, flowOperator: FlowOperator) {
        executeAndJumpTask(taskId, nodeName, flowOperator, null)
    }
}
