/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus

import com.tony.fus.db.po.FusInstance
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService

/**
 * 流程引擎接口
 * @author tangli
 * @date 2023/10/19 19:33
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
     * @param [userId] 操作人id
     * @param [args] variable
     * @return [FusInstance]?
     * @author Tang Li
     * @date 2023/10/20 19:31
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
     * @param [userId] 操作人id
     * @return [FusInstance]?
     * @author Tang Li
     * @date 2023/10/20 19:31
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
     * @param [userId] 操作人id
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 19:32
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
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/10/20 19:32
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
     * @param [userId] 操作人id
     * @param [args] variable
     * @author Tang Li
     * @date 2023/10/20 19:33
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
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/10/20 19:33
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
