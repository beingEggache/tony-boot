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

package com.tony.fus.service

import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusHistoryTaskActorMapper
import com.tony.fus.db.mapper.FusHistoryTaskMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusTaskActorMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.db.po.FusHistoryInstance
import com.tony.fus.db.po.FusHistoryTask
import com.tony.fus.db.po.FusHistoryTaskActor
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.exception.FusException
import com.tony.fus.extension.fusOneNotNull
import com.tony.fus.extension.fusSelectByIdNotNull

/**
 * 流程相关的查询服务
 * 查询服务
 * @author Tang Li
 * @date 2023/10/20 19:29
 * @since 1.0.0
 */
public sealed interface QueryService {
    /**
     * 获取流程实例
     * @param [instanceId] 实例id
     * @return [FusInstance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 19:22
     * @since 1.0.0
     */
    public fun instance(instanceId: String): FusInstance

    /**
     * 获取历史流程实例
     * @param [instanceId] 实例id
     * @return [FusHistoryInstance] 历史流程实例
     * @author Tang Li
     * @date 2023/10/10 19:23
     * @since 1.0.0
     */
    public fun historyInstance(instanceId: String): FusHistoryInstance

    /**
     * 获取任务
     * @param [taskId] 任务id
     * @return [FusTask] 任务对象
     * @author Tang Li
     * @date 2023/10/10 19:24
     * @since 1.0.0
     */
    public fun task(taskId: String): FusTask

    /**
     * 获取历史任务
     * @param [taskId] 历史任务id
     * @return [FusTask] 历史任务对象
     * @author Tang Li
     * @date 2023/10/10 19:24
     * @since 1.0.0
     */
    public fun historyTask(taskId: String): FusHistoryTask

    /**
     * 按实例id和参与者id查找任务
     * @param [instanceId] 实例id
     * @param [actorId] 任务参与者id
     * @author Tang Li
     * @date 2023/11/21 19:41
     * @since 1.0.0
     */
    public fun taskByInstanceIdAndActorId(
        instanceId: String,
        actorId: String,
    ): FusTask

    /**
     * 按实例id列出任务
     * @param [instanceId] 实例id
     * @return [List<FusTask>]
     * @author Tang Li
     * @date 2023/10/10 19:31
     * @since 1.0.0
     */
    public fun listTaskByInstanceId(instanceId: String): List<FusTask>

    /**
     * 按实例id和任务名称列出任务
     * @param [instanceId] 实例id
     * @param [taskName] 任务名称
     * @return [List<FusTask>]
     * @author Tang Li
     * @date 2023/11/10 19:50
     * @since 1.0.0
     */
    public fun listTaskByInstanceIdAndTaskName(
        instanceId: String,
        taskName: String,
    ): List<FusTask>

    /**
     * 列出历史任务
     * @param [instanceId] 实例id
     * @return [List<FusHistoryTask>]
     * @author Tang Li
     * @date 2023/10/10 19:39
     * @since 1.0.0
     */
    public fun listHistoryTask(instanceId: String): List<FusHistoryTask>

    /**
     * 最近的历史任务
     * @param [instanceId] 实例id
     * @return [FusHistoryTask]
     * @author Tang Li
     * @date 2023/10/10 19:39
     * @since 1.0.0
     */
    public fun recentHistoryTask(instanceId: String): FusHistoryTask

    /**
     * 最近的历史任务
     * @param [instanceId] 实例id
     * @param [taskName] 任务名称
     * @return [FusHistoryTask]
     * @author Tang Li
     * @date 2023/10/10 19:39
     * @since 1.0.0
     */
    public fun recentHistoryTask(
        instanceId: String,
        taskName: String,
    ): FusHistoryTask

    /**
     * 按任务id列出任务参与者
     * @param [taskId] 任务id
     * @return [List<FusTaskActor>]
     * @author Tang Li
     * @date 2023/10/10 19:32
     * @since 1.0.0
     */
    public fun listTaskActorByTaskId(taskId: String): List<FusTaskActor>

    /**
     * 按任务id列出任务参与者
     * @param [taskId] 历史任务ID
     * @return [List<FusTaskActor>]
     * @author Tang Li
     * @date 2023/10/10 19:32
     * @since 1.0.0
     */
    public fun listHistoryTaskActorByTaskId(taskId: String): List<FusHistoryTaskActor>

