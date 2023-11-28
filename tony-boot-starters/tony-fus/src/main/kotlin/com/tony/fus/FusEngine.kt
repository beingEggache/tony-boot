package com.tony.fus

import com.tony.fus.db.po.FusInstance
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
     * @param [userId] 任务创建者id
     * @param [args] variable
     * @return [FusInstance?]
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceById(
        processId: String,
        userId: String,
        args: Map<String, Any?>,
    ): FusInstance

    /**
     * 按id启动实例
     * @param [processId] 流程id
     * @param [userId] 任务创建者id
     * @return [FusInstance?]
     * @author Tang Li
     * @date 2023/10/20 16:31
     * @since 1.0.0
     */
    public fun startInstanceById(
        processId: String,
        userId: String,
    ): FusInstance =
        startInstanceById(processId, userId, mapOf())

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [userId] 处理人id
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun executeTask(
        taskId: String,
        userId: String,
        args: MutableMap<String, Any?>?,
    )

    /**
     * 执行任务
     * @param [taskId] 任务id
     * @param [userId] 处理人id
     * @author Tang Li
     * @date 2023/10/20 16:32
     * @since 1.0.0
     */
    public fun executeTask(
        taskId: String,
        userId: String,
    ) {
        executeTask(taskId, userId, null)
    }

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [userId] 处理人员id
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 16:33
     * @since 1.0.0
     */
    public fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
        args: MutableMap<String, Any?>?,
    )

    /**
     * 执行并跳转到节点
     * @param [taskId] 任务id
     * @param [nodeName] 节点名称
     * @param [userId] 处理人员id
     * @author Tang Li
     * @date 2023/10/20 16:33
     * @since 1.0.0
     */
    public fun executeAndJumpTask(
        taskId: String,
        nodeName: String,
        userId: String,
    ) {
        executeAndJumpTask(taskId, nodeName, userId, null)
    }
}
