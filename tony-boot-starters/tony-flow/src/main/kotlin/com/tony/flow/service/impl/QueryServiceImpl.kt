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
    override fun instance(instanceId: Long): FlowInstance =
        flowInstanceMapper.selectById(instanceId)

    override fun historyInstance(instanceId: Long): FlowHistoryInstance =
        flowHistoryInstanceMapper.selectById(instanceId)

    override fun task(taskId: Long): FlowTask =
        flowTaskMapper.selectById(taskId)

    override fun listTask(
        instanceId: Long,
        taskNames: Collection<String>,
    ): List<FlowTask> =
        flowTaskMapper
            .ktQuery()
            .eq(FlowTask::instanceId, instanceId)
            .`in`(FlowTask::taskName, taskNames)
            .list()

    override fun listTaskByInstanceId(instanceId: Long?): List<FlowTask> =
        flowTaskMapper
            .ktQuery()
            .eq(FlowTask::instanceId, instanceId)
            .list()

    override fun historyTask(taskId: Long): FlowHistoryTask =
        flowHistoryTaskMapper.selectById(taskId)

    override fun listHistoryTask(instanceId: Long): List<FlowHistoryTask> =
        flowHistoryTaskMapper
            .ktQuery()
            .eq(FlowHistoryTask::instanceId, instanceId)
            .orderByDesc(FlowHistoryTask::finishTime)
            .list()

    override fun listHistoryTaskByName(
        instanceId: Long,
        taskName: String,
    ): List<FlowHistoryTask> =
        flowHistoryTaskMapper
            .ktQuery()
            .eq(FlowHistoryTask::instanceId, instanceId)
            .eq(FlowHistoryTask::taskName, taskName)
            .orderByDesc(FlowHistoryTask::createTime)
            .list()

    override fun listTaskActorByTaskId(taskId: Long): List<FlowTaskActor> =
        flowTaskActorMapper
            .ktQuery()
            .eq(FlowTaskActor::taskId, taskId)
            .list()

    override fun listHistoryTaskActorByTaskId(taskId: Long): List<FlowHistoryTaskActor> =
        flowHistoryTaskActorMapper
            .ktQuery()
            .eq(FlowHistoryTaskActor::taskId, taskId)
            .list()
}
