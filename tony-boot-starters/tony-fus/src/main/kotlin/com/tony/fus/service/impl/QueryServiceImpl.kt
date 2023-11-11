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
import com.tony.fus.service.QueryService

/**
 * QueryServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
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
    override fun instance(instanceId: String?): FusInstance? =
        instanceMapper.selectById(instanceId)

    override fun historyInstance(instanceId: String): FusHistoryInstance =
        historyInstanceMapper.selectById(instanceId)

    override fun task(taskId: String): FusTask =
        taskMapper.selectById(taskId)

    override fun listTask(
        instanceId: String,
        taskNames: Collection<String>,
    ): List<FusTask> =
        taskMapper
            .ktQuery()
            .eq(FusTask::instanceId, instanceId)
            .`in`(FusTask::taskName, taskNames)
            .list()

    override fun listTaskByInstanceId(instanceId: String?): List<FusTask> =
        taskMapper
            .ktQuery()
            .eq(FusTask::instanceId, instanceId)
            .list()

    override fun listTaskByInstanceIdAndTaskName(
        instanceId: String?,
        taskName: String?,
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

    override fun listHistoryTaskByName(
        instanceId: String,
        taskName: String,
    ): List<FusHistoryTask> =
        historyTaskMapper
            .ktQuery()
            .eq(FusHistoryTask::instanceId, instanceId)
            .eq(FusHistoryTask::taskName, taskName)
            .orderByDesc(FusHistoryTask::createTime)
            .list()

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

    override fun listTaskActorsByInstanceId(instanceId: String?): List<FusTaskActor> =
        taskActorMapper
            .ktQuery()
            .eq(FusTaskActor::instanceId, instanceId)
            .list()
}
