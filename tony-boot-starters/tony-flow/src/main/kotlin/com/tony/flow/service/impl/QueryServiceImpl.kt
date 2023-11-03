package com.tony.flow.service.impl

import com.tony.flow.db.mapper.FlowHistoryInstanceMapper
import com.tony.flow.db.mapper.FlowHistoryTaskActorMapper
import com.tony.flow.db.mapper.FlowHistoryTaskMapper
import com.tony.flow.db.mapper.FlowInstanceMapper
import com.tony.flow.db.mapper.FlowTaskActorMapper
import com.tony.flow.db.mapper.FlowTaskMapper
import com.tony.flow.db.po.FlowHistoryInstance
import com.tony.flow.db.po.FlowHistoryTask
import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.service.QueryService

/**
 * QueryServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal class QueryServiceImpl(
    private val flowInstanceMapper: FlowInstanceMapper,
    private val flowHistoryInstanceMapper: FlowHistoryInstanceMapper,
    private val flowTaskMapper: FlowTaskMapper,
    private val flowTaskActorMapper: FlowTaskActorMapper,
    private val flowHistoryTaskMapper: FlowHistoryTaskMapper,
    private val flowHistoryTaskActorMapper: FlowHistoryTaskActorMapper,
) : QueryService {
    override fun instance(instanceId: String): FlowInstance =
        flowInstanceMapper.selectById(instanceId)

    override fun historyInstance(instanceId: String): FlowHistoryInstance =
        flowHistoryInstanceMapper.selectById(instanceId)

    override fun task(taskId: String): FlowTask =
        flowTaskMapper.selectById(taskId)

    override fun listTask(
        instanceId: String,
        taskNames: Collection<String>,
    ): List<FlowTask> =
        flowTaskMapper
            .ktQuery()
            .eq(FlowTask::instanceId, instanceId)
            .`in`(FlowTask::taskName, taskNames)
            .list()

    override fun listTaskByInstanceId(instanceId: String?): List<FlowTask> =
        flowTaskMapper
            .ktQuery()
            .eq(FlowTask::instanceId, instanceId)
            .list()

    override fun historyTask(taskId: String): FlowHistoryTask =
        flowHistoryTaskMapper.selectById(taskId)

    override fun listHistoryTask(instanceId: String): List<FlowHistoryTask> =
        flowHistoryTaskMapper
            .ktQuery()
            .eq(FlowHistoryTask::instanceId, instanceId)
            .orderByDesc(FlowHistoryTask::finishTime)
            .list()

    override fun listHistoryTaskByName(
        instanceId: String,
        taskName: String,
    ): List<FlowHistoryTask> =
        flowHistoryTaskMapper
            .ktQuery()
            .eq(FlowHistoryTask::instanceId, instanceId)
            .eq(FlowHistoryTask::taskName, taskName)
            .orderByDesc(FlowHistoryTask::createTime)
            .list()

    override fun listTaskActorByTaskId(taskId: String): List<FlowTaskActor> =
        flowTaskActorMapper
            .ktQuery()
            .eq(FlowTaskActor::taskId, taskId)
            .list()

    override fun listHistoryTaskActorByTaskId(taskId: String): List<FlowHistoryTaskActor> =
        flowHistoryTaskActorMapper
            .ktQuery()
            .eq(FlowHistoryTaskActor::taskId, taskId)
            .list()
}