    /**
     * 按实例id列出任务参与者
     * @param [instanceId] 实例id
     * @return [List<FusTaskActor>]
     * @author Tang Li
     * @date 2023/11/10 19:59
     * @since 1.0.0
     */
    public fun listTaskActorsByInstanceId(instanceId: String): List<FusTaskActor>
}

/**
 * QueryServiceImpl is
 * @author tangli
 * @date 2023/10/26 19:44
 * @since 1.0.0
 */
internal class QueryServiceImpl(
    private val instanceMapper: FusInstanceMapper,
    private val historyInstanceMapper: FusHistoryInstanceMapper,
    private val taskMapper: FusTaskMapper,
    private val taskActorMapper: FusTaskActorMapper,
    private val historyTaskMapper: FusHistoryTaskMapper,
    private val historyTaskActorMapper: FusHistoryTaskActorMapper,
) : QueryService {
    override fun instance(instanceId: String): FusInstance =
        instanceMapper.fusSelectByIdNotNull(instanceId)

    override fun historyInstance(instanceId: String): FusHistoryInstance =
        historyInstanceMapper.fusSelectByIdNotNull(instanceId)

    override fun task(taskId: String): FusTask =
        taskMapper.fusSelectByIdNotNull(taskId)

    override fun taskByInstanceIdAndActorId(
        instanceId: String,
        actorId: String,
    ): FusTask {
        val taskId =
            taskActorMapper
                .ktQuery()
                .select(FusTaskActor::taskId)
                .eq(FusTaskActor::instanceId, instanceId)
                .eq(FusTaskActor::actorId, actorId)
                .oneObjNotNull<String>(
                    "Task actor(actorId = $actorId) in Task(instanceId = $instanceId) Not Found",
                    ex = ::FusException
                )
        return taskMapper.fusSelectByIdNotNull(taskId)
    }

    override fun listTaskByInstanceId(instanceId: String): List<FusTask> =
        taskMapper
            .ktQuery()
            .eq(FusTask::instanceId, instanceId)
            .list()

    override fun listTaskByInstanceIdAndTaskName(
        instanceId: String,
        taskName: String,
    ): List<FusTask> =
        taskMapper
            .ktQuery()
            .eq(FusTask::instanceId, instanceId)
            .eq(FusTask::taskName, taskName)
            .list()

    override fun historyTask(taskId: String): FusHistoryTask =
        historyTaskMapper.selectById(taskId)

    override fun listHistoryTask(instanceId: String): List<FusHistoryTask> =
        historyTaskMapper
            .ktQuery()
            .eq(FusHistoryTask::instanceId, instanceId)
            .orderByDesc(FusHistoryTask::endTime)
            .list()

    override fun recentHistoryTask(instanceId: String): FusHistoryTask =
        historyTaskMapper
            .ktQuery()
            .eq(FusHistoryTask::instanceId, instanceId)
            .orderByDesc(FusHistoryTask::endTime)
            .last("limit 1")
            .fusOneNotNull()

    override fun recentHistoryTask(
        instanceId: String,
        taskName: String,
    ): FusHistoryTask =
        historyTaskMapper
            .ktQuery()
            .eq(FusHistoryTask::instanceId, instanceId)
            .eq(FusHistoryTask::taskName, taskName)
            .orderByDesc(FusHistoryTask::endTime)
            .last("limit 1")
            .fusOneNotNull()

    override fun listTaskActorByTaskId(taskId: String): List<FusTaskActor> =
        taskActorMapper
            .ktQuery()
            .eq(FusTaskActor::taskId, taskId)
            .list()

    override fun listHistoryTaskActorByTaskId(taskId: String): List<FusHistoryTaskActor> =
        historyTaskActorMapper
            .ktQuery()
            .eq(FusHistoryTaskActor::taskId, taskId)
            .list()

    override fun listTaskActorsByInstanceId(instanceId: String): List<FusTaskActor> =
        taskActorMapper
            .ktQuery()
            .eq(FusTaskActor::instanceId, instanceId)
            .list()
}
