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

package com.tony.fus.service.impl

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
import com.tony.fus.service.QueryService

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
            .orderByDesc(FusHistoryTask::finishTime)
            .list()

    override fun recentHistoryTask(instanceId: String): FusHistoryTask =
        historyTaskMapper
            .ktQuery()
            .eq(FusHistoryTask::instanceId, instanceId)
            .orderByDesc(FusHistoryTask::finishTime)
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
