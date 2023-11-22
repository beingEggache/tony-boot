package com.tony.fus

import com.tony.fus.db.po.FusInstance
import com.tony.fus.model.FusOperator
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService

/**
 * 流程引擎接口
 * @author tangli
 * @date 2023/10/19 14:33
 * @since 1.0.0
 */
public interface FusEngine {
    public val context: FusContext

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
     * @param [processId] 流程id
     * @param [operator] 任务创建者
     * @param [args] variable
     * @return [FusInstance?]
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceById(
        processId: String,
        operator: FusOperator,
        args: Map<String, Any?>,
    ): FusInstance?

    /**
     * 按id启动实例
     * @param [processId] 流程id
     * @param [operator] 任务创建者
     * @return [FusInstance?]
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceById(
        processId: String,
        operator: FusOperator,
    ): FusInstance? =
        startInstanceById(processId, operator, mapOf())

    /**
     * 按名称启动实例
     * @param [processName] 流程名称
     * @param [processVersion] 流程版本
     * @param [operator] 任务创建者
     * @param [args] variable
     * @return [FusInstance]?
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceByName(
        processName: String,
        processVersion: Int,
        operator: FusOperator,
        args: Map<String, Any?>,
    ): FusInstance?

    /**
     * 按名称启动实例
     * @param [processName] 流程名称
     * @param [processVersion] 流程版本
     * @param [operator] 任务创建者
     * @return [FusInstance]?
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun startInstanceByName(
        processName: String,
        processVersion: Int,
        operator: FusOperator,
    ): FusInstance? =
        startInstanceByName(processName, processVersion, operator, mapOf())

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [operator] 处理人员
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun executeTask(
        taskId: String,
        operator: FusOperator,
        args: MutableMap<String, Any?>?,
    )

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [operator] 处理人员
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun executeTask(
        taskId: String,
        operator: FusOperator,
    ) {
        executeTask(taskId, operator, null)
    }

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [operator] 处理人员
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 16:33
     * @since 1.0.0
     */
    public fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        operator: FusOperator,
        args: MutableMap<String, Any?>?,
    )

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [operator] 处理人员
     * @author Tang Li
     * @date 2023/10/20 16:33
     * @since 1.0.0
     */
    public fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        operator: FusOperator,
    ) {
        executeAndJumpTask(taskId, nodeName, operator, null)
    }
}
